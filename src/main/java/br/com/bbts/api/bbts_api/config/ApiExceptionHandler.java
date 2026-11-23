package br.com.bbts.api.bbts_api.config;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import br.com.bbts.api.bbts_api.dto.Respostas.ErroDto;
import jakarta.validation.ConstraintViolationException;

/** Erros no formato { status, message }, que o front mostra direto para a pessoa. */
@RestControllerAdvice
public class ApiExceptionHandler {

  private static ResponseEntity<ErroDto> erro(HttpStatus status, String mensagem) {
    return ResponseEntity.status(status).body(new ErroDto(status.value(), mensagem));
  }

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<ErroDto> status(ResponseStatusException e) {
    HttpStatus status = HttpStatus.valueOf(e.getStatusCode().value());
    return erro(status, e.getReason() == null ? status.getReasonPhrase() : e.getReason());
  }

  /** Chave estrangeira: o registro aponta para um usuário (ou equipe) que não existe. Senão, é CPF/e-mail repetido. */
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ErroDto> integridade(DataIntegrityViolationException e) {
    String detalhe = String.valueOf(e.getMostSpecificCause().getMessage()).toLowerCase();
    if (detalhe.contains("foreign key")) {
      return erro(HttpStatus.BAD_REQUEST, "O registro aponta para um usuário ou equipe que não existe.");
    }
    if (detalhe.contains("cannot be null") || detalhe.contains("doesn't have a default value")) {
      return erro(HttpStatus.BAD_REQUEST, "Faltam dados obrigatórios.");
    }
    return erro(HttpStatus.CONFLICT, "Já existe um cadastro com este CPF ou e-mail.");
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErroDto> argumento(MethodArgumentNotValidException e) {
    String mensagem = e.getBindingResult().getAllErrors().stream()
        .findFirst()
        .map(erro -> erro.getDefaultMessage())
        .orElse("Dados inválidos.");
    return erro(HttpStatus.BAD_REQUEST, mensagem);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErroDto> restricao(ConstraintViolationException e) {
    String mensagem = e.getConstraintViolations().stream().findFirst().map(v -> v.getMessage()).orElse("Dados inválidos.");
    return erro(HttpStatus.BAD_REQUEST, mensagem);
  }

  /** A validação das entidades acontece no commit e chega embrulhada numa TransactionSystemException. */
  @ExceptionHandler(TransactionSystemException.class)
  public ResponseEntity<ErroDto> transacao(TransactionSystemException e) {
    Throwable causa = e.getRootCause();
    if (causa instanceof ConstraintViolationException violacao) {
      return restricao(violacao);
    }
    return erro(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível salvar os dados.");
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErroDto> corpoInvalido(HttpMessageNotReadableException e) {
    return erro(HttpStatus.BAD_REQUEST, "Os dados enviados estão em um formato inválido.");
  }
}
