package br.sc.weg.sid.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Date;
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

    private Integer numeroAtaDG;

    @Column(nullable = false)
    @Lob
    private byte[] documentoAprovacaoAta;

    @OneToMany(cascade = CascadeType.ALL)
    List<PropostasLog> propostasLog;

    @Column(nullable = false)
    private Date dataReuniaoPauta;

    @JsonFormat(pattern = "HH:mm")
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @Column(nullable = false)
    private LocalTime horarioInicioPauta;

    @JsonFormat(pattern = "HH:mm")
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @Column(nullable = false)
    private LocalTime horarioTerminoPauta;

    @ManyToOne()
    @JoinColumn(name = "numero_cadastro_usuario")
    private Usuario analistaResponsavelPauta;
}
