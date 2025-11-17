package br.com.bbts.api.bbts_api.controller;

import java.util.List;
import java.util.Optional;

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

import br.com.bbts.api.bbts_api.models.Tecnologia;
import br.com.bbts.api.bbts_api.services.TecnologiaService;

@RestController
@RequestMapping("/tecnologias")
public class TecnologiaController {

  @Autowired
  private TecnologiaService tecnologiaService;

  // 1º ENDPOINT
  @GetMapping
  public ResponseEntity<List<Tecnologia>> listarTecnologia() {
    return ResponseEntity.ok().body(tecnologiaService.carregarTecnologiaCadastrada());
  }

  // 2º ENDPOINT
  @GetMapping("/{id}")
  public ResponseEntity<Optional<Tecnologia>> listarTecnologiaPeloId(@PathVariable("id") Long id) {
    Optional<Tecnologia> tecnologiaOpt = tecnologiaService.carregarTecnologiaPeloId(id);

    if (tecnologiaOpt.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok().body(tecnologiaOpt);

  }

  // 3º ENDPOINT
  @PostMapping
  public ResponseEntity<Tecnologia> cadastrarTecnologia(@RequestBody Tecnologia tecnologia) {
    return ResponseEntity.status(HttpStatus.CREATED).body(tecnologiaService.salvarTecnologia(tecnologia));
  }

  // 4º ENDPOINT
  @PutMapping("/{id}")
  public ResponseEntity<Tecnologia> atualizarTecnologia(@PathVariable Long id, @RequestBody Tecnologia dadosTecnologia) {
    Optional<Tecnologia> tecnologiaOpt = tecnologiaService.carregarTecnologiaPeloId(id);

    if (tecnologiaOpt.isEmpty()) {
      return ResponseEntity.noContent().build();
    }

    dadosTecnologia.setId(id);

    return ResponseEntity.ok().body(tecnologiaService.salvarTecnologia(dadosTecnologia));
  }

  // 5º ENDPOINT
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletarTecnologiaPeloId(@PathVariable("id") Long id) {
    Optional<Tecnologia> tecnologiaOpt = tecnologiaService.carregarTecnologiaPeloId(id);

    if (tecnologiaOpt.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    tecnologiaService.excluirTecnologiaPeloId(id);;

    return ResponseEntity.noContent().build();
  }

}
