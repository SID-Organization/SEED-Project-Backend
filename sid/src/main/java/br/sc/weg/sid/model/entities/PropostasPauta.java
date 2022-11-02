package br.sc.weg.sid.model.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "PROPOSTA_PAUTA")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PropostasPauta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPropostaPauta", nullable = false, unique = true)
    private Integer idPropostaPauta;

    @ManyToOne
    @JoinColumn(name = "idProposta", nullable = false)
    private Proposta idProposta;

    @ManyToOne
    @JoinColumn(name = "idPauta", nullable = false)
    private Pauta idPauta;
}
