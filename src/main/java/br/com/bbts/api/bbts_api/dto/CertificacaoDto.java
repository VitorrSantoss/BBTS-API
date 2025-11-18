package br.com.bbts.api.bbts_api.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CertificacaoDto {
  
  private String nome;
  private LocalDate dataCertificacao;

}
