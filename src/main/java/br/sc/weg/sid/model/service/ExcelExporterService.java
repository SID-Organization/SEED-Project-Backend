package br.sc.weg.sid.model.service;


import br.sc.weg.sid.model.entities.Demanda;
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

        Cell cell = headerRow.createCell(2);
        cell.setCellValue("Demanda ID");
        cell.setCellStyle(titleStyle);
        sheet.setColumnWidth(2, 12 * 256);


        cell = headerRow.createCell(3);
        cell.setCellValue("Titulo");
        cell.setCellStyle(titleStyle);
        sheet.setColumnWidth(3, 8 * 256);

        cell = headerRow.createCell(4);
        cell.setCellValue("Status");
        cell.setCellStyle(titleStyle);
        sheet.setColumnWidth(4, 8 * 256);

        cell = headerRow.createCell(5);
        cell.setCellValue("Tamanho");
        cell.setCellStyle(titleStyle);
        sheet.setColumnWidth(5, 10 * 256);

        cell = headerRow.createCell(6);
        cell.setCellValue("Data de Criação");
        cell.setCellStyle(titleStyle);
        sheet.setColumnWidth(6, 17 * 256);

        cell = headerRow.createCell(7);
        cell.setCellValue("Score");
        cell.setCellStyle(titleStyle);
        sheet.setColumnWidth(7, 7 * 256);


        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setAlignment(HorizontalAlignment.CENTER);

        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);

        int rowNum = 2;
        for (Demanda demanda : demandasList) {
            Row dataRow = sheet.createRow(rowNum++);
            Cell dataCell = dataRow.createCell(2);
            dataCell.setCellValue(demanda.getIdDemanda());
            dataCell.setCellStyle(dataStyle);

            dataCell = dataRow.createCell(3);
            dataCell.setCellValue(demanda.getTituloDemanda());

            System.out.println("tamanho titulo demanda: " + demanda.getTituloDemanda().toString());
            System.out.println("tamanho da coluna: " + sheet.getColumnWidth(3));

            if (demanda.getTituloDemanda().length() + 2 > sheet.getColumnWidth(3)) {
                sheet.setColumnWidth(3, demanda.getTituloDemanda().length() + 2 * 256);
            }

            dataCell = dataRow.createCell(4);
            dataCell.setCellValue(demanda.getStatusDemanda().getNome());

            if (demanda.getStatusDemanda().getNome().length() + 2 > sheet.getColumnWidth(4)) {
                sheet.setColumnWidth(4, demanda.getStatusDemanda().getNome().length() + 2 * 256);
            }

            dataCell = dataRow.createCell(5);
            dataCell.setCellValue(demanda.getTamanhoDemanda().getNome());

            if (demanda.getTamanhoDemanda().getNome().length() + 2 > sheet.getColumnWidth(5)) {
                sheet.setColumnWidth(5, demanda.getTamanhoDemanda().getNome().length() + 2 * 256);
            }

            dataCell = dataRow.createCell(6);
            dataCell.setCellValue(demanda.getDataCriacaoDemanda().toString());

            if (demanda.getDataCriacaoDemanda().toString().length() + 2 > sheet.getColumnWidth(6)) {
                sheet.setColumnWidth(6, demanda.getDataCriacaoDemanda().toString().length() + 2 * 256);
            }

            dataCell = dataRow.createCell(7);
            if (demanda.getScoreDemanda() != null) {
                dataCell.setCellValue(demanda.getScoreDemanda());
            }

        }

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
        System.out.println(outputStream);
    }


}
