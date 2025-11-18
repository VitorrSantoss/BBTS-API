package br.com.bbts.api.bbts_api.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExperienciaDto {

  private String empresa;
  private String cargo;
  private LocalDate dataInicio;
  private LocalDate dataFim;
  private boolean empregoAtual;
  
}
