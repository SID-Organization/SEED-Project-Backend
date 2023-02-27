package br.sc.weg.sid.model.service;

import br.sc.weg.sid.model.entities.Demanda;
import com.lowagie.text.*;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
public class GerarPDFService {

    public void export(HttpServletResponse response, Integer idDemanda) throws IOException {
        Document document = new Document(PageSize.A4, 40, 40, 25, 15);
        PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());

        DemandaService demandaService = new DemandaService();

        Optional<Demanda> demanda = demandaService.findById(idDemanda);
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

        String tituloCaixaAltaDemanda= demanda.get().getTituloDemanda().toUpperCase();

        Paragraph titleDemandParagraph = new Paragraph(tituloCaixaAltaDemanda + " – " + demanda.get().getIdDemanda(), fontTitle);
        List listTitle = new List(List.ORDERED);
        listTitle.add(new ListItem(titleDemandParagraph));

        Paragraph requesterParagraph = new Paragraph("Solicitante: " + demanda.get().getSolicitanteDemanda().getNomeUsuario() + " - " +
                demanda.get().getSolicitanteDemanda().getDepartamentoUsuario(), fontTitle);
        requesterParagraph.setSpacingBefore(8);


        Paragraph proposalParagraph = new Paragraph("Objetivo:", fontTitle);
        proposalParagraph.setSpacingBefore(8);
        proposalParagraph.setSpacingAfter(5);

        Paragraph actualSituationParagraph = new Paragraph("Situação Atual:", fontTitle);
        actualSituationParagraph.setSpacingBefore(8);
        actualSituationParagraph.setSpacingAfter(5);


        Paragraph projectScopeParagraph = new Paragraph("Escopo do Projeto:", fontTitle);
        projectScopeParagraph.setSpacingBefore(8);
        projectScopeParagraph.setSpacingAfter(5);

        document.add(paragraph);
        document.add(dateParagraph);
        document.add(listTitle);
        document.add(requesterParagraph);
        document.add(proposalParagraph);


        document.add(actualSituationParagraph);

        document.add(projectScopeParagraph);

        Paragraph evaluatedAlternativesParagraph = new Paragraph("Alternativas Avaliadas:", fontTitle);
        evaluatedAlternativesParagraph.setSpacingBefore(8);
        evaluatedAlternativesParagraph.setSpacingAfter(5);


        document.add(evaluatedAlternativesParagraph);
        document.close();

    }
}