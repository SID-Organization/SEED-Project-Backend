package br.sc.weg.sid.controller;

import br.sc.weg.sid.DTO.CadastroDemandaDTO;
import br.sc.weg.sid.exceptions.ErroCadastrarBusBeneficiadas;
import br.sc.weg.sid.exceptions.ErroSalvarChatException;
import br.sc.weg.sid.model.entities.*;
import br.sc.weg.sid.model.service.BeneficioService;
import br.sc.weg.sid.model.service.BusinessUnityService;
import br.sc.weg.sid.model.service.ChatService;
import br.sc.weg.sid.model.service.DemandaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/sid/api/demanda")
public class DemandaController {

    @Autowired
    ChatService chatService;

    @Autowired
    DemandaService demandaService;

    @Autowired
    BusinessUnityService businessUnityService;

    @Autowired
    BeneficioService beneficioService;

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping()
    public ResponseEntity<Object> cadastroDemandas(
            @RequestBody @Valid CadastroDemandaDTO cadastroDemandaDTO
    ) {



        List<BusinessUnity> businessUnities = businessUnityService.findAll();

        for (BusinessUnity bus : cadastroDemandaDTO.getBusBeneficiadas()) {
            if (!businessUnities.contains(bus)) {
                throw new ErroCadastrarBusBeneficiadas("ERROR 0004: Alguma BusinessUnity não existe no banco de dados!");
            }
        }

        if (!businessUnities.contains(cadastroDemandaDTO.getIdBuSolicitante())) {
            throw new ErroCadastrarBusBeneficiadas("ERROR 0004: A Bu do solicitante não existe no banco de dados!");
        }

        Demanda demanda = new Demanda();
        BeanUtils.copyProperties(cadastroDemandaDTO, demanda);

        Demanda demandaSalva = demandaService.save(demanda);

//        try {
//            Chat chat = cadastroDemandaDTO.getIdChat();
//            chatService.save(chat);
//            demanda.setIdChat(chat); //< ---  Talvez tenha que adicionar essa função caso o chat não seja setado automáticamente pelo DTO
//        } catch (Exception e) {
//            throw new ErroSalvarChatException("ERROR 0003: Erro ao salvar o chat da demanda!" + "\n" + e.getMessage());
//        }

        try {
            for (BusinessUnity businessUnity : cadastroDemandaDTO.getBusBeneficiadas()) {
                BusBeneficiadas busBeneficiadas = new BusBeneficiadas();
                busBeneficiadas.setIdBusinessUnity(businessUnity);
                busBeneficiadas.setIdDemanda(demandaSalva);
            }
        } catch (Exception e) {
            throw new ErroCadastrarBusBeneficiadas("ERROR 0004: Erro ao cadastrar as Business Unities beneficiadas!" + "\n" + e.getMessage());
        }

        for (Beneficio beneficio : cadastroDemandaDTO.getBeneficios()) {
            Beneficio beneficioSalvo = beneficioService.save(beneficio);
            System.out.println(beneficioSalvo.toString());
        }

        return ResponseEntity.ok(demandaSalva);
    }
}
