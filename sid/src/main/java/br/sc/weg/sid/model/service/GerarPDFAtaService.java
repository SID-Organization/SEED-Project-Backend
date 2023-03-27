package br.sc.weg.sid.model.service;

import br.sc.weg.sid.model.entities.Ata;
import br.sc.weg.sid.model.entities.Demanda;
import br.sc.weg.sid.model.entities.PdfProposta;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.List;
import com.lowagie.text.Rectangle;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.Optional;

@Service
public class GerarPDFAtaService {

    @Autowired
    AtaService ataService;

    @Autowired
    PdfPropostaService pdfPropostaService;

    public void export(HttpServletResponse response, Integer idAta) throws IOException {
        Document document = new Document(PageSize.A4, 40, 20, 25, 15);
        PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());

        Ata ata = ataService.findById(idAta).get();
        writer.setPageEvent(new PdfPageEventHelper() {
            @Override
            public void onStartPage(PdfWriter writer, Document document) {
                super.onStartPage(writer, document);
                try {
                    Image image = Image.getInstance("src/main/resources/images/weg-logo.png");
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

        Color fontFirstTitleColor = new Color(0, 112, 192);
        Font fontFirstTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, fontFirstTitleColor);

        Color fontProposalTitleColor = Color.decode("#4472c4");
        Font fontProposalTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, fontProposalTitleColor);
        Font textFont = FontFactory.getFont(FontFactory.HELVETICA, 10);


        Paragraph paragraph = new Paragraph("ATA REUNIÃO COMISSÃO PROCESSOS DE VENDAS E DESENVOLVIMENTO DE PRODUTO\n", fontFirstTitle);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);

        PdfPTable table = new PdfPTable(2);
        Font tableFontBold = new Font(Font.TIMES_ROMAN, 10, Font.BOLD);
        Font tableFont = new Font(Font.TIMES_ROMAN, 10);
        table.setHorizontalAlignment(Paragraph.ALIGN_RIGHT);
        table.setSpacingAfter(5);
        table.setWidths(new int[]{25, 30});
        table.setWidthPercentage(20);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        PdfPCell celulaTabelainicial;

        celulaTabelainicial = new PdfPCell(new Phrase("ATA Nº", tableFontBold));
        celulaTabelainicial.setBorder(Rectangle.NO_BORDER);
        celulaTabelainicial.setHorizontalAlignment(Paragraph.ALIGN_JUSTIFIED);
        table.addCell(celulaTabelainicial);

        celulaTabelainicial = new PdfPCell(new Phrase(ata.getIdAta().toString(), tableFontBold));
        celulaTabelainicial.setBorder(Rectangle.NO_BORDER);
        celulaTabelainicial.setHorizontalAlignment(Paragraph.ALIGN_RIGHT);
        table.addCell(celulaTabelainicial);

        celulaTabelainicial = new PdfPCell(new Phrase("Data", tableFontBold));
        celulaTabelainicial.setBorder(Rectangle.NO_BORDER);
        celulaTabelainicial.setHorizontalAlignment(Paragraph.ALIGN_JUSTIFIED);
        table.addCell(celulaTabelainicial);

        celulaTabelainicial = new PdfPCell(new Phrase(ata.getPautaAta().getDataReuniaoPauta().toString(), tableFontBold));
        celulaTabelainicial.setBorder(Rectangle.NO_BORDER);
        celulaTabelainicial.setHorizontalAlignment(Paragraph.ALIGN_RIGHT);
        table.addCell(celulaTabelainicial);

        celulaTabelainicial = new PdfPCell(new Phrase("Início", tableFont));
        celulaTabelainicial.setBorder(Rectangle.NO_BORDER);
        celulaTabelainicial.setHorizontalAlignment(Paragraph.ALIGN_JUSTIFIED);
        table.addCell(celulaTabelainicial);

        celulaTabelainicial = new PdfPCell(new Phrase(ata.getPautaAta().getHorarioInicioPauta().toString(), tableFont));
        celulaTabelainicial.setBorder(Rectangle.NO_BORDER);
        celulaTabelainicial.setHorizontalAlignment(Paragraph.ALIGN_RIGHT);
        table.addCell(celulaTabelainicial);

        celulaTabelainicial = new PdfPCell(new Phrase("Término", tableFont));
        celulaTabelainicial.setBorder(Rectangle.NO_BORDER);
        celulaTabelainicial.setHorizontalAlignment(Paragraph.ALIGN_JUSTIFIED);
        table.addCell(celulaTabelainicial);

        celulaTabelainicial = new PdfPCell(new Phrase(ata.getPautaAta().getHorarioTerminoPauta().toString(), tableFont));
        celulaTabelainicial.setBorder(Rectangle.NO_BORDER);
        celulaTabelainicial.setHorizontalAlignment(Paragraph.ALIGN_RIGHT);
        table.addCell(celulaTabelainicial);

        ata.getPautaAta().getPropostasPauta().forEach(proposta -> {
            Demanda demanda = proposta.getDemandaProposta();

            java.util.List<PdfProposta> pdfPropostasList = pdfPropostaService.findByProposta(proposta);
            Optional<PdfProposta> pdfProposta = Optional.ofNullable(pdfPropostasList.get(pdfPropostasList.size() - 1));

            String tituloCaixaAltaDemanda = demanda.getTituloDemanda().toUpperCase();

            Paragraph titleDemandParagraph = new Paragraph(tituloCaixaAltaDemanda + " – " + demanda.getIdDemanda(), fontProposalTitle);
            List listTitle = new List(List.ORDERED);
            listTitle.add(new ListItem(titleDemandParagraph));

            Phrase proposalPhrase = new Phrase("Objetivo: ", fontFirstTitle);
            Chunk objetivoChunk = new Chunk(demanda.getPropostaMelhoriaDemanda(), textFont);
            proposalPhrase.add(objetivoChunk);
            Paragraph proposalParagraph = new Paragraph(proposalPhrase);
            proposalParagraph.setSpacingBefore(8);

            Paragraph projectScopeParagraph = new Paragraph("Escopo do Projeto:", fontFirstTitle);
            projectScopeParagraph.setSpacingBefore(8);

            Paragraph projectScopeParagraphText = new Paragraph(pdfProposta.get().getEscopoPropostaHTML(), textFont);
            projectScopeParagraphText.setSpacingAfter(5);

            HTMLWorker htmlWorker = new HTMLWorker(document);

            String projectScopeParagraphTextHTML = pdfProposta.get().getEscopoPropostaHTML();

        });


        Paragraph titleProposalParagraph = new Paragraph("REPLANEJAMENTO DATA DE ENTREGA DE OVS EM MASSA – 1000025759", fontProposalTitle);
        titleProposalParagraph.setAlignment(Paragraph.ALIGN_JUSTIFIED);
        List listTitle = new List(List.ORDERED);
        listTitle.add(new ListItem(titleProposalParagraph));
        listTitle.setIndentationLeft(32);

        Paragraph requesterParagraph = new Paragraph("Solicitante:", fontProposalTitle);
        requesterParagraph.setSpacingBefore(8);

        Paragraph requesterNamesParagraph = new Paragraph("NOME GERENTE SOLICITANTE – Departamento RH / Corporativo\n" +
                "NOME CHEFE SOLICITANTE - Seção Recrutamento e Remuneração / Corporativo\n", textFont);
        requesterNamesParagraph.setIndentationLeft(10);

        Paragraph proposalParagraph = new Paragraph("Proposta:", fontProposalTitle);
        proposalParagraph.setSpacingBefore(8);
        proposalParagraph.setSpacingAfter(5);

        Paragraph anteriorAprovationsParagraph = new Paragraph("Aprovações Anteriores:", fontProposalTitle);
        anteriorAprovationsParagraph.setSpacingBefore(8);
        anteriorAprovationsParagraph.setSpacingAfter(5);

        Paragraph actualSituationParagraph = new Paragraph("Situação Atual:", fontProposalTitle);
        actualSituationParagraph.setSpacingBefore(8);
        actualSituationParagraph.setSpacingAfter(5);


        Paragraph projectScopeParagraph = new Paragraph("Escopo do Projeto:", fontProposalTitle);
        projectScopeParagraph.setSpacingBefore(8);
        projectScopeParagraph.setSpacingAfter(5);

        document.add(paragraph);
        document.add(table);
        document.add(listTitle);
        document.add(requesterParagraph);
        document.add(requesterNamesParagraph);
        document.add(proposalParagraph);

        document.add(anteriorAprovationsParagraph);

        document.add(actualSituationParagraph);

        document.add(projectScopeParagraph);

        document.newPage();

        Paragraph evaluatedAlternativesParagraph = new Paragraph("Alternativas Avaliadas:", fontProposalTitle);
        evaluatedAlternativesParagraph.setSpacingBefore(8);
        evaluatedAlternativesParagraph.setSpacingAfter(5);


        document.add(evaluatedAlternativesParagraph);
        document.close();

    }

}
