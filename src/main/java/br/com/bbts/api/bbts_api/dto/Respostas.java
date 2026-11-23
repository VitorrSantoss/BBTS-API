package br.com.bbts.api.bbts_api.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import br.com.bbts.api.bbts_api.constants.Perfil;
import br.com.bbts.api.bbts_api.models.Usuario;

/**
 * Formatos de resposta das telas do front. Nenhum deles expõe senha, foto em bytes
 * ou o arquivo do certificado.
 */
public final class Respostas {

  private Respostas() {
  }

  /** Competência com nível. Em `competenciasMinimas`, `nivel` é o mínimo esperado. */
  public record CompetenciaDto(String sigla, String nome, String categoria, int nivel, int nivelMax) {
  }

  /** Competência mínima da equipe que o colaborador ainda não atinge. */
  public record LacunaDto(String sigla, String nome, int nivel, int minimo) {
  }

  public record CursoDto(Long id, String titulo, String sigla) {
  }

  /** Identidade resumida de uma pessoa (gestor, autor ou destinatário de feedback). */
  public record PessoaDto(Long id, String nome, String email, String area, Long fotoVersao) {

    public static PessoaDto de(Usuario usuario, Map<Long, Long> versoesDeFoto) {
      if (usuario == null) {
        return null;
      }
      return new PessoaDto(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getArea(), versoesDeFoto.get(usuario.getId()));
    }
  }

  public record LoginDto(UsuarioDto usuario, boolean primeiroAcesso) {
  }

  public record MembroDto(Long id, String nome, String email, String area, Long fotoVersao, List<CompetenciaDto> competencias) {
  }

  public record EquipeResumoDto(Long id, String nome, String foco, List<CompetenciaDto> competenciasMinimas) {
  }

  public record EquipeDto(
      Long id,
      String nome,
      String foco,
      String descricao,
      PessoaDto gestor,
      List<CompetenciaDto> competenciasMinimas,
      List<MembroDto> membros) {
  }

  public record ItemNivelDto(Long id, String nome, String nivel) {
  }

  public record ExperienciaResumoDto(
      Long id,
      String empresa,
      String cargo,
      LocalDate dataInicio,
      LocalDate dataFim,
      boolean empregoAtual,
      String descricao) {
  }

  public record CertificacaoResumoDto(Long id, String nomeCurso, LocalDate dataConclusao, String nomeArquivo, boolean temArquivo) {
  }

  public record FormacaoResumoDto(Long id, String instituicao, String curso, Integer anoInicio, Integer anoFim, String status) {
  }

  /** Tudo o que as telas de colaborador precisam, numa resposta só. */
  public record ColaboradorDto(
      Long id,
      String nome,
      String cpf,
      LocalDate dataNascimento,
      String email,
      String telefone,
      String area,
      Perfil perfil,
      Long fotoVersao,
      EquipeResumoDto equipe,
      List<CompetenciaDto> competencias,
      List<LacunaDto> lacunas,
      List<CursoDto> jornada,
      List<ItemNivelDto> idiomas,
      List<ItemNivelDto> tecnologias,
      List<ExperienciaResumoDto> experiencias,
      List<CertificacaoResumoDto> certificacoes,
      List<FormacaoResumoDto> formacoes) {
  }

  public record FeedbackDto(Long id, String tipo, String mensagem, LocalDateTime dataEnvio, PessoaDto autor, PessoaDto destinatario) {
  }

  public record ConviteDto(List<PessoaDto> adicionados, List<String> naoEncontrados) {
  }

  public record ErroDto(int status, String message) {
  }
}
