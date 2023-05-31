package br.sc.weg.sid.model.entities;

import br.sc.weg.sid.model.enums.StatusDemanda;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "MOTIVO_RECUSA")
@Data
@AllArgsConstructor @NoArgsConstructor
public class MotivoRecusa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdMotivoRecusa", nullable = false, unique = true)
    private Integer idMotivoRecusa;

    String descricaoMotivoRecusa;

    @ManyToOne()
    @JoinColumn(name = "idDemanda")
    @JsonIgnore
    Demanda demandaMotivoRecusa;

    StatusDemanda statusDemandaMotivoRecusa;

    Integer idHistoricoWorkflow;

}
