package br.sc.weg.sid.controller;

import br.sc.weg.sid.model.entities.Usuario;
import br.sc.weg.sid.model.enums.Cargo;
import br.sc.weg.sid.model.service.UsuarioService;
import br.sc.weg.sid.utils.UsuarioUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
@RequestMapping("/sid/api/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Esta função é um mapeamento de requisição HTTP POST que cadastra um novo usuário no banco de dados.
     *
     * @param fotoUsuario - Parâmetro que representa a foto do usuário.
     * @param usuarioJson - Parâmetro que representa os dados do usuário em um formato de JSON, que será convertido para um objeto do tipo Usuario.
     * @return ResponseEntity<Object> - Retorna um objeto ResponseEntity com status 200 e o corpo contendo o usuário cadastrado no banco de dados.
     * @throws Exception        - Retorna uma mensagem de erro caso não seja possível converter a imagem do usuário.
     * @throws RuntimeException - Retorna uma mensagem de erro caso o usuário já esteja cadastrado no banco de dados.
     * @throws RuntimeException - Retorna uma mensagem de erro caso não seja possível salvar o usuário no banco de dados.
     * @throws RuntimeException - Retorna uma mensagem de erro caso não seja possível converter o JSON do usuário.
     */
    @PostMapping
    public ResponseEntity<Object> cadastroUsuario(
            @RequestParam(value = "fotoUsuario", required = false) MultipartFile fotoUsuario,
            @RequestParam(value = "usuario") String usuarioJson
    ) {
        UsuarioUtil usuarioUtil = new UsuarioUtil();

        Usuario usuario = usuarioUtil.convertJsonToModel(usuarioJson);

        try {
            usuario.setFotoUsuario(fotoUsuario.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter a imagem");
        }
        if (usuarioService.existsById(usuario.getNumeroCadastroUsuario())) {
            return ResponseEntity.status(400).body("Usuário já cadastrado!");
        }

        return ResponseEntity.ok("Usuário cadastrado com sucesso! \n" + usuarioService.save(usuario));
    }

    /**
     * Esta função é um mapeamento de requisição HTTP DELETE que deleta um usuário do banco de dados.
     *
     * @param id - Parâmetro que representa o id do usuário (numeroCadastroUsuario).
     * @return ResponseEntity<Object> - Retorna um objeto ResponseEntity com status 200 e o corpo contendo uma mensagem de sucesso.
     * @throws Exception        - Retorna uma mensagem de erro caso não seja possível deletar o usuário com o id informado.
     * @throws RuntimeException - Retorna uma mensagem de erro caso não seja possível deletar o usuário com o id informado.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Integer id) {
        try {
            usuarioService.deleteById(id);
            return ResponseEntity.ok("Usuário deletado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao deletar usuário com id: " + id + "\n Message: " + e.getMessage());
        }
    }

    /**
     * Esta função é um mapeamento de requisição HTTP PUT que atualiza um usuário no banco de dados.
     *
     * @param id          - Parâmetro que representa o id do usuário (numeroCadastroUsuario).
     * @param fotoUsuario - Parâmetro que representa a foto do usuário.
     * @param usuarioJson - Parâmetro que representa os dados do usuário em um formato de JSON, que será convertido para um objeto do tipo Usuario.
     * @return ResponseEntity<Object> - Retorna um objeto ResponseEntity com status 200 e o corpo contendo o usuário atualizado no banco de dados.
     * @throws Exception        - Retorna uma mensagem de erro caso não seja possível converter a imagem do usuário.
     * @throws RuntimeException - Retorna uma mensagem de erro caso não seja possível salvar o usuário no banco de dados.
     * @throws RuntimeException - Retorna uma mensagem de erro caso não seja possível converter o JSON do usuário.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Object> update(
            @PathVariable("id") Integer id,
            @RequestParam(value = "fotoUsuario", required = false) MultipartFile fotoUsuario,
            @RequestParam(value = "usuario") String usuarioJson
    ) {
        UsuarioUtil usuarioUtil = new UsuarioUtil();

        Usuario usuario = usuarioService.findById(id).get();

        Usuario usuarioNovo = usuarioUtil.convertJsonToModel(usuarioJson);

        try {
            BeanUtils.copyProperties(usuarioNovo, usuario, "numeroCadastroUsuario");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter o JSON do usuário");
        }

        Usuario usuarioSalvo = usuarioService.save(usuario);
        return ResponseEntity.ok("Usuário atualizado com sucesso! \n" + usuarioSalvo);
    }

    @PutMapping("/tutorial/{numeroCadastroUsuario}")
    public ResponseEntity<Object> updateTutorial(
            @PathVariable("numeroCadastroUsuario") Integer numeroCadastroUsuario
    ) {
        Usuario usuario = usuarioService.findById(numeroCadastroUsuario).get();
        try {
            usuario.setTutorialUsuario(false);
            Usuario usuarioSalvo = usuarioService.save(usuario);
            return ResponseEntity.ok(usuarioSalvo);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Erro ao atualizar usuário com id: " + numeroCadastroUsuario + "\n Message: " + e.getMessage());
        }
    }

    /**
     * Esta função é um mapeamento de requisição HTTP GET que retorna todos os usuários cadastrados no banco de dados.
     * Caso não exista nenhum usuário cadastrado, retorna uma mensagem de erro.
     * Caso exista, retorna todos os usuários cadastrados.
     * @return ResponseEntity<Object> - Retorna um objeto ResponseEntity com status 200 e o corpo contendo todos os usuários cadastrados no banco de dados.
     * @throws RuntimeException - Retorna uma mensagem de erro caso não exista nenhum usuário cadastrado no banco de dados.
     * @throws RuntimeException - Retorna uma mensagem de erro caso não seja possível retornar todos os usuários cadastrados no banco de dados.
     */
    @GetMapping
    public ResponseEntity<Object> findAll() {
        if (usuarioService.findAll().isEmpty()) {
            return ResponseEntity.status(404).body("Nenhum usuário encontrado!");
        }
        return ResponseEntity.ok(usuarioService.findAll());
    }

    /**
     * Esta função é um mapeamento de requisição HTTP GET que retorna um usuário cadastrado no banco de dados.
     * Caso não exista nenhum usuário cadastrado com o id informado, retorna uma mensagem de erro.
     * Caso exista, retorna o usuário cadastrado com o id informado.
     * @param numeroCadastro - Parâmetro que representa o id do usuário (numeroCadastroUsuario).
     * @return ResponseEntity<Object> - Retorna um objeto ResponseEntity com status 200 e o corpo contendo o usuário cadastrado com o id informado.
     * @throws RuntimeException - Retorna uma mensagem de erro caso não exista nenhum usuário cadastrado com o id informado.
     * @throws RuntimeException - Retorna uma mensagem de erro caso não seja possível retornar o usuário cadastrado com o id informado.
     * @throws RuntimeException - Retorna uma mensagem de erro caso não seja possível converter o JSON do usuário.
     * @throws RuntimeException - Retorna uma mensagem de erro caso não seja possível retornar o usuário cadastrado com o id informado.
     */
    @GetMapping("/{numeroCadastro}")
    public ResponseEntity<Object> findByNumeroCadastro(@PathVariable("numeroCadastro") Integer numeroCadastro) {
        if (usuarioService.findByNumeroCadastroUsuario(numeroCadastro) == null) {
            return ResponseEntity.status(404).body("Usuário não encontrado!");
        }
        return ResponseEntity.ok(usuarioService.findByNumeroCadastroUsuario(numeroCadastro));
    }

    /**
     * Esta função é um mapeamento de requisição HTTP GET que retorna todos os usuários cadastrados no banco de dados com o departamento informado.
     * Caso não exista nenhum usuário cadastrado com o departamento informado, retorna uma mensagem de erro.
     * Caso exista, retorna todos os usuários cadastrados com o departamento informado.
     * @param departamento - Parâmetro que representa o departamento do usuário (departamentoUsuario).
     * @return ResponseEntity<Object> - Retorna um objeto ResponseEntity com status 200 e o corpo contendo todos os usuários cadastrados com o departamento informado.
     * @throws RuntimeException - Retorna uma mensagem de erro caso não exista nenhum usuário cadastrado com o departamento informado.
     * @throws RuntimeException - Retorna uma mensagem de erro caso não seja possível retornar todos os usuários cadastrados com o departamento informado.
     * @throws RuntimeException - Retorna uma mensagem de erro caso não seja possível converter o JSON do usuário.
     */
    @GetMapping("/departamento/{departamento}")
    public ResponseEntity<Object> findByDepartamento(@PathVariable("departamento") String departamento) {
        if (usuarioService.findAllByDepartamentoUsuario(departamento).isEmpty()) {
            return ResponseEntity.status(404).body("Nenhum usuário encontrado!");
        }
        return ResponseEntity.ok(usuarioService.findAllByDepartamentoUsuario(departamento));
    }

    @GetMapping("/analistas")
    public ResponseEntity<Object> findByCargo() {
        if (usuarioService.findAllByCargoUsuario(Cargo.ANALISTA).isEmpty()) {
            return ResponseEntity.status(404).body("Nenhum usuário encontrado!");
        }
        return ResponseEntity.ok(usuarioService.findAllByCargoUsuario(Cargo.ANALISTA));
    }
}
