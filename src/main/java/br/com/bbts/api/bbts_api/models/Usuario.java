package br.com.bbts.api.bbts_api.models;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.bbts.api.bbts_api.constants.Perfil;
import br.com.bbts.api.bbts_api.dto.UsuarioDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

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

  // Guardada com BCrypt. Nula até o primeiro acesso de quem foi cadastrado sem senha.
  // WRITE_ONLY impede que ela saia nas respostas (as listas embutem o usuário dono).
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
  @Column(nullable = true)
  private String senha;

  // Só dígitos (10 ou 11) ou com máscara: (81) 91234-5678 / (81) 3234-5678
  @Pattern(regexp = "^\\(\\d{2,3}\\) \\d{4,5}-\\d{4}$|^\\d{10,11}$", message = "Telefone inválido")
  @Column(length = 20)
  private String telefone;

  // Área de atuação, opcional (ex.: "Desenvolvimento Front-End")
  @Size(max = 120, message = "Área de atuação deve ter no máximo 120 caracteres")
  @Column(length = 120)
  private String area;

  // Nulo em cadastros anteriores à coluna: vale como COLABORADOR (ver getPerfilEfetivo).
  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private Perfil perfil;

  // Equipe do colaborador. Fica fora do JSON para não entrar em ciclo (equipe -> gestor -> equipe).
  @JsonIgnore
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne
  @JoinColumn(name = "equipe_id")
  private Equipe equipe;

  @JsonIgnore
  public Perfil getPerfilEfetivo() {
    return perfil == null ? Perfil.COLABORADOR : perfil;
  }

  // Convertendo uma entidade para um DTO
  public UsuarioDto converterParaDto(){

    UsuarioDto dto = new UsuarioDto();

    dto.setId(id);
    dto.setNome(nome);
    dto.setCpf(cpf);
    dto.setDataNascimento(dataNascimento);
    dto.setEmail(email);
    dto.setTelefone(telefone);
    dto.setArea(area);
    dto.setPerfil(getPerfilEfetivo());
    dto.setEquipeId(equipe == null ? null : equipe.getId());

    return dto;

  }
}
