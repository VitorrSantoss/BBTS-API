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

/** Nível mínimo que a equipe espera de cada membro numa competência. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_competencias_minimas", uniqueConstraints = @UniqueConstraint(columnNames = { "equipe_id", "competencia_id" }))
public class CompetenciaMinima {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "equipe_id", nullable = false)
  private Equipe equipe;

  @ManyToOne
  @JoinColumn(name = "competencia_id", nullable = false)
  private Competencia competencia;

  @Column(name = "nivel_minimo", nullable = false)
  private Integer nivelMinimo;

}
