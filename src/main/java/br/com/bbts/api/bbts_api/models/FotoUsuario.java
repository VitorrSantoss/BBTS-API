package br.com.bbts.api.bbts_api.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Foto de perfil, numa tabela à parte: assim os bytes não são carregados
 * toda vez que um usuário aparece embutido em outra resposta.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_fotos_usuario")
public class FotoUsuario {

  @Id
  @Column(name = "usuario_id")
  private Long usuarioId;

  @Column(nullable = false, length = 100)
  private String tipo;

  @Lob
  @Column(nullable = false, columnDefinition = "LONGBLOB")
  private byte[] dados;

  @Column(name = "atualizada_em", nullable = false)
  private LocalDateTime atualizadaEm;

}
