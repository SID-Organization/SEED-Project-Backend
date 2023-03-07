package br.sc.weg.sid.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ConverterUtil implements AttributeConverter<JsonObject, String> {

    @Override
    public String convertToDatabaseColumn(JsonObject jsonObject) {
        return jsonObject.toString();
    }

    @Override
    public JsonObject convertToEntityAttribute(String dbData) {
        return new JsonParser().parse(dbData).getAsJsonObject();
    }
}
