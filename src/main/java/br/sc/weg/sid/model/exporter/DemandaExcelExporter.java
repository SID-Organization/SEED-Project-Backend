package br.sc.weg.sid.model.exporter;

import br.sc.weg.sid.model.entities.Demanda;
import lombok.AllArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.beans.ConstructorProperties;
import java.util.List;

public class DemandaExcelExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    private List<Demanda> demandas;

    public DemandaExcelExporter(List<Demanda> demandas) {
        this.demandas = demandas;
    }

    public void criarHeader() {

    }

    public void criarCelulas() {

    }

    public void criarArquivo(HttpServletResponse response) {

    }
}
