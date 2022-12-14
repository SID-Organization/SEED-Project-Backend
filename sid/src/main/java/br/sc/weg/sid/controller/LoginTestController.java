package br.sc.weg.sid.controller;

import br.sc.weg.sid.model.entities.Usuario;
import br.sc.weg.sid.model.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import br.sc.weg.sid.DTO.TesteLoginDTO;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/sid/api/login")
@CrossOrigin
public class LoginTestController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid TesteLoginDTO testeLoginDTO) {
       Optional<Usuario> usuarioOptional = usuarioService.findByNumeroCadastroUsuario(testeLoginDTO.getNumeroCadastroUsuario());
       if(usuarioOptional.isPresent()){
           if (usuarioOptional.get().getSenhaUsuario().equals(testeLoginDTO.getSenhaUsuario())) {
               return ResponseEntity.ok(usuarioOptional.get());
           }
       }
         return ResponseEntity.status(404).body("Login não realizado, matricula ou senha incorretos!");
    }

}
