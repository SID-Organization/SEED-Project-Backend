package br.sc.weg.sid.model.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "ATA")
@Data
@AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode
public class Ata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdAta", nullable = false, unique = true)
    private Integer idAta;

    @Column(name = "PdfAta", nullable = false)
    @Lob
    @NotNull
    private byte[] pdfAta;

    @Column(name = "NumeroDgAta", nullable = false)
    private Integer numeroDgAta;

    @Column(name = "DocumentoAprovacaoAta", nullable = false)
    @Lob
    @NotNull
    private byte[] documentoAprovacaoAta;

    @Column(name = "PropostasAta", nullable = false)
    @OneToMany(mappedBy = "idPropostaLog")
    List<PropostasLog> propostasLogAta;

    @OneToOne
    Pauta pautaAta;
}
