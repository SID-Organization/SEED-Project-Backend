package br.sc.weg.sid.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(name = "CustoDemandaValorInicial")
    private Double custoDemandaValorInicial;

    @Column(name = "CustoDemandaValorFinal")
    private Double custoDemandaValorFinal;

    @Column(name = "ScoreDemandaValorInicial")
    private Double scoreDemandaValorInicial;

    @Column(name = "ScoreDemandaValorFinal")
    private Double scoreDemandaValorFinal;

    @Column(name = "IdDemanda")
    private Integer idDemanda;

    @JoinColumn(name = "idUsuario")
    @ManyToOne
    @JsonIgnore
    private Usuario usuario;

}
