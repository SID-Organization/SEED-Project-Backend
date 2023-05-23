package br.sc.weg.sid.controller;

import br.sc.weg.sid.model.entities.Ata;
import br.sc.weg.sid.model.entities.AtaDG;
import br.sc.weg.sid.model.entities.PdfAta;
import br.sc.weg.sid.model.enums.TipoAta;
import br.sc.weg.sid.model.service.AtaDGService;
import br.sc.weg.sid.model.service.AtaService;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin
@RequestMapping("/sid/api/ata-dg")
@AllArgsConstructor
public class AtaDGController {

    AtaDGService ataDGService;

    GerarPDFAtaController gerarPDFAtaController;

    AtaService ataService;
    @GetMapping
    public ResponseEntity<Object> findAll() {
        try {
            List<AtaDG> atasDG = ataDGService.findAll();
            if (atasDG.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma ata encontrada");
            }
            return ResponseEntity.ok(atasDG);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao buscar atas: " + e.getMessage());
        }
    }

    @GetMapping("/{idAtaDG}")
    public ResponseEntity<Object> findById(@PathVariable Integer idAtaDG) {
        try {
            AtaDG ataDG = ataDGService.findById(idAtaDG).get();

            return ResponseEntity.ok(ataDG);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao buscar ata: " + e.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity<Object> save(Integer idAta) {
        try {
            AtaDG ataDG = new AtaDG();
            Ata ata = ataService.findById(idAta).get();
            ataDG.setAtaAtaDg(ata);
            AtaDG ataDGSalva = ataDGService.save(ataDG);
            return ResponseEntity.ok(ataDGSalva);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro ao salvar ata: " + e.getMessage());
        }
    }

    @GetMapping("/gera-pdf-ata-dg/{idAtaDG}")
    ResponseEntity<Object> listarAtaPdf(@PathVariable("idAtaDG") Integer idAtaDG) throws Exception {
        AtaDG ataDGSalva = ataDGService.findById(idAtaDG).get();
        gerarPDFAtaController.generatePDF(ataDGSalva.getAtaAtaDg().getIdAta());
        return ResponseEntity.ok().body("PDF's gerados com sucesso!");
    }

    /**
     * Retorna o arquivo PDF da ata publicada correspondente ao ID fornecido.
     *
     * O método recebe o ID da ata desejada como parâmetro na URL e retorna o PDF correspondente da ata publicada.
     * Se a ata existir, é construído um cabeçalho para o PDF e retornado um ResponseEntity contendo o arquivo PDF.
     * Caso contrário, uma mensagem de erro é retornada. O método pode lançar uma exceção em caso de falha na busca do PDF da ata.
     *
     * @param idAtaDG ID da ata publicada a ser buscada.
     * @return ResponseEntity contendo o arquivo PDF da ata publicada ou uma mensagem de erro em caso de falha.
     * @throws RuntimeException se ocorrer algum erro na busca do PDF da ata.
     */
    @GetMapping("/pdf-ata-dg-publicada/{idAtaDG}")
    ResponseEntity<Object> listarAtaPublicadaPdf(@PathVariable("idAtaDG") Integer idAtaDG) {
        return listarAtaPdf(idAtaDG, TipoAta.PUBLICADA);
    }

    /**
     * Retorna o arquivo PDF da ata não publicada correspondente ao ID fornecido.
     *
     * O método recebe o ID da ata desejada como parâmetro na URL e retorna o PDF correspondente da ata não publicada.
     * Se a ata existir, é construído um cabeçalho para o PDF e retornado um ResponseEntity contendo o arquivo PDF.
     * Caso contrário, uma mensagem de erro é retornada. O método pode lançar uma exceção em caso de falha na busca do PDF da ata.
     *
     * @param idAtaDG ID da ata não publicada a ser buscada.
     * @return ResponseEntity contendo o arquivo PDF da ata não publicada ou uma mensagem de erro em caso de falha.
     * @throws RuntimeException se ocorrer algum erro na busca do PDF da ata.
     */
    @GetMapping("/pdf-ata-dg-nao-publicada/{idAtaDG}")
    ResponseEntity<Object> listarAtaNaoPublicadaPdf(@PathVariable("idAtaDG") Integer idAtaDG) {
        return listarAtaPdf(idAtaDG, TipoAta.NAO_PUBLICADA);
    }

    /**
     * Este método é responsável por retornar o arquivo PDF correspondente a uma ata específica, seja ela publicada
     * ou não publicada, com base no ID fornecido. Ele oferece duas variáveis: uma para retornar o PDF da ata publicada
     * e outra para retornar o PDF da ata não publicada.
     *
     * Ao receber o ID da ata desejada como parâmetro na URL, o método busca a ata correspondente no serviço de atendimento.
     * Em seguida, ele percorre a lista de PDFs associados à ata para encontrar o PDF com o tipo de ata desejado.
     * Caso seja encontrado um PDF correspondente, o método constrói um cabeçalho adequado para o arquivo PDF e retorna
     * um ResponseEntity contendo o arquivo.
     * Se nenhum PDF correspondente for encontrado, uma mensagem de erro é retornada informando que a ata não possui PDF
     * do tipo solicitado.
     * Se a ata com o ID fornecido não existir, é retornada uma mensagem de erro indicando que a ata não existe.
     * Em caso de falha durante o processo de busca do PDF da ata, uma exceção pode ser lançada.
     *
     * @param idAtaDG ID da ata desejada.
     * @param tipoAta Tipo de ata desejado (PUBLICADA ou NAO_PUBLICADA).
     * @return ResponseEntity contendo o arquivo PDF da ata correspondente ou uma mensagem de erro em caso de falha.
     * @throws RuntimeException se ocorrer algum erro na busca do PDF da ata.
     */

    private ResponseEntity<Object> listarAtaPdf(Integer idAtaDG, TipoAta tipoAta) {
        try {
            if (ataDGService.existsById(idAtaDG)) {
                AtaDG ataDG = ataDGService.findById(idAtaDG).get();
                List<PdfAta> pdfAtaList = ataDG.getPdfAtaDG();
                PdfAta pdfAta = null;

                for (PdfAta pdf : pdfAtaList) {
                    if (pdf.getTipoAta().equals(tipoAta)) {
                        pdfAta = pdf;
                        break;
                    }
                }

                if (pdfAta != null) {
                    byte[] pdfBytes = pdfAta.getPdfAta();

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_PDF);
                    headers.setContentDisposition(ContentDisposition.builder("inline")
                            .filename("pdf-ata-" + tipoAta.name().toLowerCase() + "-" + ataDG.getIdAtaDG() + ".pdf").build());

                    return ResponseEntity.ok().headers(headers).body(pdfBytes);
                } else {
                    return ResponseEntity.badRequest().body("ERROR 0008: A ata da DG de número" + ataDG.getIdAtaDG() + " não possui um PDF de tipo "
                            + tipoAta.getDescricao() + "!");
                }
            } else {
                return ResponseEntity.badRequest().body("ERROR 0007: A ata da DG inserida não existe! ID ATA: " + idAtaDG);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body("ERROR 0005: Erro ao buscar PDF da ata de ID: " + idAtaDG + "!");
    }


}
