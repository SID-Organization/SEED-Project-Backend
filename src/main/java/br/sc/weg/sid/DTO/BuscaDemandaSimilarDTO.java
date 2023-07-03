package br.sc.weg.sid.DTO;

import lombok.Data;

@Data
public class BuscaDemandaSimilarDTO {
    private DemandaDTO demanda;
    private double similaridade;

    @Data
    public static class DemandaDTO {
        private int id_demanda;
        private String titulo;
        private String proposta_melhoria;
        private String descricao_qualitativo;
        private String frequencia_uso_demanda;
        private String situacao_atual_demanda;

        // Getters e setters
    }

    // Getters e setters
}
