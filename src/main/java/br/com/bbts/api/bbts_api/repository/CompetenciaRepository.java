package br.com.bbts.api.bbts_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.bbts.api.bbts_api.models.Competencia;

@Repository
public interface CompetenciaRepository extends JpaRepository<Competencia, Long> {

  Optional<Competencia> findBySiglaIgnoreCase(String sigla);

  List<Competencia> findAllByOrderBySigla();

}
