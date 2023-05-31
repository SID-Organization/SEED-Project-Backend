package br.sc.weg.sid.exceptions.Ata;

public class ExDocumentoAprovacao extends RuntimeException{

    String message;

    public ExDocumentoAprovacao() {
        super();
        this.message = "Erro ao setar o documento de aprovação da ata";
    }
}
