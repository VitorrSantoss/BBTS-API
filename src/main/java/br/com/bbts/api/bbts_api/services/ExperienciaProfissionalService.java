package br.com.bbts.api.bbts_api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.bbts.api.bbts_api.models.ExperienciaProfissional;
import br.com.bbts.api.bbts_api.repository.ExperienciaProfissionalRepository;

@Service
public class ExperienciaProfissionalService {

  @Autowired
  private ExperienciaProfissionalRepository experienciaProfissionalRepository;

  public List<ExperienciaProfissional> carregarExperienciaProfissionalCadastrada(){
    return experienciaProfissionalRepository.findAll();
  }

  public Optional<ExperienciaProfissional> carregarIdiomaPeloId(Long id){
    return experienciaProfissionalRepository.findById(id);
  }

  public void excluirExperienciaProfissionalPeloId(Long id){
    experienciaProfissionalRepository.deleteById(id);
  }

  public ExperienciaProfissional salvarExperienciaProfissional(ExperienciaProfissional eProfissional){
    return experienciaProfissionalRepository.save(eProfissional);
  }
}
