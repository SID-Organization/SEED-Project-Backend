package br.sc.weg.sid.DTO.filtro.demanda;

import br.sc.weg.sid.model.entities.Usuario;
import lombok.Data;

import java.util.List;

@Data
public class CadastroFiltroDemandaDTO {
    private String nomeFiltro;
    private Usuario usuario;
    private List<FiltroDemandaDTO> filtros;
    private Integer idFiltroDemanda;
}
