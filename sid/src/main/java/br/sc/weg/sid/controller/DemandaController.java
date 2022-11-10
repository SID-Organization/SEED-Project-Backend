package br.sc.weg.sid.controller;

import br.sc.weg.sid.DTO.CadastroDemandaDTO;
import br.sc.weg.sid.exceptions.ErroCadastrarBusBeneficiadas;
import br.sc.weg.sid.exceptions.ErroSalvarChatException;
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
import java.util.List;
import java.util.Optional;

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

    @Autowired
    BusBeneficiadasService busBeneficiadasService;

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

        System.out.println("user: " + usuarioService.findById(cadastroDemandaDTO.getSolicitanteDemanda().getNumeroCadastroUsuario()).get().getNomeUsuario());

        Demanda demanda = demandaUtil.convertJsonToModel(demandaJson);
        demanda.setSecaoTIResponsavel(Secao.TI);
        demanda.setStatusDemanda(Status.BACKLOG);
        demanda.setTamanhoDemanda(Tamanho.GRANDE);
        demanda.setSecaoTIResponsavel(Secao.TI);
        demanda.setSolicitanteDemanda(usuarioService.findById(cadastroDemandaDTO.getSolicitanteDemanda().getNumeroCadastroUsuario()).get());

        Demanda demandaSalva = demandaService.save(demanda);

        if (additionalImages != null) {
            try {
                for (MultipartFile additionalImage : additionalImages) {
                    ArquivoDemanda arquivoDemanda = new ArquivoDemanda();
                    arquivoDemanda.setNomeArquivo(additionalImage.getOriginalFilename());
                    arquivoDemanda.setTipoArquivo(additionalImage.getContentType());
                    arquivoDemanda.setArquivo(additionalImage.getBytes());
                    arquivoDemanda.setIdDemanda(demandaSalva);
                    arquivoDemanda.setIdUsuario(usuarioService.findById(cadastroDemandaDTO.getSolicitanteDemanda().getNumeroCadastroUsuario()).get());

                    arquivoDemandaService.save(arquivoDemanda);
                }
            } catch (Exception e) {
                throw new RuntimeException("Erro ao converter a imagem");
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


        for (Beneficio beneficio : cadastroDemandaDTO.getBeneficios()) {
            beneficio.setIdDemanda(demandaSalva);
            beneficioService.save(beneficio);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(demandaSalva);
    }
}
