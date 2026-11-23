package br.com.bbts.api.bbts_api.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.com.bbts.api.bbts_api.dto.Requisicoes.NivelCompetenciaRequest;
import br.com.bbts.api.bbts_api.dto.Respostas.CertificacaoResumoDto;
import br.com.bbts.api.bbts_api.dto.Respostas.ColaboradorDto;
import br.com.bbts.api.bbts_api.dto.Respostas.CompetenciaDto;
import br.com.bbts.api.bbts_api.dto.Respostas.CursoDto;
import br.com.bbts.api.bbts_api.dto.Respostas.EquipeResumoDto;
import br.com.bbts.api.bbts_api.dto.Respostas.ExperienciaResumoDto;
import br.com.bbts.api.bbts_api.dto.Respostas.FormacaoResumoDto;
import br.com.bbts.api.bbts_api.dto.Respostas.ItemNivelDto;
import br.com.bbts.api.bbts_api.dto.Respostas.LacunaDto;
import br.com.bbts.api.bbts_api.models.Competencia;
import br.com.bbts.api.bbts_api.models.CompetenciaMinima;
import br.com.bbts.api.bbts_api.models.CompetenciaUsuario;
import br.com.bbts.api.bbts_api.models.Equipe;
import br.com.bbts.api.bbts_api.models.Usuario;
import br.com.bbts.api.bbts_api.repository.CertificacoesRepository;
import br.com.bbts.api.bbts_api.repository.CompetenciaMinimaRepository;
import br.com.bbts.api.bbts_api.repository.CompetenciaRepository;
import br.com.bbts.api.bbts_api.repository.CompetenciaUsuarioRepository;
import br.com.bbts.api.bbts_api.repository.CursoRepository;
import br.com.bbts.api.bbts_api.repository.ExperienciaProfissionalRepository;
import br.com.bbts.api.bbts_api.repository.FormacaoRepository;
import br.com.bbts.api.bbts_api.repository.IdiomaRepository;
import br.com.bbts.api.bbts_api.repository.TecnologiaRepository;

/**
 * Visão completa do colaborador. Lacunas e jornada não são guardadas: saem da comparação
 * entre os níveis do colaborador e os mínimos da equipe dele.
 */
@Service
public class ColaboradorService {

  private final UsuarioService usuarioService;
  private final FotoService fotoService;
  private final CompetenciaRepository competenciaRepository;
  private final CompetenciaUsuarioRepository competenciaUsuarioRepository;
  private final CompetenciaMinimaRepository competenciaMinimaRepository;
  private final CursoRepository cursoRepository;
  private final IdiomaRepository idiomaRepository;
  private final TecnologiaRepository tecnologiaRepository;
  private final ExperienciaProfissionalRepository experienciaRepository;
  private final CertificacoesRepository certificacoesRepository;
  private final FormacaoRepository formacaoRepository;

  public ColaboradorService(
      UsuarioService usuarioService,
      FotoService fotoService,
      CompetenciaRepository competenciaRepository,
      CompetenciaUsuarioRepository competenciaUsuarioRepository,
      CompetenciaMinimaRepository competenciaMinimaRepository,
      CursoRepository cursoRepository,
      IdiomaRepository idiomaRepository,
      TecnologiaRepository tecnologiaRepository,
      ExperienciaProfissionalRepository experienciaRepository,
      CertificacoesRepository certificacoesRepository,
      FormacaoRepository formacaoRepository) {
    this.usuarioService = usuarioService;
    this.fotoService = fotoService;
    this.competenciaRepository = competenciaRepository;
    this.competenciaUsuarioRepository = competenciaUsuarioRepository;
    this.competenciaMinimaRepository = competenciaMinimaRepository;
    this.cursoRepository = cursoRepository;
    this.idiomaRepository = idiomaRepository;
    this.tecnologiaRepository = tecnologiaRepository;
    this.experienciaRepository = experienciaRepository;
    this.certificacoesRepository = certificacoesRepository;
    this.formacaoRepository = formacaoRepository;
  }

  private static CompetenciaDto dto(Competencia c, int nivel) {
    return new CompetenciaDto(c.getSigla(), c.getNome(), c.getCategoria(), nivel, c.getNivelMax());
  }

  public List<CompetenciaDto> competenciasDo(Long usuarioId) {
    return competenciaUsuarioRepository.findByUsuarioIdOrderById(usuarioId).stream()
        .map(cu -> dto(cu.getCompetencia(), cu.getNivel()))
        .toList();
  }

  /** Mínimos da equipe, com `nivel` = nível mínimo esperado. */
  public List<CompetenciaDto> minimosDa(Long equipeId) {
    return competenciaMinimaRepository.findByEquipeIdOrderById(equipeId).stream()
        .map(m -> dto(m.getCompetencia(), m.getNivelMinimo()))
        .toList();
  }

  /** Competências mínimas que o colaborador não atinge. Competência ausente conta como nível 0. */
  private List<LacunaDto> lacunas(List<CompetenciaUsuario> doColaborador, List<CompetenciaMinima> minimos) {
    Map<Long, Integer> niveis = doColaborador.stream()
        .collect(Collectors.toMap(cu -> cu.getCompetencia().getId(), CompetenciaUsuario::getNivel));
    List<LacunaDto> lacunas = new ArrayList<>();
    for (CompetenciaMinima minimo : minimos) {
      int nivel = niveis.getOrDefault(minimo.getCompetencia().getId(), 0);
      if (nivel < minimo.getNivelMinimo()) {
        Competencia c = minimo.getCompetencia();
        lacunas.add(new LacunaDto(c.getSigla(), c.getNome(), nivel, minimo.getNivelMinimo()));
      }
    }
    return lacunas;
  }

