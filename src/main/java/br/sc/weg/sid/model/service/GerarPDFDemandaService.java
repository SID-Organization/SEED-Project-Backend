package br.sc.weg.sid.model.service;

import br.sc.weg.sid.model.entities.Beneficio;
import br.sc.weg.sid.model.entities.Demanda;
import br.sc.weg.sid.model.entities.PdfDemanda;
import br.sc.weg.sid.model.enums.TipoBeneficio;
import com.lowagie.text.*;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@AllArgsConstructor
public class GerarPDFDemandaService {

    DemandaService demandaService;

    PdfDemandaService pdfDemandaService;

    public byte[] export(Integer idDemanda) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 40, 20, 25, 15);
        PdfWriter writer = PdfWriter.getInstance(document, baos);

        Demanda demanda = demandaService.findById(idDemanda).get();

        List<PdfDemanda> pdfDemandsList = pdfDemandaService.findByDemanda(demanda);
        PdfDemanda pdfDemanda = new PdfDemanda();
        if (pdfDemandsList.size() > 1) {
            pdfDemanda = pdfDemandsList.get(pdfDemandsList.size() - 1);
        } else {
            pdfDemanda = pdfDemandsList.get(0);
        }
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
                Font font = new Font(Font.HELVETICA, 12, Font.NORMAL);

                Phrase phrase = new Phrase(String.format("%d", writer.getPageNumber()), font);
