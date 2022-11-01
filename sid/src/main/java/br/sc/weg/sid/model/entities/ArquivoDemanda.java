package br.sc.weg.sid.model.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "ARQUIVO_DEMANDA")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ArquivoDemanda {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "IdArquivoDemanda", nullable = false, unique = true)
    private Integer idArquivoDemanda;

    @NonNull
    @Lob
    private byte[] arquivo;

    @JoinColumn(name = "idDemanda", nullable = false)
    @ManyToOne
    private Demanda idDemanda;

    @JoinColumn(name = "idUsuario", nullable = false)
    @ManyToOne
    private Usuario idUsuario;
}
