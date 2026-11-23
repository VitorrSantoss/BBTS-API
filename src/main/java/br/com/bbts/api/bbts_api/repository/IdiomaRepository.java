package br.com.bbts.api.bbts_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.bbts.api.bbts_api.models.Idioma;

@Repository
public interface IdiomaRepository extends JpaRepository <Idioma, Long> {

  List<Idioma> findByUsuarioIdOrderById(Long usuarioId);

}
