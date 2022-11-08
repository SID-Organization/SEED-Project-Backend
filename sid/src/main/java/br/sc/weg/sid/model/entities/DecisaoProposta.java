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
    @Column(name = "IdDecisaoProposta", nullable = false, unique = true)
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
    private Status status;

    @Column(name = "AtaPublicadaDecisaoProposta", nullable = false)
    private Integer ataPublicada;

    @Column(name = "ComentarioDecisaoProposta", nullable = false, length = 4000)
    private String comentario;

    @Column(name = "NumeroSequencialDecisaoProposta", nullable = false)
    private Integer numeroSequencial;


}
