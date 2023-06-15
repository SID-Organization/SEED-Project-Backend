package br.sc.weg.sid.model.service;


import br.sc.weg.sid.model.entities.Demanda;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelExporterService {


    public void criarTabelaDemandaExcel(HttpServletResponse response, List<Demanda> demandasList) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Demandas");

        Row row = sheet.createRow(0);

        Cell cell = row.createCell(1);
        cell.setCellValue("Demanda ID");

        System.out.println("Criando arquivo excel");

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();

    }

}
