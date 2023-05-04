package br.sc.weg.sid.exceptions.Ata;

public class ExConversaoDTOAta extends RuntimeException{

    String message;

    public ExConversaoDTOAta() {
        super();
        this.message = "Erro ao converter JSON para CadastroAtaDTO";
    }
}
