package br.sc.weg.sid.controller;

import br.sc.weg.sid.DTO.CadastroPropostaDTO;
import br.sc.weg.sid.model.entities.Demanda;
import br.sc.weg.sid.model.entities.Proposta;
import br.sc.weg.sid.model.entities.ResponsaveisNegocio;
import br.sc.weg.sid.model.entities.Status;
import br.sc.weg.sid.model.service.DemandaService;
import br.sc.weg.sid.model.service.PropostaService;
import br.sc.weg.sid.model.service.ResponsaveisNegocioService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@CrossOrigin
@RequestMapping("/sid/api/proposta")
public class PropostaController {
    @Autowired
    private PropostaService propostaService;

    @Autowired
    private ResponsaveisNegocioService responsaveisNegocioService;

    @Autowired
    private DemandaService demandaService;

    @PostMapping()
    ResponseEntity<Object> cadastrarProsposta(@RequestBody @Valid CadastroPropostaDTO cadastroPropostaDTO) {
        try {
            Proposta proposta = new Proposta();

            Optional<Demanda> demandaOptional = demandaService.findById(cadastroPropostaDTO.getIdDemanda().getIdDemanda());

            if (demandaOptional.isPresent() && demandaOptional.get().getStatusDemanda() != Status.CANCELADA) {
                Demanda demanda = demandaOptional.get();
                demanda.setLinkJira(cadastroPropostaDTO.getLinkJira());
                demandaService.save(demanda);
            } else {
                return ResponseEntity.badRequest().body("ERROR 0006: A demanda inserida não existe ou foi reprovada! ID DEMANDA: " + cadastroPropostaDTO.getIdDemanda().getIdDemanda());
            }

            BeanUtils.copyProperties(cadastroPropostaDTO, proposta);

            Proposta propostaSalva = propostaService.save(proposta);

            for (ResponsaveisNegocio responsaveisNegocio : cadastroPropostaDTO.getResponsaveisNegocio()) {
                try {
                    responsaveisNegocio.setIdProposta(propostaSalva);
                    responsaveisNegocioService.save(responsaveisNegocio);
                } catch (Exception e) {
                    propostaService.deleteById(propostaSalva.getIdProposta());
                    return ResponseEntity.badRequest().body("ERROR 0004: Erro ao salvar responsável pelo negócio, a proposta não foi salva!" + "\nMessage: " + e.getMessage());
                }
            }


            return ResponseEntity.ok(propostaSalva);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR 0001: Erro ao cadastrar proposta!" + "\nMessage: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Object> deletarProposta(@PathVariable("id") Integer id) {
        List<ResponsaveisNegocio> responsaveisNegocio = responsaveisNegocioService.findAllByIdProposta(propostaService.findById(id).get());
        try {
            for (ResponsaveisNegocio responsaveisNegocio1 : responsaveisNegocio) {
                try {
                    responsaveisNegocioService.deleteById(responsaveisNegocio1.getIdResponsaveisNegocio());
                } catch (Exception e) {
                    return ResponseEntity.badRequest().body("ERROR 0005: Erro ao deletar responsável pelo negócio, a proposta não foi deletada!" + "\nMessage: " + e.getMessage());
                }
            }
            propostaService.deleteById(id);
            return ResponseEntity.ok("Proposta deletada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR 0002: Erro ao deletar proposta!" + "\nMessage: " + e.getMessage());
        }
    }

    @GetMapping
    ResponseEntity<Object> listarPropostas() {
        try {
            return ResponseEntity.ok(propostaService.findAll());
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
            return ResponseEntity.ok(propostaService.findByIdDemanda(demanda.get()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR 0003: Erro ao listar propostas!" + "\nMessage: " + e.getMessage());
        }
    }

    @GetMapping("/payback/{value}")
    ResponseEntity<Object> listarPropostaPorPayback(@PathVariable("value") Double value) {
        try {
            return ResponseEntity.ok(propostaService.findAllByPaybackProposta(value));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR 0003: Erro ao listar propostas!" + "\nMessage: " + e.getMessage());
        }
    }
}
