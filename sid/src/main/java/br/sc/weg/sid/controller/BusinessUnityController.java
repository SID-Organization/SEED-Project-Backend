package br.sc.weg.sid.controller;

import br.sc.weg.sid.DTO.CadastroBusinessUnityDTO;
import br.sc.weg.sid.model.entities.BusinessUnity;
import br.sc.weg.sid.model.entities.CentroCusto;
import br.sc.weg.sid.model.service.BusinessUnityService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/sid/api/business-unity")
public class BusinessUnityController {

    @Autowired
    private BusinessUnityService businessUnityService;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping()
    public ResponseEntity<Object> findAll() {
        List<BusinessUnity> businessUnities = businessUnityService.findAll();
        if (businessUnities.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma Business Unity encontrada!");
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(businessUnities);
    }

    @CrossOrigin(origins = "http://localhost:3000")
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

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/id/{idBusinessUnity}")
    public ResponseEntity<Object> findByIdBusinessUnity(@PathVariable Integer idBusinessUnity) {
        if(!businessUnityService.existsById(idBusinessUnity)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Business Unity com o id: " + idBusinessUnity + " não existe!");
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(businessUnityService.findById(idBusinessUnity).get());
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/nome/{nameBusinessUnity}")
    public ResponseEntity<Object> findByNameBusinessUnity(@PathVariable String nameBusinessUnity) {
        Optional<BusinessUnity> businessUnity = businessUnityService.findByNomeBusinessUnity(nameBusinessUnity);
        if (businessUnity.isPresent()) {
            return ResponseEntity.status(HttpStatus.FOUND).body(businessUnity.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Business Unity com o nome: " + nameBusinessUnity + " não existe!");
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/{idBusinessUnity}")
    public ResponseEntity<Object> update(@PathVariable Integer idBusinessUnity, @RequestBody CadastroBusinessUnityDTO businessUnityDTO) {
        if(!businessUnityService.existsById(idBusinessUnity)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Business Unity com o id: " + idBusinessUnity + " não existe!");
        }
        Optional<BusinessUnity> businessUnityOptional = businessUnityService.findById(idBusinessUnity);
        BusinessUnity businessUnity = businessUnityOptional.get();
        BeanUtils.copyProperties(businessUnityDTO, businessUnity);
        return ResponseEntity.status(HttpStatus.OK).body(businessUnityService.save(businessUnity));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/{idBusinessUnity}")
    public ResponseEntity<Object> delete(@PathVariable Integer idBusinessUnity) {
        if(!businessUnityService.existsById(idBusinessUnity)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Business Unity com o id: " + idBusinessUnity + " não existe!");
        }
        businessUnityService.deleteById(idBusinessUnity);
        return ResponseEntity.status(HttpStatus.OK).body("Business Unity com o id: " + idBusinessUnity + " deletada com sucesso!");
    }

}
