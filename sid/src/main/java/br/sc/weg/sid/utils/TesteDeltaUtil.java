package br.sc.weg.sid.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonParser;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.Data;
import lombok.ToString;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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

    public String converter(JsonObject deltaJson) {
        System.out.println("deltaJson: " + deltaJson);
//        StringBuilder html = new StringBuilder();
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            JsonNode delta = mapper.readTree(String.valueOf(deltaJson));
//            for (JsonNode op : delta.get("ops")) {
//                if (op.has("insert")) {
//                    String text = op.get("insert").asText();
//                    if (op.has("attributes")) {
//                        JsonNode attrs = op.get("attributes");
//                        if (attrs.has("bold")) {
//                            text = "<strong>" + text + "</strong>";
//                        }
//                        if (attrs.has("italic")) {
//                            text = "<em>" + text + "</em>";
//                        }
//                        if (attrs.has("list")) {
//                            String tag = attrs.get("list").asText().equals("ordered") ? "ol" : "ul";
//                            text = "<" + tag + "><li>" + text + "</li></" + tag + ">";
//                        }
//                    }
//                    html.append(text);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return html.toString();
        return deltaJson.toString();
    }
}
