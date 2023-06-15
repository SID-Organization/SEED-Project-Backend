package br.sc.weg.sid.controller;

import br.sc.weg.sid.model.entities.Ata;
import br.sc.weg.sid.model.entities.PdfAta;
import br.sc.weg.sid.model.entities.PropostasLog;
import br.sc.weg.sid.model.enums.TipoAta;
import br.sc.weg.sid.model.service.AtaService;
import br.sc.weg.sid.model.service.GerarPDFAtaService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/sid/api/ata-PDF")
@AllArgsConstructor
public class GerarPDFAtaController {

    GerarPDFAtaService gerarPDFAtaService;

    AtaService ataService;


    /**
     * Esta função é um mapeamento de requisição HTTP GET que retorna o PDF da ata de acordo com o id da ata informado.
     *
     * @param idAta - Parâmetro que representa o id da ata.
     * @return ResponseEntity<Object> - Retorna um objeto ResponseEntity com status 200 e o corpo contendo o PDF da ata.
     * @throws Exception - Retorna uma mensagem de erro caso não exista uma ata com o id informado.
     */
    @GetMapping("/gerar-pdf/{idAta}")
    public ResponseEntity<Object> generatePDF(@PathVariable("idAta") Integer idAta) throws Exception {

        if (ataService.existsById(idAta)) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "ata-num" + idAta + ".pdf");
            Ata ata = ataService.findById(idAta).get();
            Ata ataPublicada = new Ata();
            Ata ataNaoPublicada = new Ata();
            BeanUtils.copyProperties(ata, ataPublicada);
            BeanUtils.copyProperties(ata, ataNaoPublicada);

            List<PropostasLog> listaPropostasPublicadas = new ArrayList<>();
            List<PropostasLog> listaPropostasNaoPublicadas = new ArrayList<>();

            // Filtra as propostas por tipo
            ata.getPropostasLog().forEach(propostaLog -> {
                if (propostaLog.getTipoAtaPropostaLog() == TipoAta.PUBLICADA) {
                    listaPropostasPublicadas.add(propostaLog);
                } else if (propostaLog.getTipoAtaPropostaLog() == TipoAta.NAO_PUBLICADA) {
                    listaPropostasNaoPublicadas.add(propostaLog);
                }
            });

            List<PdfAta> listaPDFAta = new ArrayList<>();
            if (!listaPropostasPublicadas.isEmpty()) {
                ataPublicada.setPropostasLog(listaPropostasPublicadas);
                byte[] pdfAtaPublicada = gerarPDFAtaService.export(ataPublicada);
                PdfAta pdfAtaPublicadaClasse = new PdfAta();
                pdfAtaPublicadaClasse.setPdfAta(pdfAtaPublicada);
                pdfAtaPublicadaClasse.setTipoAta(TipoAta.PUBLICADA);
                listaPDFAta.add(pdfAtaPublicadaClasse);
            }

            if (!listaPropostasNaoPublicadas.isEmpty()) {
                ataNaoPublicada.setPropostasLog(listaPropostasNaoPublicadas);
                byte[] pdfAtaNaoPublicada = gerarPDFAtaService.export(ataNaoPublicada);
                PdfAta pdfAtaNaoPublicadaClasse = new PdfAta();
                pdfAtaNaoPublicadaClasse.setPdfAta(pdfAtaNaoPublicada);
                pdfAtaNaoPublicadaClasse.setTipoAta(TipoAta.NAO_PUBLICADA);
                listaPDFAta.add(pdfAtaNaoPublicadaClasse);
            }
            ata.setPdfAta(listaPDFAta);
            ataService.save(ata);
            return ResponseEntity.ok().headers(headers).body("PDF gerado com sucesso!");
        } else {
            return ResponseEntity.badRequest().body("ERROR 0006: A ata inserida não existe! ID ATA: " + idAta);
        }
    }
}
