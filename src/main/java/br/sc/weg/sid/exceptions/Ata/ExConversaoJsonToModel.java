package br.sc.weg.sid.exceptions.Ata;

public class ExConversaoJsonToModel extends RuntimeException{

        String message;

        public ExConversaoJsonToModel() {
            super();
            this.message = "Erro ao converter JSON para Ata";
        }
}
