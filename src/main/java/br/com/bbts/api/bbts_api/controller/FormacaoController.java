package br.com.bbts.api.bbts_api.controller;

import java.util.List;

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

import br.com.bbts.api.bbts_api.models.Formacao;
import br.com.bbts.api.bbts_api.repository.FormacaoRepository;

/** CRUD de formação acadêmica, no mesmo formato de /idiomas (o corpo traz `usuario: { id }`). */
@RestController
@RequestMapping("/formacoes")
public class FormacaoController {

  private final FormacaoRepository formacaoRepository;

  public FormacaoController(FormacaoRepository formacaoRepository) {
    this.formacaoRepository = formacaoRepository;
  }

  @GetMapping
  public ResponseEntity<List<Formacao>> listar() {
    return ResponseEntity.ok(formacaoRepository.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Formacao> buscar(@PathVariable Long id) {
    return ResponseEntity.of(formacaoRepository.findById(id));
  }

  @PostMapping
  public ResponseEntity<Formacao> cadastrar(@RequestBody Formacao formacao) {
    formacao.setId(null);
    return ResponseEntity.status(HttpStatus.CREATED).body(formacaoRepository.save(formacao));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Formacao> atualizar(@PathVariable Long id, @RequestBody Formacao dados) {
    if (!formacaoRepository.existsById(id)) {
      return ResponseEntity.notFound().build();
    }
    dados.setId(id);
    return ResponseEntity.ok(formacaoRepository.save(dados));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> excluir(@PathVariable Long id) {
    if (!formacaoRepository.existsById(id)) {
      return ResponseEntity.notFound().build();
    }
    formacaoRepository.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
