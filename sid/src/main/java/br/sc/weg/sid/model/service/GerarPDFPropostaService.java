package br.sc.weg.sid.model.service;

import br.sc.weg.sid.model.entities.*;
import br.sc.weg.sid.model.enums.TipoBeneficio;
import br.sc.weg.sid.model.enums.TipoDeDespesa;
import com.lowagie.text.*;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Service
@AllArgsConstructor
public class GerarPDFPropostaService {

    private final DemandaService demandaService;

    private final PdfPropostaService pdfPropostaService;

    private final CentroCustoService centroCustoService;

    public byte[] export(Proposta proposta) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 40, 40, 25, 15);
        PdfWriter writer = PdfWriter.getInstance(document, baos);

        Demanda demanda = proposta.getDemandaProposta();
        java.util.List<PdfProposta> pdfPropostasList = pdfPropostaService.findByProposta(proposta);

        PdfProposta pdfProposta = new PdfProposta();
        if (pdfPropostasList.size() > 1) {
            pdfProposta = pdfPropostasList.get(pdfPropostasList.size() - 1);
        } else {
            pdfProposta = pdfPropostasList.get(0);
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
                Font font = new Font(Font.HELVETICA, 12, Font.NORMAL);

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

        String tituloCaixaAltaDemanda = demanda.getTituloDemanda().toUpperCase();

        Paragraph titleDemandParagraph = new Paragraph(tituloCaixaAltaDemanda + " – " + demanda.getIdDemanda(), fontTitle);
        List listTitle = new List(List.ORDERED);
        listTitle.add(new ListItem(titleDemandParagraph));

        Phrase requesterPhrase = new Phrase("Solicitante: ", fontTitle);
        Chunk solicitanteChunk = new Chunk(demanda.getSolicitanteDemanda().getNomeUsuario() + " - " +
                demanda.getSolicitanteDemanda().getDepartamentoUsuario(), textFont);
        requesterPhrase.add(solicitanteChunk);
        Paragraph requesterParagraph = new Paragraph(requesterPhrase);
        requesterParagraph.setSpacingBefore(8);

        Phrase proposalPhrase = new Phrase("Objetivo: ", fontTitle);
        Chunk objetivoChunk = new Chunk(demanda.getPropostaMelhoriaDemanda(), textFont);
        proposalPhrase.add(objetivoChunk);
        Paragraph proposalParagraph = new Paragraph(proposalPhrase);
        proposalParagraph.setSpacingBefore(8);
        proposalParagraph.setSpacingAfter(5);

        Paragraph actualSituationParagraph = new Paragraph("Situação Atual - Problema a ser tratado/ resolvido:", fontTitle);
        actualSituationParagraph.setSpacingBefore(8);

        Paragraph actualSituationParagraphText = new Paragraph(demanda.getSituacaoAtualDemanda(), textFont);
        actualSituationParagraphText.setSpacingAfter(5);

        Paragraph projectScopeParagraph = new Paragraph("Escopo do Projeto:", fontTitle);
        projectScopeParagraph.setSpacingBefore(8);

        HTMLWorker htmlWorker = new HTMLWorker(document);

        String projectScopeParagraphTextHTML = pdfProposta.getEscopoPropostaHTML();

        Paragraph noPartOfScopeProjectParagraph;
        if (!pdfProposta.getNaoFazParteDoEscopoPropostaHTML().isEmpty()) {
            noPartOfScopeProjectParagraph = new Paragraph("Não faz parte do escopo do projeto:", fontTitle);
            projectScopeParagraph.setSpacingBefore(8);
        } else {
            noPartOfScopeProjectParagraph = new Paragraph();
        }

        String projectNotInScopeParagraphTextHTML = pdfProposta.getEscopoPropostaHTML();

        Paragraph evaluatedAlternativesParagraph = new Paragraph("Alternativas Avaliadas:", fontTitle);
        evaluatedAlternativesParagraph.setSpacingBefore(13);


        String evaluatedAlternativesParagraphTextHTML = pdfProposta.getAlternativasAvaliadasPropostaHTML();

        Paragraph projectCoverageParagraph = new Paragraph("Abrangência do Projeto:", fontTitle);
        projectCoverageParagraph.setSpacingBefore(8);

        String projectCoverageParagraphTextHTML = pdfProposta.getAbrangenciaProjetoPropostaHTML();

        Paragraph mainRisksParagraph = new Paragraph("Riscos Principais/Plano de Mitigação:", fontTitle);
        mainRisksParagraph.setSpacingBefore(8);

        String mainRisksParagraphTextHTML = pdfProposta.getPlanoMitigacaoPropostaHTML();

        java.util.List<Beneficio> beneficiosDemanda = pdfProposta.getProposta().getDemandaProposta().getBeneficiosDemanda();

        String expectedResultsQualitativeParagraphTextHTML = "";
        String expectedResultsPotentialParagraphTextHTML = "";
        for (Beneficio beneficio : beneficiosDemanda) {
            if (beneficio.getTipoBeneficio() == TipoBeneficio.QUALITATIVO) {
                expectedResultsQualitativeParagraphTextHTML = beneficio.getMemoriaCalculoBeneficio() + " - " + beneficio.getDescricaoBeneficioHTML();
            } else if (beneficio.getTipoBeneficio() == TipoBeneficio.POTENCIAL) {
                expectedResultsPotentialParagraphTextHTML = beneficio.getMemoriaCalculoBeneficio() + " - " + beneficio.getDescricaoBeneficioHTML();
            }
        }
        Paragraph expectedResultsParagraph = null;
        if (expectedResultsQualitativeParagraphTextHTML != null) {
            expectedResultsParagraph = new Paragraph("Resultados Esperados (Qualitativos):", fontTitle);
            expectedResultsParagraph.setSpacingBefore(8);
        }

        Paragraph expectedResultsPotentialParagraph = new Paragraph("Resultados Esperados (Ganhos potenciais):", fontTitle);
        expectedResultsPotentialParagraph.setSpacingBefore(8);

        PdfPTable tableExpenses = new PdfPTable(5);
        Font tableFontBold = new Font(Font.COURIER, 9, Font.BOLD);
        Font tableFont = new Font(Font.COURIER, 9);
        tableExpenses.setSpacingBefore(8);
        tableExpenses.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
        tableExpenses.setWidths(new int[]{18, 8, 8, 8, 10});
        tableExpenses.setWidthPercentage(90);
        tableExpenses.getDefaultCell().setBorder(Rectangle.BOX);
        tableExpenses.getDefaultCell().setBorderWidth(0.2f);

        PdfPCell celulaTabelExpenses;

        PdfPCell celulaTotalHoursWorked;

        celulaTabelExpenses = new PdfPCell(new Phrase("Despesas (Com desembolso)", tableFontBold));
        celulaTabelExpenses.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        tableExpenses.addCell(celulaTabelExpenses);

        celulaTabelExpenses = new PdfPCell(new Phrase("Esforço", tableFontBold));
        celulaTabelExpenses.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        tableExpenses.addCell(celulaTabelExpenses);

        celulaTabelExpenses = new PdfPCell(new Phrase("Valor Hora", tableFontBold));
        celulaTabelExpenses.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        tableExpenses.addCell(celulaTabelExpenses);

        celulaTabelExpenses = new PdfPCell(new Phrase("Total", tableFontBold));
        celulaTabelExpenses.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        tableExpenses.addCell(celulaTabelExpenses);

        celulaTabelExpenses = new PdfPCell(new Phrase("CCs", tableFontBold));
        celulaTabelExpenses.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        tableExpenses.addCell(celulaTabelExpenses);


        proposta.getTabelaCusto().forEach(tabelaCusto -> {
            if (tabelaCusto.getTipoDespesa() == TipoDeDespesa.EXTERNA) {
                PdfPCell celulaTabelExpensesForEach;
                boolean centroCustoFeito = false;
                for (int i = 0; i < tabelaCusto.getTabelaCustoLinha().size(); i++) {
                    celulaTabelExpensesForEach = new PdfPCell(new Phrase(tabelaCusto.getTabelaCustoLinha().get(i).getPerfilDespesaTabelaCustoLinha(), tableFont));
                    celulaTabelExpensesForEach.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
                    tableExpenses.addCell(celulaTabelExpensesForEach);

                    celulaTabelExpensesForEach = new PdfPCell(new Phrase(tabelaCusto.getTabelaCustoLinha().get(i).getQuantidadeHorasTabelaCusto().toString() + "h", tableFont));
                    celulaTabelExpensesForEach.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
                    tableExpenses.addCell(celulaTabelExpensesForEach);

                    celulaTabelExpensesForEach = new PdfPCell(new Phrase("R$" + tabelaCusto.getTabelaCustoLinha().get(i).getValorHoraTabelaCusto().toString(), tableFont));
                    celulaTabelExpensesForEach.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
                    tableExpenses.addCell(celulaTabelExpensesForEach);

                    Double valorTotal = tabelaCusto.getTabelaCustoLinha().get(i).getQuantidadeHorasTabelaCusto() * tabelaCusto.getTabelaCustoLinha().get(i).getValorHoraTabelaCusto();

                    celulaTabelExpensesForEach = new PdfPCell(new Phrase("R$" + valorTotal.toString(), tableFont));
                    celulaTabelExpensesForEach.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
                    tableExpenses.addCell(celulaTabelExpensesForEach);

                    if (!centroCustoFeito){
                        centroCustoFeito = true;
                        // Cria uma String para armazenar a informação de todos os centros de custo
                        String centrosDeCusto = "";

                        // Percorre todos os centros de custo da tabela e adiciona a informação na String
                        for (int j = 0; j < tabelaCusto.getCentroCustoTabelaCusto().size(); j++) {
                            CentroCusto centroCusto = centroCustoService.findById(tabelaCusto.getCentroCustoTabelaCusto().get(j).getCentroCusto().getIdCentroCusto()).get();
                            centrosDeCusto += centroCusto.getNumeroCentroCusto() + " - " + tabelaCusto.getCentroCustoTabelaCusto().get(j).getPorcentagemDespesa() + "%";

                            // Adiciona uma quebra de linha, exceto na última iteração do loop
                            if (j < tabelaCusto.getCentroCustoTabelaCusto().size() - 1) {
                                centrosDeCusto += "\n";
                            }
                        }

                        // Cria a célula com a informação dos centros de custo e define o rowspan
                        PdfPCell celulaTabelExpensesCentroCusto = new PdfPCell(new Phrase(centrosDeCusto, tableFont));
                        celulaTabelExpensesCentroCusto.setRowspan(tabelaCusto.getTabelaCustoLinha().size());
                        celulaTabelExpensesCentroCusto.setHorizontalAlignment(Paragraph.ALIGN_CENTER);

                        // Adiciona a célula à tabela
                        tableExpenses.addCell(celulaTabelExpensesCentroCusto);
                    }

                }

            }
        });

        celulaTabelExpenses = new PdfPCell(new Phrase("Total Despesas (Desembolso): ", tableFontBold));
        celulaTabelExpenses.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        tableExpenses.addCell(celulaTabelExpenses);

        Integer totalHoursWorked = 0;
        Double totalValueHours = 0.0;
        Double totalValueProject = 0.0;
        for (int i = 0; i < proposta.getTabelaCusto().size(); i++) {
            if (proposta.getTabelaCusto().get(i).getTipoDespesa() == TipoDeDespesa.EXTERNA) {
                for (int j = 0; j < proposta.getTabelaCusto().get(i).getTabelaCustoLinha().size(); j++) {
                    totalHoursWorked += proposta.getTabelaCusto().get(i).getTabelaCustoLinha().get(j).getQuantidadeHorasTabelaCusto();
                    Double valorTotal = proposta.getTabelaCusto().get(i).getTabelaCustoLinha().get(j).getQuantidadeHorasTabelaCusto() *
                            proposta.getTabelaCusto().get(i).getTabelaCustoLinha().get(j).getValorHoraTabelaCusto();
                    totalValueHours += valorTotal;
                }
            }
        }

        celulaTotalHoursWorked = new PdfPCell(new Phrase(totalHoursWorked + "h", tableFontBold));
        celulaTotalHoursWorked.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        tableExpenses.addCell(celulaTotalHoursWorked);

        celulaTabelExpenses = new PdfPCell(new Phrase(" ", tableFontBold));
        celulaTabelExpenses.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        tableExpenses.addCell(celulaTabelExpenses);

        celulaTabelExpenses = new PdfPCell(new Phrase("R$" + totalValueHours.toString(), tableFontBold));
        celulaTabelExpenses.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        tableExpenses.addCell(celulaTabelExpenses);

        Phrase totalExpensePhrase = new Phrase("Total Despesas (Desembolso): ", textFont);
        Chunk totalExpenseChunk = new Chunk("R$ " + totalValueHours, textFont);
        totalExpensePhrase.add(totalExpenseChunk);
        Paragraph totalExpenseParagraph = new Paragraph(totalExpensePhrase);

        totalValueProject += totalValueHours;

        celulaTabelExpenses = new PdfPCell(new Phrase(" ", tableFontBold));
        celulaTabelExpenses.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        tableExpenses.addCell(celulaTabelExpenses);

        PdfPTable tableInternResources = new PdfPTable(5);
        tableInternResources.setSpacingBefore(8);
        tableInternResources.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
        tableInternResources.setWidths(new int[]{18, 8, 8, 8, 10});
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

        celulaTableInternResources = new PdfPCell(new Phrase("Valor Hora", tableFontBold));
        celulaTableInternResources.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        tableInternResources.addCell(celulaTableInternResources);

        celulaTableInternResources = new PdfPCell(new Phrase("Total", tableFontBold));
        celulaTableInternResources.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        tableInternResources.addCell(celulaTableInternResources);

        celulaTableInternResources = new PdfPCell(new Phrase("CCs", tableFontBold));
        celulaTableInternResources.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        tableInternResources.addCell(celulaTableInternResources);

        proposta.getTabelaCusto().forEach(tabelaCusto -> {
            if (tabelaCusto.getTipoDespesa() == TipoDeDespesa.INTERNA) {
                PdfPCell celulaTableInternResourcesForEach;
                boolean centroCustoFeito = false;
                for (int i = 0; i < tabelaCusto.getTabelaCustoLinha().size(); i++) {
                    celulaTableInternResourcesForEach = new PdfPCell(new Phrase(tabelaCusto.getTabelaCustoLinha().get(i).getPerfilDespesaTabelaCustoLinha(), tableFontBold));
                    celulaTableInternResourcesForEach.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
                    tableInternResources.addCell(celulaTableInternResourcesForEach);

                    celulaTableInternResourcesForEach = new PdfPCell(new Phrase(tabelaCusto.getTabelaCustoLinha().get(i).getQuantidadeHorasTabelaCusto().toString() + "h", tableFontBold));
                    celulaTableInternResourcesForEach.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
                    tableInternResources.addCell(celulaTableInternResourcesForEach);

                    celulaTableInternResourcesForEach = new PdfPCell(new Phrase("R$" + tabelaCusto.getTabelaCustoLinha().get(i).getValorHoraTabelaCusto().toString(), tableFontBold));
                    celulaTableInternResourcesForEach.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
                    tableInternResources.addCell(celulaTableInternResourcesForEach);

                    Double valorTotal = tabelaCusto.getTabelaCustoLinha().get(i).getQuantidadeHorasTabelaCusto() * tabelaCusto.getTabelaCustoLinha().get(i).getValorHoraTabelaCusto();

                    celulaTableInternResourcesForEach = new PdfPCell(new Phrase("R$" + valorTotal.toString(), tableFontBold));
                    celulaTableInternResourcesForEach.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
                    tableInternResources.addCell(celulaTableInternResourcesForEach);


                    if (!centroCustoFeito){
                        centroCustoFeito = true;
                        // Cria uma String para armazenar a informação de todos os centros de custo
                        String centrosDeCusto = "";

                        // Percorre todos os centros de custo da tabela e adiciona a informação na String
                        for (int j = 0; j < tabelaCusto.getCentroCustoTabelaCusto().size(); j++) {
                            CentroCusto centroCusto = centroCustoService.findById(tabelaCusto.getCentroCustoTabelaCusto().get(j).getCentroCusto().getIdCentroCusto()).get();
                            centrosDeCusto += centroCusto.getNumeroCentroCusto() + " - " + tabelaCusto.getCentroCustoTabelaCusto().get(j).getPorcentagemDespesa() + "%";

                            // Adiciona uma quebra de linha, exceto na última iteração do loop
                            if (j < tabelaCusto.getCentroCustoTabelaCusto().size() - 1) {
                                centrosDeCusto += "\n";
                            }
                        }

                        // Cria a célula com a informação dos centros de custo e define o rowspan
                        PdfPCell celulaTableInternResourcesCentroCusto = new PdfPCell(new Phrase(centrosDeCusto, tableFont));
                        celulaTableInternResourcesCentroCusto.setRowspan(tabelaCusto.getTabelaCustoLinha().size());
                        celulaTableInternResourcesCentroCusto.setHorizontalAlignment(Paragraph.ALIGN_CENTER);

                        // Adiciona a célula à tabela
                        tableInternResources.addCell(celulaTableInternResourcesCentroCusto);
                    }
                }
            }
        });

        celulaTableInternResources = new PdfPCell(new Phrase("Total Recursos Internos: ", tableFontBold));
        celulaTableInternResources.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        tableInternResources.addCell(celulaTableInternResources);

        totalHoursWorked = 0;
        totalValueHours = 0.0;

        for (int i = 0; i < proposta.getTabelaCusto().size(); i++) {
            if (proposta.getTabelaCusto().get(i).getTipoDespesa() == TipoDeDespesa.INTERNA) {
                for (int j = 0; j < proposta.getTabelaCusto().get(i).getTabelaCustoLinha().size(); j++) {
                    totalHoursWorked += proposta.getTabelaCusto().get(i).getTabelaCustoLinha().get(j).getQuantidadeHorasTabelaCusto();
                    Double valorTotal = proposta.getTabelaCusto().get(i).getTabelaCustoLinha().get(j).getQuantidadeHorasTabelaCusto() *
                            proposta.getTabelaCusto().get(i).getTabelaCustoLinha().get(j).getValorHoraTabelaCusto();
                    totalValueHours += valorTotal;
                }
            }
        }

        celulaTotalHoursWorked = new PdfPCell(new Phrase(totalHoursWorked + "h", tableFontBold));
        celulaTotalHoursWorked.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        tableInternResources.addCell(celulaTotalHoursWorked);

        celulaTableInternResources = new PdfPCell(new Phrase(" ", tableFontBold));
        celulaTableInternResources.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        tableInternResources.addCell(celulaTableInternResources);

        celulaTableInternResources = new PdfPCell(new Phrase("R$" + totalValueHours.toString(), tableFontBold));
        celulaTableInternResources.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        tableInternResources.addCell(celulaTableInternResources);

        Phrase totalExpenseInternResourcesPhrase = new Phrase("Total Despesas - Recursos Internos: ", textFont);
        Chunk totalExpenseInternResourcesChunk = new Chunk("R$ " + totalValueHours, textFont);
        totalExpenseInternResourcesPhrase.add(totalExpenseInternResourcesChunk);
        Paragraph totalExpenseInternResourcesParagraph = new Paragraph(totalExpenseInternResourcesPhrase);

        totalValueProject += totalValueHours;

        celulaTableInternResources = new PdfPCell(new Phrase(" ", tableFontBold));
        celulaTableInternResources.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        tableInternResources.addCell(celulaTableInternResources);


        Phrase totalCostPhrase = new Phrase("Custo Total do Projeto: ", fontTitle);
        Chunk totalCostChunk = new Chunk("R$ " + totalValueProject, textFont);
        totalCostPhrase.add(totalCostChunk);
        Paragraph totalCostParagraph = new Paragraph(totalCostPhrase);

        SimpleDateFormat formatarData = new SimpleDateFormat("dd/MM/yyyy");
        String dataInicioFormatada = formatarData.format(pdfProposta.getProposta().getPeriodoExecucaoFimProposta());
        String dataFimFormatada = formatarData.format(pdfProposta.getProposta().getPeriodoExecucaoFimProposta());

        Phrase executionPeriodPhrase = new Phrase("Período de execução: ", fontTitle);
        Chunk executionPeriodChunk = new Chunk(dataInicioFormatada + " à " +
                dataFimFormatada, textFont);
        executionPeriodPhrase.add(executionPeriodChunk);
        Paragraph executionPeriodParagraph = new Paragraph(executionPeriodPhrase);
        executionPeriodParagraph.setSpacingBefore(8);

        Phrase paybackPhrase = new Phrase("Payback: ", fontTitle);
        Chunk paybackChunk = new Chunk(pdfProposta.getProposta().getPaybackProposta().toString(), textFont);
        paybackPhrase.add(paybackChunk);
        Paragraph paybackParagraph = new Paragraph(paybackPhrase);
        paybackParagraph.setSpacingBefore(8);

        PdfPTable table = new PdfPTable(2);
        table.setSpacingBefore(8);
        table.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
        int nomeResponsavelLength = 20;
        int nomeGestorLength = 20;
        if (pdfProposta.getProposta().getNomeResponsavelNegocio().length() > 19) {
            nomeResponsavelLength = pdfProposta.getProposta().getNomeResponsavelNegocio().length() + 1;
        }
        if (pdfProposta.getProposta().getDemandaProposta().getGestorResponsavelDemanda().getNomeUsuario().length() > 20) {
            nomeGestorLength = pdfProposta.getProposta().getDemandaProposta().getGestorResponsavelDemanda().getNomeUsuario().length() + 1;
        }
        table.setWidths(new int[]{nomeResponsavelLength, nomeGestorLength});
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

        if (!pdfProposta.getNaoFazParteDoEscopoPropostaHTML().isEmpty()) {
            document.add(noPartOfScopeProjectParagraph);
            htmlWorker.parse(new StringReader(projectNotInScopeParagraphTextHTML));
        }

        document.add(evaluatedAlternativesParagraph);
        htmlWorker.parse(new StringReader(evaluatedAlternativesParagraphTextHTML));

        document.add(projectCoverageParagraph);
        htmlWorker.parse(new StringReader(projectCoverageParagraphTextHTML));

        document.add(mainRisksParagraph);
        htmlWorker.parse(new StringReader(mainRisksParagraphTextHTML));

        if (expectedResultsQualitativeParagraphTextHTML != null) {
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