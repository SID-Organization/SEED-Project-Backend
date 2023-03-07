package br.sc.weg.sid.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.Data;
import lombok.ToString;

import java.io.FileOutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Data
@ToString
public class TesteDeltaUtil {

    public String decoderDelta(String deltaCodificado) {
        String deltaDecodificado = "";
        if (deltaCodificado != null && !deltaCodificado.isEmpty()) {
            try {
                deltaDecodificado = URLDecoder.decode(deltaCodificado, StandardCharsets.UTF_8.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return deltaDecodificado;
    }

    public String encodeDelta(String deltaDecodificado) {
        String deltaCodificado = "";
        deltaDecodificado = deltaDecodificado.replaceAll("\\s", "");
        if (deltaDecodificado != null && !deltaDecodificado.isEmpty()) {
            try {
                deltaCodificado = URLEncoder.encode(deltaDecodificado, StandardCharsets.UTF_8.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return deltaCodificado;
    }

    public byte[] inserirDeltaPDF(byte[] pdf, String delta) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\otavio_a_santos\\Documents\\testedelta.pdf"));
            document.open();
            document.add(new Paragraph(delta));
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pdf;
    }

    public String converter(String delta) {
        String html = "";

        try {
            // Carrega o arquivo JavaScript
            String script = new String(Files.readAllBytes(Paths.get("C:\\Users\\otavio_a_santos\\Documents\\GitHub\\SID-Project-Backend\\sid\\src\\main\\resources\\static\\QuillDeltaToHtmlConverter.js")), StandardCharsets.UTF_8);

            // Cria uma instância do mecanismo de script Nashorn
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

            // Executa o arquivo JavaScript
            engine.eval(script);

            // Obtém a referência da função "convertDeltaToHtml" no arquivo JavaScript
            Object function = engine.eval("convertDeltaToHtml");

            // Converte o delta para HTML usando a função obtida acima
            html = (String) ((javax.script.Invocable) engine).invokeFunction("convertDeltaToHtml", delta);
        } catch (ScriptException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return html;
    }


}
