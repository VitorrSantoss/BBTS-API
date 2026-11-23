package br.com.bbts.api.bbts_api.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Nível atual de um colaborador numa competência do catálogo. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_competencias_usuario", uniqueConstraints = @UniqueConstraint(columnNames = { "usuario_id", "competencia_id" }))
public class CompetenciaUsuario {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "usuario_id", nullable = false)
  private Usuario usuario;

  @ManyToOne
  @JoinColumn(name = "competencia_id", nullable = false)
  private Competencia competencia;

  @Column(nullable = false)
  private Integer nivel;

}
