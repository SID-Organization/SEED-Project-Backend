package br.sc.weg.sid.model.exporter;

import br.sc.weg.sid.model.entities.Demanda;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.beans.ConstructorProperties;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class DemandaExcelExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    private List<Demanda> demandas;

    public DemandaExcelExporter(List<Demanda> demandas) {
        this.demandas = demandas;
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Demandas");
    }

    public void criarHeader() {
        Row row = sheet.createRow(0);

        Cell cell = row.createCell(1);
        cell.setCellValue("Demanda ID");

//        sheet.setColumnWidth(1, 12 * 256);

    }

    public void setarDadosCelulas() {

    }

    public byte[] criarBytes() throws IOException {
        criarHeader();
        setarDadosCelulas();

        System.out.println("Criando bytes");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }

}
