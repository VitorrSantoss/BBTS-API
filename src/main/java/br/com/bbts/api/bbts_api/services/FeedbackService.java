package br.com.bbts.api.bbts_api.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.com.bbts.api.bbts_api.dto.Requisicoes.NovoFeedbackRequest;
import br.com.bbts.api.bbts_api.dto.Respostas.FeedbackDto;
import br.com.bbts.api.bbts_api.dto.Respostas.PessoaDto;
import br.com.bbts.api.bbts_api.models.Feedback;
import br.com.bbts.api.bbts_api.models.Usuario;
import br.com.bbts.api.bbts_api.repository.FeedbackRepository;
import br.com.bbts.api.bbts_api.repository.UsuarioRepository;

@Service
public class FeedbackService {

  private final FeedbackRepository feedbackRepository;
  private final UsuarioRepository usuarioRepository;
  private final FotoService fotoService;

  public FeedbackService(FeedbackRepository feedbackRepository, UsuarioRepository usuarioRepository, FotoService fotoService) {
    this.feedbackRepository = feedbackRepository;
    this.usuarioRepository = usuarioRepository;
    this.fotoService = fotoService;
  }

  private static FeedbackDto paraDto(Feedback f, Map<Long, Long> versoes) {
    return new FeedbackDto(f.getId(), f.getTipo().name(), f.getMensagem(), f.getDataEnvio(),
        PessoaDto.de(f.getAutor(), versoes), PessoaDto.de(f.getDestinatario(), versoes));
  }

  @Transactional
  public FeedbackDto enviar(NovoFeedbackRequest requisicao) {
    Usuario destinatario = usuarioRepository.findByEmailIgnoreCase(requisicao.destinatarioEmail().trim())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum colaborador cadastrado com este e-mail."));
    Usuario autor = requisicao.autorId() == null ? null
        : usuarioRepository.findById(requisicao.autorId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Autor do feedback não encontrado."));

    Feedback feedback = new Feedback(null, autor, destinatario, requisicao.tipo(), requisicao.mensagem().trim(), LocalDateTime.now());
    return paraDto(feedbackRepository.save(feedback), fotoService.versoes());
  }

  public List<FeedbackDto> recebidos(Long usuarioId) {
    Map<Long, Long> versoes = fotoService.versoes();
    return feedbackRepository.findByDestinatarioIdOrderByDataEnvioDesc(usuarioId).stream().map(f -> paraDto(f, versoes)).toList();
  }

  public List<FeedbackDto> enviados(Long autorId) {
    Map<Long, Long> versoes = fotoService.versoes();
    return feedbackRepository.findByAutorIdOrderByDataEnvioDesc(autorId).stream().map(f -> paraDto(f, versoes)).toList();
  }
}
