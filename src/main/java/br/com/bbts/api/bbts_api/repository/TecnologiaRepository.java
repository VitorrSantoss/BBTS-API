package br.com.bbts.api.bbts_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.bbts.api.bbts_api.models.Tecnologia;

@Repository
public interface TecnologiaRepository extends JpaRepository <Tecnologia, Long> {

  List<Tecnologia> findByUsuarioIdOrderById(Long usuarioId);

}
