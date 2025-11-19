package br.com.bbts.api.bbts_api.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

  // --- MÉTODO ATUALIZADO ---
  public Certificacoes salvarCertificacoes(Certificacoes certificacao, MultipartFile arquivo) throws IOException {
    
    // Se um arquivo foi enviado, processa-o
    if (arquivo != null && !arquivo.isEmpty()) {
        certificacao.setNomeArquivo(arquivo.getOriginalFilename());
        certificacao.setTipoArquivo(arquivo.getContentType());
        certificacao.setDadosArquivo(arquivo.getBytes());
    }
    
    return certificacoesRepository.save(certificacao);


}
}
