package br.sc.weg.sid.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor
public class AtaDG {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAtaDG;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"ataDg", "documentoAprovacaoAta", "pdfAta"})
    Ata ataAtaDg;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "idAtaDG")
    @JsonIgnore
    private List<PdfAta> pdfAtaDG;

}
