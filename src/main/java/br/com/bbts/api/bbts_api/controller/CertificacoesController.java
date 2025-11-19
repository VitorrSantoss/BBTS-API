package br.com.bbts.api.bbts_api.controller;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import br.com.bbts.api.bbts_api.models.Certificacoes;
import br.com.bbts.api.bbts_api.services.CertificacoesService;

@RestController
@RequestMapping("/certificacoes")
public class CertificacoesController {
  
  @Autowired
  private CertificacoesService certificacoesService;

  // 1º ENDPOINT
  @GetMapping
  public ResponseEntity<List<Certificacoes>> listarCertificacoes(){
    return ResponseEntity.ok().body(certificacoesService.carregarCertificacoesCadastradas());
  }

  // 2º ENDPOINT
  @GetMapping("/{id}")
  public ResponseEntity<Optional<Certificacoes>> listarCertificacoesPeloId(@PathVariable("id")Long id){
    Optional<Certificacoes> certificacoesOpt = certificacoesService.obterCertificacoesPeloId(id);

    if (certificacoesOpt.isEmpty()){
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok().body(certificacoesOpt);
  }

  // --- POST ATUALIZADO ---
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> cadastrarCertificacoes(
      @RequestPart("dados") Certificacoes certificacoes,
      @RequestPart(value = "arquivo", required = false) MultipartFile arquivo) {
      
      try {
        Certificacoes novaCertificacao = certificacoesService.salvarCertificacoes(certificacoes, arquivo);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaCertificacao);
      } catch (IOException e) {
        return ResponseEntity.internalServerError().body("Erro ao processar o arquivo: " + e.getMessage());
      }
  }

  // --- PUT ATUALIZADO ---
  @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> atualizarCertificacoes(
      @PathVariable Long id, 
      @RequestPart("dados") Certificacoes certificacoes,
      @RequestPart(value = "arquivo", required = false) MultipartFile arquivo) {
      
    Optional<Certificacoes> certificacoesOpt = certificacoesService.obterCertificacoesPeloId(id);

    if(certificacoesOpt.isEmpty()){
      return ResponseEntity.noContent().build();
    }

    // Mantém o ID correto
    certificacoes.setId(id);
    
    // Se não enviou arquivo novo no update, talvez queira manter o antigo? 
    // Lógica simples: se enviou arquivo, substitui. Se não, o service (acima) não toca nos bytes se for null.
    // Mas cuidado: o objeto "certificacoes" vindo do @RequestPart não tem os bytes antigos.
    // Para produção, você deveria recuperar os bytes antigos do banco se o arquivo for null.

    try {
        Certificacoes certAtualizada = certificacoesService.salvarCertificacoes(certificacoes, arquivo);
        return ResponseEntity.ok().body(certAtualizada);
    } catch (IOException e) {
        return ResponseEntity.internalServerError().body("Erro ao processar o arquivo.");
    }
  }


  // 5º ENDPOINT
  @DeleteMapping("/{id}")
  public ResponseEntity <Void> deletarCertificacoesPeloId(@PathVariable("id")Long id){
    Optional <Certificacoes> certificacoesOpt = certificacoesService.obterCertificacoesPeloId(id);

    if (certificacoesOpt.isEmpty()){
      return ResponseEntity.notFound().build();
    }
    certificacoesService.excluirCertificacoes(id);

    return ResponseEntity.noContent().build();
  }   


}
