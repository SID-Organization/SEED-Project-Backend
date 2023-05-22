package br.sc.weg.sid.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PdfAta> pdfAta;

    @Column(name = "NumeroDgAta", nullable = false)
    private Integer numeroDgAta;

    @Column(name = "DocumentoAprovacaoAta", nullable = false)
    @Lob
    private byte[] documentoAprovacaoAta;

    @OneToMany(cascade = CascadeType.ALL)
    List<PropostasLog> propostasLog;

    @JsonIgnoreProperties({"propostasPauta", "forumPauta", "horarioTerminoPauta"})
    @OneToOne
    Pauta pautaAta;

    @OneToOne(mappedBy = "ataAtaDg")
    AtaDG ataDg;
}
