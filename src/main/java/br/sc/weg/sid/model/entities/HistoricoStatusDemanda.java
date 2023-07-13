package br.sc.weg.sid.model.entities;

import br.sc.weg.sid.model.enums.StatusDemanda;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "HISTORICO_STATUS_DEMANDA")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class HistoricoStatusDemanda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idHistoricoStatusDemanda", nullable = false, unique = true)
    private Integer idHistoricoStatusDemanda;

    @ManyToOne()
    @JoinColumn(name = "idDemanda", nullable = false)
    private Demanda demanda;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusDemanda statusDemanda;

    @Column(nullable = false)
    private Date dataAlteracaoStatusDemanda;
}
