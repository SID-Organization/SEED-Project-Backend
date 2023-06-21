package br.sc.weg.sid.model.service;


import br.sc.weg.sid.model.entities.BusinessUnity;
import br.sc.weg.sid.model.entities.Demanda;
import br.sc.weg.sid.model.entities.Proposta;
import br.sc.weg.sid.model.enums.TipoBeneficio;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ExcelExporterService {

    DemandaService demandaService;

    PropostaService propostaService;

    public void criarTabelaDemandaExcel(HttpServletResponse response, List<Integer> demandasIdList) throws IOException {
        List<Demanda> demandasList = new ArrayList<>();
        for (Integer id : demandasIdList) {
            demandasList.add(demandaService.findById(id).get());
        }
        response.setHeader("Content-Disposition", "attachment; filename=tabela-demandas.xlsx");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Demandas");


        Row headerRow = sheet.createRow(1);

        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setAlignment(HorizontalAlignment.CENTER);

        Font font = workbook.createFont();
        font.setBold(true);

        titleStyle.setFont(font);
        titleStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        titleStyle.setBorderTop(BorderStyle.MEDIUM);
        titleStyle.setBorderBottom(BorderStyle.MEDIUM);
        titleStyle.setBorderLeft(BorderStyle.MEDIUM);
        titleStyle.setBorderRight(BorderStyle.MEDIUM);

        createCell(headerRow, 1, "Demanda ID", 0, 0, titleStyle, sheet);

        createCell(headerRow, 2, "Titulo", 0, 0, titleStyle, sheet);

        createCell(headerRow, 3, "Solicitante da demanda", 0, 0, titleStyle, sheet);

        createCell(headerRow, 4, "Analista responsável", 0, 0, titleStyle, sheet);

        createCell(headerRow, 5, "Gerente da área", 0, 0, titleStyle, sheet);

        createCell(headerRow, 6, "Gestor de T.I", 0, 0, titleStyle, sheet);

        createCell(headerRow, 7, "Status", 0, 0, titleStyle, sheet);

        createCell(headerRow, 8, "Tamanho", 0, 0, titleStyle, sheet);

        createCell(headerRow, 9, "Data de criação", 0, 0, titleStyle, sheet);

        createCell(headerRow, 10, "Score", 0, 0, titleStyle, sheet);

        createCell(headerRow, 11, "Bu solicitante", 0, 0, titleStyle, sheet);

        createCell(headerRow, 12, "Seção de T.I responsável", 0, 0, titleStyle, sheet);

        createCell(headerRow, 13, "Bu's beneficiadas", 0, 0, titleStyle, sheet);

        createCell(headerRow, 14, "Beneficios potenciais", 0, 0, titleStyle, sheet);

        createCell(headerRow, 15, "Beneficios reais", 0, 0, titleStyle, sheet);

        createCell(headerRow, 16, "Payback", 0, 0, titleStyle, sheet);

        createCell(headerRow, 17, "Data início da execução", 0, 0, titleStyle, sheet);

        createCell(headerRow, 18, "Data final da execução", 0, 0, titleStyle, sheet);

        createCell(headerRow, 19, "Custos externos", 0, 0, titleStyle, sheet);

        createCell(headerRow, 20, "Custos internos", 0, 0, titleStyle, sheet);

        createCell(headerRow, 21, "Custo Total", 0, 0, titleStyle, sheet);

        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setAlignment(HorizontalAlignment.CENTER);

        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);

        int rowNum = 2;
        for (Demanda demanda : demandasList) {
            List<Proposta> propostasList = propostaService.findByDemandaProposta(demanda);

            Proposta proposta = null;
            if (propostasList.size() != 0) {
                proposta = propostasList.get(propostasList.size() - 1);
            }

            Row dataRow = sheet.createRow(rowNum++);

            createCell(dataRow, 1, null, demanda.getIdDemanda(), 0, dataStyle, sheet);

            createCell(dataRow, 2, demanda.getTituloDemanda(), 0, 0, dataStyle, sheet);

            createCell(dataRow, 3, demanda.getSolicitanteDemanda().getNomeUsuario(), 0, 0, dataStyle, sheet);

            if (demanda.getAnalistaResponsavelDemanda() == null) {
                createCell(dataRow, 4, "N/A", 0, 0, dataStyle, sheet);
            } else {
                createCell(dataRow, 4, demanda.getAnalistaResponsavelDemanda().getNomeUsuario(), 0, 0, dataStyle, sheet);
            }

            if (demanda.getGerenteDaAreaDemanda() == null) {
                createCell(dataRow, 5, "N/A", 0, 0, dataStyle, sheet);
            } else {
                createCell(dataRow, 5, demanda.getGerenteDaAreaDemanda().getNomeUsuario(), 0, 0, dataStyle, sheet);
            }

            if (demanda.getGestorResponsavelDemanda() == null) {
                createCell(dataRow, 6, "N/A", 0, 0, dataStyle, sheet);
            } else {
                createCell(dataRow, 6, demanda.getGestorResponsavelDemanda().getNomeUsuario(), 0, 0, dataStyle, sheet);
            }

            createCell(dataRow, 7, demanda.getStatusDemanda().getNome(), 0, 0, dataStyle, sheet);

            if (demanda.getTamanhoDemanda() == null) {
                String tamanhoNulo = "N/A";
                createCell(dataRow, 8, tamanhoNulo, 0, 0, dataStyle, sheet);
            } else {
                createCell(dataRow, 8, demanda.getTamanhoDemanda().getNome(), 0, 0, dataStyle, sheet);
            }

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

            createCell(dataRow, 9, formatter.format(demanda.getDataCriacaoDemanda()), 0, 0, dataStyle, sheet);

            if (demanda.getScoreDemanda() == 0) {
                String scoreNulo = "N/A";
                createCell(dataRow, 10, scoreNulo, 0, 0, dataStyle, sheet);
            } else {
                createCell(dataRow, 10, null, 0, demanda.getScoreDemanda(), dataStyle, sheet);
            }

            if (demanda.getBuSolicitanteDemanda() == null) {
                String buNulo = "N/A";
                createCell(dataRow, 11, buNulo, 0, 0, dataStyle, sheet);
            } else {
                createCell(dataRow, 11, demanda.getBuSolicitanteDemanda().getNomeBusinessUnity(), 0, 0, dataStyle, sheet);
            }

            if (demanda.getSecaoTIResponsavelDemanda() == null) {
                String secaoNula = "N/A";
                createCell(dataRow, 12, secaoNula, 0, 0, dataStyle, sheet);
            } else {
                createCell(dataRow, 12, demanda.getSecaoTIResponsavelDemanda().getNomeSecaoTIResponsavel(), 0, 0, dataStyle, sheet);
            }

            if (demanda.getBusBeneficiadasDemanda().isEmpty()) {
                String buVazio = "N/A";
                createCell(dataRow, 13, buVazio, 0, 0, dataStyle, sheet);
            } else {
                StringBuilder buBeneficiadas = new StringBuilder();
                for (int i = 0; i < demanda.getBusBeneficiadasDemanda().size(); i++) {
                    BusinessUnity bu = demanda.getBusBeneficiadasDemanda().get(i);
                    buBeneficiadas.append(bu.getNomeBusinessUnity());

                    if (i < demanda.getBusBeneficiadasDemanda().size() - 1) {
                        buBeneficiadas.append(", ");
                    }
                }
                createCell(dataRow, 13, buBeneficiadas.toString(), 0, 0, dataStyle, sheet);
            }

            if (demanda.getBeneficiosDemanda().isEmpty()) {
                String beneficioVazio = "N/A";
                createCell(dataRow, 14, beneficioVazio, 0, 0, dataStyle, sheet);
                createCell(dataRow, 15, beneficioVazio, 0, 0, dataStyle, sheet);
            } else {

                //Quando existe um único benefício e o tipo é qualitativo, seria problemático deixar as células correspondentes vazias.
                // Portanto, as variáveis adicionais são inicializadas com o valor "N/A" (não aplicável) para preencher as células no
                // Excel caso esse caso específico ocorra.
                String beneficiosPotenciais = "N/A", beneficiosReais = "N/A";

                // Variáveis criadas para caso haja um único benefício na demanda e que o tipo dele seja qualitativo, então para a célula
                // do excel não ficar vazia decidi usar 2 variáveis diferentes
                String beneficiosPotenciaisFor = "", beneficiosReaisFor = "";
                int qtdBeneficiosPotenciais = 0, qtdBeneficiosReais = 0;
                for (int i = 0; i < demanda.getBeneficiosDemanda().size(); i++) {
                    if (demanda.getBeneficiosDemanda().get(i).getTipoBeneficio() == TipoBeneficio.POTENCIAL) {
                        qtdBeneficiosPotenciais++;
                        beneficiosPotenciaisFor += qtdBeneficiosPotenciais + " - R$" + demanda.getBeneficiosDemanda().get(i).getValorBeneficio() + " ";
                    } else if (demanda.getBeneficiosDemanda().get(i).getTipoBeneficio() == TipoBeneficio.REAL) {
                        qtdBeneficiosReais++;
                        beneficiosReaisFor += qtdBeneficiosReais + " - R$" + demanda.getBeneficiosDemanda().get(i).getValorBeneficio() + " ";
                    }
                }
                if (qtdBeneficiosPotenciais > 0) {
                    beneficiosPotenciais = beneficiosPotenciaisFor;
                }
                if (qtdBeneficiosReais > 0) {
                    beneficiosReais = beneficiosReaisFor;
                }
                createCell(dataRow, 14, beneficiosPotenciais, 0, 0, dataStyle, sheet);
                createCell(dataRow, 15, beneficiosReais, 0, 0, dataStyle, sheet);
            }

            if (proposta == null) {
                String propostaNula = "N/A";
                createCell(dataRow, 16, propostaNula, 0, 0, dataStyle, sheet);
                createCell(dataRow, 17, propostaNula, 0, 0, dataStyle, sheet);
                createCell(dataRow, 18, propostaNula, 0, 0, dataStyle, sheet);
                createCell(dataRow, 19, propostaNula, 0, 0, dataStyle, sheet);
                createCell(dataRow, 20, propostaNula, 0, 0, dataStyle, sheet);
                createCell(dataRow, 21, propostaNula, 0, 0, dataStyle, sheet);
            } else {
                createCell(dataRow, 16, null, 0, proposta.getPaybackProposta(), dataStyle, sheet);
                SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy"); // Defina o formato desejado
                if (proposta.getPeriodoExecucaoDemandaInicio() == null) {
                    createCell(dataRow, 17, "N/A", 0, 0, dataStyle, sheet);
                } else {
                    createCell(dataRow, 17, formatador.format(proposta.getPeriodoExecucaoDemandaInicio()), 0, 0, dataStyle, sheet);
                }
                if (proposta.getPeriodoExecucaoDemandaFim() == null) {
                    createCell(dataRow, 18, "N/A", 0, 0, dataStyle, sheet);
                } else {
                    createCell(dataRow, 18, formatador.format(proposta.getPeriodoExecucaoDemandaFim()), 0, 0, dataStyle, sheet);
                }

                if (proposta.getCustosExternosDoProjeto() == null) {
                    createCell(dataRow, 19, "N/A", 0, 0, dataStyle, sheet);
                } else {
                    createCell(dataRow, 19, null, 0, proposta.getCustosExternosDoProjeto(), dataStyle, sheet);
                }

                if (proposta.getCustosInternosDoProjeto() == null) {
                    createCell(dataRow, 20, "N/A", 0, 0, dataStyle, sheet);
                } else {
                    createCell(dataRow, 20, null, 0, proposta.getCustosInternosDoProjeto(), dataStyle, sheet);
                }

                if (demanda.getCustoTotalDemanda() == null) {
                    String custoNulo = "N/A";
                    createCell(dataRow, 21, custoNulo, 0, 0, dataStyle, sheet);
                } else {
                    createCell(dataRow, 21, null, 0, demanda.getCustoTotalDemanda(), dataStyle, sheet);
                }
            }
        }

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }


    public void createCell(Row row, int column, String stringValue, int intValue, double doubleValue, CellStyle style, Sheet sheet) {
        Cell cell = row.createCell(column);
        String value = "";
        if (stringValue != null) {
            cell.setCellValue(stringValue);
            value = stringValue;
        } else if (intValue != 0) {
            cell.setCellValue(intValue);
            value = String.valueOf(intValue);
        } else {
            cell.setCellValue(doubleValue);
            value = String.valueOf(doubleValue);
        }
        cell.setCellStyle(style);
        if (sheet.getColumnWidth(column) < (value.length() + 2) * 256) {
            sheet.setColumnWidth(column, (value.length() + 2) * 256);
        }
    }


}
