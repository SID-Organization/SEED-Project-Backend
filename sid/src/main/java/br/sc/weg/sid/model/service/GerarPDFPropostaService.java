package br.sc.weg.sid.model.service;

import br.sc.weg.sid.model.entities.*;
import com.lowagie.text.*;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.lowagie.text.List;
import com.itextpdf.layout.element.Cell;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GerarPDFPropostaService {

    private final DemandaService demandaService;

    private final PdfPropostaService pdfPropostaService;

    public byte[] export(Integer idDemanda, Proposta proposta) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 40, 40, 25, 15);
        PdfWriter writer = PdfWriter.getInstance(document, baos);

        Optional<Demanda> demanda = demandaService.findById(idDemanda);
        java.util.List<PdfProposta> pdfPropostasList = pdfPropostaService.findByProposta(proposta);

        Optional<PdfProposta> pdfProposta = Optional.ofNullable(pdfPropostasList.get(0));
        if (pdfPropostasList.size() > 1) {
            pdfProposta = Optional.ofNullable(pdfPropostasList.get(pdfPropostasList.size() - 1));
        }

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

        HTMLWorker htmlWorker = new HTMLWorker(document);

        String projectScopeParagraphTextHTML = pdfProposta.get().getEscopoPropostaHTML();

        Paragraph noPartOfScopeProjectParagraph;
        if(!pdfProposta.get().getNaoFazParteDoEscopoPropostaHTML().isEmpty()){
            noPartOfScopeProjectParagraph = new Paragraph("Não faz parte do escopo do projeto:", fontTitle);
            projectScopeParagraph.setSpacingBefore(8);
        }else {
            noPartOfScopeProjectParagraph = new Paragraph();
        }

        String projectNotInScopeParagraphTextHTML = pdfProposta.get().getEscopoPropostaHTML();

        Paragraph evaluatedAlternativesParagraph = new Paragraph("Alternativas Avaliadas:", fontTitle);
        evaluatedAlternativesParagraph.setSpacingBefore(13);


        String evaluatedAlternativesParagraphTextHTML = pdfProposta.get().getAlternativasAvaliadasPropostaHTML();

        Paragraph projectCoverageParagraph = new Paragraph("Abrangência do Projeto:", fontTitle);
        projectCoverageParagraph.setSpacingBefore(8);

        Paragraph projectCoverageParagraphText = new Paragraph(pdfProposta.get().getAbrangenciaProjetoPropostaHTML(), textFont);
        projectCoverageParagraphText.setSpacingAfter(5);

        Paragraph mainRisksParagraph = new Paragraph("Riscos Principais/Plano de Mitigação:", fontTitle);
        mainRisksParagraph.setSpacingBefore(8);

        String mainRisksParagraphTextHTML = pdfProposta.get().getPlanoMitigacaoPropostaHTML();

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
            expectedResultsParagraph = new Paragraph("Resultados Esperados (Qualitativos):", fontTitle);
            expectedResultsParagraph.setSpacingBefore(8);
        }

        Paragraph expectedResultsPotentialParagraph = new Paragraph("Resultados Esperados (Ganhos potenciais):", fontTitle);
        expectedResultsPotentialParagraph.setSpacingBefore(8);

        Phrase totalCostPhrase = new Phrase("Custo Total do Projeto: ", fontTitle);
        Chunk totalCostChunk = new Chunk("R$ " + "38.902,00", textFont);
        totalCostPhrase.add(totalCostChunk);
        Paragraph totalCostParagraph = new Paragraph(totalCostPhrase);
        totalCostParagraph.setSpacingBefore(8);

        Phrase totalExpensePhrase = new Phrase("Total Despesas (Desembolso): ", textFont);
        Chunk totalExpenseChunk = new Chunk("R$ " + "38.902,00", textFont);
        totalExpensePhrase.add(totalExpenseChunk);
        Paragraph totalExpenseParagraph = new Paragraph(totalExpensePhrase);
        totalExpenseParagraph.setSpacingBefore(8);

        Phrase totalExpenseInternResourcesPhrase = new Phrase("Total Despesas - Recursos Internos: ", textFont);
        Chunk totalExpenseInternResourcesChunk = new Chunk("R$ " + "38.902,00", textFont);
        totalExpenseInternResourcesPhrase.add(totalExpenseInternResourcesChunk);
        Paragraph totalExpenseInternResourcesParagraph = new Paragraph(totalExpenseInternResourcesPhrase);
        totalExpenseInternResourcesParagraph.setSpacingBefore(8);

        PdfPTable tableExpenses = new PdfPTable(5);
        Font tableFontBold = new Font(Font.COURIER, 9, Font.BOLD);
        Font tableFont = new Font(Font.COURIER, 9);
        tableExpenses.setSpacingBefore(8);
        tableExpenses.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
        tableExpenses.setWidths(new int[]{18, 8, 8, 10});
        tableExpenses.setWidthPercentage(90);
        tableExpenses.getDefaultCell().setBorder(Rectangle.BOX);
        tableExpenses.getDefaultCell().setBorderWidth(0.2f);

        PdfPCell celulaTabelExpenses;

        celulaTabelExpenses = new PdfPCell(new Phrase("Despesas (Com desembolso)", tableFontBold));
        celulaTabelExpenses.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        tableExpenses.addCell(celulaTabelExpenses);

        celulaTabelExpenses = new PdfPCell(new Phrase("Esforço", tableFontBold));
        celulaTabelExpenses.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        tableExpenses.addCell(celulaTabelExpenses);

        celulaTabelExpenses = new PdfPCell(new Phrase("Valor Hora", tableFontBold));
        celulaTabelExpenses.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        tableExpenses.addCell(celulaTabelExpenses);

        celulaTabelExpenses = new PdfPCell(new Phrase("Valor Total", tableFontBold));
        celulaTabelExpenses.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        tableExpenses.addCell(celulaTabelExpenses);

        celulaTabelExpenses = new PdfPCell(new Phrase("CCs", tableFontBold));
        celulaTabelExpenses.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        tableExpenses.addCell(celulaTabelExpenses);

