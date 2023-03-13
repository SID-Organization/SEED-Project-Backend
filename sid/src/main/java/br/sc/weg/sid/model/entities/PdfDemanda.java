package br.sc.weg.sid.model.entities;

import lombok.Data;
import lombok.Generated;

import javax.persistence.*;

@Data
@Entity
public class PdfDemanda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPdfDemanda;

    @Column(columnDefinition = "TEXT", name = "propostaMelhoriaDemandaHTML")
    private String propostaMelhoriaDemandaHTML;

    @Column(columnDefinition = "TEXT", name = "situacaoAtualDemandaHTML")
    private String situacaoAtualDemandaHTML;

    @JoinColumn(name = "idDemanda", referencedColumnName = "idDemanda")
    @ManyToOne
    private Demanda demanda;
}
