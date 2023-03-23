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
@CrossOrigin
@RequestMapping("/sid/api/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

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
        if (usuarioService.existsById(usuario.getNumeroCadastroUsuario())){
            return ResponseEntity.status(400).body("Usuário já cadastrado!");
        }

        return ResponseEntity.ok("Usuário cadastrado com sucesso! \n" + usuarioService.save(usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Integer id) {
        try {
            usuarioService.deleteById(id);
            return ResponseEntity.ok("Usuário deletado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao deletar usuário com id: " + id + "\n Message: " + e.getMessage());
        }
    }

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

    @GetMapping
    public ResponseEntity<Object> findAll() {
        if (usuarioService.findAll().isEmpty()) {
            return ResponseEntity.status(404).body("Nenhum usuário encontrado!");
        }
        return ResponseEntity.ok(usuarioService.findAll());
    }

    @GetMapping("/{numeroCadastro}")
    public ResponseEntity<Object> findByNumeroCadastro(@PathVariable("numeroCadastro") Integer numeroCadastro) {
        if (usuarioService.findByNumeroCadastroUsuario(numeroCadastro) == null) {
            return ResponseEntity.status(404).body("Usuário não encontrado!");
        }
        return ResponseEntity.ok(usuarioService.findByNumeroCadastroUsuario(numeroCadastro));
    }
    
    @GetMapping("/departamento/{departamento}")
    public ResponseEntity<Object> findByDepartamento(@PathVariable("departamento") String departamento) {
        if (usuarioService.findAllByDepartamentoUsuario(departamento).isEmpty()) {
            return ResponseEntity.status(404).body("Nenhum usuário encontrado!");
        }
        return ResponseEntity.ok(usuarioService.findAllByDepartamentoUsuario(departamento));
    }
}
