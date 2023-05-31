package br.sc.weg.sid.controller;

import br.sc.weg.sid.DTO.CadastroBusinessUnityDTO;
import br.sc.weg.sid.model.entities.BusinessUnity;
import br.sc.weg.sid.model.service.BusinessUnityService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/sid/api/business-unity")
public class BusinessUnityController {

    /**
     * Injeção de dependência para utilizar os métodos da classe BusinessUnityService.
     */
    @Autowired
    private BusinessUnityService businessUnityService;


    /**
     * Busca todas as Business Unities cadastradas no sistema.
     *
     * @return ResponseEntity com status HTTP e a lista de Business Unities encontradas, ou mensagem de erro caso não exista nenhuma.
     */
    @GetMapping()
    public ResponseEntity<Object> findAll() {
        List<BusinessUnity> businessUnities = businessUnityService.findAll();
        if (businessUnities.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma Business Unity encontrada!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(businessUnities);
    }

    /**
     * Cadastra uma nova Business Unity.
     *
     * @param businessUnityDTO DTO contendo as informações da Business Unity a ser cadastrada.
     * @return ResponseEntity com status CREATED e a Business Unity cadastrada ou BAD_REQUEST caso ocorra um erro no cadastro.
     */
    @PostMapping()
    public ResponseEntity<Object> save(@RequestBody CadastroBusinessUnityDTO businessUnityDTO) {
        try {
            BusinessUnity businessUnity = new BusinessUnity();
            BeanUtils.copyProperties(businessUnityDTO, businessUnity);
            return ResponseEntity.status(HttpStatus.CREATED).body(businessUnityService.save(businessUnity));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao cadastrar Business Unity: " + e.getMessage());
        }
    }

    /**
     * Busca uma Business Unity pelo seu ID.
     *
     * @param idBusinessUnity ID da Business Unity a ser buscada, passado como parâmetro pela URL.
     * @return ResponseEntity com status OK e a Business Unity buscada ou NOT_FOUND caso não exista uma Business Unity com o ID informado.
     */
    @GetMapping("/id/{idBusinessUnity}")
    public ResponseEntity<Object> findByIdBusinessUnity(@PathVariable Integer idBusinessUnity) {
        if (!businessUnityService.existsById(idBusinessUnity)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Business Unity com o id: " + idBusinessUnity + " não existe!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(businessUnityService.findById(idBusinessUnity).get());
    }


    /**
     * Retorna a Business Unity com o nome especificado.
     *
     * @param nameBusinessUnity o nome da Business Unity a ser encontrada, passado como parâmetro pela URL.
     * @return ResponseEntity com a Business Unity encontrada, se existir, ou uma mensagem de erro se não existir.
     */
    @GetMapping("/nome/{nameBusinessUnity}")
    public ResponseEntity<Object> findByNameBusinessUnity(@PathVariable String nameBusinessUnity) {
        Optional<BusinessUnity> businessUnity = businessUnityService.findByNomeBusinessUnity(nameBusinessUnity);
        if (businessUnity.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(businessUnity.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Business Unity com o nome: " + nameBusinessUnity + " não existe!");
    }

    /**
     * Atualiza a Business Unity com o ID especificado.
     *
     * @param idBusinessUnity  o ID da Business Unity a ser atualizada, passado como parâmetro pela URL.
     * @param businessUnityDTO os dados da Business Unity atualizada.
     * @return ResponseEntity com a Business Unity atualizada, se a atualização for bem-sucedida, ou uma mensagem de erro se não for.
     */
    @PutMapping("/{idBusinessUnity}")
    public ResponseEntity<Object> update(@PathVariable Integer idBusinessUnity, @RequestBody CadastroBusinessUnityDTO businessUnityDTO) {
        if (!businessUnityService.existsById(idBusinessUnity)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Business Unity com o id: " + idBusinessUnity + " não existe!");
        }
        Optional<BusinessUnity> businessUnityOptional = businessUnityService.findById(idBusinessUnity);
        BusinessUnity businessUnity = businessUnityOptional.get();
        BeanUtils.copyProperties(businessUnityDTO, businessUnity);
        return ResponseEntity.status(HttpStatus.OK).body(businessUnityService.save(businessUnity));
    }

    /**
     * Deleta a Business Unity com o ID especificado.
     *
     * @param idBusinessUnity o ID da Business Unity a ser deletada, passado como parâmetro pela URL.
     * @return ResponseEntity com uma mensagem de sucesso, se for bem-sucedida, ou uma mensagem de erro se não for;
     */
    @DeleteMapping("/{idBusinessUnity}")
    public ResponseEntity<Object> delete(@PathVariable Integer idBusinessUnity) {
        if (!businessUnityService.existsById(idBusinessUnity)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Business Unity com o id: " + idBusinessUnity + " não existe!");
        }
        businessUnityService.deleteById(idBusinessUnity);
        return ResponseEntity.status(HttpStatus.OK).body("Business Unity com o id: " + idBusinessUnity + " deletada com sucesso!");
    }
}
