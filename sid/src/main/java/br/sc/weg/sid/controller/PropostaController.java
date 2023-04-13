package br.sc.weg.sid.controller;

import br.sc.weg.sid.DTO.CadastroPropostaDTO;
import br.sc.weg.sid.DTO.GerarPDFDTO;
import br.sc.weg.sid.DTO.UpdatePropostaDTO;
import br.sc.weg.sid.model.entities.*;
import br.sc.weg.sid.model.service.*;
import br.sc.weg.sid.utils.PropostaUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/sid/api/proposta")
public class PropostaController {
    @Autowired
    private PropostaService propostaService;

    @Autowired
    private DemandaService demandaService;

    @Autowired
    private PdfPropostaService pdfPropostaService;

    @Autowired
    private GerarPDFPropostaController gerarPDFPropostaController;

    @Autowired
    private TabelaCustoLinhaService tabelaCustoLinhaService;

    @Autowired
    private CentroCustoTabelaCustoService centroCustoTabelaCustoService;

    @Autowired
    private TabelaCustoService tabelaCustoService;

    @PostMapping()
    @Transactional
    public ResponseEntity<Object> cadastrarProposta(@RequestBody @Valid CadastroPropostaDTO cadastroPropostaDTO) {
        try {
            Proposta proposta = new Proposta();

            Optional<Demanda> demandaOptional = demandaService.findById(cadastroPropostaDTO.getDemandaProposta().getIdDemanda());

            if (demandaOptional.isPresent() && demandaOptional.get().getStatusDemanda() != StatusDemanda.RASCUNHO || demandaOptional.get().getStatusDemanda() != StatusDemanda.CANCELADA) {
                Demanda demanda = demandaOptional.get();
                demanda.setLinkJiraDemanda(cadastroPropostaDTO.getLinkJiraProposta());
                demandaService.save(demanda);
            } else {
                return ResponseEntity.badRequest().body("ERROR 0006: A demanda inserida não existe ou foi reprovada! ID DEMANDA: " +
                        cadastroPropostaDTO.getDemandaProposta().getIdDemanda());
            }

            BeanUtils.copyProperties(cadastroPropostaDTO, proposta);
            Proposta propostaSalva = propostaService.save(proposta);
            return ResponseEntity.status(HttpStatus.CREATED).body(propostaSalva);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("ERROR 0001: Erro ao cadastrar proposta!" + "\nMessage: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Object> deletarProposta(@PathVariable("id") Integer id) {
        try {
            Optional<Proposta> propostaOptional = propostaService.findById(id);
            if (propostaOptional.isPresent()) {
                propostaService.deleteById(id);
                return ResponseEntity.ok("Proposta deletada com sucesso!");
            } else {
                return ResponseEntity.badRequest().body("ERROR 0002: A proposta inserida não existe! ID PROPOSTA: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR 0003: Erro ao deletar proposta!" + "\nMessage: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Object> atualizarProposta(@PathVariable("id") Integer id, @RequestParam(value = "updatePropostaForm") String updatePropostaForm, @RequestParam(value = "pdfPropostaForm") String pdfPropostaForm) {
        Optional<Proposta> propostaOptional = propostaService.findById(id);
        Proposta propostaAntiga = propostaOptional.get();
        propostaService.findById(id).get().getTabelaCusto();
        if (!propostaOptional.isPresent()) {
            return ResponseEntity.badRequest().body("ERROR 0004: A proposta inserida não existe! ID PROPOSTA: " + id);
        }
        PropostaUtil propostaUtil = new PropostaUtil();
        try {
            UpdatePropostaDTO updatePropostaDTO = propostaUtil.convertToUpdateProspotaDTO(updatePropostaForm);
            Proposta proposta = propostaOptional.get();
            try {
                try {
                    System.out.println(propostaService.findById(id).get().getTabelaCusto());
                    List<TabelaCusto> tabelaCustoList = propostaAntiga.getTabelaCusto();
                    if (!tabelaCustoList.isEmpty()) {
                        BeanUtils.copyProperties(updatePropostaDTO, proposta);
                        System.out.println("tabela custo não está vazia");
                        tabelaCustoService.deleteById(tabelaCustoList.get(0).getIdTabelaCusto());
                        tabelaCustoService.deleteById(tabelaCustoList.get(1).getIdTabelaCusto());

                        for (TabelaCustoLinha tabelaCustoLinha : updatePropostaDTO.getTabelaCusto().get(0).getTabelaCustoLinha()) {
                            tabelaCustoLinha.setTabelaCusto(proposta.getTabelaCusto().get(0));
                            tabelaCustoLinhaService.save(tabelaCustoLinha);
                        }

                        for (TabelaCustoLinha tabelaCustoLinha : updatePropostaDTO.getTabelaCusto().get(1).getTabelaCustoLinha()) {
                            tabelaCustoLinha.setTabelaCusto(proposta.getTabelaCusto().get(1));
                            tabelaCustoLinhaService.save(tabelaCustoLinha);
                        }

                        for (CentroCustoTabelaCusto centroCustoTabelaCusto : updatePropostaDTO.getTabelaCusto().get(0).getCentroCustoTabelaCusto()) {
                            centroCustoTabelaCusto.setTabelaCusto(proposta.getTabelaCusto().get(0));
                            centroCustoTabelaCustoService.save(centroCustoTabelaCusto);
                        }

                        for (CentroCustoTabelaCusto centroCustoTabelaCusto : updatePropostaDTO.getTabelaCusto().get(1).getCentroCustoTabelaCusto()) {
                            centroCustoTabelaCusto.setTabelaCusto(proposta.getTabelaCusto().get(1));
                            centroCustoTabelaCustoService.save(centroCustoTabelaCusto);
                        }

                        proposta.getTabelaCusto().get(0).setPropostaTabelaCusto(proposta);
                        proposta.getTabelaCusto().get(1).setPropostaTabelaCusto(proposta);
                        proposta.getTabelaCusto().get(0).setIdTabelaCusto(tabelaCustoList.get(0).getIdTabelaCusto());
                        proposta.getTabelaCusto().get(1).setIdTabelaCusto(tabelaCustoList.get(1).getIdTabelaCusto());
                    } else {
                        BeanUtils.copyProperties(updatePropostaDTO, proposta);
                        System.out.println("Tabela custo está vazia");
                        TabelaCusto tabelaCustoInterna = updatePropostaDTO.getTabelaCusto().get(0);
                        tabelaCustoInterna.setPropostaTabelaCusto(proposta);
                        TabelaCusto tabelaCustoSalva = tabelaCustoService.save(tabelaCustoInterna);
                        System.out.println(tabelaCustoSalva);
                        for (int i = 0; i < tabelaCustoInterna.getTabelaCustoLinha().size(); i++) {
                            System.out.println(tabelaCustoInterna.getTabelaCustoLinha().get(0).toString());
                            tabelaCustoInterna.getTabelaCustoLinha().get(i).setTabelaCusto(tabelaCustoSalva);
                            tabelaCustoLinhaService.save(tabelaCustoInterna.getTabelaCustoLinha().get(i));
                        }

                        for (CentroCustoTabelaCusto centroCustoTabelaCusto : proposta.getTabelaCusto().get(0).getCentroCustoTabelaCusto()) {
                            centroCustoTabelaCusto.setTabelaCusto(tabelaCustoSalva);
                            centroCustoTabelaCustoService.save(centroCustoTabelaCusto);
                        }

                        TabelaCusto tabelaCustoExterna = proposta.getTabelaCusto().get(1);
                        tabelaCustoExterna.setPropostaTabelaCusto(proposta);
                        TabelaCusto tabelaCustoSalva2 = tabelaCustoService.save(tabelaCustoExterna);
                        for (TabelaCustoLinha tabelaCustoLinha : proposta.getTabelaCusto().get(1).getTabelaCustoLinha()) {
                            System.out.println("tabela" + " " + tabelaCustoLinha);
                            tabelaCustoLinha.setTabelaCusto(tabelaCustoSalva2);
                            tabelaCustoLinhaService.save(tabelaCustoLinha);
                        }

                        for (CentroCustoTabelaCusto centroCustoTabelaCusto : proposta.getTabelaCusto().get(1).getCentroCustoTabelaCusto()) {
                            centroCustoTabelaCusto.setTabelaCusto(tabelaCustoSalva2);
                            centroCustoTabelaCustoService.save(centroCustoTabelaCusto);
                        }
                    }
                } catch (Exception e) {
                    return ResponseEntity.badRequest().body("ERROR 0008: Erro ao salvar tabela de custo!" + "\nMessage: " + e.getMessage());
                }
                Proposta propostaSalva = propostaService.save(proposta);
                try {
                    PdfProposta pdfProposta = propostaUtil.convertJsonToModel(pdfPropostaForm);
                    pdfProposta.setProposta(propostaSalva);
                    pdfPropostaService.save(pdfProposta);
                    GerarPDFDTO gerarPDFDTO = new GerarPDFDTO();
                    gerarPDFDTO.setProposta(propostaSalva);
                    gerarPDFDTO.setIdDemanda(propostaSalva.getDemandaProposta().getIdDemanda());
                    gerarPDFPropostaController.gerarPDF(gerarPDFDTO);
                } catch (Exception e) {
                    e.printStackTrace();
                    return ResponseEntity.badRequest().body("ERROR 0009: Erro ao gerar PDF!" + "\nMessage: " + e.getMessage());
                }
                return ResponseEntity.status(HttpStatus.CREATED).body(propostaSalva);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("ERROR 0007: Erro ao salvar proposta!" + "\nMessage: " + e.getMessage());
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR 0005: Erro ao converter JSON para objeto!" + "\nMessage: " + e.getMessage());
        }
    }

    @GetMapping
    ResponseEntity<Object> listarPropostas() {
        try {
            List<Proposta> propostas = propostaService.findAll();
            List<PropostaResumida> propostasResumidas = PropostaUtil.converterPropostaParaPropostaResumida(propostas);
            if (propostasResumidas.isEmpty()) {
                return ResponseEntity.badRequest().body("ERROR 0004: Não existem propostas cadastradas!");
            }
            return ResponseEntity.ok(propostasResumidas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR 0003: Erro ao listar propostas!" + "\nMessage: " + e.getMessage());
        }
    }

    @GetMapping("/pdf-proposta/{id}")
    ResponseEntity<Object> listarPropostaPdf(@PathVariable("id") Integer id) {
        try {
            Optional<Proposta> propostaOptional = propostaService.findById(id);
            if (propostaOptional.isPresent()) {
                Proposta proposta = propostaOptional.get();
                byte[] pdfBytes = proposta.getPdfProposta();

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDisposition(ContentDisposition.builder("inline").filename("pdf-proposta" + proposta.getIdProposta() + ".pdf").build());

                return ResponseEntity.ok().headers(headers).body(pdfBytes);
            } else {
                return ResponseEntity.badRequest().body("ERROR 0007: A proposta inserida não existe! ID PROPOSTA: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body("ERROR 0005: Erro ao buscar pdf da proposta de id: " + id + "!");
    }


    @GetMapping("/{id}")
    ResponseEntity<Object> listarPropostaPorId(@PathVariable("id") Integer id) {
        try {
            return ResponseEntity.ok(propostaService.findById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR 0003: Erro ao listar propostas!" + "\nMessage: " + e.getMessage());
        }
    }

    @GetMapping("/demanda/{id}")
    ResponseEntity<Object> listarPropostaPorIdDemanda(@PathVariable("id") Integer id) {
        try {
            Optional<Demanda> demanda = demandaService.findById(id);
            return ResponseEntity.ok(propostaService.findByDemandaProposta(demanda.get()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR 0003: Erro ao listar propostas!" + "\nMessage: " + e.getMessage());
        }
    }

    @GetMapping("/proposta-pronta")
    ResponseEntity<Object> listarPropostaPorStatusDemanda() {
        try {
            List<Proposta> proposta = propostaService.findAll();
            List<Proposta> propostasFiltradas = new ArrayList<>();
            for (Proposta p : proposta) {
                if (p.getDemandaProposta().getStatusDemanda() == StatusDemanda.PROPOSTA_PRONTA) {
                    propostasFiltradas.add(p);
                }
            }
            List<PropostaResumida> propostasResumidas = PropostaUtil.converterPropostaParaPropostaResumida(propostasFiltradas);
            if (propostasResumidas.isEmpty()) {
                return ResponseEntity.badRequest().body("ERROR 0004: Não existem propostas cadastradas!");
            }
            return ResponseEntity.ok(propostasResumidas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR 0003: Erro ao listar propostas!" + "\nMessage: " + e.getMessage());
        }
    }

    @GetMapping("/payback/{payback}")
    ResponseEntity<Object> listarPropostaPorPayback(@PathVariable("payback") Double payback) {
        try {
            List<Proposta> proposta = propostaService.findAllByPaybackProposta(payback);
            List<PropostaResumida> propostasResumidas = PropostaUtil.converterPropostaParaPropostaResumida(proposta);
            if (propostasResumidas.isEmpty()) {
                return ResponseEntity.badRequest().body("ERROR 0004: Não existem propostas cadastradas!");
            }
            return ResponseEntity.ok(propostasResumidas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR 0003: Erro ao listar propostas!" + "\nMessage: " + e.getMessage());
        }
    }

}
