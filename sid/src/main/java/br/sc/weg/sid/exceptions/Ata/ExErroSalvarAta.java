package br.sc.weg.sid.exceptions.Ata;

public class ExErroSalvarAta extends RuntimeException{

        String message;

        public ExErroSalvarAta() {
            super();
            this.message = "Erro ao salvar a ata no banco de dados";
        }
}
