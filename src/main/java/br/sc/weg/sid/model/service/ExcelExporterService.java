package br.sc.weg.sid.model.service;


import br.sc.weg.sid.model.entities.Demanda;
import br.sc.weg.sid.model.entities.Proposta;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelExporterService {

    @Autowired
    DemandaService demandaService;

    @Autowired
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

        createCell(headerRow, 2, "Demanda ID", 0 , 0, titleStyle, sheet);

        createCell(headerRow, 3, "Titulo",0 , 0, titleStyle, sheet);

        createCell(headerRow, 4, "Status", 0 , 0, titleStyle, sheet);

        createCell(headerRow, 5, "Tamanho", 0 , 0, titleStyle, sheet);

        createCell(headerRow, 6, "Data de Criação", 0 , 0, titleStyle, sheet);

        createCell(headerRow, 7, "Score", 0 , 0, titleStyle, sheet);

        createCell(headerRow, 8, "Custo Total", 0 , 0, titleStyle, sheet);

        createCell(headerRow, 9, "Bu solicitante", 0 , 0, titleStyle, sheet);

        createCell(headerRow, 10, "Seção de T.I responsável", 0 , 0, titleStyle, sheet);

        createCell(headerRow, 11, "Bu's beneficiadas", 0 , 0, titleStyle, sheet);

        createCell(headerRow, 12, "Beneficios potenciais", 0 , 0, titleStyle, sheet);

        createCell(headerRow, 13, "Beneficios reais", 0 , 0, titleStyle, sheet);

        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setAlignment(HorizontalAlignment.CENTER);

        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);

        int rowNum = 2;
        for (Demanda demanda : demandasList) {

            List<Proposta> propostasList = propostaService.findByDemandaProposta(demanda);

            Proposta proposta = propostasList.get(propostasList.size() - 1);

            createCell(headerRow, 12, "Payback", 0 , 0, titleStyle, sheet);

            createCell(headerRow, 13, "Dias restantes para execução",0 , 0, titleStyle, sheet);

            Row dataRow = sheet.createRow(rowNum++);
            Cell dataCell = dataRow.createCell(2);
            createCell(dataRow, 2, null, demanda.getIdDemanda(), 0, dataStyle, sheet);
//            dataCell.setCellValue(demanda.getIdDemanda());
//            dataCell.setCellStyle(dataStyle);

            dataCell = dataRow.createCell(3);
            dataCell.setCellValue(demanda.getTituloDemanda());
            dataCell.setCellStyle(dataStyle);

            int titleLength = (demanda.getTituloDemanda().length() + 3) * 256; // Comprimento do título + 2 caracteres de margem e largura necessária para exibir o título corretamente

            int columnWidth = sheet.getColumnWidth(3); // Largura atual da coluna em unidades de 1/256 de um caractere

            if (columnWidth < titleLength) {
                sheet.setColumnWidth(3, titleLength); // Define a largura da coluna para exibir o título corretamente
            }

            dataCell = dataRow.createCell(4);
            dataCell.setCellValue(demanda.getStatusDemanda().getNome());
            dataCell.setCellStyle(dataStyle);

            int statusLength = (demanda.getStatusDemanda().getNome().length() + 3) * 256;

            if (sheet.getColumnWidth(4) < statusLength) {
                sheet.setColumnWidth(4, statusLength);
            }

            dataCell = dataRow.createCell(5);
            dataCell.setCellValue(demanda.getTamanhoDemanda().getNome());
            dataCell.setCellStyle(dataStyle);

            int tamanhoLength = (demanda.getTamanhoDemanda().getNome().length() + 3) * 256;

            if (sheet.getColumnWidth(5) < tamanhoLength) {
                sheet.setColumnWidth(5, tamanhoLength);
            }

            dataCell = dataRow.createCell(6);
            dataCell.setCellValue(demanda.getDataCriacaoDemanda().toString());
            dataCell.setCellStyle(dataStyle);

            int dataLength = (demanda.getDataCriacaoDemanda().toString().length() + 3) * 256;

            if (sheet.getColumnWidth(6) < dataLength) {
                sheet.setColumnWidth(6, dataLength);
            }

            dataCell = dataRow.createCell(7);
            if (demanda.getScoreDemanda() != null) {
                dataCell.setCellValue(demanda.getScoreDemanda());
                dataCell.setCellStyle(dataStyle);

                int scoreLength = (demanda.getScoreDemanda().toString().length() + 3) * 256;

                if (sheet.getColumnWidth(7) < scoreLength) {
                    sheet.setColumnWidth(7, scoreLength);
                }

            }else {
                dataCell.setCellValue("");
                dataCell.setCellStyle(dataStyle);
            }

            dataCell = dataRow.createCell(8);

            if (demanda.getCustoTotalDemanda() != null){
                dataCell.setCellValue(demanda.getCustoTotalDemanda().toString());
                dataCell.setCellStyle(dataStyle);

                int custoLength = (demanda.getCustoTotalDemanda().toString().length() + 3) * 256;

                if (sheet.getColumnWidth(8) < custoLength) {
                    sheet.setColumnWidth(8, custoLength);
                }
            }else {
                dataCell.setCellValue("");
                dataCell.setCellStyle(dataStyle);
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
        if (sheet.getColumnWidth(column) < (value.length()+2) * 256){
            sheet.setColumnWidth(column, (value.length()+2) * 256);
        }
    }


}
