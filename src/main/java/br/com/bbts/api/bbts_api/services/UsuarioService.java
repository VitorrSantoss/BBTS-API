package br.com.bbts.api.bbts_api.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.bbts.api.bbts_api.constants.Perfil;
import br.com.bbts.api.bbts_api.dto.UsuarioDto;
import br.com.bbts.api.bbts_api.models.Usuario;
import br.com.bbts.api.bbts_api.repository.UsuarioRepository;

@Service
public class UsuarioService {

  private static final BCryptPasswordEncoder BCRYPT = new BCryptPasswordEncoder();
  private static final int SENHA_MINIMA = 6;

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Autowired
  private FotoService fotoService;

  public List<UsuarioDto> carregarUsuarioCadastrado() {
    Map<Long, Long> versoes = fotoService.versoes();
    return usuarioRepository.findAll().stream().map(usuario -> {
      UsuarioDto dto = usuario.converterParaDto();
      dto.setFotoVersao(versoes.get(usuario.getId()));
      return dto;
    }).toList();
  }

  public UsuarioDto obterUsuarioPeloId(Long id) {
    Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
    if (usuarioOpt.isEmpty()) {
      return null;
    }
    return paraDto(usuarioOpt.get());
  }

  public UsuarioDto paraDto(Usuario usuario) {
    UsuarioDto dto = usuario.converterParaDto();
    dto.setFotoVersao(fotoService.versao(usuario.getId()));
    return dto;
  }

  public Usuario buscarOuFalhar(Long id) {
    return usuarioRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));
  }

  public void excluirUsuario(Long id) {
    usuarioRepository.deleteById(id);
  }

  /** Senha em texto puro vira hash BCrypt. Recusa senha curta antes do hash (o hash sempre teria 60 caracteres). */
  public static String hashDaSenha(String senha) {
    if (senha == null || senha.length() < SENHA_MINIMA) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A senha precisa ter no mínimo 6 caracteres.");
    }
    return BCRYPT.encode(senha);
  }

  /** Hash BCrypt ($2a$, $2b$ ou $2y$). Senhas antigas foram gravadas em texto puro. */
  public static boolean ehHash(String senha) {
    return senha != null && senha.matches("^\\$2[aby]\\$\\d{2}\\$.{53}$");
  }

  public static boolean senhaConfere(String senha, String hash) {
    return senha != null && hash != null && BCRYPT.matches(senha, hash);
  }

  private static String normalizarEmail(String email) {
    return email == null ? null : email.trim().toLowerCase();
  }

  /**
   * Só os campos do cadastro são alterados. Perfil, equipe e senha ficam como estavam,
   * a não ser que uma senha nova venha no corpo.
   */
  public Usuario atualizarUsuario(Long id, Usuario dados) {
    Usuario atual = buscarOuFalhar(id);
    atual.setNome(dados.getNome());
    atual.setCpf(dados.getCpf());
    atual.setDataNascimento(dados.getDataNascimento());
    atual.setEmail(normalizarEmail(dados.getEmail()));
    atual.setTelefone(dados.getTelefone());
    atual.setArea(dados.getArea());
    if (dados.getSenha() != null && !dados.getSenha().isBlank()) {
      atual.setSenha(hashDaSenha(dados.getSenha()));
    }
    return usuarioRepository.save(atual);
  }

  /** Cadastro público: sempre entra como colaborador e sem equipe. */
  public Usuario salvarUsuario(Usuario usuario) {
    usuario.setId(null);
    usuario.setPerfil(Perfil.COLABORADOR);
    usuario.setEquipe(null);
    usuario.setEmail(normalizarEmail(usuario.getEmail()));
    if (usuario.getSenha() != null && !usuario.getSenha().isBlank()) {
      usuario.setSenha(hashDaSenha(usuario.getSenha()));
    } else {
      usuario.setSenha(null);
    }
    return usuarioRepository.save(usuario);
  }

}
