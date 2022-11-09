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
    BusinessUnityService businessUnityService;

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
        System.out.println("Tamanho da lista de unidades: " + businessUnities.get(0).getNomeBusinessUnity());

        Demanda demanda = new Demanda();
        demanda.setSecaoTIResponsavel(Secao.TI);
        demanda.setStatusDemanda(Status.BACKLOG);
        demanda.setTamanhoDemanda(Tamanho.GRANDE);
        demanda.setSecaoTIResponsavel(Secao.TI);
        BeanUtils.copyProperties(cadastroDemandaDTO, demanda);

        Demanda demandaSalva = demandaService.save(demanda);

        for (BusinessUnity bus : cadastroDemandaDTO.getBusBeneficiadas()) {
            System.out.println("Entrou: " + bus.getNomeBusinessUnity());
            if (!businessUnities.contains(bus)) {
                throw new ErroCadastrarBusBeneficiadas("ERROR 0004: A BusinessUnity "+ bus.getIdBusinessUnity() +" não existe no banco de dados!");
            }else {
                BusBeneficiadas busBeneficiadas = new BusBeneficiadas();
                busBeneficiadas.setIdBusinessUnity(bus);
                busBeneficiadas.setIdDemanda(demandaSalva);
            }
        }


        if (!businessUnities.contains(cadastroDemandaDTO.getIdBuSolicitante())) {
            throw new ErroCadastrarBusBeneficiadas("ERROR 0004: A Bu do solicitante não existe no banco de dados!");
        }

//        try {
//            Chat chat = cadastroDemandaDTO.getIdChat();
//            chatService.save(chat);
//            demanda.setIdChat(chat); //< ---  Talvez tenha que adicionar essa função caso o chat não seja setado automáticamente pelo DTO
//        } catch (Exception e) {
//            throw new ErroSalvarChatException("ERROR 0003: Erro ao salvar o chat da demanda!" + "\n" + e.getMessage());
//        }


        for (Beneficio beneficio : cadastroDemandaDTO.getBeneficios()) {
            Beneficio beneficioSalvo = beneficioService.save(beneficio);
            System.out.println(beneficioSalvo.toString());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(demandaSalva);
    }
}
