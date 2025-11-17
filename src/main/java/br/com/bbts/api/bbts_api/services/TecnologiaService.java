package br.com.bbts.api.bbts_api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.bbts.api.bbts_api.models.Tecnologia;
import br.com.bbts.api.bbts_api.repository.TecnologiaRepository;

@Service
public class TecnologiaService {
  
  @Autowired 
  private TecnologiaRepository tecnologiaRepository;

  public List<Tecnologia> carregarTecnologiaCadastrada(){
    return tecnologiaRepository.findAll();
  }

  public Optional<Tecnologia> carregarTecnologiaPeloId(Long id){
    return tecnologiaRepository.findById(id);
  }

  public void excluirTecnologiaPeloId(Long id){
    tecnologiaRepository.deleteById(id);
  }

  public Tecnologia salvarTecnologia(Tecnologia tecnologia){
    return tecnologiaRepository.save(tecnologia);
  }

}
