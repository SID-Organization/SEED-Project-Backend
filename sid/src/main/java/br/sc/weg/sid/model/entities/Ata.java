package br.sc.weg.sid.model.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ATA")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Ata {
    @Id
    @Column(name = "IdAta", nullable = false, unique = true)
    private Integer idAta;

    @Column(name = "PdfAta", nullable = false)
    private byte[] pdfAta;

    @Column(name = "NumeroDgAta", nullable = false)
    private Integer numeroDgAta;

    @Column(name = "DocumentoAprovacaoAta", nullable = false)
    private byte[] documentoAprovacaoAta;
}
