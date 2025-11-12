package br.com.bbts.api.bbts_api.models;

import java.time.LocalDate;

import br.com.bbts.api.bbts_api.dto.UsuarioDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
  
  // Convertendo uma entidade para um DTO
  public UsuarioDto converterParaDto(){

    UsuarioDto dto = new UsuarioDto();

    dto.setId(id);
    dto.setNome(nome);
    dto.setCpf(cpf);
    dto.setDataNascimento(dataNascimento);
    dto.setEmail(email);
    dto.setTelefone(telefone);
    
    return dto;

  }
}