//        proposta.getTabelaCusto().forEach(tabelaCusto -> {
//            if(tabelaCusto.getTipoDespesa() == TipoDeDespesa.EXTERNA) {
//                PdfPCell celulaTabelExpensesForEach;
//
//                celulaTabelExpensesForEach = new PdfPCell(new Phrase(tabelaCusto.getPerfilDespesaTabelaCusto(), tableFontBold));
//                celulaTabelExpensesForEach.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
//                tableExpenses.addCell(celulaTabelExpensesForEach);
//
//                celulaTabelExpensesForEach = new PdfPCell(new Phrase(tabelaCusto.getQuantidadeHorasTabelaCusto().toString() + "h", tableFontBold));
//                celulaTabelExpensesForEach.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
//                tableExpenses.addCell(celulaTabelExpensesForEach);
//
//                celulaTabelExpensesForEach = new PdfPCell(new Phrase("R$" + tabelaCusto.getValorHoraTabelaCusto().toString(), tableFontBold));
//                celulaTabelExpensesForEach.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
//                tableExpenses.addCell(celulaTabelExpensesForEach);
//
//                Double valorTotal = tabelaCusto.getQuantidadeHorasTabelaCusto() * tabelaCusto.getValorHoraTabelaCusto();
//
//                celulaTabelExpensesForEach = new PdfPCell(new Phrase(valorTotal.toString(), tableFontBold));
//                celulaTabelExpensesForEach.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
//                tableExpenses.addCell(celulaTabelExpensesForEach);
//
////                celulaTabelExpensesForEach = new PdfPCell(new Phrase(tabelaCusto.get(), tableFontBold));
////                celulaTabelExpensesForEach.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
////                tableExpenses.addCell(celulaTabelExpensesForEach);
//            }
//        });


        PdfPTable tableInternResources = new PdfPTable(5);
        tableInternResources.setSpacingBefore(8);
        tableInternResources.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
        tableInternResources.setWidths(new int[]{18, 8, 8, 10});
        tableInternResources.setWidthPercentage(90);
        tableInternResources.getDefaultCell().setBorder(Rectangle.BOX);
        tableInternResources.getDefaultCell().setBorderWidth(0.2f);

        PdfPCell celulaTableInternResources;

        celulaTableInternResources = new PdfPCell(new Phrase("Recursos Internos", tableFontBold));
        celulaTableInternResources.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        tableInternResources.addCell(celulaTableInternResources);

        celulaTableInternResources = new PdfPCell(new Phrase("Esforço", tableFontBold));
        celulaTableInternResources.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        tableInternResources.addCell(celulaTableInternResources);

        celulaTableInternResources = new PdfPCell(new Phrase("Total", tableFontBold));
        celulaTableInternResources.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        tableInternResources.addCell(celulaTableInternResources);

        celulaTableInternResources = new PdfPCell(new Phrase("CCs", tableFontBold));
        celulaTableInternResources.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        tableInternResources.addCell(celulaTableInternResources);

        celulaTableInternResources = new PdfPCell(new Phrase("Arquiteto Maestro (TI)", tableFont));
        celulaTableInternResources.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
        tableInternResources.addCell(celulaTableInternResources);

        celulaTableInternResources = new PdfPCell(new Phrase("100h", tableFont));
        celulaTableInternResources.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        tableInternResources.addCell(celulaTableInternResources);

        celulaTableInternResources = new PdfPCell(new Phrase("R$ 5.000", tableFont));
        celulaTableInternResources.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        tableInternResources.addCell(celulaTableInternResources);

        celulaTableInternResources = new PdfPCell(new Phrase("90100340 - 50%", tableFont));
        celulaTableInternResources.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        tableInternResources.addCell(celulaTableInternResources);

        celulaTableInternResources = new PdfPCell(new Phrase("Desenvolvedor Front-end", tableFont));
        celulaTableInternResources.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
        tableInternResources.addCell(celulaTableInternResources);

        celulaTableInternResources = new PdfPCell(new Phrase("200h", tableFont));
        celulaTableInternResources.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        tableInternResources.addCell(celulaTableInternResources);

        celulaTableInternResources = new PdfPCell(new Phrase("R$ 10.000", tableFont));
        celulaTableInternResources.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        tableInternResources.addCell(celulaTableInternResources);

        celulaTableInternResources = new PdfPCell(new Phrase("90100341 - 30%", tableFont));
        celulaTableInternResources.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        tableInternResources.addCell(celulaTableInternResources);

        celulaTableInternResources = new PdfPCell(new Phrase("Total Recursos Internos", tableFontBold));
        celulaTableInternResources.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
        tableInternResources.addCell(celulaTableInternResources);

        celulaTableInternResources = new PdfPCell(new Phrase("300h", tableFontBold));
        celulaTableInternResources.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        tableInternResources.addCell(celulaTableInternResources);

        celulaTableInternResources = new PdfPCell(new Phrase("R$ 15.000", tableFontBold));
        celulaTableInternResources.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        tableInternResources.addCell(celulaTableInternResources);

        celulaTableInternResources = new PdfPCell(new Phrase("90100342 - 20%", tableFontBold));
        celulaTableInternResources.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        tableInternResources.addCell(celulaTableInternResources);

        SimpleDateFormat formatarData = new SimpleDateFormat("dd/MM/yyyy");
        String dataInicioFormatada = formatarData.format(pdfProposta.get().getProposta().getPeriodoExecucaoFimProposta());
        String dataFimFormatada = formatarData.format(pdfProposta.get().getProposta().getPeriodoExecucaoFimProposta());

        Phrase executionPeriodPhrase = new Phrase("Período de execução: ", fontTitle);
        Chunk executionPeriodChunk = new Chunk(dataInicioFormatada + " à " +
                dataFimFormatada, textFont);
        executionPeriodPhrase.add(executionPeriodChunk);
        Paragraph executionPeriodParagraph = new Paragraph(executionPeriodPhrase);
        executionPeriodParagraph.setSpacingBefore(8);

        Phrase paybackPhrase = new Phrase("Payback: ", fontTitle);
        Chunk paybackChunk = new Chunk(pdfProposta.get().getProposta().getPaybackProposta().toString(), textFont);
        paybackPhrase.add(paybackChunk);
        Paragraph paybackParagraph = new Paragraph(paybackPhrase);
        paybackParagraph.setSpacingBefore(8);

        PdfPTable table = new PdfPTable(2);
        table.setSpacingBefore(8);
        table.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
        table.setWidths(new int[]{5, 5});
        table.setWidthPercentage(50);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        PdfPCell celulaTabelainicial;

        celulaTabelainicial = new PdfPCell(new Phrase(proposta.getNomeResponsavelNegocio(), tableFont));
        celulaTabelainicial.setBorder(Rectangle.NO_BORDER);
        celulaTabelainicial.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        table.addCell(celulaTabelainicial);

        celulaTabelainicial = new PdfPCell(new Phrase(proposta.getDemandaProposta().getGestorResponsavelDemanda().getNomeUsuario(), tableFont));
        celulaTabelainicial.setBorder(Rectangle.NO_BORDER);
        celulaTabelainicial.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        table.addCell(celulaTabelainicial);

        celulaTabelainicial = new PdfPCell(new Phrase("Responsável Negócio", tableFontBold));
        celulaTabelainicial.setBorder(Rectangle.NO_BORDER);
        celulaTabelainicial.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        table.addCell(celulaTabelainicial);

        celulaTabelainicial = new PdfPCell(new Phrase("Chefe Responsável TI", tableFontBold));
        celulaTabelainicial.setBorder(Rectangle.NO_BORDER);
        celulaTabelainicial.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        table.addCell(celulaTabelainicial);

        document.add(paragraph);
        document.add(dateParagraph);
        document.add(listTitle);
        document.add(requesterParagraph);
        document.add(proposalParagraph);

        document.add(actualSituationParagraph);
        document.add(actualSituationParagraphText);

        document.add(projectScopeParagraph);
        htmlWorker.parse(new StringReader(projectScopeParagraphTextHTML));

        if (!pdfProposta.get().getNaoFazParteDoEscopoPropostaHTML().isEmpty()) {
            document.add(noPartOfScopeProjectParagraph);
            htmlWorker.parse(new StringReader(projectNotInScopeParagraphTextHTML));
        }

        document.add(evaluatedAlternativesParagraph);
        htmlWorker.parse(new StringReader(evaluatedAlternativesParagraphTextHTML));

        document.add(projectCoverageParagraph);
        document.add(projectCoverageParagraphText);

        document.add(mainRisksParagraph);
        htmlWorker.parse(new StringReader(mainRisksParagraphTextHTML));

        if (expectedResultsQualitativeParagraphTextHTML != null){
            document.add(expectedResultsParagraph);
            htmlWorker.parse(new StringReader(expectedResultsQualitativeParagraphTextHTML));
        }

        document.add(expectedResultsPotentialParagraph);
        htmlWorker.parse(new StringReader(expectedResultsPotentialParagraphTextHTML));

        document.add(totalCostParagraph);
        document.add(totalExpenseParagraph);
        document.add(totalExpenseInternResourcesParagraph);

        document.add(tableExpenses);
        document.add(tableInternResources);

        document.add(executionPeriodParagraph);

        document.add(paybackParagraph);

        document.add(table);

        document.close();

        return baos.toByteArray();
    }
}
