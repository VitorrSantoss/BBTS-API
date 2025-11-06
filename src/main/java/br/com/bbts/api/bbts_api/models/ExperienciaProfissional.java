package br.com.bbts.api.bbts_api.models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tb_experiencias_profissionais")
public class ExperienciaProfissional {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Nome da empresa é obrigatório")
  @Column(nullable = false, length = 200)
  private String empresa;

  @NotBlank(message = "Cargo é obrigatório")
  @Column(nullable = false, length = 150)
  private String cargo;

  @NotNull(message = "Data de início é obrigatória")
  @Column(name = "data_inicio", nullable = false)
  private LocalDate dataInicio;

  @Column(name = "data_fim")
  private LocalDate dataFim;

  @Column(name = "emprego_atual")
  private Boolean empregoAtual = false;

  @Column(name = "descricao", columnDefinition = "TEXT")
  private String descricao;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "usuario_id", nullable = false)
  private Usuario usuario;

}
