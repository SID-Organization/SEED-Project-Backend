package br.sc.weg.sid.controller;

import br.sc.weg.sid.utils.TesteDeltaUtil;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Controller
@CrossOrigin
@RequestMapping("/sid/api/pdf-teste")
public class PdfTesteController {
    @PostMapping()
    ResponseEntity<Object> mandaCodificadoVoltaPDF(@RequestBody String deltaCodificado) {
        TesteDeltaUtil testeDeltaUtil = new TesteDeltaUtil();
        String deltaDecodificado = testeDeltaUtil.decoderDelta(deltaCodificado);
        return new ResponseEntity<>(testeDeltaUtil.converter(deltaDecodificado), HttpStatus.OK);
    }
}