package br.sc.weg.sid.controller;

import br.sc.weg.sid.DTO.CadastroDemandaDTO;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/sid/api/demanda")
public class DemandaController {

    @Autowired
    ChatService chatService;

    @Autowired
    DemandaService demandaService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    BusinessUnityService businessUnityService;

//    @Autowired
//    BusBeneficiadasService busBeneficiadasService;

    @Autowired
    ArquivoDemandaService arquivoDemandaService;

    @Autowired
    BeneficioService beneficioService;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping()
    public ResponseEntity<Object> findAll() {
        List<Demanda> demandas = demandaService.findAll();
        if (demandas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma demanda encontrada");
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(demandas);
    }


    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping()
    public ResponseEntity<Object> cadastroDemandas(

            @RequestParam("demandaForm") @Valid String demandaJson,
            @RequestParam(value = "arquivosDemanda", required = false) MultipartFile[] additionalImages
    ) {
        DemandaUtil demandaUtil = new DemandaUtil();
        CadastroDemandaDTO cadastroDemandaDTO = demandaUtil.convertToDto(demandaJson);
        Demanda demanda = demandaUtil.convertDtoToModel(cadastroDemandaDTO);
        demanda.setSecaoTIResponsavel(Secao.TI);
        demanda.setStatusDemanda(Status.BACKLOG);

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

        demanda.setTamanhoDemanda(Tamanho.GRANDE);
        demanda.setSecaoTIResponsavel(Secao.TI);
        try{
            demanda.setSolicitanteDemanda(usuarioService.findById(cadastroDemandaDTO.getSolicitanteDemanda().getNumeroCadastroUsuario()).get());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Solicitante não encontrado!");
        }

        List<Beneficio> beneficios = cadastroDemandaDTO.getBeneficios();


        demanda.setBusBeneficiadas(null);

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
                }
            } catch (Exception e) {
                arquivoDemandaService.deleteById(arquivoDemandaSalvo.getIdArquivoDemanda());
                demandaService.deleteById(demandaSalva.getIdDemanda());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao salvar arquivos");
            }
        }


//        if (!businessUnities.contains(cadastroDemandaDTO.getIdBuSolicitante())) {
//            throw new ErroCadastrarBusBeneficiadas("ERROR 0004: A Bu do solicitante não existe no banco de dados!");
//        }

//        try {
//            Chat chat = cadastroDemandaDTO.getIdChat();
//            chatService.save(chat);
//            demanda.setIdChat(chat); //< ---  Talvez tenha que adicionar essa função caso o chat não seja setado automáticamente pelo DTO
//        } catch (Exception e) {
//            throw new ErroSalvarChatException("ERROR 0003: Erro ao salvar o chat da demanda!" + "\n" + e.getMessage());
//        }


        try{
            for (Beneficio beneficio : beneficios) {
                beneficio.setIdDemanda(demandaSalva);
                beneficioService.save(beneficio);
            }
        } catch (Exception e) {
            demandaService.deleteById(demandaSalva.getIdDemanda());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro ao cadastrar os benefícios");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(demandaSalva);
    }

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

    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarDemanda(@PathVariable("id") Integer id) {
        if (!demandaService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Não foi encontrado a demanda com o id " + id);
        }
        if (demandaService.findById(id).get().getStatusDemanda().equals("Rascunho")) {
            demandaService.deleteById(id);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Demanda deletada com sucesso!");
    }

}
