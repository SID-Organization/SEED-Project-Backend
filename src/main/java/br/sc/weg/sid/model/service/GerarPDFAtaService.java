package br.sc.weg.sid.model.service;

import br.sc.weg.sid.model.entities.*;
import br.sc.weg.sid.model.enums.TipoBeneficio;
import br.sc.weg.sid.model.enums.TipoDeDespesa;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.*;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@AllArgsConstructor
public class GerarPDFAtaService {

    PdfPropostaService pdfPropostaService;

    PautaService pautaService;

    CentroCustoService centroCustoService;

    public byte[] export(Ata ata) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 40, 20, 25, 15);
        PdfWriter writer = PdfWriter.getInstance(document, baos);

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

                Phrase phrase2 = new Phrase("Ata CDVP nº " + ata.getHorarioInicioPauta() + "   "
                        + ata.getHorarioTerminoPauta(), font);
//                phrase2.setLeading(10);
                ColumnText.showTextAligned(writer.getDirectContent(), com.itextpdf.text.Element.ALIGN_LEFT, phrase2, 40, 40, 0);
            }
        });

        document.open();

        Color fontFirstTitleColor = new Color(0, 112, 192);
        Font fontFirstTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, fontFirstTitleColor);

        Color fontProposalTitleColor = Color.decode("#4472c4");
        Font fontProposalTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, fontProposalTitleColor);

        Font fontSubTitle = new Font(Font.HELVETICA, 12, Font.BOLD);


        Paragraph paragraph = new Paragraph("ATA " +  ata.getForumAta().getComissaoForum().getNomeComissao().toUpperCase() + "\n", fontFirstTitle);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        paragraph.setSpacingBefore(3);

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
        String dataReuniaoFormatada = formatarData.format(ata.getDataReuniaoPauta());

        celulaTabelainicial = new PdfPCell(new Phrase(dataReuniaoFormatada, tableFontBold));
        celulaTabelainicial.setBorder(Rectangle.NO_BORDER);
        celulaTabelainicial.setHorizontalAlignment(Paragraph.ALIGN_RIGHT);
        table.addCell(celulaTabelainicial);

        celulaTabelainicial = new PdfPCell(new Phrase("Início", tableFont));
        celulaTabelainicial.setBorder(Rectangle.NO_BORDER);
        celulaTabelainicial.setHorizontalAlignment(Paragraph.ALIGN_JUSTIFIED);
        table.addCell(celulaTabelainicial);

        celulaTabelainicial = new PdfPCell(new Phrase(ata.getHorarioInicioPauta().toString(), tableFont));
        celulaTabelainicial.setBorder(Rectangle.NO_BORDER);
        celulaTabelainicial.setHorizontalAlignment(Paragraph.ALIGN_RIGHT);
        table.addCell(celulaTabelainicial);

        celulaTabelainicial = new PdfPCell(new Phrase("Término", tableFont));
        celulaTabelainicial.setBorder(Rectangle.NO_BORDER);
        celulaTabelainicial.setHorizontalAlignment(Paragraph.ALIGN_JUSTIFIED);
        table.addCell(celulaTabelainicial);

        celulaTabelainicial = new PdfPCell(new Phrase(ata.getHorarioTerminoPauta().toString(), tableFont));
        celulaTabelainicial.setBorder(Rectangle.NO_BORDER);
        celulaTabelainicial.setHorizontalAlignment(Paragraph.ALIGN_RIGHT);
        table.addCell(celulaTabelainicial);

        document.add(paragraph);
        document.add(table);

        AtomicInteger numeroProposta = new AtomicInteger();
        ata.getPropostasLog().forEach(propostaLog -> {

            Proposta proposta = propostaLog.getPropostaPropostaLog();

            Demanda demanda = proposta.getDemandaProposta();

            java.util.List<PdfProposta> pdfPropostasList = pdfPropostaService.findByProposta(proposta);
            PdfProposta pdfProposta = new PdfProposta();
            if (pdfPropostasList.size() > 1) {
                pdfProposta = pdfPropostasList.get(pdfPropostasList.size() - 1);
            } else {
                pdfProposta = pdfPropostasList.get(0);
            }

            String tituloCaixaAltaDemanda = demanda.getTituloDemanda().toUpperCase();

            numeroProposta.getAndIncrement();
            Paragraph titleDemandParagraph = new Paragraph(numeroProposta + ".    " + tituloCaixaAltaDemanda + " – " + demanda.getIdDemanda(), fontProposalTitle);
            titleDemandParagraph.setSpacingBefore(8);

            Phrase proposalPhrase = new Phrase("Objetivo: ", fontSubTitle);
            Chunk objetivoChunk = new Chunk(demanda.getPropostaMelhoriaDemanda());
            proposalPhrase.add(objetivoChunk);
            Paragraph proposalParagraph = new Paragraph(proposalPhrase);
            proposalParagraph.setSpacingBefore(8);
            proposalParagraph.setSpacingAfter(5);

            Paragraph projectScopeParagraph = new Paragraph("Escopo do Projeto:", fontSubTitle);
            projectScopeParagraph.setSpacingBefore(8);

            HTMLWorker htmlWorker = new HTMLWorker(document);

            String projectScopeParagraphTextHTML = pdfProposta.getEscopoPropostaHTML();

            Paragraph noPartOfScopeProjectParagraph;
            if (!pdfProposta.getNaoFazParteDoEscopoPropostaHTML().isEmpty()) {
                noPartOfScopeProjectParagraph = new Paragraph("Não faz parte do escopo do projeto:", fontSubTitle);
                projectScopeParagraph.setSpacingBefore(8);
            } else {
                noPartOfScopeProjectParagraph = new Paragraph();
            }

            String projectNotInScopeParagraphTextHTML = pdfProposta.getEscopoPropostaHTML();

            Paragraph projectCoverageParagraph = new Paragraph("Abrangência do Projeto:", fontSubTitle);
            projectCoverageParagraph.setSpacingBefore(8);

            String projectCoverageParagraphTextHTML = pdfProposta.getAbrangenciaProjetoPropostaHTML();

            java.util.List<Beneficio> beneficiosDemanda = pdfProposta.getProposta().getDemandaProposta().getBeneficiosDemanda();

            String expectedResultsQualitativeParagraphTextHTML = "";
            String expectedResultsPotentialParagraphTextHTML = "";
            for (Beneficio beneficio : beneficiosDemanda) {
                if (beneficio.getTipoBeneficio() == TipoBeneficio.QUALITATIVO) {
                    expectedResultsQualitativeParagraphTextHTML = beneficio.getMemoriaCalculoBeneficio() + " - " + beneficio.getMemoriaCalculoBeneficioHTML();
                } else if (beneficio.getTipoBeneficio() == TipoBeneficio.POTENCIAL) {
                    expectedResultsPotentialParagraphTextHTML = beneficio.getMemoriaCalculoBeneficio() + " - " + beneficio.getMemoriaCalculoBeneficioHTML();
                }
            }
            Paragraph expectedResultsParagraph = null;
            if (expectedResultsQualitativeParagraphTextHTML != null) {
                expectedResultsParagraph = new Paragraph("Resultados Esperados (Qualitativos):", fontSubTitle);
                expectedResultsParagraph.setSpacingBefore(8);
            }

            Paragraph expectedResultsPotentialParagraph = new Paragraph("Benefícios potenciais:", fontSubTitle);
            expectedResultsPotentialParagraph.setSpacingBefore(8);

            PdfPTable tableExpenses = new PdfPTable(4);
            Font tableFontBold2 = new Font(Font.HELVETICA, 10, Font.BOLD);
            Font tableFont2 = new Font(Font.HELVETICA, 10);
            tableExpenses.setSpacingBefore(8);
            tableExpenses.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            tableExpenses.setWidths(new int[]{18, 8, 8, 10});
            tableExpenses.setWidthPercentage(90);
            tableExpenses.getDefaultCell().setBorder(Rectangle.BOX);
            tableExpenses.getDefaultCell().setBorderWidth(0.2f);

            PdfPCell celulaTabelExpenses;

            PdfPCell celulaTotalHoursWorked;

            celulaTabelExpenses = new PdfPCell(new Phrase("Despesas (Com desembolso)", tableFontBold2));
            celulaTabelExpenses.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
            tableExpenses.addCell(celulaTabelExpenses);

            celulaTabelExpenses = new PdfPCell(new Phrase("Esforço", tableFontBold2));
            celulaTabelExpenses.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
            tableExpenses.addCell(celulaTabelExpenses);

            celulaTabelExpenses = new PdfPCell(new Phrase("Valor Total", tableFontBold2));
            celulaTabelExpenses.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
            tableExpenses.addCell(celulaTabelExpenses);

            celulaTabelExpenses = new PdfPCell(new Phrase("CC Pagante", tableFontBold2));
            celulaTabelExpenses.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
            tableExpenses.addCell(celulaTabelExpenses);

            proposta.getTabelaCusto().forEach(tabelaCusto -> {
                if (tabelaCusto.getTipoDespesa() == TipoDeDespesa.EXTERNA) {
                    PdfPCell celulaTabelExpensesForEach;
                    boolean centroCustoFeito = false;
                    for (int i = 0; i < tabelaCusto.getTabelaCustoLinha().size(); i++) {
                        celulaTabelExpensesForEach = new PdfPCell(new Phrase(tabelaCusto.getTabelaCustoLinha().get(i).getPerfilDespesaTabelaCustoLinha(), tableFont2));
                        celulaTabelExpensesForEach.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
                        tableExpenses.addCell(celulaTabelExpensesForEach);

                        celulaTabelExpensesForEach = new PdfPCell(new Phrase(tabelaCusto.getTabelaCustoLinha().get(i).getQuantidadeHorasTabelaCusto().toString() + "h", tableFont2));
                        celulaTabelExpensesForEach.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
                        tableExpenses.addCell(celulaTabelExpensesForEach);

                        Double valorTotal = tabelaCusto.getTabelaCustoLinha().get(i).getQuantidadeHorasTabelaCusto() * tabelaCusto.getTabelaCustoLinha().get(i).getValorHoraTabelaCusto();

                        celulaTabelExpensesForEach = new PdfPCell(new Phrase("R$" + valorTotal.toString(), tableFont2));
                        celulaTabelExpensesForEach.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
                        tableExpenses.addCell(celulaTabelExpensesForEach);

                        if (!centroCustoFeito) {
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
                            PdfPCell celulaTabelExpensesCentroCusto = new PdfPCell(new Phrase(centrosDeCusto, tableFont2));
                            celulaTabelExpensesCentroCusto.setRowspan(tabelaCusto.getTabelaCustoLinha().size());
                            celulaTabelExpensesCentroCusto.setHorizontalAlignment(Paragraph.ALIGN_CENTER);

                            // Adiciona a célula à tabela
                            tableExpenses.addCell(celulaTabelExpensesCentroCusto);
                        }

                    }
                }
            });

            celulaTabelExpenses = new PdfPCell(new Phrase("TOTAL Despesas: ", tableFontBold2));
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

            celulaTotalHoursWorked = new PdfPCell(new Phrase(totalHoursWorked + "h", tableFontBold2));
            celulaTotalHoursWorked.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
            tableExpenses.addCell(celulaTotalHoursWorked);

            celulaTabelExpenses = new PdfPCell(new Phrase("R$" + totalValueHours.toString(), tableFontBold2));
            celulaTabelExpenses.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
            tableExpenses.addCell(celulaTabelExpenses);

            Phrase totalExpensePhrase = new Phrase("Total Despesas (Desembolso): ");
            Chunk totalExpenseChunk = new Chunk("R$ " + totalValueHours);
            totalExpensePhrase.add(totalExpenseChunk);
            Paragraph totalExpenseParagraph = new Paragraph(totalExpensePhrase);

            totalValueProject += totalValueHours;

            celulaTabelExpenses = new PdfPCell(new Phrase(" ", tableFontBold2));
            celulaTabelExpenses.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
            tableExpenses.addCell(celulaTabelExpenses);

            PdfPTable tableInternResources = new PdfPTable(4);
            tableInternResources.setSpacingBefore(8);
            tableInternResources.setHorizontalAlignment(Paragraph.ALIGN_LEFT);
            tableInternResources.setWidths(new int[]{18, 8, 8, 10});
            tableInternResources.setWidthPercentage(90);
            tableInternResources.getDefaultCell().setBorder(Rectangle.BOX);
            tableInternResources.getDefaultCell().setBorderWidth(0.2f);

            PdfPCell celulaTableInternResources;

            celulaTableInternResources = new PdfPCell(new Phrase("Recursos Internos", tableFontBold2));
            celulaTableInternResources.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
            tableInternResources.addCell(celulaTableInternResources);

            celulaTableInternResources = new PdfPCell(new Phrase("Esforço", tableFontBold2));
            celulaTableInternResources.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
            tableInternResources.addCell(celulaTableInternResources);

            celulaTableInternResources = new PdfPCell(new Phrase("Total", tableFontBold2));
            celulaTableInternResources.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
            tableInternResources.addCell(celulaTableInternResources);

            celulaTableInternResources = new PdfPCell(new Phrase("CCs", tableFontBold2));
            celulaTableInternResources.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
            tableInternResources.addCell(celulaTableInternResources);

            proposta.getTabelaCusto().forEach(tabelaCusto -> {
                if (tabelaCusto.getTipoDespesa() == TipoDeDespesa.INTERNA) {
                    PdfPCell celulaTableInternResourcesForEach;
                    boolean centroCustoFeito = false;
                    for (int i = 0; i < tabelaCusto.getTabelaCustoLinha().size(); i++) {
                        celulaTableInternResourcesForEach = new PdfPCell(new Phrase(tabelaCusto.getTabelaCustoLinha().get(i).getPerfilDespesaTabelaCustoLinha(), tableFont2));
                        celulaTableInternResourcesForEach.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
                        tableInternResources.addCell(celulaTableInternResourcesForEach);

                        celulaTableInternResourcesForEach = new PdfPCell(new Phrase(tabelaCusto.getTabelaCustoLinha().get(i).getQuantidadeHorasTabelaCusto().toString() + "h", tableFont2));
                        celulaTableInternResourcesForEach.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
                        tableInternResources.addCell(celulaTableInternResourcesForEach);

                        Double valorTotal = tabelaCusto.getTabelaCustoLinha().get(i).getQuantidadeHorasTabelaCusto() * tabelaCusto.getTabelaCustoLinha().get(i).getValorHoraTabelaCusto();

                        celulaTableInternResourcesForEach = new PdfPCell(new Phrase("R$" + valorTotal.toString(), tableFont2));
                        celulaTableInternResourcesForEach.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
                        tableInternResources.addCell(celulaTableInternResourcesForEach);

                        if (!centroCustoFeito) {
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
                            PdfPCell celulaTableInternResourcesCentroCusto = new PdfPCell(new Phrase(centrosDeCusto, tableFont2));
                            celulaTableInternResourcesCentroCusto.setRowspan(tabelaCusto.getTabelaCustoLinha().size());
                            celulaTableInternResourcesCentroCusto.setHorizontalAlignment(Paragraph.ALIGN_CENTER);

                            // Adiciona a célula à tabela
                            tableInternResources.addCell(celulaTableInternResourcesCentroCusto);
                        }
                    }
                }
            });

            celulaTableInternResources = new PdfPCell(new Phrase("TOTAL Despesas: ", tableFontBold2));
            celulaTableInternResources.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
            tableInternResources.addCell(celulaTableInternResources);

            totalHoursWorked = 0;
            totalValueHours = 0.0;

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

            celulaTotalHoursWorked = new PdfPCell(new Phrase(totalHoursWorked + "h", tableFontBold2));
            celulaTotalHoursWorked.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
            tableInternResources.addCell(celulaTotalHoursWorked);

            celulaTableInternResources = new PdfPCell(new Phrase(totalValueProject.toString(), tableFontBold2));
            celulaTableInternResources.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
            tableInternResources.addCell(celulaTableInternResources);

            Phrase totalExpenseInternResourcesPhrase = new Phrase("Total Despesas - Recursos Internos: ");
            Chunk totalExpenseInternResourcesChunk = new Chunk("R$ " + totalValueHours);
            totalExpenseInternResourcesPhrase.add(totalExpenseInternResourcesChunk);
            Paragraph totalExpenseInternResourcesParagraph = new Paragraph(totalExpenseInternResourcesPhrase);

            totalValueProject += totalValueHours;

            celulaTableInternResources = new PdfPCell(new Phrase(" ", tableFontBold2));
            celulaTableInternResources.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
            tableInternResources.addCell(celulaTableInternResources);


            Phrase totalCostPhrase = new Phrase("Custo Total do Projeto: ", fontSubTitle);
            Chunk totalCostChunk = new Chunk("R$ " + totalValueProject);
            totalCostPhrase.add(totalCostChunk);
            Paragraph totalCostParagraph = new Paragraph(totalCostPhrase);

            String dataInicioFormatada = formatarData.format(pdfProposta.getProposta().getPeriodoExecucaoFimProposta());
            String dataFimFormatada = formatarData.format(pdfProposta.getProposta().getPeriodoExecucaoFimProposta());

            Phrase executionPeriodPhrase = new Phrase("Período de execução: ", fontSubTitle);
            Phrase executionPeriodPhraseText = new Phrase(dataInicioFormatada + " à " +
                    dataFimFormatada);
            Paragraph executionPeriodParagraph = new Paragraph();
            executionPeriodParagraph.add(executionPeriodPhrase);
            executionPeriodParagraph.add(executionPeriodPhraseText);
            executionPeriodParagraph.setSpacingBefore(8);

            Phrase paybackPhrase = new Phrase("Payback: ", fontSubTitle);
            DecimalFormat df = new DecimalFormat("#.00");
            Phrase paybackPhraseText = new Phrase(df.format(pdfProposta.getProposta().getPaybackProposta()));
            Paragraph paybackParagraph = new Paragraph();
            paybackParagraph.add(paybackPhrase);
            paybackParagraph.add(paybackPhraseText);
            paybackParagraph.setSpacingBefore(8);

            Phrase businessResponsable = new Phrase("Responsável Negócio: ", fontSubTitle);
            Phrase responsablePhraseText = new Phrase(proposta.getNomeResponsavelNegocio() + " - "
                    + proposta.getAreaResponsavelNegocio());
            Paragraph businessResponsableParagraph = new Paragraph();
            businessResponsableParagraph.add(businessResponsable);
            businessResponsableParagraph.add(responsablePhraseText);
            proposalParagraph.setSpacingBefore(8);

            Font commissionOpinionText = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD);
            commissionOpinionText.setStyle(Font.UNDERLINE);

            Phrase commissionOpinionPhrase = new Phrase("PARECER COMISSÃO", commissionOpinionText);
            Phrase commissionOpinionPhraseText = new Phrase(": " + propostaLog.getParecerComissaoPropostaLog().toString(), fontSubTitle);
            Paragraph commissionOpinionParagraph = new Paragraph();
            commissionOpinionParagraph.add(commissionOpinionPhrase);
            commissionOpinionParagraph.add(commissionOpinionPhraseText);
            commissionOpinionParagraph.setSpacingBefore(10);

            Phrase considerationOpinionPhrase = new Phrase("Considerações: ", fontSubTitle);
            Phrase considerationOpinionPhraseText = new Phrase(propostaLog.getConsideracoesPropostaLog());
            Paragraph considerationOpinionParagraph = new Paragraph();
            considerationOpinionParagraph.add(considerationOpinionPhrase);
            considerationOpinionParagraph.add(considerationOpinionPhraseText);
            considerationOpinionParagraph.setSpacingBefore(4);


            Paragraph dgOpinionParagraph = new Paragraph();
            Paragraph considerationDGOpinionParagraph = new Paragraph();
            if (propostaLog.getParecerDGPropostaLog() != null) {
                Phrase dgOpinionPhrase = new Phrase("PARECER DG", commissionOpinionText);
                Phrase dgOpinionPhraseText = new Phrase(": " + propostaLog.getParecerDGPropostaLog().toString(), fontSubTitle);
                dgOpinionParagraph.add(dgOpinionPhrase);
                dgOpinionParagraph.add(dgOpinionPhraseText);
                dgOpinionParagraph.setSpacingBefore(15);
                Phrase considerationDGOpinionPhrase = new Phrase("Considerações: ", fontSubTitle);
                Phrase considerationDGOpinionPhraseText = new Phrase(propostaLog.getConsideracoesParecerDGPropostaLog());
                considerationDGOpinionParagraph.add(considerationDGOpinionPhrase);
                considerationDGOpinionParagraph.add(considerationDGOpinionPhraseText);
                considerationDGOpinionParagraph.setSpacingBefore(4);
            }

            document.add(titleDemandParagraph);
            document.add(proposalParagraph);


            document.add(projectScopeParagraph);
            try {
                htmlWorker.parse(new StringReader(projectScopeParagraphTextHTML));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (!pdfProposta.getNaoFazParteDoEscopoPropostaHTML().isEmpty()) {
                document.add(noPartOfScopeProjectParagraph);
                try {
                    htmlWorker.parse(new StringReader(projectNotInScopeParagraphTextHTML));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            document.add(projectCoverageParagraph);
            try {
                htmlWorker.parse(new StringReader(projectCoverageParagraphTextHTML));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (expectedResultsQualitativeParagraphTextHTML != null) {
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

            document.add(totalCostParagraph);
            document.add(totalExpenseParagraph);
            document.add(totalExpenseInternResourcesParagraph);

            document.add(tableExpenses);
            document.add(tableInternResources);

            document.add(executionPeriodParagraph);

            document.add(paybackParagraph);

            document.add(businessResponsableParagraph);

            document.add(commissionOpinionParagraph);
            document.add(considerationOpinionParagraph);

            if (propostaLog.getConsideracoesParecerDGPropostaLog() != null) {
                document.add(dgOpinionParagraph);
                document.add(considerationDGOpinionParagraph);
            }

            if (numeroProposta.get() != ata.getPropostasLog().size()) {
                document.newPage();
            }
        });
        document.close();

        return baos.toByteArray();
    }

}