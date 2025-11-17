package br.com.bbts.api.bbts_api.models;

import br.com.bbts.api.bbts_api.constants.NivelHabilidades;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Entity(name = "tb_tecnologia")
public class Tecnologia {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Nome é obrigatório")
  @Column(nullable = false, length = 30)
  private String nome;
  
  @NotNull(message = "Nível de habilidade é obrigatório")
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 30)
  private NivelHabilidades nivel;

  @ManyToOne
  @JoinColumn(name = "usuario_id", nullable = false)
  private Usuario usuario;
}
