package br.sc.weg.sid.model.entities;

import br.sc.weg.sid.model.enums.StatusDemanda;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "DECISAO_PROPOSTA")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class DecisaoProposta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idDecisaoProposta", nullable = false, unique = true)
    private Integer idDecisaoProposta;

    @ManyToOne
    @JoinColumn(name = "propostaDecisaoProposta", nullable = false)
    private Proposta propostaDecisaoProposta;

    @ManyToOne
    @JoinColumn(name = "pautaDecisaoProposta", nullable = false)
    private Pauta pautaDecisaoProposta;

    @ManyToOne
    @JoinColumn(name = "ataDecisaoProposta", nullable = false)
    private Ata ataDecisaoProposta;

    @Enumerated(EnumType.STRING)
    @Column(name = "StatusComissaoDecisaoProposta", nullable = false)
    private StatusDemanda statusDemandaDecisaoProposta;

    @Column(name = "AtaPublicadaDecisaoProposta", nullable = false)
    private Integer ataPublicadaDecisaoProposta;

    @Column(name = "ComentarioDecisaoProposta", nullable = false, length = 999999999)
    private String comentarioDecisaoProposta;

    @Column(name = "NumeroSequencialDecisaoProposta", nullable = false)
    private Integer numeroSequencialDecisaoProposta;


}
