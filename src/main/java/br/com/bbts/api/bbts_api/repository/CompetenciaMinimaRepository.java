package br.com.bbts.api.bbts_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.bbts.api.bbts_api.models.CompetenciaMinima;

@Repository
public interface CompetenciaMinimaRepository extends JpaRepository<CompetenciaMinima, Long> {

  List<CompetenciaMinima> findByEquipeIdOrderById(Long equipeId);

}
