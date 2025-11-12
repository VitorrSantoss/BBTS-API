package br.com.bbts.api.bbts_api.dto;

import java.time.LocalDate;

import br.com.bbts.api.bbts_api.constants.NivelIdioma;
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
  private NivelIdioma nivelIdioma;

}
