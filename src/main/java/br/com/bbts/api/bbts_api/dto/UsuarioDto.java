package br.com.bbts.api.bbts_api.dto;

import java.time.LocalDate;

import br.com.bbts.api.bbts_api.constants.NivelIdioma;
import br.com.bbts.api.bbts_api.constants.Perfil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDto {
  private Long id;
  private String nome;
  private String cpf;
  private LocalDate dataNascimento;
  private String email;
  private String telefone;
  private String area;
  private NivelIdioma nivelIdioma;
  private Perfil perfil;
  private Long equipeId;
  /** Muda a cada nova foto (epoch em ms). Nulo quando o usuário não tem foto. */
  private Long fotoVersao;

}
