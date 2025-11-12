package br.com.bbts.api.bbts_api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.bbts.api.bbts_api.models.Idioma;
import br.com.bbts.api.bbts_api.repository.IdiomaRepository;

@Service
public class IdiomaService {
  
  @Autowired
  private IdiomaRepository idiomaRepository;

  public List<Idioma> carregarIdiomasCadastrados(){
    return idiomaRepository.findAll();
  }

  public Optional<Idioma> carregarIdiomaPeloId(Long id){
    return idiomaRepository.findById(id);
  }

  public void excluirIdiomaPeloId(Long id){
    idiomaRepository.deleteById(id);
  }

  public Idioma salvarIdioma(Idioma idioma){
    return idiomaRepository.save(idioma);
  }

}
