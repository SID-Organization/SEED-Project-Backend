package br.sc.weg.sid.model.entities;

import lombok.Data;
import lombok.Generated;

import javax.persistence.*;
import java.util.List;

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

    @Column(columnDefinition = "TEXT", name = "frequenciaUsoDemandaHTML")
    private String frequenciaUsoDemandaHTML;

    @JoinColumn(name = "idDemanda", referencedColumnName = "idDemanda")
    @ManyToOne
    private Demanda demanda;
}
