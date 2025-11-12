package br.com.bbts.api.bbts_api.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.bbts.api.bbts_api.dto.UsuarioDto;
import br.com.bbts.api.bbts_api.models.Usuario;
import br.com.bbts.api.bbts_api.repository.UsuarioRepository;

@Service
public class UsuarioService {

  @Autowired
  private UsuarioRepository usuarioRepository;

  public List<UsuarioDto> carregarUsuarioCadastrado() {
    List<Usuario> usuarios = usuarioRepository.findAll();

    List<UsuarioDto> dtos = new ArrayList<>();

    for (Usuario usuario : usuarios) {
      dtos.add(usuario.converterParaDto());
    }

    return dtos;
  }

  public UsuarioDto obterUsuarioPeloId(Long id) {
    Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);

    if (usuarioOpt.isPresent()) {
      Usuario usuario = usuarioOpt.get();

      return usuario.converterParaDto();
    }
    return null;
  }

  public void excluirUsuario(Long id) {
    usuarioRepository.deleteById(id);
  }

  public Usuario salvarUsuario(Usuario usuario) {
    usuarioRepository.save(usuario);
   
    return usuarioRepository.save(usuario);
  }

}
