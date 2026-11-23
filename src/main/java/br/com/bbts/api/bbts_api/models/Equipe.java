package br.com.bbts.api.bbts_api.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Equipe acompanhada por um gestor. Os membros apontam para ela (Usuario.equipe). */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_equipes")
public class Equipe {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Nome da equipe é obrigatório")
  @Size(max = 60)
  @Column(nullable = false, length = 60)
  private String nome;

  @NotBlank(message = "Foco da equipe é obrigatório")
  @Size(max = 80)
  @Column(nullable = false, length = 80)
  private String foco;

  @Column(columnDefinition = "TEXT")
  private String descricao;

  @ManyToOne
  @JoinColumn(name = "gestor_id", nullable = false)
  private Usuario gestor;

}
