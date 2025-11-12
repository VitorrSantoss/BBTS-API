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

  // 3º ENDPOINT
  @PostMapping
  public ResponseEntity<Certificacoes> cadastrarCertificacoes(@RequestBody Certificacoes certificacoes){
    return ResponseEntity.status(HttpStatus.CREATED).body(certificacoesService.salvarCertificacoes(certificacoes));
  }

  // 4º ENDPOINT
  @PutMapping("{id}")
  public ResponseEntity<Certificacoes> atualizarCertificacoes(@PathVariable Long id, @RequestBody Certificacoes certificacoes){
    Optional<Certificacoes> certificacoesOpt = certificacoesService.obterCertificacoesPeloId(id);

    if(certificacoesOpt.isEmpty()){
      return ResponseEntity.noContent().build();
    }

    certificacoes.setId(id);

    return ResponseEntity.ok().body(certificacoesService.salvarCertificacoes(certificacoes));

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
