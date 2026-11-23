package br.com.bbts.api.bbts_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.bbts.api.bbts_api.models.CompetenciaUsuario;

@Repository
public interface CompetenciaUsuarioRepository extends JpaRepository<CompetenciaUsuario, Long> {

  List<CompetenciaUsuario> findByUsuarioIdOrderById(Long usuarioId);

  Optional<CompetenciaUsuario> findByUsuarioIdAndCompetenciaId(Long usuarioId, Long competenciaId);

}
