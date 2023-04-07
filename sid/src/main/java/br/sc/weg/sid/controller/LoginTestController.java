package br.sc.weg.sid.controller;

import br.sc.weg.sid.model.entities.CentroCustoTabelaCusto;
import br.sc.weg.sid.model.entities.Usuario;
import br.sc.weg.sid.model.service.CentroCustoTabelaCustoService;
import br.sc.weg.sid.model.service.TabelaCustoLinhaService;
import br.sc.weg.sid.model.service.TabelaCustoService;
import br.sc.weg.sid.model.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import br.sc.weg.sid.DTO.TesteLoginDTO;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/sid/api/login")
@CrossOrigin
public class LoginTestController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TabelaCustoService tabelaCustoService;

    @Autowired
    private CentroCustoTabelaCustoService centroCustoTabelaCustoService;

    @Autowired
    private TabelaCustoLinhaService tabelaCustoLinhaService;

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid TesteLoginDTO testeLoginDTO) {
       Optional<Usuario> usuarioOptional = usuarioService.findByNumeroCadastroUsuario(testeLoginDTO.getNumeroCadastroUsuario());
       if(usuarioOptional.isPresent()){
           System.out.println("Usuario encontrado: " + usuarioOptional.get());
           if (usuarioOptional.get().getSenhaUsuario().equals(testeLoginDTO.getSenhaUsuario())) {
               return ResponseEntity.ok(usuarioOptional.get());
           }
       }
         return ResponseEntity.status(404).body("Login não realizado, matricula ou senha incorretos!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Integer id) {
        centroCustoTabelaCustoService.deleteByTabelaCusto(tabelaCustoService.findById(id).get());
        tabelaCustoLinhaService.deleteByTabelaCusto(tabelaCustoService.findById(id).get());


        return ResponseEntity.ok("a");
    }

}
