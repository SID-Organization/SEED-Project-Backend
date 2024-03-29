package br.sc.weg.sid.model.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class PdfDemanda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPdfDemanda;

    @Lob
    @Column(columnDefinition = "TEXT", name = "propostaMelhoriaDemandaHTML")
    private String propostaMelhoriaDemandaHTML;

    @Lob
    @Column(columnDefinition = "TEXT", name = "situacaoAtualDemandaHTML")
    private String situacaoAtualDemandaHTML;

    @Lob
    @Column(columnDefinition = "TEXT", name = "frequenciaUsoDemandaHTML")
    private String frequenciaUsoDemandaHTML;

    @JoinColumn(name = "idDemanda", referencedColumnName = "idDemanda")
    @ManyToOne(cascade = CascadeType.REMOVE)
    private Demanda demanda;
}
