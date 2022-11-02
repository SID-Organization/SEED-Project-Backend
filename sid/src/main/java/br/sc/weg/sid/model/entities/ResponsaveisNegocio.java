package br.sc.weg.sid.model.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "RESPONSAVEIS_NEGOCIO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ResponsaveisNegocio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idResponsaveisNegocio", nullable = false, unique = true)
    private Integer idResponsaveisNegocio;

    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario idUsuario;

    @ManyToOne
    @JoinColumn(name = "idProposta", nullable = false)
    private Proposta idProposta;
}
