package br.com.bbts.api.bbts_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.bbts.api.bbts_api.dto.Requisicoes.NovoFeedbackRequest;
import br.com.bbts.api.bbts_api.dto.Respostas.FeedbackDto;
import br.com.bbts.api.bbts_api.services.FeedbackService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/feedbacks")
public class FeedbackController {

  private final FeedbackService feedbackService;

  public FeedbackController(FeedbackService feedbackService) {
    this.feedbackService = feedbackService;
  }

  /** `destinatarioId`: feedbacks recebidos. `autorId`: feedbacks enviados. */
  @GetMapping
  public ResponseEntity<List<FeedbackDto>> listar(
      @RequestParam(required = false) Long destinatarioId,
      @RequestParam(required = false) Long autorId) {
    if (destinatarioId != null) {
      return ResponseEntity.ok(feedbackService.recebidos(destinatarioId));
    }
    if (autorId != null) {
      return ResponseEntity.ok(feedbackService.enviados(autorId));
    }
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe destinatarioId ou autorId.");
  }

  @PostMapping
  public ResponseEntity<FeedbackDto> enviar(@Valid @RequestBody NovoFeedbackRequest requisicao) {
    return ResponseEntity.status(HttpStatus.CREATED).body(feedbackService.enviar(requisicao));
  }
}
