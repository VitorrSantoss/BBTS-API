package br.com.bbts.api.bbts_api.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.com.bbts.api.bbts_api.constants.Perfil;
import br.com.bbts.api.bbts_api.dto.Requisicoes.LoginRequest;
import br.com.bbts.api.bbts_api.dto.Respostas.LoginDto;
import br.com.bbts.api.bbts_api.models.Usuario;
import br.com.bbts.api.bbts_api.repository.UsuarioRepository;

/**
 * Login por e-mail e senha, conferindo o perfil escolhido na entrada.
 * Quem foi cadastrado sem senha define a senha no primeiro acesso.
 */
@Service
public class AuthService {

  private static final String CREDENCIAIS_INVALIDAS = "E-mail ou senha incorretos.";

  private final UsuarioRepository usuarioRepository;
  private final UsuarioService usuarioService;

  public AuthService(UsuarioRepository usuarioRepository, UsuarioService usuarioService) {
    this.usuarioRepository = usuarioRepository;
    this.usuarioService = usuarioService;
  }

  @Transactional
  public LoginDto entrar(LoginRequest requisicao) {
    Usuario usuario = usuarioRepository.findByEmailIgnoreCase(requisicao.email().trim())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, CREDENCIAIS_INVALIDAS));

    String gravada = usuario.getSenha();
    boolean primeiroAcesso = gravada == null || gravada.isBlank();
    // Cadastros antigos guardavam a senha em texto puro: confere assim e já troca pelo hash.
    boolean legada = !primeiroAcesso && !UsuarioService.ehHash(gravada);

    if (primeiroAcesso || legada) {
      if (legada && !gravada.equals(requisicao.senha())) {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, CREDENCIAIS_INVALIDAS);
      }
      usuario.setSenha(UsuarioService.hashDaSenha(requisicao.senha()));
    } else if (!UsuarioService.senhaConfere(requisicao.senha(), gravada)) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, CREDENCIAIS_INVALIDAS);
    }

    if (usuario.getPerfilEfetivo() != requisicao.perfil()) {
      String perfil = requisicao.perfil() == Perfil.GESTOR ? "gestor" : "colaborador";
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Este e-mail não tem acesso como " + perfil + ". Volte e troque o perfil.");
    }

    // A senha nova (ou o hash da legada) só é gravada depois de o perfil conferir:
    // a exceção acima desfaz a transação.
    if (primeiroAcesso || legada) {
      usuarioRepository.save(usuario);
    }
    return new LoginDto(usuarioService.paraDto(usuario), primeiroAcesso);
  }
}
