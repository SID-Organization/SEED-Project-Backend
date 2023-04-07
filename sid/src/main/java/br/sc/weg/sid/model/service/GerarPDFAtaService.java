package br.sc.weg.sid.model.service;

import br.sc.weg.sid.model.entities.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.List;
import com.lowagie.text.Rectangle;
import com.lowagie.text.*;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Optional;

@Service
public class GerarPDFAtaService {

    @Autowired
    AtaService ataService;

    @Autowired
    PdfPropostaService pdfPropostaService;

    public byte[] export(Integer idAta) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 40, 20, 25, 15);
        PdfWriter writer = PdfWriter.getInstance(document, baos);

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
        Font fontFirstTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, fontFirstTitleColor);

        Color fontProposalTitleColor = Color.decode("#4472c4");
        Font fontProposalTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, fontProposalTitleColor);

        Font titleFont = new Font(Font.HELVETICA, 10, Font.BOLD);
        Font textFont = new Font(Font.HELVETICA, 10);


        Paragraph paragraph = new Paragraph("ATA REUNIÃO COMISSÃO PROCESSOS DE VENDAS E DESENVOLVIMENTO DE PRODUTO\n", fontFirstTitle);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);

        PdfPTable table = new PdfPTable(2);
        Font tableFontBold = new Font(Font.TIMES_ROMAN, 10, Font.BOLD);
        Font tableFont = new Font(Font.TIMES_ROMAN, 10);
        table.setHorizontalAlignment(Paragraph.ALIGN_RIGHT);
        table.setSpacingBefore(8);
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

        SimpleDateFormat formatarData = new SimpleDateFormat("dd/MM/yyyy");
        String dataReuniaoFormatada = formatarData.format(ata.getPautaAta().getDataReuniaoPauta());

        celulaTabelainicial = new PdfPCell(new Phrase(dataReuniaoFormatada, tableFontBold));
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

        document.add(paragraph);
        document.add(table);

        ata.getPropostasLogAta().forEach(propostaLog -> {

            Proposta proposta = propostaLog.getPropostaPropostaLog();

            Demanda demanda = proposta.getDemandaProposta();

            java.util.List<PdfProposta> pdfPropostasList = pdfPropostaService.findByProposta(proposta);
            Optional<PdfProposta> pdfProposta = Optional.ofNullable(pdfPropostasList.get(pdfPropostasList.size() - 1));

            String tituloCaixaAltaDemanda = demanda.getTituloDemanda().toUpperCase();

            Paragraph titleDemandParagraph = new Paragraph(tituloCaixaAltaDemanda + " – " + demanda.getIdDemanda(), fontProposalTitle);
            List listTitle = new List(List.ORDERED);
            listTitle.add(new ListItem(titleDemandParagraph));

            Phrase proposalPhrase = new Phrase("Objetivo: ", titleFont);
            Phrase objetivoPhrase = new Phrase(demanda.getPropostaMelhoriaDemanda(), textFont);
            Paragraph proposalParagraph = new Paragraph();
            proposalParagraph.add(proposalPhrase);
            proposalParagraph.add(objetivoPhrase);
            proposalParagraph.setSpacingBefore(8);

            Paragraph projectScopeParagraph = new Paragraph("Escopo do Projeto:", titleFont);
            projectScopeParagraph.setSpacingBefore(8);

            Paragraph projectScopeParagraphText = new Paragraph(pdfProposta.get().getEscopoPropostaHTML(), textFont);
            projectScopeParagraphText.setSpacingAfter(5);

            HTMLWorker htmlWorker = new HTMLWorker(document);

            String projectScopeParagraphTextHTML = pdfProposta.get().getEscopoPropostaHTML();

            Paragraph noPartOfScopeProjectParagraph;
            if(!pdfProposta.get().getNaoFazParteDoEscopoPropostaHTML().isEmpty()){
                noPartOfScopeProjectParagraph = new Paragraph("Não faz parte do escopo do projeto:", titleFont);
                projectScopeParagraph.setSpacingBefore(8);
            }else {
                noPartOfScopeProjectParagraph = new Paragraph();
            }

            String projectNotInScopeParagraphTextHTML = pdfProposta.get().getEscopoPropostaHTML();

            Paragraph projectCoverageParagraph = new Paragraph("Abrangência do Projeto:", titleFont);
            projectCoverageParagraph.setSpacingBefore(8);

            Paragraph projectCoverageParagraphText = new Paragraph(pdfProposta.get().getAbrangenciaProjetoPropostaHTML(), textFont);
            projectCoverageParagraphText.setSpacingAfter(5);

            java.util.List<Beneficio> beneficiosDemanda = pdfProposta.get().getProposta().getDemandaProposta().getBeneficiosDemanda();

            String expectedResultsQualitativeParagraphTextHTML = "";
            String expectedResultsPotentialParagraphTextHTML = "";
            for (Beneficio beneficio : beneficiosDemanda) {
                if (beneficio.getTipoBeneficio() == TipoBeneficio.QUALITATIVO) {
                    expectedResultsQualitativeParagraphTextHTML = beneficio.getMemoriaCalculoBeneficio();
                } else if (beneficio.getTipoBeneficio() == TipoBeneficio.POTENCIAL) {
                    expectedResultsPotentialParagraphTextHTML = beneficio.getMemoriaCalculoBeneficio();
                }
            }
            Paragraph expectedResultsParagraph = null;
            if (expectedResultsQualitativeParagraphTextHTML != null){
                expectedResultsParagraph = new Paragraph("Resultados Esperados (Qualitativos):", titleFont);
                expectedResultsParagraph.setSpacingBefore(8);
            }

            Paragraph expectedResultsPotentialParagraph = new Paragraph("Benefícios potenciais:", titleFont);
            expectedResultsPotentialParagraph.setSpacingBefore(8);

            String dataInicioFormatada = formatarData.format(pdfProposta.get().getProposta().getPeriodoExecucaoFimProposta());
            String dataFimFormatada = formatarData.format(pdfProposta.get().getProposta().getPeriodoExecucaoFimProposta());

            Phrase executionPeriodPhrase = new Phrase("Período de execução: ", titleFont);
            Phrase executionPeriodPhraseText = new Phrase(dataInicioFormatada + " à " +
                    dataFimFormatada, textFont);
            Paragraph executionPeriodParagraph = new Paragraph();
            executionPeriodParagraph.add(executionPeriodPhrase);
            executionPeriodParagraph.add(executionPeriodPhraseText);
            executionPeriodParagraph.setSpacingBefore(8);

            Phrase paybackPhrase = new Phrase("Payback: ", titleFont);
            Phrase paybackPhraseText = new Phrase(pdfProposta.get().getProposta().getPaybackProposta().toString(), textFont);
            Paragraph paybackParagraph = new Paragraph();
            paybackParagraph.add(paybackPhrase);
            paybackParagraph.add(paybackPhraseText);
            paybackParagraph.setSpacingBefore(8);

            Phrase businessResponsable = new Phrase("Responsável Negócio: ", titleFont);
            Phrase responsablePhraseText = new Phrase(proposta.getNomeResponsavelNegocio() + " - "
                    + proposta.getAreaResponsavelNegocio(), textFont);
            Paragraph businessResponsableParagraph = new Paragraph();
            businessResponsableParagraph.add(businessResponsable);
            businessResponsableParagraph.add(responsablePhraseText);
            proposalParagraph.setSpacingBefore(8);

            Font commissionOpinionText = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD);
            commissionOpinionText.setStyle(Font.UNDERLINE);

            Phrase commissionOpinionPhrase = new Phrase("PARECER COMISSÃO", commissionOpinionText);
            Phrase commissionOpinionPhraseText = new Phrase(": " + propostaLog.getParecerComissaoPropostaLog().toString(), titleFont);
            Paragraph commissionOpinionParagraph = new Paragraph();
            commissionOpinionParagraph.add(commissionOpinionPhrase);
            commissionOpinionParagraph.add(commissionOpinionPhraseText);
            commissionOpinionParagraph.setSpacingBefore(8);

            document.add(listTitle);
            document.add(proposalParagraph);


            document.add(projectScopeParagraph);
            try {
                htmlWorker.parse(new StringReader(projectScopeParagraphTextHTML));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (!pdfProposta.get().getNaoFazParteDoEscopoPropostaHTML().isEmpty()) {
                document.add(noPartOfScopeProjectParagraph);
                try {
                    htmlWorker.parse(new StringReader(projectNotInScopeParagraphTextHTML));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            document.add(projectCoverageParagraph);
            document.add(projectCoverageParagraphText);

            if (expectedResultsQualitativeParagraphTextHTML != null){
                document.add(expectedResultsParagraph);
                try {
                    htmlWorker.parse(new StringReader(expectedResultsQualitativeParagraphTextHTML));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            document.add(expectedResultsPotentialParagraph);
            try {
                htmlWorker.parse(new StringReader(expectedResultsPotentialParagraphTextHTML));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            document.add(executionPeriodParagraph);

            document.add(paybackParagraph);

            document.add(businessResponsableParagraph);

            document.add(commissionOpinionParagraph);
        });
        document.close();

        return baos.toByteArray();
    }

}