  @Transactional(readOnly = true)
  public ColaboradorDto completo(Long id) {
    Usuario usuario = usuarioService.buscarOuFalhar(id);
    Equipe equipe = usuario.getEquipe();

    List<CompetenciaUsuario> competencias = competenciaUsuarioRepository.findByUsuarioIdOrderById(id);
    List<CompetenciaMinima> minimos = equipe == null ? List.of() : competenciaMinimaRepository.findByEquipeIdOrderById(equipe.getId());
    List<LacunaDto> lacunas = lacunas(competencias, minimos);

    Map<String, Long> idPorSigla = minimos.stream()
        .map(CompetenciaMinima::getCompetencia)
        .collect(Collectors.toMap(Competencia::getSigla, Competencia::getId, (a, b) -> a));
    List<Long> idsComLacuna = lacunas.stream().map(l -> idPorSigla.get(l.sigla())).toList();
    List<CursoDto> jornada = idsComLacuna.isEmpty() ? List.of()
        : cursoRepository.findByCompetenciaIdInOrderById(idsComLacuna).stream()
            .map(c -> new CursoDto(c.getId(), c.getTitulo(), c.getCompetencia().getSigla()))
            .toList();

    EquipeResumoDto equipeDto = equipe == null ? null
        : new EquipeResumoDto(equipe.getId(), equipe.getNome(), equipe.getFoco(),
            minimos.stream().map(m -> dto(m.getCompetencia(), m.getNivelMinimo())).toList());

    return new ColaboradorDto(
        usuario.getId(),
        usuario.getNome(),
        usuario.getCpf(),
        usuario.getDataNascimento(),
        usuario.getEmail(),
        usuario.getTelefone(),
        usuario.getArea(),
        usuario.getPerfilEfetivo(),
        fotoService.versao(id),
        equipeDto,
        competencias.stream().map(cu -> dto(cu.getCompetencia(), cu.getNivel())).toList(),
        lacunas,
        jornada,
        idiomaRepository.findByUsuarioIdOrderById(id).stream()
            .map(i -> new ItemNivelDto(i.getId(), i.getNome(), i.getNivel() == null ? null : i.getNivel().name()))
            .toList(),
        tecnologiaRepository.findByUsuarioIdOrderById(id).stream()
            .map(t -> new ItemNivelDto(t.getId(), t.getNome(), t.getNivel() == null ? null : t.getNivel().name()))
            .toList(),
        experienciaRepository.findByUsuarioIdOrderByDataInicioDesc(id).stream()
            .map(e -> new ExperienciaResumoDto(e.getId(), e.getEmpresa(), e.getCargo(), e.getDataInicio(), e.getDataFim(),
                Boolean.TRUE.equals(e.getEmpregoAtual()), e.getDescricao()))
            .toList(),
        certificacoesRepository.findByUsuarioIdOrderByDataConclusaoDesc(id).stream()
            .map(c -> new CertificacaoResumoDto(c.getId(), c.getNomeCurso(), c.getDataConclusao(), c.getNomeArquivo(),
                c.getDadosArquivo() != null && c.getDadosArquivo().length > 0))
            .toList(),
        formacaoRepository.findByUsuarioIdOrderByAnoInicioDesc(id).stream()
            .map(f -> new FormacaoResumoDto(f.getId(), f.getInstituicao(), f.getCurso(), f.getAnoInicio(), f.getAnoFim(),
                f.getStatus() == null ? null : f.getStatus().name()))
            .toList());
  }

  /** Cria ou atualiza o nível do colaborador numa competência do catálogo. */
  @Transactional
  public List<CompetenciaDto> definirNivel(Long usuarioId, NivelCompetenciaRequest requisicao) {
    Usuario usuario = usuarioService.buscarOuFalhar(usuarioId);
    Competencia competencia = competenciaRepository.findBySiglaIgnoreCase(requisicao.sigla())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Competência " + requisicao.sigla().toUpperCase() + " não existe no catálogo."));
    if (requisicao.nivel() > competencia.getNivelMax()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          "O nível máximo de " + competencia.getSigla() + " é " + competencia.getNivelMax() + ".");
    }
    CompetenciaUsuario registro = competenciaUsuarioRepository
        .findByUsuarioIdAndCompetenciaId(usuarioId, competencia.getId())
        .orElseGet(() -> {
          CompetenciaUsuario novo = new CompetenciaUsuario();
          novo.setUsuario(usuario);
          novo.setCompetencia(competencia);
          return novo;
        });
    registro.setNivel(requisicao.nivel());
    competenciaUsuarioRepository.save(registro);
    return competenciasDo(usuarioId);
  }

  @Transactional
  public void removerCompetencia(Long usuarioId, String sigla) {
    Competencia competencia = competenciaRepository.findBySiglaIgnoreCase(sigla)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Competência não encontrada."));
    competenciaUsuarioRepository.findByUsuarioIdAndCompetenciaId(usuarioId, competencia.getId())
        .ifPresent(competenciaUsuarioRepository::delete);
  }

  /** Mapa sigla → competência do catálogo, usado pela carga inicial. */
  public Map<String, Competencia> catalogoPorSigla() {
    return competenciaRepository.findAll().stream().collect(Collectors.toMap(Competencia::getSigla, Function.identity()));
  }
}