//                phrase.setLeading(10);
                ColumnText.showTextAligned(writer.getDirectContent(), com.itextpdf.text.Element.ALIGN_RIGHT, phrase, 559, 40, 0);
            }
        });

        document.open();


        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);

        Font textFont = new Font(Font.HELVETICA, 10);


        Paragraph titleParagraph = new Paragraph(demanda.getTituloDemanda() + " - " + demanda.getIdDemanda(), fontTitle);
        titleParagraph.setAlignment(Paragraph.ALIGN_CENTER);
        titleParagraph.setSpacingAfter(10);

        Paragraph requesterParagraph = new Paragraph("Solicitante: ", fontTitle);
        requesterParagraph.setAlignment(Paragraph.ALIGN_LEFT);
        Paragraph requesterNameParagraph = new Paragraph(demanda.getSolicitanteDemanda().getNomeUsuario() + " - " + demanda.getSolicitanteDemanda().getDepartamentoUsuario().getNomeBusinessUnity(), textFont);
        requesterNameParagraph.setAlignment(Paragraph.ALIGN_LEFT);
        requesterNameParagraph.setIndentationLeft(30);

        Paragraph businessUnitsBenefitedParagraph = new Paragraph("Business Units Beneficiadas: ", fontTitle);
        businessUnitsBenefitedParagraph.setAlignment(Paragraph.ALIGN_LEFT);
        businessUnitsBenefitedParagraph.setSpacingBefore(5);

        AtomicReference<Paragraph> businessUnitsBenefitedText = new AtomicReference<>(new Paragraph());
        businessUnitsBenefitedText.get().setSpacingAfter(5);

        demanda.getBusBeneficiadasDemanda().forEach(businessUnit -> {
            Phrase businessUnitPhrase = new Phrase(businessUnit.getSiglaBusinessUnity() + " - ", fontTitle);
            Chunk businessUnitChunk = new Chunk(businessUnit.getNomeBusinessUnity(), textFont);
            businessUnitPhrase.add(businessUnitChunk);
            businessUnitsBenefitedText.set(new Paragraph(businessUnitPhrase));
            businessUnitsBenefitedText.get().setIndentationLeft(30);
        });

        Paragraph actualSituationParagraph = new Paragraph("Situação Atual: ", fontTitle);
        actualSituationParagraph.setAlignment(Paragraph.ALIGN_LEFT);
        actualSituationParagraph.setSpacingBefore(5);

        HTMLWorker htmlWorker = new HTMLWorker(document);

        Paragraph proposalParagraph = new Paragraph("Proposta de Melhoria: ", fontTitle);
        proposalParagraph.setAlignment(Paragraph.ALIGN_LEFT);
        proposalParagraph.setSpacingBefore(5);


        Paragraph frequencyParagraph = new Paragraph("Frequência de Uso: ", fontTitle);
        frequencyParagraph.setAlignment(Paragraph.ALIGN_LEFT);
        frequencyParagraph.setSpacingBefore(5);

        Paragraph realBenefitParagraph = new Paragraph("Benefícios Reais: ", fontTitle);
        realBenefitParagraph.setAlignment(Paragraph.ALIGN_LEFT);
        realBenefitParagraph.setSpacingBefore(10);
        realBenefitParagraph.setSpacingAfter(10);

        PdfPTable tableRealBenefits = new PdfPTable(2);
        Font tableFontBold = new Font(Font.HELVETICA, 10, Font.BOLD);
        tableRealBenefits.setSpacingBefore(8);
        tableRealBenefits.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        tableRealBenefits.setWidths(new int[]{10, 10});
        tableRealBenefits.setWidthPercentage(80);
        tableRealBenefits.getDefaultCell().setBorder(Rectangle.BOX);
        tableRealBenefits.getDefaultCell().setBorderWidth(0.2f);

        PdfPCell celulaTableRealBenefits;

        celulaTableRealBenefits = new PdfPCell(new Phrase("VALOR MENSAL", tableFontBold));
        celulaTableRealBenefits.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        tableRealBenefits.addCell(celulaTableRealBenefits);

        celulaTableRealBenefits = new PdfPCell(new Phrase("DESCRIÇÃO", tableFontBold));
        celulaTableRealBenefits.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        tableRealBenefits.addCell(celulaTableRealBenefits);

        demanda.getBeneficiosDemanda().forEach(beneficio -> {
            PdfPCell celulaTableRealBenefitsForEach;
            if (beneficio.getTipoBeneficio() == TipoBeneficio.REAL) {
                celulaTableRealBenefitsForEach = new PdfPCell(new Phrase("R$" + beneficio.getValorBeneficio().toString(), textFont));
                celulaTableRealBenefitsForEach.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
                tableRealBenefits.addCell(celulaTableRealBenefitsForEach);

                celulaTableRealBenefitsForEach = new PdfPCell(new Phrase(beneficio.getMemoriaCalculoBeneficio(), textFont));
                celulaTableRealBenefitsForEach.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
                tableRealBenefits.addCell(celulaTableRealBenefitsForEach);
            }
        });

        Paragraph potentialBenefitParagraph = new Paragraph("Benefícios Potenciais: ", fontTitle);
        potentialBenefitParagraph.setAlignment(Paragraph.ALIGN_LEFT);
        potentialBenefitParagraph.setSpacingBefore(10);
        potentialBenefitParagraph.setSpacingAfter(10);

        PdfPTable tablePotentialBenefits = new PdfPTable(2);
        tablePotentialBenefits.setSpacingBefore(8);
        tablePotentialBenefits.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        tablePotentialBenefits.setWidths(new int[]{10, 10});
        tablePotentialBenefits.setWidthPercentage(80);
        tablePotentialBenefits.getDefaultCell().setBorder(Rectangle.BOX);
        tablePotentialBenefits.getDefaultCell().setBorderWidth(0.2f);

        PdfPCell celulaTablePotentialBenefits;

        celulaTablePotentialBenefits = new PdfPCell(new Phrase("VALOR MENSAL", tableFontBold));
        celulaTablePotentialBenefits.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        tablePotentialBenefits.addCell(celulaTablePotentialBenefits);

        celulaTablePotentialBenefits = new PdfPCell(new Phrase("DESCRIÇÃO", tableFontBold));
        celulaTablePotentialBenefits.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        tablePotentialBenefits.addCell(celulaTablePotentialBenefits);

        demanda.getBeneficiosDemanda().forEach(beneficio -> {
            PdfPCell celulaTablePotentialBenefitsForEach;
            if (beneficio.getTipoBeneficio() == TipoBeneficio.POTENCIAL) {
                celulaTablePotentialBenefitsForEach = new PdfPCell(new Phrase("R$" + beneficio.getValorBeneficio().toString(), textFont));
                celulaTablePotentialBenefitsForEach.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
                tablePotentialBenefits.addCell(celulaTablePotentialBenefitsForEach);

                celulaTablePotentialBenefitsForEach = new PdfPCell(new Phrase(beneficio.getMemoriaCalculoBeneficio(), textFont));
                celulaTablePotentialBenefitsForEach.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
                tablePotentialBenefits.addCell(celulaTablePotentialBenefitsForEach);
            }
        });

        Paragraph qualitativeBenefitParagraph = null;
        Paragraph qualitativeBenefitText = null;

        for (Beneficio beneficio : demanda.getBeneficiosDemanda()) {
            if (beneficio.getTipoBeneficio() == TipoBeneficio.QUALITATIVO) {
                qualitativeBenefitParagraph = new Paragraph("Benefícios Qualitativos:", fontTitle);
                qualitativeBenefitParagraph.setAlignment(Paragraph.ALIGN_LEFT);
                qualitativeBenefitParagraph.setSpacingBefore(10);
                qualitativeBenefitParagraph.setSpacingAfter(10);

                qualitativeBenefitText = new Paragraph(beneficio.getMemoriaCalculoBeneficio(), textFont);
            }
        }

        document.add(titleParagraph);

        document.add(requesterParagraph);
        document.add(requesterNameParagraph);

        document.add(businessUnitsBenefitedParagraph);
        document.add(businessUnitsBenefitedText.get());

        document.add(actualSituationParagraph);
        htmlWorker.parse(new StringReader(pdfDemanda.getSituacaoAtualDemandaHTML()));

        document.add(proposalParagraph);

        htmlWorker.parse(new StringReader(pdfDemanda.getPropostaMelhoriaDemandaHTML()));

        document.add(frequencyParagraph);
        htmlWorker.parse(new StringReader(pdfDemanda.getFrequenciaUsoDemandaHTML()));

        document.add(realBenefitParagraph);
        document.add(tableRealBenefits);

        document.add(potentialBenefitParagraph);
        document.add(tablePotentialBenefits);

        if (qualitativeBenefitParagraph != null && qualitativeBenefitText != null) {
            document.add(qualitativeBenefitParagraph);
            document.add(qualitativeBenefitText);
        }


        document.close();

        return baos.toByteArray();
    }

}
