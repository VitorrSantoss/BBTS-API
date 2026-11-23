package br.com.bbts.api.bbts_api.services;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.com.bbts.api.bbts_api.constants.Perfil;
import br.com.bbts.api.bbts_api.dto.Requisicoes.NovaEquipeRequest;
import br.com.bbts.api.bbts_api.dto.Respostas.ConviteDto;
import br.com.bbts.api.bbts_api.dto.Respostas.EquipeDto;
import br.com.bbts.api.bbts_api.dto.Respostas.MembroDto;
import br.com.bbts.api.bbts_api.dto.Respostas.PessoaDto;
import br.com.bbts.api.bbts_api.models.Equipe;
import br.com.bbts.api.bbts_api.models.Usuario;
import br.com.bbts.api.bbts_api.repository.EquipeRepository;
import br.com.bbts.api.bbts_api.repository.UsuarioRepository;

@Service
public class EquipeService {

  private final EquipeRepository equipeRepository;
  private final UsuarioRepository usuarioRepository;
  private final UsuarioService usuarioService;
  private final ColaboradorService colaboradorService;
  private final FotoService fotoService;

  public EquipeService(
      EquipeRepository equipeRepository,
      UsuarioRepository usuarioRepository,
      UsuarioService usuarioService,
      ColaboradorService colaboradorService,
      FotoService fotoService) {
    this.equipeRepository = equipeRepository;
    this.usuarioRepository = usuarioRepository;
    this.usuarioService = usuarioService;
    this.colaboradorService = colaboradorService;
    this.fotoService = fotoService;
  }

  private EquipeDto paraDto(Equipe equipe, Map<Long, Long> versoes) {
    List<MembroDto> membros = usuarioRepository.findByEquipeIdOrderByNome(equipe.getId()).stream()
        .map(m -> new MembroDto(m.getId(), m.getNome(), m.getEmail(), m.getArea(), versoes.get(m.getId()),
            colaboradorService.competenciasDo(m.getId())))
        .toList();
    return new EquipeDto(
        equipe.getId(),
        equipe.getNome(),
        equipe.getFoco(),
        equipe.getDescricao(),
        PessoaDto.de(equipe.getGestor(), versoes),
        colaboradorService.minimosDa(equipe.getId()),
        membros);
  }

  private Equipe buscarOuFalhar(Long id) {
    return equipeRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipe não encontrada."));
  }

  /** Equipes de um gestor (ou todas, sem `gestorId`), com membros e mínimos. */
  @Transactional(readOnly = true)
  public List<EquipeDto> listar(Long gestorId) {
    Map<Long, Long> versoes = fotoService.versoes();
    List<Equipe> equipes = gestorId == null ? equipeRepository.findAllByOrderById() : equipeRepository.findByGestorIdOrderById(gestorId);
    return equipes.stream().map(e -> paraDto(e, versoes)).toList();
  }

  @Transactional(readOnly = true)
  public EquipeDto buscar(Long id) {
    return paraDto(buscarOuFalhar(id), fotoService.versoes());
  }

  @Transactional
  public EquipeDto criar(NovaEquipeRequest requisicao) {
    Usuario gestor = usuarioService.buscarOuFalhar(requisicao.gestorId());
    if (gestor.getPerfilEfetivo() != Perfil.GESTOR) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Só gestores podem criar equipes.");
    }
    Equipe equipe = new Equipe();
    equipe.setNome(requisicao.nome().trim());
    equipe.setFoco(requisicao.foco().trim());
    equipe.setDescricao(requisicao.descricao() == null || requisicao.descricao().isBlank() ? null : requisicao.descricao().trim());
    equipe.setGestor(gestor);
    equipeRepository.save(equipe);

    if (requisicao.emails() != null && !requisicao.emails().isEmpty()) {
      adicionarMembros(equipe.getId(), requisicao.emails());
    }
    return buscar(equipe.getId());
  }

  /**
   * Coloca na equipe os colaboradores cadastrados com esses e-mails.
   * Quem já estava em outra equipe é transferido; e-mail sem cadastro (ou de gestor) volta em `naoEncontrados`.
   */
  @Transactional
  public ConviteDto adicionarMembros(Long equipeId, List<String> emails) {
    Equipe equipe = buscarOuFalhar(equipeId);
    Map<Long, Long> versoes = fotoService.versoes();
    List<PessoaDto> adicionados = new ArrayList<>();
    List<String> naoEncontrados = new ArrayList<>();

    for (String email : new LinkedHashSet<>(emails)) {
      String normalizado = email == null ? "" : email.trim().toLowerCase();
      if (normalizado.isEmpty()) {
        continue;
      }
      Optional<Usuario> usuario = usuarioRepository.findByEmailIgnoreCase(normalizado)
          .filter(u -> u.getPerfilEfetivo() == Perfil.COLABORADOR);
      if (usuario.isEmpty()) {
        naoEncontrados.add(normalizado);
        continue;
      }
      Usuario membro = usuario.get();
      membro.setEquipe(equipe);
      usuarioRepository.save(membro);
      adicionados.add(PessoaDto.de(membro, versoes));
    }
    return new ConviteDto(adicionados, naoEncontrados);
  }

  @Transactional
  public void removerMembro(Long equipeId, Long usuarioId) {
    Usuario membro = usuarioService.buscarOuFalhar(usuarioId);
    if (membro.getEquipe() == null || !membro.getEquipe().getId().equals(equipeId)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Este colaborador não está na equipe.");
    }
    membro.setEquipe(null);
    usuarioRepository.save(membro);
  }
}
