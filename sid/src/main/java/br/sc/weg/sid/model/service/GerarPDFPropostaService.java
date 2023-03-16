package br.sc.weg.sid.model.service;

import br.sc.weg.sid.model.entities.Beneficio;
import br.sc.weg.sid.model.entities.Demanda;
import br.sc.weg.sid.model.entities.PdfProposta;
import br.sc.weg.sid.model.entities.Proposta;
import com.lowagie.text.*;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.lowagie.text.List;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GerarPDFPropostaService {

    private final DemandaService demandaService;

    private final PdfPropostaService pdfPropostaService;

    public ResponseEntity<byte[]> export(HttpServletResponse response, Integer idDemanda, Proposta proposta) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 40, 40, 25, 15);
        PdfWriter writer = PdfWriter.getInstance(document, baos);

        Optional<Demanda> demanda = demandaService.findById(idDemanda);
        Optional<PdfProposta> pdfProposta = pdfPropostaService.findByProposta(proposta);
        writer.setPageEvent(new PdfPageEventHelper() {
            @Override
            public void onStartPage(PdfWriter writer, Document document) {
                super.onStartPage(writer, document);
                try {
                    Image image = image = Image.getInstance("src/main/resources/images/weg-logo.png");
                    image.setAlignment(Image.ALIGN_RIGHT);
                    image.scaleAbsolute(55, 40);
                    document.add(image);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public void onEndPage(PdfWriter writer, Document document) {
                Font font = new Font( Font.HELVETICA, 12, Font.NORMAL);

                Phrase phrase = new Phrase(String.format("%d", writer.getPageNumber()), font);
                phrase.setLeading(10);
                ColumnText.showTextAligned(writer.getDirectContent(), com.itextpdf.text.Element.ALIGN_RIGHT, phrase, 559, 40, 0);
            }
        });

        document.open();

        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
        Font textFont = FontFactory.getFont(FontFactory.HELVETICA, 10);


        Paragraph paragraph = new Paragraph("PROPOSTA PARA COMITÊ/COMISSÃO DE PROCESSOS\n", fontTitle);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);

        LocalDate localDate = LocalDate.now();
        Date dataRegistroArquivo = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT);

        Font dateFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);

        Paragraph dateParagraph = new Paragraph(dateFormatter.format(dataRegistroArquivo), dateFont);
        dateParagraph.setAlignment(Paragraph.ALIGN_RIGHT);
        dateParagraph.setSpacingAfter(5);

        String tituloCaixaAltaDemanda = demanda.get().getTituloDemanda().toUpperCase();

        Paragraph titleDemandParagraph = new Paragraph(tituloCaixaAltaDemanda + " – " + demanda.get().getIdDemanda(), fontTitle);
        List listTitle = new List(List.ORDERED);
        listTitle.add(new ListItem(titleDemandParagraph));

        Phrase requesterPhrase = new Phrase("Solicitante: ", fontTitle);
        Chunk solicitanteChunk = new Chunk(demanda.get().getSolicitanteDemanda().getNomeUsuario() + " - " +
                demanda.get().getSolicitanteDemanda().getDepartamentoUsuario(), textFont);
        requesterPhrase.add(solicitanteChunk);
        Paragraph requesterParagraph = new Paragraph(requesterPhrase);
        requesterParagraph.setSpacingBefore(8);


        Phrase proposalPhrase = new Phrase("Objetivo: ", fontTitle);
        Chunk objetivoChunk = new Chunk(demanda.get().getPropostaMelhoriaDemanda(), textFont);
        proposalPhrase.add(objetivoChunk);
        Paragraph proposalParagraph = new Paragraph(proposalPhrase);
        proposalParagraph.setSpacingBefore(8);
        proposalParagraph.setSpacingAfter(5);

        Paragraph actualSituationParagraph = new Paragraph("Situação Atual - Problema a ser tratado/ resolvido:", fontTitle);
        actualSituationParagraph.setSpacingBefore(8);

        Paragraph actualSituationParagraphText = new Paragraph(demanda.get().getSituacaoAtualDemanda(), textFont);
        actualSituationParagraphText.setSpacingAfter(5);

        Paragraph projectScopeParagraph = new Paragraph("Escopo do Projeto:", fontTitle);
        projectScopeParagraph.setSpacingBefore(8);






        Paragraph projectScopeParagraphText = new Paragraph(pdfProposta.get().getEscopoPropostaHTML(), textFont);
        projectScopeParagraphText.setSpacingAfter(5);

        Paragraph noPartOfScopeProjectParagraph;
        if(!pdfProposta.get().getNaoFazParteDoEscopoPropostaHTML().isEmpty()){
            noPartOfScopeProjectParagraph = new Paragraph("Não faz parte do escopo do projeto:", fontTitle);
            projectScopeParagraph.setSpacingBefore(8);
        }else {
            noPartOfScopeProjectParagraph = new Paragraph();
        }

        if(!pdfProposta.get().getNaoFazParteDoEscopoPropostaHTML().isEmpty()){
            String projectScopeParagraphTextHTML = pdfProposta.get().getEscopoPropostaHTML();

            HTMLWorker htmlWorker = new HTMLWorker(document);
            htmlWorker.parse(new StringReader(projectScopeParagraphTextHTML));
        }

        Paragraph evaluatedAlternativesParagraph = new Paragraph("Alternativas Avaliadas:", fontTitle);
        evaluatedAlternativesParagraph.setSpacingBefore(13);

        Paragraph evaluatedAlternativesParagraphText = new Paragraph(pdfProposta.get().getAlternativasAvaliadasPropostaHTML(), textFont);
        evaluatedAlternativesParagraphText.setSpacingAfter(5);

        Paragraph projectCoverageParagraph = new Paragraph("Abrangência do Projeto:", fontTitle);
        projectCoverageParagraph.setSpacingBefore(8);

        Paragraph projectCoverageParagraphText = new Paragraph(pdfProposta.get().getAbrangenciaProjetoPropostaHTML(), textFont);
        projectCoverageParagraphText.setSpacingAfter(5);

        Paragraph mainRisksParagraph = new Paragraph("Riscos Principais/Plano de Mitigação:", fontTitle);
        mainRisksParagraph.setSpacingBefore(8);

        Paragraph mainRisksParagraphText = new Paragraph(pdfProposta.get().getPlanoMitigacaoPropostaHTML(), textFont);
        mainRisksParagraphText.setSpacingAfter(5);

        java.util.List<Beneficio> beneficiosDemanda = pdfProposta.get().getProposta().getDemandaProposta().getBeneficiosDemanda();

        Paragraph expectedResultsQualitativeParagraphText = new Paragraph();
        Paragraph expectedResultsPotentialParagraphText = new Paragraph();
        for (Beneficio beneficio : beneficiosDemanda) {
            if (beneficio.getTipoBeneficio().equals("Qualitativo")) {
                expectedResultsQualitativeParagraphText = new Paragraph(beneficio.getDescricaoBeneficio(), textFont);
                expectedResultsQualitativeParagraphText.setSpacingAfter(5);
            } else if (beneficio.getTipoBeneficio().equals("Potencial")) {
                expectedResultsPotentialParagraphText = new Paragraph(beneficio.getDescricaoBeneficio(), textFont);
                expectedResultsPotentialParagraphText.setSpacingAfter(5);
            }
        }
        Paragraph expectedResultsParagraph = null;
        if (expectedResultsQualitativeParagraphText != null){
            expectedResultsParagraph = new Paragraph("Resultados Esperados (Qualitativos):", fontTitle);
            expectedResultsParagraph.setSpacingBefore(8);
        }

        Paragraph expectedResultsPotentialParagraph = new Paragraph("Resultados Esperados (Qualitativos):", fontTitle);
        expectedResultsPotentialParagraph.setSpacingBefore(8);



        Phrase executionPeriodPhrase = new Phrase("Período de execução: ", fontTitle);
        Chunk executionPeriodChunk = new Chunk(pdfProposta.get().getProposta().getPeriodoExecucaoInicioProposta().toString() + "à" +
                pdfProposta.get().getProposta().getPeriodoExecucaoFimProposta().toString(), textFont);
        executionPeriodPhrase.add(executionPeriodChunk);
        Paragraph executionPeriodParagraph = new Paragraph(requesterPhrase);
        executionPeriodParagraph.setSpacingBefore(8);

        Phrase paybackPhrase = new Phrase("Payback: ", fontTitle);
        Chunk paybackChunk = new Chunk(pdfProposta.get().getProposta().getPaybackProposta().toString(), textFont);
        paybackPhrase.add(paybackChunk);
        Paragraph paybackParagraph = new Paragraph(paybackPhrase);
        paybackParagraph.setSpacingBefore(8);




        document.add(paragraph);
        document.add(dateParagraph);
        document.add(listTitle);
        document.add(requesterParagraph);
        document.add(proposalParagraph);


        document.add(actualSituationParagraph);
        document.add(actualSituationParagraphText);

        document.add(projectScopeParagraph);
        document.add(projectScopeParagraphText);

        if (!pdfProposta.get().getNaoFazParteDoEscopoPropostaHTML().isEmpty()) {
            document.add(noPartOfScopeProjectParagraph);
        }

        document.add(evaluatedAlternativesParagraph);
        document.add(evaluatedAlternativesParagraphText);

        document.add(projectCoverageParagraph);
        document.add(projectCoverageParagraphText);

        document.add(mainRisksParagraph);
        document.add(mainRisksParagraphText);

        if (expectedResultsQualitativeParagraphText != null){
            document.add(expectedResultsParagraph);
            document.add(expectedResultsQualitativeParagraphText);
        }

        document.add(expectedResultsPotentialParagraph);
        document.add(expectedResultsPotentialParagraphText);


        document.close();


        byte[] pdfBytes = baos.toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "documento.pdf");


        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}
