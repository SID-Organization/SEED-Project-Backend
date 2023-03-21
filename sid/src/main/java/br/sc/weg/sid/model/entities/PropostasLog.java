package br.sc.weg.sid.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

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
    private Integer demandaTempoExecucaoPropostaLog;

    @Column(name = "demandaValorPropostaLog", nullable = false)
    private Integer demandaValorPropostaLog;

    @Column(name = "parecerComissaoPropostaLog", nullable = false)
    private ParecerComissao parecerComissaoPropostaLog;

    @Column(name = "consideracoesProposta", nullable = false)
    private String consideracoesProposta;

    @Column(name = "tipoAta", nullable = false)
    private TipoAta tipoAta;

//    @Column(name = "propostaPropostaLog", nullable = false)
//    @OneToOne
//    @JsonIgnore
//    private Proposta propostaPropostaLog;

}
