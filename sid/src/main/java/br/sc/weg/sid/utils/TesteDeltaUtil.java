package br.sc.weg.sid.utils;

import lombok.Data;
import lombok.ToString;
import java.net.URLDecoder;

@Data
@ToString
public class TesteDeltaUtil {

    public String decoderDelta(String deltaCodificado) {
        String deltaDecodificado = "";
        try {
            deltaDecodificado = URLDecoder.decode(deltaCodificado, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deltaDecodificado;
    }


}
