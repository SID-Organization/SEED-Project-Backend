package br.sc.weg.sid.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

@Controller
@CrossOrigin
@RequestMapping("/sid/api/pdf-teste")
public class PdfTesteController {
    @GetMapping
    public ResponseEntity<Object> pdfTeste() throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("pdfTeste.pdf"));
        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        Chunk chunk = new Chunk("PDF TESTE", font);
        Paragraph paragraph = new Paragraph("Isto é um Parágrafo");
        document.add(paragraph);
        document.add(chunk);
        document.close();
        return ResponseEntity.status(HttpStatus.CREATED).body(document);
    }
}
