package br.com.bbts.api.bbts_api.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tb_usuarios")
public class Usuario {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Nome completo é obrigatório")
  @Size(max = 200)
  @Column(name = "nome_completo", nullable = false, length = 200)
  private String nome;

  @Column(name = "CPF", nullable = false, unique = true)
  private String cpf;

  @NotNull(message = "Data de nascimento é obrigatória")
  @Column(name = "data_nascimento", nullable = false)
  private LocalDate dataNascimento;

  @NotBlank(message = "E-mail é obrigatório")
  @Email(message = "E-mail inválido")
  @Column(nullable = false, unique = true, length = 150)
  private String email;

  @NotBlank(message = "Senha é obrigatória")
  @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
  @Column(nullable = false)
  private String senha;

  @Pattern(regexp = "^\\(\\d{3}\\) \\d{5}-\\d{4}$|^\\d{10,11}$", message = "Telefone inválido")
  @Column(length = 20)
  private String telefone;

  // Relacionamentos
  @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Idioma> idiomas = new ArrayList<>();

  @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ExperienciaProfissional> experiencias = new ArrayList<>();

  @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Certificacoes> certificacoes = new ArrayList<>();

  // Metadados
  @CreationTimestamp
  @Column(name = "criado_em", nullable = false, updatable = false)
  private LocalDateTime criadoEm;

  @UpdateTimestamp
  @Column(name = "atualizado_em")
  private LocalDateTime atualizadoEm;

  @Column(name = "ativo")
  private Boolean ativo = true;

  // Métodos auxiliares para relacionamentos bidirecionais
  public void adicionarIdioma(Idioma idioma) {
    idiomas.add(idioma);
    idioma.setUsuario(this);
  }

  public void removerIdioma(Idioma idioma) {
    idiomas.remove(idioma);
    idioma.setUsuario(null);
  }

  public void adicionarExperiencia(ExperienciaProfissional experiencia) {
    experiencias.add(experiencia);
    experiencia.setUsuario(this);
  }

  public void removerExperiencia(ExperienciaProfissional experiencia) {
    experiencias.remove(experiencia);
    experiencia.setUsuario(null);
  }

  public void adicionarCertificacao(Certificacoes certificacao) {
    certificacoes.add(certificacao);
    certificacao.setUsuario(this);
  }

  public void removerCertificacao(Certificacoes certificacao) {
    certificacoes.remove(certificacao);
    certificacao.setUsuario(null);
  }

}
