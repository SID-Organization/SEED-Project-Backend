package br.sc.weg.sid.model.enums;

public enum TamanhoDemanda {
    MUITO_PEQUENA("Muito Pequena"), // 1 a 40 horas
    PEQUENA("Pequena"), // 41 a 300 horas
    MEDIA("Média"), // 301 a 1000 horas
    GRANDE("Grande"), // 1001 a 3000 horas
    MUITO_GRANDE("Muito Grande"); // acima de 3000 horas

    String nome;
    TamanhoDemanda(String nome){
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}
