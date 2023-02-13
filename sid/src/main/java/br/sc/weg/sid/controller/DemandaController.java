package br.sc.weg.sid.controller;

import br.sc.weg.sid.DTO.CadastroBusBeneficiadasDemandaDTO;
import br.sc.weg.sid.DTO.CadastroDemandaDTO;
import br.sc.weg.sid.DTO.CadastroHistoricoWorkflowDTO;
import br.sc.weg.sid.model.entities.*;
import br.sc.weg.sid.model.service.*;
import br.sc.weg.sid.utils.DemandaUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@CrossOrigin
@RequestMapping("/sid/api/demanda")
public class DemandaController {

    @Autowired
    HistoricoWorkflowController historicoWorkflowController;

    @Autowired
    DemandaService demandaService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    BusinessUnityService businessUnityService;

    @Autowired
    ArquivoDemandaService arquivoDemandaService;

    @Autowired
    BeneficioService beneficioService;

    //Get all, pega todas as demandas
    @GetMapping()
    public ResponseEntity<Object> findAll() {
        List<Demanda> demandas = demandaService.findAll();
        if (demandas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma demanda encontrada");
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(demandas);
    }

    //Get all, pega todas as demandas
    @GetMapping("/titulos-id-demanda")
    public ResponseEntity<Object> findAllTitles() {
        List<Demanda> demandas = demandaService.findAll();
        if (demandas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma demanda encontrada");
        }

        List<Map<String, Object>> demandasResumidas = demandas.stream().map(demanda -> {
            Map<String, Object> demandaResumida = new HashMap<>();
            demandaResumida.put("idDemanda", demanda.getIdDemanda());
            demandaResumida.put("tituloDemanda", demanda.getTituloDemanda());
            return demandaResumida;
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.FOUND).body(demandasResumidas);


    }


    //Cria uma demanda(caso a demanda não tenha os campos totalmente preenchidos cadastrará com o status de RASCUNHO) e retorna a demanda criada
    @PostMapping()
    public ResponseEntity<Object> cadastroDemanda(

            @RequestParam("demandaForm") @Valid String demandaJson,
            @RequestParam(value = "arquivosDemanda", required = false) MultipartFile[] additionalFiles
    ) {
        try {
            DemandaUtil demandaUtil = new DemandaUtil();
            CadastroDemandaDTO cadastroDemandaDTO = demandaUtil.convertToDto(demandaJson);
            System.out.println(demandaJson);
            Demanda demanda = demandaUtil.convertDtoToModel(cadastroDemandaDTO);
            try {
                demanda.setSolicitanteDemanda(usuarioService.findById(cadastroDemandaDTO.getSolicitanteDemanda().getNumeroCadastroUsuario()).get());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Solicitante não encontrado!");
            }
            demanda.setScoreDemanda(549.00);
            demanda.setStatusDemanda(StatusDemanda.ABERTA);

            //Verifica se a demanda possui todos os campos preenchidos, se não possuir, o status será RASCUNHO
            Class<? extends CadastroDemandaDTO> classe = cadastroDemandaDTO.getClass();
            List<Field> atributos = Arrays.asList(classe.getDeclaredFields());
            atributos.forEach(atributo -> {
                try {
                    Object valor = atributo.get(cadastroDemandaDTO);
                    if (!atributo.getName().equals("descricaoQualitativoDemanda")) {
                        if (valor == null || valor.equals("")) {
                            demanda.setStatusDemanda(StatusDemanda.RASCUNHO);
                        }
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });
            Demanda demandaSalva = demandaService.save(demanda);

            //essa variável tem como objetivo buscar a data do dia atual para ser inserida no arquivo de demanda
            LocalDate localDate = LocalDate.now();
            Date dataRegistroArquivo = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            ArquivoDemanda arquivoDemandaSalvo = new ArquivoDemanda();
            //Cadastra os arquivos da demanda
            if (additionalFiles != null) {
                try {
                    for (MultipartFile additionalImage : additionalFiles) {
                        ArquivoDemanda arquivoDemanda = new ArquivoDemanda();
                        arquivoDemanda.setNomeArquivo(additionalImage.getOriginalFilename());
                        arquivoDemanda.setTipoArquivo(additionalImage.getContentType());
                        arquivoDemanda.setArquivo(additionalImage.getBytes());
                        arquivoDemanda.setIdDemanda(demandaSalva);
                        arquivoDemanda.setIdUsuario(usuarioService.findById(cadastroDemandaDTO.getSolicitanteDemanda().getNumeroCadastroUsuario()).get());
                        arquivoDemanda.setDataRegistroArquivo(dataRegistroArquivo);
                        arquivoDemandaSalvo = arquivoDemandaService.save(arquivoDemanda);
                        demandaSalva.getArquivosDemandas().add(arquivoDemandaSalvo);
                    }
                } catch (Exception e) {
                    arquivoDemandaService.deleteById(arquivoDemandaSalvo.getIdArquivoDemanda());
                    demandaService.deleteById(demandaSalva.getIdDemanda());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao salvar arquivos: " + e.getMessage());
                }
            }
            //Cadastra os benefícios da demanda
            for (Beneficio beneficio : demandaSalva.getBeneficiosDemanda()) {
                beneficio.setDemandaBeneficio(demandaSalva);
                beneficioService.save(beneficio);
            }

            //Se a demanda tiver em status Aberta(Backlog) um historico de workflow é criado
            if (demandaSalva.getStatusDemanda().equals(StatusDemanda.ABERTA)) {
                CadastroHistoricoWorkflowDTO historicoWorkflowDTO = new CadastroHistoricoWorkflowDTO();
                historicoWorkflowDTO.setDemandaHistorico(demandaSalva);
                historicoWorkflowDTO.setIdResponsavel(demandaSalva.getSolicitanteDemanda());
                historicoWorkflowDTO.setTarefaHistoricoWorkflow(TarefaWorkflow.PREENCHER_DEMANDA);
                historicoWorkflowDTO.setAcaoFeitaHistorico("Enviar");
                try {
                    historicoWorkflowController.cadastroHistoricoWorkflow(historicoWorkflowDTO);
                    historicoWorkflowDTO.setTarefaHistoricoWorkflow(TarefaWorkflow.CLASSIFICACAO_APROVACAO);
                    historicoWorkflowDTO.setIdResponsavel(demandaSalva.getAnalistaResponsavelDemanda());
                    historicoWorkflowController.cadastroHistoricoWorkflow(historicoWorkflowDTO);
                } catch (Exception e) {
                    demandaService.deleteById(demandaSalva.getIdDemanda());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao salvar histórico de workflow: " + e.getMessage());
                }
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(demandaSalva);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro ao cadastrar demanda: " + e.getMessage());
        }
    }

    //Busca demanda por id
    @GetMapping("/id/{id}")
    public ResponseEntity<Object> findById(@PathVariable("id") Integer id) {
        try {
            Demanda demanda = demandaService.findById(id).get();
            return ResponseEntity.status(HttpStatus.FOUND).body(demanda);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Demanda com id: " + id + " não encontrada!");
        }
    }

    //Busca demandas por statusDemanda
    @GetMapping("/statusDemanda/{statusDemanda}")
    public ResponseEntity<Object> findByStatus(@PathVariable("statusDemanda") StatusDemanda statusDemanda) {
        try {
            List<Demanda> demandas = demandaService.findByStatusDemanda(statusDemanda);
            if (demandas.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma demanda com statusDemanda " + statusDemanda + " encontrada!");
            }
            return ResponseEntity.status(HttpStatus.FOUND).body(demandas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma demanda com statusDemanda " + statusDemanda + " encontrada!");
        }
    }

    //Busca demanda por solicitante
    @GetMapping("/solicitante/{numeroCadastroSoliciante}")
    public ResponseEntity<Object> findBySolicitante(@PathVariable("numeroCadastroSoliciante") Integer numeroCadastroSoliciante) {
        try {
            Usuario solicitanteDemanda = usuarioService.findById(numeroCadastroSoliciante).get();
            List<Demanda> demandas = demandaService.findBySolicitanteDemanda(solicitanteDemanda);
            if (demandas.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("O solicitante " + solicitanteDemanda.getNomeUsuario() + " não possui demandas!");
            }
            return ResponseEntity.status(HttpStatus.FOUND).body(demandas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Solicitante com matrícula: " + numeroCadastroSoliciante + " não encontrado!");
        }
    }

    //Busca demandas por Seção
    @GetMapping("/secao/{secao}")
    public ResponseEntity<Object> findBySecao(@PathVariable("secao") String secao) {
        try {
            List<Demanda> demandas = demandaService.findBySecaoTIResponsavelDemanda(secao);
            if (demandas.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma demanda na seção " + secao + " encontrada!");
            }
            return ResponseEntity.status(HttpStatus.FOUND).body(demandas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seção " + secao + " não existe!");
        }
    }

    //Busca demandas por data de criação (mais nova a mais antiga)
    @GetMapping("/data-decrescente")
    public ResponseEntity<Object> findByDataDecrescente() {
        List<Demanda> demandas = demandaService.findByPrazoElaboracaoDemandaDesc();
        if (demandas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma demanda encontrada!");
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(demandas);
    }

    //Busca demanda por data de criação (mais antiga a mais nova)
    @GetMapping("/data-crescente")
    public ResponseEntity<Object> findByDataCrescente() {
        List<Demanda> demandas = demandaService.findByPrazoElaboracaoDemandaAsc();
        if (demandas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma demanda encontrada!");
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(demandas);
    }

    //Busca demanda por score
    @GetMapping("/score/{score}")
    public ResponseEntity<Object> findByScore(@PathVariable("score") Double score) {
        List<Demanda> demandas = demandaService.findByScoreDemanda(score);
        if (demandas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma demanda com score de: " + score + " foi encontrada!");
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(demandas);
    }

    //Busca demandas pelo titulo
    @GetMapping("/titulo-demanda/{tituloDemanda}")
    public ResponseEntity<Object> findByTituloDemanda(@PathVariable("tituloDemanda") String tituloDemanda) {
        List<Demanda> demandas = demandaService.findByTituloDemanda(tituloDemanda);
        if (demandas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma demanda com o título: " + tituloDemanda + " foi encontrada!");
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(demandas);
    }

    //Busca demandas pelo tamanhoDemanda
    @GetMapping("/tamanhoDemanda/{tamanhoDemanda}")
    public ResponseEntity<Object> findByTamanho(@PathVariable("tamanhoDemanda") TamanhoDemanda tamanhoDemanda) {
        List<Demanda> demandas = demandaService.findByTamanhoDemanda(tamanhoDemanda);
        if (demandas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma demanda com o tamanhoDemanda: " + tamanhoDemanda + " foi encontrada!");
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(demandas);
    }

    //Busca demandas de um determinado analista responsável
    @GetMapping("/analista/{numeroCadastroAnalista}")
    public ResponseEntity<Object> findByAnalista(@PathVariable("numeroCadastroAnalista") Integer numeroCadastroAnalista) {
        try {
            Usuario analistaDemanda = usuarioService.findById(numeroCadastroAnalista).get();
            if (analistaDemanda.getCargoUsuario() != Cargo.ANALISTA) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("O usuário " + analistaDemanda.getNomeUsuario() + " não é um analista! " +
                        "Ele é um " + analistaDemanda.getCargoUsuario().getNome());
            }
            List<Demanda> demandas = demandaService.findByAnalistaResponsavelDemanda(analistaDemanda);
            if (demandas.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("O analista " + analistaDemanda.getNomeUsuario() + " não possui demandas!");
            }
            return ResponseEntity.status(HttpStatus.FOUND).body(demandas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //Busca demandas de um determinado gerente da área responsável
    @GetMapping("/gerente-da-area/{numeroCadastroUsuario}")
    public ResponseEntity<Object> findByGerente(@PathVariable("numeroCadastroUsuario") Integer numeroCadastroGerente) {
        try {
            Usuario gerenteDaAreaDemanda = usuarioService.findById(numeroCadastroGerente).get();
            if (gerenteDaAreaDemanda.getCargoUsuario() != Cargo.GERENTE) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("O usuário " + gerenteDaAreaDemanda.getNomeUsuario() + " não é um gerente! " +
                        "Ele é um " + gerenteDaAreaDemanda.getCargoUsuario().getNome());
            }
            List<Demanda> demandas = demandaService.findByGerenteDaAreaDemanda(gerenteDaAreaDemanda);
            if (demandas.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("O gerente " + gerenteDaAreaDemanda.getNomeUsuario() + " não possui demandas!");
            }
            return ResponseEntity.status(HttpStatus.FOUND).body(demandas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum usuário com o número de cadastro: " + numeroCadastroGerente + " foi encontrado!");
        }
    }

    //Busca demandas de um determinado gestor de ti responsável
    @GetMapping("/gestor-ti/{numeroCadastroUsuario}")
    public ResponseEntity<Object> findByGestor(@PathVariable("numeroCadastroUsuario") Integer numeroCadastroGestor) {
        try {
            Usuario gestorDeTiDemanda = usuarioService.findById(numeroCadastroGestor).get();
            if (gestorDeTiDemanda.getCargoUsuario() != Cargo.GESTOR_TI) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("O usuário " + gestorDeTiDemanda.getNomeUsuario() + " não é um gestor! " +
                        "Ele é um " + gestorDeTiDemanda.getCargoUsuario().getNome());
            }
            List<Demanda> demandas = demandaService.findByGestorResponsavelDemanda(gestorDeTiDemanda);
            if (demandas.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("O gestor " + gestorDeTiDemanda.getNomeUsuario() + " não possui demandas!");
            }
            return ResponseEntity.status(HttpStatus.FOUND).body(demandas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //Atualiza status da demanda
    @PutMapping("/status/{id}")
    public ResponseEntity<Object> atualizarStatusDemanda(
            @PathVariable("id") Integer idDemanda,
            @RequestBody Map<String, String> requestBody) {

        StatusDemanda statusDemanda = StatusDemanda.valueOf(requestBody.get("statusDemanda"));

        if (!demandaService.existsById(idDemanda)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Não foi encontrado a demanda com o id " + idDemanda);
        }
        Demanda demanda = demandaService.findById(idDemanda).get();
        if (statusDemanda == StatusDemanda.CANCELADA) {
            demanda.setAnalistaResponsavelDemanda(null);
            demanda.setGerenteDaAreaDemanda(null);
            demanda.setGestorResponsavelDemanda(null);
        }
        demanda.setStatusDemanda(statusDemanda);
        demandaService.save(demanda);
        return ResponseEntity.status(HttpStatus.OK).body(demanda);
    }

    //Atualiza uma demanda informando seu id
    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizarDemanda(
            @PathVariable("id") Integer idDemanda,
            @RequestParam("demandaForm") @Valid String demandaJson
    ) {
        DemandaUtil demandaUtil = new DemandaUtil();
        CadastroDemandaDTO cadastroDemandaDTO = demandaUtil.convertToDto(demandaJson);
        if (!demandaService.existsById(idDemanda)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Não foi encontrado a demanda com o id " + idDemanda);
        }
        Demanda demanda = demandaService.findById(idDemanda).get();
        BeanUtils.copyProperties(cadastroDemandaDTO, demanda);
        historicoWorkflowController.atualizaVersaoWorkflow(demanda.getHistoricoWorkflowUltimaVersao().getIdHistoricoWorkflow(),
                demanda.getHistoricoWorkflowUltimaVersao());
        demandaService.save(demanda);
        return ResponseEntity.status(HttpStatus.OK).body(demanda);
    }

    @PutMapping("/atualiza-bus-beneficiadas/{id}")
    public ResponseEntity<Object> atualizaBusBeneficiadas(
            @PathVariable("id") Integer id,
            @RequestBody @Valid CadastroBusBeneficiadasDemandaDTO cadastroBusBeneficiadasDemandaDTO,
            @RequestBody @Valid MultipartFile[] additionalFiles
    ) {
        if (!demandaService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Não foi encontrado a demanda com o id: " + id);
        }
        Demanda demanda = demandaService.findById(id).get();
        BeanUtils.copyProperties(cadastroBusBeneficiadasDemandaDTO, demanda);
        demanda.setStatusDemanda(StatusDemanda.CLASSIFICADO_PELO_ANALISTA);
        Demanda demandaSalva = demandaService.save(demanda);
        LocalDate localDate = LocalDate.now();
        Date dataRegistroArquivo = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        ArquivoDemanda arquivoDemandaSalvo = new ArquivoDemanda();
        if (additionalFiles != null) {
            try {
                for (MultipartFile additionalImage : additionalFiles) {
                    ArquivoDemanda arquivoDemanda = new ArquivoDemanda();
                    arquivoDemanda.setNomeArquivo(additionalImage.getOriginalFilename());
                    arquivoDemanda.setTipoArquivo(additionalImage.getContentType());
                    arquivoDemanda.setArquivo(additionalImage.getBytes());
                    arquivoDemanda.setIdDemanda(demandaSalva);
                    arquivoDemanda.setIdUsuario(usuarioService.findById(demandaSalva.getSolicitanteDemanda().getNumeroCadastroUsuario()).get());
                    arquivoDemanda.setDataRegistroArquivo(dataRegistroArquivo);
                    arquivoDemandaSalvo = arquivoDemandaService.save(arquivoDemanda);
                    demandaSalva.getArquivosDemandas().add(arquivoDemandaSalvo);
                }
            } catch (Exception e) {
                arquivoDemandaService.deleteById(arquivoDemandaSalvo.getIdArquivoDemanda());
                demandaService.deleteById(demandaSalva.getIdDemanda());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao salvar arquivos: " + e.getMessage());
            }
        }
        historicoWorkflowController.atualizaVersaoWorkflow(demanda.getHistoricoWorkflowUltimaVersao().getIdHistoricoWorkflow(),
                demanda.getHistoricoWorkflowUltimaVersao());
        return ResponseEntity.status(HttpStatus.OK).body(demandaSalva);
    }

    //Deleta uma demanda informando seu id
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarDemanda(@PathVariable("id") Integer idDemanda) {
        try {
            if (!demandaService.existsById(idDemanda)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Não foi encontrado a demanda com o id " + idDemanda);
            }
            if (demandaService.findById(idDemanda).get().getStatusDemanda().equals(StatusDemanda.RASCUNHO)) {
                demandaService.deleteById(idDemanda);
                return ResponseEntity.status(HttpStatus.OK).body("Demanda com o id: " + idDemanda + " deletada com sucesso!");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Demanda com o id: " + idDemanda + " não pode ser deletada pois não tem o status rascunho!");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao deletar a demanda: " + e.getMessage());
        }
    }
}