package br.sc.weg.sid.controller;

import br.sc.weg.sid.DTO.CadastroPautaDTO;
import br.sc.weg.sid.model.entities.Pauta;
import br.sc.weg.sid.model.entities.PautaResumida;
import br.sc.weg.sid.model.entities.Proposta;
import br.sc.weg.sid.model.entities.PropostaResumida;
import br.sc.weg.sid.model.service.PautaService;
import br.sc.weg.sid.model.service.PropostaService;
import br.sc.weg.sid.utils.PautaUtil;
import br.sc.weg.sid.utils.PropostaUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/sid/api/pauta")
public class PautaController {

    @Autowired
    PautaService pautaService;

    @Autowired
    PropostaService propostaService;

    /**
     * Esta função é um mapeamento de requisição HTTP POST que cadastra uma pauta no banco de dados.
     * @param cadastroPautaDTO - Parâmetro que representa um objeto DTO com os dados da pauta.
     * @return ResponseEntity<Object> - Retorna um objeto ResponseEntity com status 200 e o corpo contendo a pauta cadastrada no banco de dados.
     * @throws Exception - Retorna uma mensagem de erro caso não seja possível cadastrar a pauta no banco de dados.
     * @throws Exception - Retorna uma mensagem de erro caso não seja possível encontrar uma proposta cadastrada no banco de dados com o id informado.
     * @throws Exception - Retorna uma mensagem de erro caso não seja possível encontrar uma pauta cadastrada no banco de dados com o id informado.
     */
    @PostMapping
    public ResponseEntity<Object> cadastroPauta(@RequestBody @Valid CadastroPautaDTO cadastroPautaDTO) {
        Pauta pauta = new Pauta();
        BeanUtils.copyProperties(cadastroPautaDTO, pauta);
        Pauta pautaSalva = pautaService.save(pauta);
        List<Proposta> propostas = pautaSalva.getPropostasPauta();
        List<Proposta> propostasEncontradas = new ArrayList<>();
        for (Proposta proposta : propostas) {
            propostasEncontradas.add(propostaService.findById(proposta.getIdProposta()).get());
        }
        for (Proposta proposta : propostasEncontradas) {
            List<Pauta> pautas = proposta.getPautaProposta();
            pautas.add(pautaSalva);
            proposta.setPautaProposta(pautas);
            propostaService.save(proposta);
        }
        return ResponseEntity.ok("Pauta cadastrada com sucesso! \n" + pautaSalva);
    }

    /**
     * Esta função é um mapeamento de requisição HTTP GET que lista todas as pautas cadastradas no banco de dados.
     * @return ResponseEntity<Object> - Retorna um objeto ResponseEntity com status 200 e o corpo contendo uma lista de pautas cadastradas no banco de dados.
     * @throws Exception - Retorna uma mensagem de erro caso não seja possível listar as pautas cadastradas no banco de dados.
     */
    @GetMapping
    public ResponseEntity<Object> listarPautas() {
        try {
            List<Pauta> pautas = pautaService.findAll();
            List<PautaResumida> pautasResumidas = PautaUtil.converterPautaParaPautaResumida(pautas);
            if (pautasResumidas.isEmpty()) {
                return ResponseEntity.ok().body("Nenhuma pauta cadastrada!");
            }
            return ResponseEntity.ok(pautasResumidas);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body("Erro ao listar pautas!");
    }

    /**
     * Esta função é um mapeamento de requisição HTTP GET que lista uma pauta cadastrada no banco de dados de acordo com o id informado.
     * @param id - Parâmetro que representa o id da pauta.
     * @return ResponseEntity<Object> - Retorna um objeto ResponseEntity com status 200 e o corpo contendo a pauta cadastrada no banco de dados.
     * @throws Exception - Retorna uma mensagem de erro caso não seja possível encontrar uma pauta cadastrada no banco de dados com o id informado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> listarPautaPorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(pautaService.findById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao listar pauta: " + e.getMessage());
        }
    }

    /**
     * Esta função é um mapeamento de requisição HTTP GET que lista todas as propostas cadastradas no banco de dados de acordo com o id da pauta informado.
     * @param id - Parâmetro que representa o id da pauta.
     * @return ResponseEntity<Object> - Retorna um objeto ResponseEntity com status 200 e o corpo contendo uma lista de propostas cadastradas no banco de dados.
     * @throws Exception - Retorna uma mensagem de erro caso não seja possível encontrar uma pauta cadastrada no banco de dados com o id informado.
     */
    @GetMapping("/propostas/{id}")
    public ResponseEntity<Object> listarPropostasPorPauta(@PathVariable Integer id) {
        try {
            List<Proposta> propostasPauta = pautaService.findById(id).get().getPropostasPauta();
            List<PropostaResumida> propostasResumidas = PropostaUtil.converterPropostaParaPropostaResumida(propostasPauta);
            if (propostasResumidas.isEmpty()) {
                return ResponseEntity.ok().body("Nenhuma proposta cadastrada para essa pauta!");
            }
            return ResponseEntity.ok().body(propostasResumidas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Pauta com id informado não existe! " + e.getMessage());
        }
    }

    /**
     * Esta função é um mapeamento de requisição HTTP DELETE que deleta uma pauta cadastrada no banco de dados de acordo com o id informado.
     * @param id - Parâmetro que representa o id da pauta.
     * @return ResponseEntity<Object> - Retorna um objeto ResponseEntity com status 200 e o corpo contendo uma mensagem de sucesso.
     * @throws Exception - Retorna uma mensagem de erro caso não seja possível encontrar uma pauta cadastrada no banco de dados com o id informado.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarPauta(@PathVariable Integer id) {
        try {
            pautaService.deleteById(id);
            return ResponseEntity.ok("Pauta deletada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao deletar pauta: " + e.getMessage());
        }
    }

}
