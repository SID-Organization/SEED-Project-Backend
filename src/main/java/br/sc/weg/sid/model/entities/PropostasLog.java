package br.sc.weg.sid.model.entities;

import br.sc.weg.sid.model.enums.ParecerComissao;
import br.sc.weg.sid.model.enums.TipoAta;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "PROPOSTA_LOG")
@Data
@AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode
public class PropostasLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPropostaLog", nullable = false, unique = true)
    private Integer idPropostaLog;

    @Column(name = "demandaTituloPropostaLog", nullable = false)
    private String demandaTituloPropostaLog;

    @Column(name = "demandaTempoExecucaoPropostaLog", nullable = false)
    private Long demandaTempoExecucaoPropostaLog;

    @Column(name = "demandaValorPropostaLog", nullable = false)
    private Double demandaValorPropostaLog;

    @Column(name = "parecerComissaoPropostaLog", nullable = false)
    private ParecerComissao parecerComissaoPropostaLog;

    @Column(name = "consideracoesProposta", nullable = false)
    private String consideracoesPropostaLog;

    @Column(name = "parecerDGPropostaLog")
    private ParecerComissao parecerDGPropostaLog;

    @Column(name = "consideracoesParecerDGPropostaLog")
    private String consideracoesParecerDGPropostaLog;

    @Column(name = "tipoAta", nullable = false)
    private TipoAta tipoAtaPropostaLog;

    @Column(name = "pdfPropostaLog")
    @Lob
    byte[] pdfPropostaLog;

    private Integer idDemanda;

    @JsonIgnoreProperties({"responsaveisNegocio", "pautaProposta", "pdfProposta", "demandaProposta"})
    @OneToOne
    private Proposta propostaPropostaLog;

}
