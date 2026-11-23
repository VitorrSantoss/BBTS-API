package br.com.bbts.api.bbts_api.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Curso de capacitação ligado a uma competência. Entra na jornada de quem tem lacuna nela. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_cursos")
public class Curso {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 150)
  private String titulo;

  @ManyToOne
  @JoinColumn(name = "competencia_id", nullable = false)
  private Competencia competencia;

}
