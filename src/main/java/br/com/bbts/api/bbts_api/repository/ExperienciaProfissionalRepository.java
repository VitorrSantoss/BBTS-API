package br.com.bbts.api.bbts_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.bbts.api.bbts_api.models.ExperienciaProfissional;

@Repository
public interface ExperienciaProfissionalRepository extends JpaRepository <ExperienciaProfissional, Long> {

  List<ExperienciaProfissional> findByUsuarioIdOrderByDataInicioDesc(Long usuarioId);

}
