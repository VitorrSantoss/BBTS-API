package br.com.bbts.api.bbts_api.models;

import br.com.bbts.api.bbts_api.constants.StatusFormacao;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Formação acadêmica do usuário. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_formacoes")
public class Formacao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Instituição é obrigatória")
  @Column(nullable = false, length = 150)
  private String instituicao;

  @NotBlank(message = "Curso é obrigatório")
  @Column(nullable = false, length = 150)
  private String curso;

  @NotNull(message = "Ano de início é obrigatório")
  @Column(name = "ano_inicio", nullable = false)
  private Integer anoInicio;

  @Column(name = "ano_fim")
  private Integer anoFim;

  @NotNull(message = "Situação é obrigatória")
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private StatusFormacao status;

  @ManyToOne
  @JoinColumn(name = "usuario_id", nullable = false)
  private Usuario usuario;

}
