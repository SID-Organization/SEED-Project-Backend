package br.sc.weg.sid.controller;

import br.sc.weg.sid.DTO.CadastroDemandaDTO;
import br.sc.weg.sid.exceptions.ErroCadastrarBusBeneficiadas;
import br.sc.weg.sid.exceptions.ErroSalvarChatException;
import br.sc.weg.sid.model.entities.*;
import br.sc.weg.sid.model.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
            @RequestBody @Valid CadastroDemandaDTO cadastroDemandaDTO
    ) {

        List<BusinessUnity> businessUnities = businessUnityService.findAll();

        Demanda demanda = new Demanda();
        demanda.setSecaoTIResponsavel(Secao.TI);
        demanda.setStatusDemanda(Status.BACKLOG);
        demanda.setTamanhoDemanda(Tamanho.GRANDE);
        demanda.setSecaoTIResponsavel(Secao.TI);
        usuarioService.findById(cadastroDemandaDTO.getIdUsuario());

        demanda.setSolicitanteDemanda(cadastroDemandaDTO.getIdUsuario());
        BeanUtils.copyProperties(cadastroDemandaDTO, demanda);
        Demanda demandaSalva = demandaService.save(demanda);

        for (BusinessUnity bus : cadastroDemandaDTO.getBusBeneficiadas()) {

            businessUnityService.findByNomeBusinessUnity(bus.getNomeBusinessUnity()).ifPresentOrElse(
                    (busSalva) -> {
                        System.out.println("Id: " + busSalva.getIdBusinessUnity());
                        BusBeneficiadas busBeneficiadas = new BusBeneficiadas();
                        busBeneficiadas.setIdDemanda(demandaSalva);
                        busBeneficiadas.setIdBusinessUnity(busSalva);
                        busBeneficiadasService.save(busBeneficiadas);
                    },
                    () -> {
                        throw new ErroCadastrarBusBeneficiadas("Erro ao cadastrar as business beneficiadas");
                    }
            );
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
