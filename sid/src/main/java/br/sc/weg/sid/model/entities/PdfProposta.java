package br.sc.weg.sid.model.entities;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
public class PdfProposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPdfProposta;

    @Column(columnDefinition = "TEXT", name = "propostaMelhoriaPropostaHTML")
    private String escopoPropostaHTML;

    @Column(columnDefinition = "TEXT", name = "naoFazParteDoEscopoPropostaHTML")
    private String naoFazParteDoEscopoPropostaHTML;

    @Column(columnDefinition = "TEXT", name = "alternativasAvaliadasPropostaHTML")
    private String alternativasAvaliadasPropostaHTML;

    @Column(columnDefinition = "TEXT", name = "planoMitigacaoPropostaHTML")
    private String planoMitigacaoPropostaHTML;

    @Column(columnDefinition = "TEXT", name = "abrangenciaProjetoPropostaHTML")
    private String abrangenciaProjetoPropostaHTML;

    @JoinColumn(name = "proposta", referencedColumnName = "idProposta")
    @ManyToOne
    @ToString.Exclude
    private Proposta proposta;
}
