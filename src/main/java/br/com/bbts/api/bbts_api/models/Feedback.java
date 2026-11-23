package br.com.bbts.api.bbts_api.models;

import java.time.LocalDateTime;

import br.com.bbts.api.bbts_api.constants.TipoFeedback;
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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Feedback de um gestor para um colaborador. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_feedbacks")
public class Feedback {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "autor_id")
  private Usuario autor;

  @ManyToOne
  @JoinColumn(name = "destinatario_id", nullable = false)
  private Usuario destinatario;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private TipoFeedback tipo;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String mensagem;

  @Column(name = "data_envio", nullable = false)
  private LocalDateTime dataEnvio;

}
