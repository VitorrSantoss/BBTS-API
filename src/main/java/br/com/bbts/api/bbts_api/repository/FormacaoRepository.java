package br.com.bbts.api.bbts_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.bbts.api.bbts_api.models.Formacao;

@Repository
public interface FormacaoRepository extends JpaRepository<Formacao, Long> {

  List<Formacao> findByUsuarioIdOrderByAnoInicioDesc(Long usuarioId);

}
