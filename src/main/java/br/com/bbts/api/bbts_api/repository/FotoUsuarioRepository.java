package br.com.bbts.api.bbts_api.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.bbts.api.bbts_api.models.FotoUsuario;

@Repository
public interface FotoUsuarioRepository extends JpaRepository<FotoUsuario, Long> {

  /** Só a data de cada foto, sem carregar os bytes. */
  interface VersaoFoto {
    Long getUsuarioId();
    LocalDateTime getAtualizadaEm();
  }

  @Query("select f.usuarioId as usuarioId, f.atualizadaEm as atualizadaEm from FotoUsuario f")
  List<VersaoFoto> listarVersoes();

  @Query("select f.usuarioId as usuarioId, f.atualizadaEm as atualizadaEm from FotoUsuario f where f.usuarioId = :usuarioId")
  Optional<VersaoFoto> buscarVersao(Long usuarioId);

}
