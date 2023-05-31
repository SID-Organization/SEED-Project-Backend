package br.sc.weg.sid.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ARQUIVO_DEMANDA")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
public class ArquivoDemanda {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "IdArquivoDemanda", nullable = false, unique = true)
    private Integer idArquivoDemanda;

    @NonNull
    private String nomeArquivo;

    @NonNull
    private String tipoArquivo;

    @NonNull
    @Lob
    private byte[] arquivo;

    @JoinColumn(name = "DataRegistroArquivo", nullable = false)
    private Date dataRegistroArquivo;

    @JoinColumn(name = "idDemanda", nullable = false)
    @ManyToOne
    @JsonIgnore
    private Demanda idDemanda;

    @JoinColumn(name = "idUsuario", nullable = false)
    @ManyToOne
    @JsonIgnore
    private Usuario idUsuario;
}
