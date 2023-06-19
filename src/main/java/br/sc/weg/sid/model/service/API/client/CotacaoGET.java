package br.sc.weg.sid.model.service.API.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CotacaoGET {

    public Double getCotacaoDolar() {
        OkHttpClient client = new OkHttpClient();

        String url = "https://economia.awesomeapi.com.br/json/last/USD-BRL";

        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                assert response.body() != null;
                String responseBody = response.body().string();

                // Faz o parsing do JSON
                JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();

                // Obtém o objeto "USDBRL"
                JsonObject usdBrlObject = jsonObject.getAsJsonObject("USDBRL");

                // Obtém o valor "high"
                String highValue = usdBrlObject.get("high").getAsString();

                // Converte o valor para Double e retorna
                return Double.parseDouble(highValue);
            } else {
                return -1.0;
            }
        } catch (IOException e) {
            return -1.0;
        }
    }

    public Double getCotacaoEuro() {
        OkHttpClient client = new OkHttpClient();

        String url = "https://economia.awesomeapi.com.br/json/last/EUR-BRL";

        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                assert response.body() != null;
                String responseBody = response.body().string();

                // Faz o parsing do JSON
                JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();

                // Obtém o objeto "USDBRL"
                JsonObject usdBrlObject = jsonObject.getAsJsonObject("EURBRL");

                // Obtém o valor "high"
                String highValue = usdBrlObject.get("high").getAsString();

                // Converte o valor para Double e retorna
                return Double.parseDouble(highValue);
            } else {
                return -1.0;
            }
        } catch (IOException e) {
            return -1.0;
        }
    }


}

