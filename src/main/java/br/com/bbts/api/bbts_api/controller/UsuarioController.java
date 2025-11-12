package br.com.bbts.api.bbts_api.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.bbts.api.bbts_api.dto.UsuarioDto;
import br.com.bbts.api.bbts_api.models.Usuario;
import br.com.bbts.api.bbts_api.services.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
  
  @Autowired
  private UsuarioService usuarioService;

  // 1º ENDPOINT
  @GetMapping
  public ResponseEntity<List<UsuarioDto>> listarTodosUsuarios(){
    return ResponseEntity.ok().body(usuarioService.carregarUsuarioCadastrado());
  }

  // 2º ENDPOINT
  @GetMapping("/{id}")
  public ResponseEntity<UsuarioDto> listarUsuarioPeloId(@PathVariable("id")Long id){
    UsuarioDto usuarioDto = usuarioService.obterUsuarioPeloId(id);

    if (Objects.isNull(usuarioDto)){
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok().body(usuarioDto);
  } 

  // 3º ENDPOINT
  @PostMapping
  public ResponseEntity<Usuario> cadastrarUsuario(@RequestBody Usuario usuario){
    return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.salvarUsuario(usuario));
  }

  // 4º ENDPOINT
  @PutMapping("/{id}")
  public ResponseEntity<Usuario> atualizarUsuario(@PathVariable("id")Long id, @RequestBody Usuario dadosUsuario){
    UsuarioDto usuario = usuarioService.obterUsuarioPeloId(id);

    if (Objects.isNull(usuario)){
      return ResponseEntity.notFound().build();
    }

    dadosUsuario.setId(id);

    return ResponseEntity.ok().body(usuarioService.salvarUsuario(dadosUsuario));
  }

  // 5º ENDPOINT
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletarProjetoPeloId(@PathVariable("id")Long id){
    UsuarioDto usuarioDto = usuarioService.obterUsuarioPeloId(id);

    if (Objects.isNull(usuarioDto)){
      return ResponseEntity.notFound().build();
    }

    usuarioService.excluirUsuario(id);

    return ResponseEntity.noContent().build();
  }


}
