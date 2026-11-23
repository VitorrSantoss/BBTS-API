package br.com.bbts.api.bbts_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.bbts.api.bbts_api.models.Competencia;
import br.com.bbts.api.bbts_api.repository.CompetenciaRepository;
import jakarta.validation.Valid;

/** Catálogo de competências SFIA. */
@RestController
@RequestMapping("/competencias")
public class CompetenciaController {

  private final CompetenciaRepository competenciaRepository;

  public CompetenciaController(CompetenciaRepository competenciaRepository) {
    this.competenciaRepository = competenciaRepository;
  }

  @GetMapping
  public ResponseEntity<List<Competencia>> listar() {
    return ResponseEntity.ok(competenciaRepository.findAllByOrderBySigla());
  }

  @PostMapping
  public ResponseEntity<Competencia> cadastrar(@Valid @RequestBody Competencia competencia) {
    competencia.setId(null);
    competencia.setSigla(competencia.getSigla().trim().toUpperCase());
    if (competenciaRepository.findBySiglaIgnoreCase(competencia.getSigla()).isPresent()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "A sigla " + competencia.getSigla() + " já está no catálogo.");
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(competenciaRepository.save(competencia));
  }
}
