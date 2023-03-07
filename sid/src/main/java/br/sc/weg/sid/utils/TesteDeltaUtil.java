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
import java.util.LinkedHashMap;
import java.util.List;
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

//    public String converter( deltaJson) {
//
//    }

    private static final Map<String, String> FORMAT_TAGS_MAP = new LinkedHashMap<String, String>() {{
        put("header", "h");
        put("bold", "strong");
        put("italic", "em");
        put("underline", "u");
        put("strike", "s");
        put("link", "a");
        put("image", "img");
        put("code-block", "pre");
        put("blockquote", "blockquote");
        put("list", "ul");
        put("ordered", "ol");
        put("indent", "blockquote");
        put("align", "div");
    }};

    public String convertDeltaHtml(Map<String, Object> delta) {
        String html = "";
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.convertValue(delta, JsonNode.class);
            html = convertDeltaToHtml(jsonNode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return html;
    }

    private String convertDeltaToHtml(JsonNode delta) {
        StringBuilder html = new StringBuilder();
        if (delta.isArray()) {
            for (JsonNode node : delta) {
                html.append(convertDeltaToHtml(node));
            }
        } else if (delta.isObject()) {
            String format = delta.get("insert").asText();
            if (FORMAT_TAGS_MAP.containsKey(format)) {
                String tag = FORMAT_TAGS_MAP.get(format);
                html.append("<").append(tag).append(">");
                if (delta.has("attributes")) {
                    JsonNode attributes = delta.get("attributes");
                    if (attributes.has("header")) {
                        html.append("<h").append(attributes.get("header").asInt()).append(">");
                    }
                    if (attributes.has("link")) {
                        html.append("<a href=\"").append(attributes.get("link").asText()).append("\">");
                    }
                    if (attributes.has("image")) {
                        html.append("<img src=\"").append(attributes.get("image").asText()).append("\">");
                    }
                    if (attributes.has("align")) {
                        html.append("<div style=\"text-align:").append(attributes.get("align").asText()).append("\">");
                    }
                }
                if (delta.has("insert")) {
                    html.append(delta.get("insert").asText());
                }
                if (delta.has("attributes")) {
                    JsonNode attributes = delta.get("attributes");
                    if (attributes.has("header")) {
                        html.append("</h").append(attributes.get("header").asInt()).append(">");
                    }
                    if (attributes.has("link")) {
                        html.append("</a>");
                    }
                    if (attributes.has("image")) {
                        html.append("</img>");
                    }
                    if (attributes.has("align")) {
                        html.append("</div>");
                    }
                }
                html.append("</").append(tag).append(">");
            } else if (format.equals("list")) {
                html.append("<ul>");
                if (delta.has("insert")) {
                    JsonNode insert = delta.get("insert");
                    if (insert.isArray()) {
                        for (JsonNode node : insert) {
                            html.append("<li>").append(node.asText()).append("</li>");
                        }
                    }
                }
                html.append("</ul>");
            } else if (format.equals("ordered")) {
                html.append("<ol>");
                if (delta.has("insert")) {
                    JsonNode insert = delta.get("insert");
                    if (insert.isArray()) {
                        for (JsonNode node : insert) {
                            html.append("<li>").append(node.asText()).append("</li>");
                        }
                    }
                    

}

