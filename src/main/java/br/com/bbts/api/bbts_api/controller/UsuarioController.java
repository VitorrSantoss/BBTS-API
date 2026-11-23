package br.com.bbts.api.bbts_api.controller;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.bbts.api.bbts_api.dto.Requisicoes.NivelCompetenciaRequest;
import br.com.bbts.api.bbts_api.dto.Respostas.ColaboradorDto;
import br.com.bbts.api.bbts_api.dto.Respostas.CompetenciaDto;
import br.com.bbts.api.bbts_api.dto.UsuarioDto;
import br.com.bbts.api.bbts_api.models.FotoUsuario;
import br.com.bbts.api.bbts_api.models.Usuario;
import br.com.bbts.api.bbts_api.services.ColaboradorService;
import br.com.bbts.api.bbts_api.services.FotoService;
import br.com.bbts.api.bbts_api.services.UsuarioService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

  @Autowired
  private UsuarioService usuarioService;

  @Autowired
  private ColaboradorService colaboradorService;

  @Autowired
  private FotoService fotoService;

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
  public ResponseEntity<UsuarioDto> cadastrarUsuario(@RequestBody Usuario usuario){
    return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.paraDto(usuarioService.salvarUsuario(usuario)));
  }

  // 4º ENDPOINT
  @PutMapping("/{id}")
  public ResponseEntity<UsuarioDto> atualizarUsuario(@PathVariable("id")Long id, @RequestBody Usuario dadosUsuario){
    UsuarioDto usuario = usuarioService.obterUsuarioPeloId(id);

    if (Objects.isNull(usuario)){
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok().body(usuarioService.paraDto(usuarioService.atualizarUsuario(id, dadosUsuario)));
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

  /** Perfil completo: dados, equipe, competências, lacunas, jornada, idiomas, tecnologias, experiências, certificados e formação. */
  @GetMapping("/{id}/completo")
  public ResponseEntity<ColaboradorDto> completo(@PathVariable Long id) {
    return ResponseEntity.ok(colaboradorService.completo(id));
  }

  /** Cria ou atualiza o nível do usuário numa competência do catálogo. */
  @PutMapping("/{id}/competencias")
  public ResponseEntity<List<CompetenciaDto>> definirCompetencia(@PathVariable Long id, @Valid @RequestBody NivelCompetenciaRequest requisicao) {
    return ResponseEntity.ok(colaboradorService.definirNivel(id, requisicao));
  }

  @DeleteMapping("/{id}/competencias/{sigla}")
  public ResponseEntity<Void> removerCompetencia(@PathVariable Long id, @PathVariable String sigla) {
    colaboradorService.removerCompetencia(id, sigla);
    return ResponseEntity.noContent().build();
  }

  /** A URL leva `?v=<fotoVersao>`, então a imagem pode ficar em cache sem prazo. */
  @GetMapping("/{id}/foto")
  public ResponseEntity<byte[]> foto(@PathVariable Long id) {
    return fotoService.buscar(id)
        .map((FotoUsuario foto) -> ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(foto.getTipo()))
            .cacheControl(CacheControl.maxAge(365, TimeUnit.DAYS).cachePublic())
            .body(foto.getDados()))
        .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping(value = "/{id}/foto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UsuarioDto> enviarFoto(@PathVariable Long id, @RequestPart("foto") MultipartFile foto) {
    usuarioService.buscarOuFalhar(id);
    fotoService.salvar(id, foto);
    return ResponseEntity.ok(usuarioService.obterUsuarioPeloId(id));
  }

}
