package br.com.bbts.api.bbts_api.models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tb_certificacoes")
public class Certificacoes {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Nome do curso é obrigatório")
  @Column(name = "nome", nullable = false, length = 200)
  private String nomeCurso;

  @NotNull(message = "Data de conclusão é obrigatória")
  @Column(name = "data_conclusao", nullable = false)
  private LocalDate dataConclusao;

  // --- NOVOS CAMPOS PARA O PDF ---
  @Column(name = "nome_arquivo")
  private String nomeArquivo; // Ex: certificado_java.pdf

  @Column(name = "tipo_arquivo")
  private String tipoArquivo; // Ex: application/pdf

  @Lob // Indica que é um "Large Object" (BLOB)
  @Column(name = "dados_arquivo") // LONGBLOB para MySQL suportar arquivos maiores
  private byte[] dadosArquivo;
   

  @ManyToOne
  @JoinColumn(name = "usuario_id", nullable = false)
  private Usuario usuario;

}
