package br.com.bbts.api.bbts_api.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import br.com.bbts.api.bbts_api.models.FotoUsuario;
import br.com.bbts.api.bbts_api.repository.FotoUsuarioRepository;

/** Foto de perfil. A "versão" (epoch em ms) vai no DTO para o front invalidar o cache da imagem. */
@Service
public class FotoService {

  private static final long TAMANHO_MAXIMO = 5L * 1024 * 1024;

  private final FotoUsuarioRepository fotoRepository;

  public FotoService(FotoUsuarioRepository fotoRepository) {
    this.fotoRepository = fotoRepository;
  }

  private static Long emMilis(LocalDateTime data) {
    return data.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
  }

  /** Versão da foto de cada usuário que tem uma, sem carregar os bytes. */
  public Map<Long, Long> versoes() {
    return fotoRepository.listarVersoes().stream()
        .collect(Collectors.toMap(FotoUsuarioRepository.VersaoFoto::getUsuarioId, v -> emMilis(v.getAtualizadaEm())));
  }

  public Long versao(Long usuarioId) {
    return fotoRepository.buscarVersao(usuarioId).map(v -> emMilis(v.getAtualizadaEm())).orElse(null);
  }

  public Optional<FotoUsuario> buscar(Long usuarioId) {
    return fotoRepository.findById(usuarioId);
  }

  public Long salvar(Long usuarioId, MultipartFile arquivo) {
    if (arquivo == null || arquivo.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Envie um arquivo de imagem.");
    }
    String tipo = arquivo.getContentType();
    if (tipo == null || !tipo.startsWith("image/")) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A foto precisa ser uma imagem.");
    }
    if (arquivo.getSize() > TAMANHO_MAXIMO) {
      throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "A foto pode ter no máximo 5 MB.");
    }
    try {
      FotoUsuario foto = new FotoUsuario(usuarioId, tipo, arquivo.getBytes(), LocalDateTime.now());
      fotoRepository.save(foto);
      return emMilis(foto.getAtualizadaEm());
    } catch (IOException e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível ler a foto.");
    }
  }
}
