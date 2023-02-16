package br.sc.weg.sid.DTO;

import br.sc.weg.sid.model.entities.Usuario;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CadastroNotificacaoDTO {
    @NotBlank(message = "O campo texto da notificação é obrigatório")
    private String textoNotificacao;
    @NotBlank(message = "O campo link da notificação é obrigatório")
    private String linkNotificacao;
    @NotNull(message = "O campo usuários é obrigatório")
    private List<Usuario> usuarios;
}
