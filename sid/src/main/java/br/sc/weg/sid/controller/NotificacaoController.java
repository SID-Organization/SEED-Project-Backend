package br.sc.weg.sid.controller;

import br.sc.weg.sid.DTO.CadastroNotificaoDTO;
import br.sc.weg.sid.model.entities.Notificacao;
import br.sc.weg.sid.model.service.NotificacaoService;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/sid/api/notificacao")
public class NotificacaoController {

    @Autowired
    private NotificacaoService notificacaoService;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid CadastroNotificaoDTO cadastroNotificaoDTO) {
        Notificacao notificacao = new Notificacao();
        BeanUtils.copyProperties(cadastroNotificaoDTO, notificacao);
        try {
            Notificacao notificacaoSalva = notificacaoService.save(notificacao);
            return ResponseEntity.status(HttpStatus.CREATED).body(notificacaoSalva);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping
    public ResponseEntity<Object> findAll() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(notificacaoService.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable Integer id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(notificacaoService.findById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Integer id) {
        try {
            notificacaoService.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Notificação excluída com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
