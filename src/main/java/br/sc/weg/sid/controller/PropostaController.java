package br.sc.weg.sid.controller;

import br.sc.weg.sid.DTO.CadastroPropostaDTO;
import br.sc.weg.sid.DTO.UpdatePropostaDTO;
import br.sc.weg.sid.model.entities.*;
import br.sc.weg.sid.model.enums.StatusDemanda;
import br.sc.weg.sid.model.enums.TipoBeneficio;
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

    /**
     * Esta função é um mapeamento de requisição HTTP POST para cadastrar uma nova proposta.
     * O objeto DTO CadastroPropostaDTO contém as informações da proposta a ser cadastrada e é validado.
     * A anotação @Transactional garante que a operação será executada em uma transação.
     * Isso significa que todas as operações de banco de dados serão executadas de forma atômica,
     * ou seja, se ocorrer uma exceção em qualquer parte do método, todas as operações de banco de dados serão revertidas.
     * Se a operação for bem-sucedida, a função retorna um objeto ResponseEntity com o status HTTP 201 CREATED e o objeto Proposta salvo no banco de dados.
     * Caso ocorra alguma exceção durante a execução da operação de cadastro de proposta,
     * a função retorna um objeto ResponseEntity com o status HTTP 400 BAD REQUEST e uma mensagem de erro explicando o que aconteceu.
     *
     * @param cadastroPropostaDTO Um objeto DTO que contém as informações da proposta a ser cadastrada.
     * @return ResponseEntity<Object> Um objeto ResponseEntity contendo o status da operação e o objeto Proposta salvo no banco de dados ou uma mensagem de erro.
     */
    @PostMapping()
    @Transactional
    public ResponseEntity<Object> cadastrarProposta(@RequestBody @Valid CadastroPropostaDTO cadastroPropostaDTO) {
        try {
            Proposta proposta = new Proposta();

            Optional<Demanda> demandaOptional = demandaService.findById(cadastroPropostaDTO.getDemandaProposta().getIdDemanda());

            if (demandaOptional.isPresent() && demandaOptional.get().getStatusDemanda() != StatusDemanda.RASCUNHO && demandaOptional.get().getStatusDemanda() != StatusDemanda.CANCELADA) {
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

    /**
     * Esta função é um mapeamento de requisição HTTP DELETE para deletar uma proposta existente com o ID fornecido.
     * O parâmetro "id" é fornecido na URL e é usado para identificar a proposta a ser deletada.
     * Se a proposta existir no banco de dados, a função retorna um objeto ResponseEntity com o status HTTP 200 OK e uma mensagem de sucesso indicando que a proposta foi deletada com sucesso.
     * Caso a proposta não exista no banco de dados, a função retorna um objeto ResponseEntity com o status HTTP 400 BAD REQUEST e uma mensagem de erro explicando que a proposta não existe.
     * Caso ocorra uma exceção durante a operação de deleção, a função retorna um objeto ResponseEntity com o status HTTP 400 BAD REQUEST e uma mensagem de erro explicando o que aconteceu.
     *
     * @param id O ID da proposta a ser deletada.
     * @return ResponseEntity<Object> Um objeto ResponseEntity contendo o status da operação e uma mensagem de sucesso ou uma mensagem de erro.
     */
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
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR 0003: Erro ao deletar proposta!" + "\nMessage: " + e.getMessage());
        }
    }

    /**
     * Esta função é um mapeamento de requisição HTTP PUT para atualizar uma proposta existente com o ID fornecido, ela será utilizada após a criação de uma proposta.
     * Após a criação da proposta um analista deve montar uma proposta, com mais informações, este método deve ser utilizado para atualizar a proposta com essas informações.
     * O parâmetro "id" é fornecido na URL e é usado para identificar a proposta a ser atualizada.
     * O objeto DTO UpdatePropostaDTO contém as informações da proposta a ser atualizada e é validado.
     * A anotação @Transactional garante que a operação será executada em uma transação.
     * Isso significa que todas as operações de banco de dados serão executadas de forma atômica,
     * ou seja, se ocorrer uma exceção em qualquer parte do método, todas as operações de banco de dados serão revertidas.
     * Se a operação for bem-sucedida, a função retorna um objeto ResponseEntity com o status HTTP 200 OK e o objeto Proposta atualizado no banco de dados.
     * Caso a proposta não exista no banco de dados, a função retorna um objeto ResponseEntity com o status HTTP 400 BAD REQUEST e uma mensagem de erro explicando que a proposta não existe
     * Caso ocorra uma exceção durante a operação de atualização, a função retorna um objeto ResponseEntity com o status HTTP 400 BAD REQUEST e uma mensagem de erro explicando o que aconteceu.
     *
     * @param id                 O ID da proposta a ser atualizada.
     * @param updatePropostaForm Um objeto DTO que contém as informações da proposta a ser atualizada.
     * @param pdfPropostaForm    Um objeto DTO que contém o PDF da proposta a ser atualizada.
     * @return ResponseEntity<Object> Um objeto ResponseEntity contendo o status da operação e o objeto Proposta atualizado no banco de dados ou uma mensagem de erro.
     */
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
                    List<TabelaCusto> tabelaCustoList = propostaAntiga.getTabelaCusto();
                    if (!tabelaCustoList.isEmpty()) {
                        BeanUtils.copyProperties(updatePropostaDTO, proposta);
                        propostaUtil.criacaoTabelaCusto(
                                tabelaCustoService,
                                tabelaCustoList,
                                updatePropostaDTO,
                                proposta,
                                tabelaCustoLinhaService,
                                centroCustoTabelaCustoService
                        );
                    } else if (updatePropostaDTO.getTabelaCusto() == null) {
                        BeanUtils.copyProperties(updatePropostaDTO, proposta);
                    } else {
                        BeanUtils.copyProperties(updatePropostaDTO, proposta);
                        TabelaCusto tabelaCustoInterna = updatePropostaDTO.getTabelaCusto().get(0);
                        tabelaCustoInterna.setPropostaTabelaCusto(proposta);
                        TabelaCusto tabelaCustoSalva = tabelaCustoService.save(tabelaCustoInterna);
                        for (int i = 0; i < tabelaCustoInterna.getTabelaCustoLinha().size(); i++) {
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
                double somaValorBeneficios = 0;

                for (Beneficio beneficio : proposta.getDemandaProposta().getBeneficiosDemanda()) {
                    if (beneficio.getTipoBeneficio() == TipoBeneficio.REAL || beneficio.getTipoBeneficio() == TipoBeneficio.POTENCIAL) {
                        somaValorBeneficios += beneficio.getValorBeneficio();
                    }
                }
                double payback = proposta.getCustosTotaisDoProjeto() / somaValorBeneficios;
                proposta.setPaybackProposta(payback);
                Proposta propostaSalva = propostaService.save(proposta);
                try {
                    PdfProposta pdfProposta = propostaUtil.convertJsonToModel(pdfPropostaForm);
                    pdfProposta.setProposta(propostaSalva);
                    pdfPropostaService.save(pdfProposta);
                    gerarPDFPropostaController.gerarPDF(propostaSalva.getIdProposta());
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

    /**
     * Esta função é um método HTTP GET que retorna uma lista de todas as propostas cadastradas no banco de dados.
     *
     * @return ResponseEntity<Object> - Retorna um objeto ResponseEntity com a lista de propostas cadastradas no banco de dados.
     * Caso não existam propostas cadastradas, retorna um objeto ResponseEntity com uma mensagem de erro.
     * Caso ocorra algum erro na execução do método, retorna um objeto ResponseEntity com uma mensagem de erro.
     */
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

    /**
     * Esta função é um método HTTP GET que retorna o pdf de uma proposta cadastrada no banco de dados.
     *
     * @param id - Este parâmetro representa o ID da proposta que será buscada no banco de dados.
     * @return ResponseEntity<Object> - Retorna um objeto ResponseEntity com o pdf da proposta.
     * Caso a proposta não exista, retorna um objeto ResponseEntity com uma mensagem de erro.
     * Caso ocorra algum erro na execução do método, retorna um objeto ResponseEntity com uma mensagem de erro.
     */
    @GetMapping("/pdf-proposta/{id}")
    ResponseEntity<Object> listarPropostaPdf(@PathVariable("id") Integer id) {
        try {
            Optional<Proposta> propostaOptional = propostaService.findById(id);
            if (propostaOptional.isPresent()) {
                Proposta proposta = propostaOptional.get();
                byte[] pdfBytes = proposta.getPdfProposta();

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDisposition(ContentDisposition.builder("inline").filename("Proposta " + proposta.getDemandaProposta().getTituloDemanda() + ".pdf").build());

                return ResponseEntity.ok().headers(headers).body(pdfBytes);
            } else {
                return ResponseEntity.badRequest().body("ERROR 0007: A proposta inserida não existe! ID PROPOSTA: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body("ERROR 0005: Erro ao buscar pdf da proposta de id: " + id + "!");
    }


    /**
     * Esta função é um método HTTP GET que retorna uma proposta cadastrada no banco de dados.
     *
     * @param id - Este parâmetro representa o ID da proposta que será buscada no banco de dados.
     * @return ResponseEntity<Object> - Retorna um objeto ResponseEntity com a proposta cadastrada no banco de dados.
     * Caso a proposta não exista, retorna um objeto ResponseEntity com uma mensagem de erro.
     * Caso ocorra algum erro na execução do método, retorna um objeto ResponseEntity com uma mensagem de erro.
     */
    @GetMapping("/{id}")
    ResponseEntity<Object> listarPropostaPorId(@PathVariable("id") Integer id) {
        try {
            return ResponseEntity.ok(propostaService.findById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR 0003: Erro ao listar propostas!" + "\nMessage: " + e.getMessage());
        }
    }

    /**
     * Esta função é um método HTTP GET que retorna uma lista de propostas cadastradas no banco de dados de acordo com o ID da demanda.
     *
     * @param id - Este parâmetro representa o ID da demanda que será buscada no banco de dados.
     * @return ResponseEntity<Object> - Retorna um objeto ResponseEntity com a lista de propostas cadastradas no banco de dados.
     * Caso a demanda não exista, retorna um objeto ResponseEntity com uma mensagem de erro.
     * Caso não existam propostas cadastradas para a demanda, retorna um objeto ResponseEntity com uma mensagem de erro.
     * Caso ocorra algum erro na execução do método, retorna um objeto ResponseEntity com uma mensagem de erro.
     */
    @GetMapping("/demanda/{id}")
    ResponseEntity<Object> listarPropostaPorIdDemanda(@PathVariable("id") Integer id) {
        try {
            Optional<Demanda> demanda = demandaService.findById(id);
            return ResponseEntity.ok(propostaService.findByDemandaProposta(demanda.get()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR 0003: Erro ao listar propostas!" + "\nMessage: " + e.getMessage());
        }
    }

    /**
     * Esta função é um método HTTP GET que retorna uma lista de propostas cadastradas no banco de dados que estão com o status PROPOSTA_PRONTA.
     *
     * @return ResponseEntity<Object> - Retorna um objeto ResponseEntity com a lista de propostas cadastradas no banco de dados.
     * Caso não existam propostas cadastradas com o status PROPOSTA_PRONTA, retorna um objeto ResponseEntity com uma mensagem de erro.
     * Caso ocorra algum erro na execução do método, retorna um objeto ResponseEntity com uma mensagem de erro.
     */
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

    /**
     * Esta função é um método HTTP GET que retorna uma lista de propostas cadastradas no banco de dados de acordo com o payback informado.
     *
     * @param payback - Este parâmetro representa o payback que será buscado no banco de dados.
     * @return ResponseEntity<Object> - Retorna um objeto ResponseEntity com a lista de propostas cadastradas no banco de dados.
     * Caso não existam propostas cadastradas com o payback informado, retorna um objeto ResponseEntity com uma mensagem de erro.
     * Caso ocorra algum erro na execução do método, retorna um objeto ResponseEntity com uma mensagem de erro.
     */
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
