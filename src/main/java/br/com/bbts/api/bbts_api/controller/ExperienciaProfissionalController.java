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

import br.com.bbts.api.bbts_api.models.ExperienciaProfissional;
import br.com.bbts.api.bbts_api.services.ExperienciaProfissionalService;


@RestController
@RequestMapping("/experienciaProfissional")
public class ExperienciaProfissionalController {


  @Autowired
  private ExperienciaProfissionalService experienciaProfissionalService;

  // 1º ENDPOINT
  @GetMapping
  public ResponseEntity<List<ExperienciaProfissional>> listarExperienciaProfissional(){
    return ResponseEntity.ok().body(experienciaProfissionalService.carregarExperienciaProfissionalCadastrada());
  }

  // 2º ENDPOINT
  @GetMapping("/{id}")
  public ResponseEntity<Optional<ExperienciaProfissional>> listarExperienciaProfissionalPeloId(@PathVariable("id")Long id){
    Optional<ExperienciaProfissional> exProfissionalOpt = experienciaProfissionalService.carregarIdiomaPeloId(id);

    if (exProfissionalOpt.isEmpty()){
      return ResponseEntity.ok().body(exProfissionalOpt);
    }

    return ResponseEntity.ok().body(exProfissionalOpt);
  }

  // 3º ENDPOINT
  @PostMapping
  public ResponseEntity<ExperienciaProfissional> cadastrarExperienciaProfissional(@RequestBody ExperienciaProfissional experienciaProfissional){
    return ResponseEntity.status(HttpStatus.CREATED).body(experienciaProfissionalService.salvarExperienciaProfissional(experienciaProfissional));
  }

  // 4º ENDPOINT
  @PutMapping("{id}")
  public ResponseEntity<ExperienciaProfissional> atualizarExperienciaProfissional(@PathVariable Long id, @RequestBody ExperienciaProfissional dadosExperienciaProfissional){
    Optional<ExperienciaProfissional> experienciaProfissionalOpt = experienciaProfissionalService.carregarIdiomaPeloId(id);

    if (experienciaProfissionalOpt.isEmpty()){
      return ResponseEntity.noContent().build();
    }

    dadosExperienciaProfissional.setId(id);

    return ResponseEntity.ok().body(experienciaProfissionalService.salvarExperienciaProfissional(dadosExperienciaProfissional));
  }

  // 5º ENDPOINT
  @DeleteMapping("/{id}")
  public ResponseEntity <Void> deletarExperienciaProfissionalPeloId(@PathVariable("id")Long id){
    Optional <ExperienciaProfissional> experienciaProfissionalOpt = experienciaProfissionalService.carregarIdiomaPeloId(id);

    if (experienciaProfissionalOpt.isEmpty()){
      return ResponseEntity.notFound().build();
    }
    experienciaProfissionalService.excluirExperienciaProfissionalPeloId(id);

    return ResponseEntity.noContent().build();
  }   















}
