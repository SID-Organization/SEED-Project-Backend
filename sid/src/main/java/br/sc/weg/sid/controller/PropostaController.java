package br.sc.weg.sid.controller;

import br.sc.weg.sid.DTO.CadastroPdfPropostaDTO;
import br.sc.weg.sid.DTO.CadastroPropostaDTO;
import br.sc.weg.sid.DTO.UpdatePropostaDTO;
import br.sc.weg.sid.model.entities.*;
import br.sc.weg.sid.model.service.DemandaService;
import br.sc.weg.sid.model.service.PdfPropostaService;
import br.sc.weg.sid.model.service.PropostaService;
import br.sc.weg.sid.model.service.TabelaCustoService;
import br.sc.weg.sid.utils.PropostaUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
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
    private TabelaCustoService tabelaCustoService;

    @PostMapping()
    ResponseEntity<Object> cadastrarProposta(@RequestBody @Valid CadastroPropostaDTO cadastroPropostaDTO) {
        try {
            Proposta proposta = new Proposta();

            Optional<Demanda> demandaOptional = demandaService.findById(cadastroPropostaDTO.getDemandaProposta().getIdDemanda());

            if (demandaOptional.isPresent() && demandaOptional.get().getStatusDemanda() != StatusDemanda.RASCUNHO || demandaOptional.get().getStatusDemanda() != StatusDemanda.CANCELADA) {
                Demanda demanda = demandaOptional.get();
                demanda.setLinkJiraDemanda(cadastroPropostaDTO.getLinkJiraProposta());
                demandaService.save(demanda);
            } else {
                return ResponseEntity.badRequest().body("ERROR 0006: A demanda inserida não existe ou foi reprovada! ID DEMANDA: " + cadastroPropostaDTO.getDemandaProposta().getIdDemanda());
            }

            BeanUtils.copyProperties(cadastroPropostaDTO, proposta);
            Proposta propostaSalva = propostaService.save(proposta);

            return ResponseEntity.status(HttpStatus.CREATED).body(propostaSalva);
        } catch (Exception e) {
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

    @PutMapping("/update/{id}")
    ResponseEntity<Object> atualizarProposta(
            @PathVariable("id") Integer id,
            @RequestParam(value = "updatePropostaForm") String updatePropostaForm,
            @RequestParam(value = "pdfPropostaForm", required = false) String pdfPropostaForm
    ) {
        try {
            Optional<Proposta> propostaOptional = propostaService.findById(id);
            if (propostaOptional.isPresent()) {
                PropostaUtil propostaUtil = new PropostaUtil();
                PdfProposta pdfProposta = propostaUtil.convertJsonToModel(pdfPropostaForm);

                UpdatePropostaDTO updatePropostaDTO = propostaUtil.convertToUpdateProspotaDTO(updatePropostaForm);

                Proposta proposta = propostaOptional.get();
                BeanUtils.copyProperties(updatePropostaDTO, proposta);
                Proposta propostaSalva = propostaService.save(proposta);
                pdfProposta.setProposta(propostaSalva);
                pdfPropostaService.save(pdfProposta);
                return ResponseEntity.ok(proposta);
            } else {
                return ResponseEntity.badRequest().body("ERROR 0007: A proposta inserida não existe! ID PROPOSTA: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body("ERROR 0005: Erro ao atualizar proposta!");
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
        try{
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
        }catch (Exception e){
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

//    @GetMapping("/proposta.pdf/{idProposta}")
//    public ResponseEntity<byte[]> getPropostaPDF(@PathVariable("idProposta") Integer idProposta) throws IOException {
//        // Lógica para carregar o arquivo PDF em um byte array
//        Proposta proposta = propostaService.findById(idProposta).get();
//        byte[] pdfBytes = proposta.getPropostaPDF();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_PDF);
//        headers.setContentDisposition(ContentDisposition.builder("inline").filename("proposta.pdf").build());
//
//        return ResponseEntity.ok().headers(headers).body(pdfBytes);
//    }

}
