package br.com.bbts.api.bbts_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.bbts.api.bbts_api.dto.Requisicoes.LoginRequest;
import br.com.bbts.api.bbts_api.dto.Respostas.LoginDto;
import br.com.bbts.api.bbts_api.services.AuthService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/login")
  public ResponseEntity<LoginDto> login(@Valid @RequestBody LoginRequest requisicao) {
    return ResponseEntity.ok(authService.entrar(requisicao));
  }
}
