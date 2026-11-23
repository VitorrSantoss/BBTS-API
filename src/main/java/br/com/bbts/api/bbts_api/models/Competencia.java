package br.com.bbts.api.bbts_api.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Catálogo SFIA: sigla de 4 letras e o nível máximo da competência (de 1 a 7). */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_competencias")
public class Competencia {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Sigla é obrigatória")
  @Pattern(regexp = "^[A-Z]{4}$", message = "A sigla SFIA tem exatamente 4 letras maiúsculas")
  @Column(nullable = false, unique = true, length = 4)
  private String sigla;

  @NotBlank(message = "Nome é obrigatório")
  @Column(nullable = false, length = 120)
  private String nome;

  @Column(length = 80)
  private String categoria;

  @NotNull(message = "Nível máximo é obrigatório")
  @Min(1)
  @Max(7)
  @Column(name = "nivel_max", nullable = false)
  private Integer nivelMax;

}
