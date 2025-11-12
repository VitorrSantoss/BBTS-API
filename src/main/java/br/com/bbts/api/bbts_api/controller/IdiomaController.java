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

import br.com.bbts.api.bbts_api.models.Idioma;
import br.com.bbts.api.bbts_api.services.IdiomaService;

@RestController
@RequestMapping("/idiomas")
public class IdiomaController {

  @Autowired
  private IdiomaService idiomaService;

  // 1º ENDPOINT
  @GetMapping
  public ResponseEntity<List<Idioma>> listarIdioma() {
    return ResponseEntity.ok().body(idiomaService.carregarIdiomasCadastrados());
  }

  // 2º ENDPOINT
  @GetMapping("/{id}")
  public ResponseEntity<Optional<Idioma>> listarTarefasPeloId(@PathVariable("id")Long id){
    Optional<Idioma> idiomaOpt = idiomaService.carregarIdiomaPeloId(id);
    
    if (idiomaOpt.isEmpty()){
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok().body(idiomaOpt);

  }

  // 3º ENDPOINT
  @PostMapping
  public ResponseEntity<Idioma> cadastrarIdioma(@RequestBody Idioma idioma){
    return ResponseEntity.status(HttpStatus.CREATED).body(idiomaService.salvarIdioma(idioma));
  }

  // 4º ENDPOINT
  @PutMapping("/{id}")
  public ResponseEntity<Idioma> atualizarIdioma(@PathVariable Long id, @RequestBody Idioma dadosIdioma){
    Optional<Idioma> idiomaOpt = idiomaService.carregarIdiomaPeloId(id);

    if (idiomaOpt.isEmpty()){
      return ResponseEntity.noContent().build();
    }

    dadosIdioma.setId(id);

    return ResponseEntity.ok().body(idiomaService.salvarIdioma(dadosIdioma));
  }

  // 5º ENDPOINT
  @DeleteMapping("/{id}")
  public ResponseEntity <Void> deletarIdiomaPeloId(@PathVariable("id")Long id){
    Optional<Idioma> idiomaOpt = idiomaService.carregarIdiomaPeloId(id);

    if (idiomaOpt.isEmpty()){
      return ResponseEntity.notFound().build();
    }
    idiomaService.excluirIdiomaPeloId(id);

    return ResponseEntity.noContent().build();
  }



}
