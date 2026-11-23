package br.com.bbts.api.bbts_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.bbts.api.bbts_api.models.Equipe;

@Repository
public interface EquipeRepository extends JpaRepository<Equipe, Long> {

  List<Equipe> findByGestorIdOrderById(Long gestorId);

  List<Equipe> findAllByOrderById();

}
