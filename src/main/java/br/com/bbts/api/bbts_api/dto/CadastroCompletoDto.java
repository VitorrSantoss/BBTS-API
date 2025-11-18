package br.com.bbts.api.bbts_api.dto;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CadastroCompletoDto {
  
  private UsuarioDto usuario;
  private List<IdiomaDto> idiomas;
  private List<ExperienciaDto> experiencias;
  private List<CertificacaoDto> certificacoes;
  
}
