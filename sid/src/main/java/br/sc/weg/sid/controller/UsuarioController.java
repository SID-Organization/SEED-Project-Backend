package br.sc.weg.sid.controller;

import br.sc.weg.sid.model.entities.Usuario;
import br.sc.weg.sid.model.service.UsuarioService;
import br.sc.weg.sid.utils.UsuarioUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/sid/api/usuario")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping
    public ResponseEntity<Object> cadastroUsuario(
            @RequestParam(value = "fotoUsuario", required = false) MultipartFile fotoUsuario,
            @RequestParam(value = "usuario") String usuarioJson
    ) {
        UsuarioUtil usuarioUtil = new UsuarioUtil();

        Usuario usuario = usuarioUtil.convertJsonToModel(usuarioJson);

        try{
            usuario.setFotoUsuario(fotoUsuario.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter a imagem");
        }

        Usuario usuarioSalvo = usuarioService.save(usuario);
        return ResponseEntity.ok("Usuário cadastrado com sucesso! \n" + usuarioSalvo);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Integer id) {
        try {
            usuarioService.deleteById(id);
            return ResponseEntity.ok("Usuário deletado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao deletar usuário com id: " + id + "\n Message: " + e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/{id}")
    public ResponseEntity<Object> update(
            @PathVariable("id") Integer id,
            @RequestParam(value = "fotoUsuario", required = false) MultipartFile fotoUsuario,
            @RequestParam(value = "usuario") String usuarioJson
    ) {
        UsuarioUtil usuarioUtil = new UsuarioUtil();

        Usuario usuario = usuarioUtil.convertJsonToModel(usuarioJson);

        try{
            usuario.setFotoUsuario(fotoUsuario.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter a imagem");
        }

        Usuario usuarioSalvo = usuarioService.save(usuario);
        return ResponseEntity.ok("Usuário atualizado com sucesso! \n" + usuarioSalvo);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping
    public ResponseEntity<Object> findAll() {
        if (usuarioService.findAll().isEmpty()) {
            return ResponseEntity.status(404).body("Nenhum usuário encontrado!");
        }
        return ResponseEntity.ok(usuarioService.findAll());
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{numeroCadastro}")
    public ResponseEntity<Object> findByNumeroCadastro(@PathVariable("numeroCadastro") Integer numeroCadastro) {
        if (usuarioService.findByNumeroCadastroUsuario(numeroCadastro) == null) {
            return ResponseEntity.status(404).body("Usuário não encontrado!");
        }
        return ResponseEntity.ok(usuarioService.findByNumeroCadastroUsuario(numeroCadastro));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/departamento/{departamento}")
    public ResponseEntity<Object> findByDepartamento(@PathVariable("departamento") String departamento) {
        if (usuarioService.findAllByDepartamentoUsuario(departamento).isEmpty()) {
            return ResponseEntity.status(404).body("Nenhum usuário encontrado!");
        }
        return ResponseEntity.ok(usuarioService.findAllByDepartamentoUsuario(departamento));
    }

}
