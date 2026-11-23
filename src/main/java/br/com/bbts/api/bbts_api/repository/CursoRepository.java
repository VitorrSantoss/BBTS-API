package br.com.bbts.api.bbts_api.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.bbts.api.bbts_api.models.Curso;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

  List<Curso> findByCompetenciaIdInOrderById(Collection<Long> competenciaIds);

}
