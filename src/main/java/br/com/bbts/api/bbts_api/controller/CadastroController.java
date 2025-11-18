package br.com.bbts.api.bbts_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType; // import correto
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.bbts.api.bbts_api.dto.CadastroCompletoDto;
import br.com.bbts.api.bbts_api.services.CadastroService;

@RestController
@RequestMapping("/api/cadastro")
public class CadastroController {

  @Autowired
  private CadastroService cadastroService; 

  @PostMapping(value = "/salvar-completo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> salvarCompleto(
      @RequestPart("dados") CadastroCompletoDto dados,
      @RequestPart(value = "foto", required = false) MultipartFile foto,
      @RequestPart(value = "certificados", required = false) List<MultipartFile> certificados) {

    cadastroService.salvarCompleto(dados, foto, certificados);
    return ResponseEntity.ok("Cadastro realizado com sucesso!");
  }
}
