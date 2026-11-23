package br.com.bbts.api.bbts_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.bbts.api.bbts_api.dto.Requisicoes.EmailsRequest;
import br.com.bbts.api.bbts_api.dto.Requisicoes.NovaEquipeRequest;
import br.com.bbts.api.bbts_api.dto.Respostas.ConviteDto;
import br.com.bbts.api.bbts_api.dto.Respostas.EquipeDto;
import br.com.bbts.api.bbts_api.services.EquipeService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/equipes")
public class EquipeController {

  private final EquipeService equipeService;

  public EquipeController(EquipeService equipeService) {
    this.equipeService = equipeService;
  }

  /** Equipes com membros, competências dos membros e mínimos. `gestorId` filtra as de um gestor. */
  @GetMapping
  public ResponseEntity<List<EquipeDto>> listar(@RequestParam(required = false) Long gestorId) {
    return ResponseEntity.ok(equipeService.listar(gestorId));
  }

  @GetMapping("/{id}")
  public ResponseEntity<EquipeDto> buscar(@PathVariable Long id) {
    return ResponseEntity.ok(equipeService.buscar(id));
  }

  @PostMapping
  public ResponseEntity<EquipeDto> criar(@Valid @RequestBody NovaEquipeRequest requisicao) {
    return ResponseEntity.status(HttpStatus.CREATED).body(equipeService.criar(requisicao));
  }

  /** Adiciona à equipe os colaboradores cadastrados com estes e-mails. */
  @PostMapping("/{id}/membros")
  public ResponseEntity<ConviteDto> adicionarMembros(@PathVariable Long id, @Valid @RequestBody EmailsRequest requisicao) {
    return ResponseEntity.ok(equipeService.adicionarMembros(id, requisicao.emails()));
  }

  @DeleteMapping("/{id}/membros/{usuarioId}")
  public ResponseEntity<Void> removerMembro(@PathVariable Long id, @PathVariable Long usuarioId) {
    equipeService.removerMembro(id, usuarioId);
    return ResponseEntity.noContent().build();
  }
}
