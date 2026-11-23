package br.com.bbts.api.bbts_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.bbts.api.bbts_api.models.Feedback;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

  List<Feedback> findByDestinatarioIdOrderByDataEnvioDesc(Long destinatarioId);

  List<Feedback> findByAutorIdOrderByDataEnvioDesc(Long autorId);

}
