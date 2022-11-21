package br.sc.weg.sid.model.entities;

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
    @JoinColumn(name = "idProposta", nullable = false)
    private Proposta idProposta;

    @ManyToOne
    @JoinColumn(name = "idPauta", nullable = false)
    private Pauta idPauta;

    @ManyToOne
    @JoinColumn(name = "idAta", nullable = false)
    private Ata idAta;

    @Enumerated(EnumType.STRING)
    @Column(name = "StatusComissaoDecisaoProposta", nullable = false)
    private Status statusDecisaoProposta;

    @Column(name = "AtaPublicadaDecisaoProposta", nullable = false)
    private Integer ataPublicadaDecisaoProposta;

    @Column(name = "ComentarioDecisaoProposta", nullable = false, length = 4000)
    private String comentarioDecisaoProposta;

    @Column(name = "NumeroSequencialDecisaoProposta", nullable = false)
    private Integer numeroSequencialDecisaoProposta;


}
