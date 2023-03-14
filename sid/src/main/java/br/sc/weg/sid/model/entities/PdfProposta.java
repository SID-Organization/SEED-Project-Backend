package br.sc.weg.sid.model.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import java.util.Date;
import java.util.List;

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

    @JoinColumn(name = "proposta", referencedColumnName = "idProposta")
    @ManyToOne
    private Proposta proposta;
}
