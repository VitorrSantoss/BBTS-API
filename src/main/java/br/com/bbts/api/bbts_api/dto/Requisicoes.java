package br.com.bbts.api.bbts_api.dto;

import java.util.List;

import br.com.bbts.api.bbts_api.constants.Perfil;
import br.com.bbts.api.bbts_api.constants.TipoFeedback;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** Corpos aceitos pelos endpoints novos. */
public final class Requisicoes {

  private Requisicoes() {
  }

  public record LoginRequest(
      @NotBlank(message = "Informe o e-mail.") String email,
      @NotBlank(message = "Informe a senha.") @Size(min = 6, message = "A senha tem no mínimo 6 caracteres.") String senha,
      @NotNull(message = "Informe o perfil.") Perfil perfil) {
  }

  public record NovaEquipeRequest(
      @NotBlank(message = "Dê um nome à equipe.") @Size(max = 60) String nome,
      @NotBlank(message = "Conte o foco da equipe.") @Size(max = 80) String foco,
      String descricao,
      @NotNull(message = "Informe o gestor.") Long gestorId,
      List<String> emails) {
  }

  public record EmailsRequest(@NotNull List<String> emails) {
  }

  public record NivelCompetenciaRequest(
      @NotBlank(message = "Informe a sigla.") @Pattern(regexp = "^[A-Za-z]{4}$", message = "A sigla SFIA tem 4 letras.") String sigla,
      @NotNull(message = "Informe o nível.") @Min(value = 1, message = "Use um nível de 1 a 7.") @Max(value = 7, message = "Use um nível de 1 a 7.") Integer nivel) {
  }

  public record NovoFeedbackRequest(
      Long autorId,
      @NotBlank(message = "Informe o e-mail do colaborador.") @Email(message = "E-mail inválido.") String destinatarioEmail,
      @NotNull(message = "Informe o tipo.") TipoFeedback tipo,
      @NotBlank(message = "Escreva a mensagem.") @Size(max = 1000, message = "Use até 1.000 caracteres.") String mensagem) {
  }
}
