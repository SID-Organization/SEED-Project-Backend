package br.sc.weg.sid.model.entities;

import br.sc.weg.sid.model.enums.TipoAta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PdfAta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPdfAta;

    @Lob
    private byte[] pdfAta;

    TipoAta tipoAta;

    @ManyToOne
    @JoinColumn(name = "ata", referencedColumnName = "idAta")
    private Ata ataPdfAta;


}
