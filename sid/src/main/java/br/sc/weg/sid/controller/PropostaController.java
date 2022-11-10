package br.sc.weg.sid.controller;

import br.sc.weg.sid.DTO.CadastroPropostaDTO;
import br.sc.weg.sid.model.entities.Proposta;
import br.sc.weg.sid.model.entities.ResponsaveisNegocio;
import br.sc.weg.sid.model.service.PropostaService;
import br.sc.weg.sid.model.service.ResponsaveisNegocioService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/sid/api/proposta")
public class PropostaController {
    @Autowired
    PropostaService propostaService;

    @Autowired
    ResponsaveisNegocioService responsaveisNegocioService;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping()
    ResponseEntity<Object> cadastrarProsposta(@RequestBody @Valid CadastroPropostaDTO cadastroPropostaDTO) {
        System.out.println("passou aqui");
        try {
            Proposta proposta = new Proposta();
            BeanUtils.copyProperties(cadastroPropostaDTO, proposta);
            Proposta propostaSalva = propostaService.save(proposta);

            for (ResponsaveisNegocio responsaveisNegocio : cadastroPropostaDTO.getResponsaveisNegocio()) {
                try {
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
}
