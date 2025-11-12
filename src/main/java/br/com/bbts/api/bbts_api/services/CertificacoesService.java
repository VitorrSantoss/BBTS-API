package br.com.bbts.api.bbts_api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.bbts.api.bbts_api.models.Certificacoes;
import br.com.bbts.api.bbts_api.repository.CertificacoesRepository;

@Service
public class CertificacoesService {
  
  @Autowired
  private CertificacoesRepository certificacoesRepository;

  public List<Certificacoes> carregarCertificacoesCadastradas(){
    return certificacoesRepository.findAll();
  }

  public Optional<Certificacoes> obterCertificacoesPeloId(Long id){
    return certificacoesRepository.findById(id);
  }

  public void excluirCertificacoes(Long id){
    certificacoesRepository.deleteById(id);
  }

  public Certificacoes salvarCertificacoes(Certificacoes certificacoes){
    return certificacoesRepository.save(certificacoes);
  }


}
