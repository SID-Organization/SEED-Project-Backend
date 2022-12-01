package br.sc.weg.sid.controller;

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
import java.util.*;

@Controller
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
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping()
    public ResponseEntity<Object> findAll() {
        List<Demanda> demandas = demandaService.findAll();
        if (demandas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma demanda encontrada");
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(demandas);
    }

    //Cria uma demanda(caso a demanda não tenha os campos totalmente preenchidos cadastrará com o status de RASCUNHO) e retorna a demanda criada
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping()
    public ResponseEntity<Object> cadastroDemandas(

            @RequestParam("demandaForm") @Valid String demandaJson,
            @RequestParam(value = "arquivosDemanda", required = false) MultipartFile[] additionalImages
    ) {
        try{
            DemandaUtil demandaUtil = new DemandaUtil();
            CadastroDemandaDTO cadastroDemandaDTO = demandaUtil.convertToDto(demandaJson);
            Demanda demanda = demandaUtil.convertDtoToModel(cadastroDemandaDTO);
            demanda.setSecaoTIResponsavel(Secao.TI);
            demanda.setStatusDemanda(Status.BACKLOG);
            demanda.setScoreDemanda(549.00);
            demanda.setTamanhoDemanda(Tamanho.GRANDE);

            Class<? extends CadastroDemandaDTO> classe = cadastroDemandaDTO.getClass();
            List<Field> atributos = Arrays.asList(classe.getDeclaredFields());
            atributos.forEach(atributo -> {
                try {
                    Object valor = atributo.get(cadastroDemandaDTO);
                    if (valor == null){
                        demanda.setStatusDemanda(Status.RASCUNHO);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });
            try{
                demanda.setSolicitanteDemanda(usuarioService.findById(cadastroDemandaDTO.getSolicitanteDemanda().getNumeroCadastroUsuario()).get());
            }catch (Exception e){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Solicitante não encontrado!");
            }

            Demanda demandaSalva = demandaService.save(demanda);
            ArquivoDemanda arquivoDemandaSalvo = new ArquivoDemanda();
            if (additionalImages != null) {
                try {
                    for (MultipartFile additionalImage : additionalImages) {
                        ArquivoDemanda arquivoDemanda = new ArquivoDemanda();
                        arquivoDemanda.setNomeArquivo(additionalImage.getOriginalFilename());
                        arquivoDemanda.setTipoArquivo(additionalImage.getContentType());
                        arquivoDemanda.setArquivo(additionalImage.getBytes());
                        arquivoDemanda.setIdDemanda(demandaSalva);
                        arquivoDemanda.setIdUsuario(usuarioService.findById(cadastroDemandaDTO.getSolicitanteDemanda().getNumeroCadastroUsuario()).get());

                        arquivoDemandaSalvo =  arquivoDemandaService.save(arquivoDemanda);
                        demandaSalva.getArquivosDemandas().add(arquivoDemandaSalvo);
                    }
                } catch (Exception e) {
                    arquivoDemandaService.deleteById(arquivoDemandaSalvo.getIdArquivoDemanda());
                    demandaService.deleteById(demandaSalva.getIdDemanda());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao salvar arquivos: " + e.getMessage());
                }
            }

            for (Beneficio beneficio : demandaSalva.getBeneficiosDemanda()){
                beneficio.setIdDemanda(demandaSalva);
                beneficioService.save(beneficio);
            }
            if(demandaSalva.getStatusDemanda().equals(Status.BACKLOG)){
                CadastroHistoricoWorkflowDTO historicoWorkflowDTO = new CadastroHistoricoWorkflowDTO();
                historicoWorkflowDTO.setDemandaHistorico(demandaSalva);
                historicoWorkflowDTO.setIdResponsavel(demandaSalva.getSolicitanteDemanda());
                historicoWorkflowDTO.setTarefaHistoricoWorkflow(TarefaWorkflow.PREENCHER_DEMANDA);
                try{
                    historicoWorkflowController.cadastroHistoricoWorkflow(historicoWorkflowDTO);
                }catch (Exception e){
                    demandaService.deleteById(demandaSalva.getIdDemanda());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao salvar histórico de workflow: " + e.getMessage());
                }
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(demandaSalva);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro ao cadastrar demanda: " + e.getMessage());
        }
    }

    //Busca demanda por id
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/id/{id}")
    public ResponseEntity<Object> findById(@PathVariable("id") Integer id) {
        try{
            Demanda demanda = demandaService.findById(id).get();
            return ResponseEntity.status(HttpStatus.FOUND).body(demanda);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Demanda com id: " + id + " não encontrada!");
        }
    }

    //Busca demandas por status
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/status/{status}")
    public ResponseEntity<Object> findByStatus(@PathVariable("status") Status status) {
        try{
            List<Demanda> demandas = demandaService.findByStatusDemanda(status);
            if (demandas.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma demanda com status "+ status + " encontrada!");
            }
            return ResponseEntity.status(HttpStatus.FOUND).body(demandas);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma demanda com status "+ status + " encontrada!");
        }
    }

    //Busca demanda por solicitante
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/solicitante/{numeroCadastroSoliciante}")
    public ResponseEntity<Object> findBySolicitante(@PathVariable("numeroCadastroSoliciante") Integer numeroCadastroSoliciante) {
        try{
            Usuario solicitanteDemanda = usuarioService.findById(numeroCadastroSoliciante).get();
            List<Demanda> demandas = demandaService.findBySolicitanteDemanda(solicitanteDemanda);
            if (demandas.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("O solicitante " + solicitanteDemanda.getNomeUsuario() + " não possui demandas!");
            }
            return ResponseEntity.status(HttpStatus.FOUND).body(demandas);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Solicitante com matrícula: " + numeroCadastroSoliciante + " não encontrado!");
        }
    }

    //Busca demandas por Seção
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/secao/{secao}")
    public ResponseEntity<Object> findBySecao(@PathVariable("secao") Secao secao) {
        try{
            List<Demanda> demandas = demandaService.findBySecaoTIResponsavel(secao);
            if (demandas.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma demanda na seção "+ secao + " encontrada!");
            }
            return ResponseEntity.status(HttpStatus.FOUND).body(demandas);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seção " + secao + " não existe!");
        }
    }

    //Busca demandas por data de criação (mais nova a mais antiga)
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/data-decrescente")
    public ResponseEntity<Object> findByDataDecrescente() {
        List<Demanda> demandas = demandaService.findByPrazoElaboracaoDemandaDesc();
        if (demandas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma demanda encontrada!");
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(demandas);
    }

    //Busca demanda por data de criação (mais antiga a mais nova)
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/data-crescente")
    public ResponseEntity<Object> findByDataCrescente() {
        List<Demanda> demandas = demandaService.findByPrazoElaboracaoDemandaAsc();
        if (demandas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma demanda encontrada!");
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(demandas);
    }

    //Busca demanda por score
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/score/{score}")
    public ResponseEntity<Object> findByScore(@PathVariable("score") Double score) {
        List<Demanda> demandas = demandaService.findByScoreDemanda(score);
        if (demandas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma demanda com score de: "+ score + " foi encontrada!");
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(demandas);
    }

    //Busca demandas pelo titulo
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/titulo-demanda/{tituloDemanda}")
    public ResponseEntity<Object> findByTituloDemanda(@PathVariable("tituloDemanda") String tituloDemanda) {
        List<Demanda> demandas = demandaService.findByTituloDemanda(tituloDemanda);
        if (demandas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma demanda com o título: "+ tituloDemanda + " foi encontrada!");
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(demandas);
    }

    //Busca demandas pelo tamanho
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/tamanho/{tamanho}")
    public ResponseEntity<Object> findByTamanho(@PathVariable("tamanho") Tamanho tamanho) {
        List<Demanda> demandas = demandaService.findByTamanhoDemanda(tamanho);
        if (demandas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma demanda com o tamanho: "+ tamanho + " foi encontrada!");
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(demandas);
    }

    //Atualiza uma demanda informando seu id
    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizarDemanda(
            @PathVariable("id") Integer id,
            @RequestParam("demandaForm") @Valid String demandaJson
    ) {
        DemandaUtil demandaUtil = new DemandaUtil();
        CadastroDemandaDTO cadastroDemandaDTO = demandaUtil.convertToDto(demandaJson);
        if (!demandaService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Não foi encontrado a demanda com o id " + id);
        }
        Demanda demanda = demandaService.findById(id).get();
        BeanUtils.copyProperties(cadastroDemandaDTO, demanda);
        demandaService.save(demanda);
        return ResponseEntity.status(HttpStatus.OK).body(demanda);
    }

    //Deleta uma demanda informando seu id
    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarDemanda(@PathVariable("id") Integer id) {
        try {
            if (!demandaService.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Não foi encontrado a demanda com o id " + id);
            }
            if (demandaService.findById(id).get().getStatusDemanda().equals(Status.RASCUNHO)) {
                demandaService.deleteById(id);
                return ResponseEntity.status(HttpStatus.OK).body("Demanda com o id: " + id + " deletada com sucesso!");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Demanda com o id: " + id + " não pode ser deletada pois não tem o status rascunho!");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao deletar a demanda: " + e.getMessage());
        }
    }
}
