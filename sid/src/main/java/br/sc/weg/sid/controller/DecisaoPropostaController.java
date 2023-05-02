package br.sc.weg.sid.controller;

import br.sc.weg.sid.DTO.CadastroDecisaoPropostaDTO;
import br.sc.weg.sid.model.entities.Ata;
import br.sc.weg.sid.model.entities.DecisaoProposta;
import br.sc.weg.sid.model.entities.Pauta;
import br.sc.weg.sid.model.entities.Proposta;
import br.sc.weg.sid.model.service.AtaService;
import br.sc.weg.sid.model.service.DecisaoPropostaService;
import br.sc.weg.sid.model.service.PautaService;
import br.sc.weg.sid.model.service.PropostaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/sid/api/decisao-proposta")
public class DecisaoPropostaController {

    @Autowired
    private DecisaoPropostaService decisaoPropostaService;

    @Autowired
    private PropostaService propostaService;

    @Autowired
    private AtaService ataService;

    @Autowired
    private PautaService pautaService;

    /**
     * Endpoint para cadastrar uma nova decisão para uma proposta.
     * @param cadastroDecisaoPropostaDTO DTO contendo os dados da decisão da proposta a ser cadastrada.
     * @return ResponseEntity com o status HTTP 201 CREATED e a decisão da proposta cadastrada no corpo da resposta em caso de sucesso,
     * ou com o status HTTP 400 BAD REQUEST e uma mensagem de erro em caso de falha.
     */
    @PostMapping
    public ResponseEntity<Object> cadastraDecisaoProposta(@RequestBody @Valid CadastroDecisaoPropostaDTO cadastroDecisaoPropostaDTO) {
        DecisaoProposta decisaoProposta = new DecisaoProposta();
        BeanUtils.copyProperties(cadastroDecisaoPropostaDTO, decisaoProposta);
        try {
            DecisaoProposta decisaoPropostaSalva = decisaoPropostaService.save(decisaoProposta);
            return ResponseEntity.status(HttpStatus.CREATED).body(decisaoPropostaSalva);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao cadastrar decisão da proposta: " + e.getMessage());
        }
    }


    /**
     * Retorna todas as decisões de propostas cadastradas.
     *
     * @return ResponseEntity contendo a lista de todas as decisões de propostas cadastradas.
     * @throws Exception caso ocorra algum erro ao buscar as decisões de propostas.
     */
    @GetMapping
    public ResponseEntity<Object> findAll() {
        try {
            return ResponseEntity.ok(decisaoPropostaService.findAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao buscar decisões das propostas: " + e.getMessage());
        }
    }

    /**
     * Exclui uma decisão de proposta pelo ID.
     * @param id ID da decisão de proposta a ser excluída passado por URL
     * @return ResponseEntity com mensagem de sucesso caso a exclusão tenha sido realizada com sucesso,
     * ou ResponseEntity com mensagem de erro caso contrário.
     * @throws Exception caso ocorra algum erro ao excluir a decisão de proposta.
    */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Integer id) {
        try {
            decisaoPropostaService.deleteById(id);
            return ResponseEntity.ok().body("Decisão da proposta excluída com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao deletar decisão da proposta: " + e.getMessage());
        }
    }


    /**
     * Busca todas as decisões de proposta associadas a uma determinada pauta.
     * @param id o ID da pauta associada às decisões de proposta que serão buscadas passado pela URL.
     * @return um ResponseEntity contendo a lista de decisões de proposta encontradas, ou uma mensagem de erro caso ocorra alguma exceção.
     */
    @GetMapping("/pauta/{id}")
    public ResponseEntity<Object> findByPautaDecisaoProposta(@PathVariable Integer id) {
        try {
            Optional<Pauta> pautaOptional = pautaService.findById(id);
            if (pautaOptional.isPresent()) {
                return ResponseEntity.ok(decisaoPropostaService.findByPautaDecisaoProposta(pautaOptional.get()));
            } else {
                return ResponseEntity.badRequest().body("Pauta não encontrada");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao buscar decisões das propostas: " + e.getMessage());
        }
    }


    /**
     * Retorna as decisões relacionadas a uma determinada proposta.
     * @param id o ID da proposta a ser buscada passado pela URL.
     * @return um ResponseEntity contendo as decisões relacionadas à proposta encontrada, ou uma mensagem de erro se não for possível encontrar a proposta.
     */
    @GetMapping("/proposta/{id}")
    public ResponseEntity<Object> findByPropostaDecisaoProposta(@PathVariable Integer id) {
        try {
            Optional<Proposta> proposta = propostaService.findById(id);
            if (proposta.isPresent()) {
                return ResponseEntity.ok(decisaoPropostaService.findByPropostaDecisaoProposta(proposta.get()));
            } else {
                return ResponseEntity.badRequest().body("Proposta não encontrada");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao buscar decisões das propostas: " + e.getMessage());
        }
    }

    /**
     * Endpoint responsável por buscar todas as decisões das propostas relacionadas a uma ata específica.
     *
     * @param id O id da ata pela qual deseja buscar as decisões das propostas.
     * @return ResponseEntity com a lista de decisões das propostas relacionadas à ata encontrada.
     * Caso a ata não seja encontrada, retorna um ResponseEntity com status HTTP 400 e uma mensagem de erro.
     * Caso ocorra algum erro ao buscar as decisões das propostas, retorna um ResponseEntity com status HTTP 400 e uma mensagem de erro.
     */
    @GetMapping("/ata/{id}")
    public ResponseEntity<Object> findByAtaDecisaoProposta(@PathVariable Integer id) {
        try {
            Optional<Ata> ata = ataService.findById(id);
            if (ata.isPresent()) {
                return ResponseEntity.ok(decisaoPropostaService.findByAtaDecisaoProposta(ata.get()));
            } else {
                return ResponseEntity.badRequest().body("Ata não encontrada");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao buscar decisões das propostas: " + e.getMessage());
        }
    }
}
