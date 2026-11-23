package br.com.bbts.api.bbts_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.bbts.api.bbts_api.models.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository <Usuario, Long> {

  Optional<Usuario> findByEmailIgnoreCase(String email);

  List<Usuario> findByEquipeIdOrderByNome(Long equipeId);

}
