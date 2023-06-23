package br.sc.weg.sid.model.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class FiltroDemanda {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idFiltroDemanda;

    @Column(name = "NomeFiltroDemanda")
    private String nomeFiltroDemanda;

    @Column(name = "NomeSolicitanteFiltroDemanda")
    private String nomeSolicitanteFiltroDemanda;

    @Column(name = "NomeGerenteResponsavelDemanda")
    private String nomeGerenteResponsavelDemanda;

    @Column(name = "NomeAnalistaResponsavel")
    private String nomeAnalistaResponsavel;

    @Column(name = "CodigoPPMDemanda")
    private Integer codigoPPMDemanda;

    @Column(name = "DepartamentoDemanda")
    private String departamentoDemanda;

    @Column(name = "ForumDeAprovacaoDemanda")
    private String forumDeAprovacaoDemanda;

    @Column(name = "TamanhoDemanda")
    private String tamanhoDemanda;

    @Column(name = "TituloDemanda")
    private String tituloDemanda;

    @Column(name = "StatusDemanda")
    private String statusDemanda;

    @Column(name = "ValorDemandaInicial")
    private Double valorDemandaInicial;

    @Column(name = "ValorDemandaFinal")
    private Double valorDemandaFinal;

    @Column(name = "ScoreDemandaValorInicial")
    private Double scoreDemandaValorInicial;

    @Column(name = "ScoreDemandaValorFinal")
    private Double scoreDemandaValorFinal;

    @Column(name = "IdDemanda")
    private Integer idDemanda;

    @JoinColumn(name = "idUsuario")
    @ManyToOne
    private Usuario usuario;

}
