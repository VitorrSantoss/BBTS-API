package br.com.bbts.api.bbts_api.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.bbts.api.bbts_api.dto.CadastroCompletoDto;
import br.com.bbts.api.bbts_api.models.Certificacoes;
import br.com.bbts.api.bbts_api.models.ExperienciaProfissional;
import br.com.bbts.api.bbts_api.models.Idioma;
import br.com.bbts.api.bbts_api.models.Usuario;
import br.com.bbts.api.bbts_api.repository.CertificacoesRepository;
import br.com.bbts.api.bbts_api.repository.ExperienciaProfissionalRepository;
import br.com.bbts.api.bbts_api.repository.IdiomaRepository;
import br.com.bbts.api.bbts_api.repository.UsuarioRepository;
import jakarta.transaction.Transactional;

@Service
public class CadastroService {

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Autowired
  private IdiomaRepository idiomaRepository;

  @Autowired
  private ExperienciaProfissionalRepository experienciaProfissionalRepository;

  @Autowired
  private CertificacoesRepository certificacoesRepository;

  @Transactional
  public void salvarCompleto(
      CadastroCompletoDto dto,
      MultipartFile foto,
      List<MultipartFile> certificados) {

    // =========================
    // 1️⃣ Salvar USUARIO
    // =========================
    Usuario usuario = new Usuario();
    usuario.setNome(dto.getUsuario().getNome());
    usuario.setCpf(dto.getUsuario().getCpf());
    usuario.setEmail(dto.getUsuario().getEmail());
    usuario.setTelefone(dto.getUsuario().getTelefone());
    usuario.setDataNascimento(dto.getUsuario().getDataNascimento());

    usuarioRepository.save(usuario);

    // =========================
    // 2️⃣ Salvar IDIOMAS
    // =========================
    if (dto.getIdiomas() != null) {
      dto.getIdiomas().forEach(i -> {
        Idioma idioma = new Idioma();
        idioma.setNome(i.getNome());
        //Idioma.setNivel(i.getNivel());
        idioma.setUsuario(usuario);

        idiomaRepository.save(idioma);
      });
    }

    // =========================
    // 3️⃣ Salvar EXPERIÊNCIAS
    // =========================
    if (dto.getExperiencias() != null) {
      dto.getExperiencias().forEach(exp -> {
        ExperienciaProfissional e = new ExperienciaProfissional();
        e.setEmpresa(exp.getEmpresa());
        e.setCargo(exp.getCargo());
        e.setDataInicio(exp.getDataInicio());
        e.setDataFim(exp.getDataFim());
        e.setEmpregoAtual(exp.isEmpregoAtual());
        e.setUsuario(usuario);

        experienciaProfissionalRepository.save(e);
      });
    }

    // =========================
    // 4️⃣ Salvar CERTIFICAÇÕES
    // =========================
    if (dto.getCertificacoes() != null) {
      dto.getCertificacoes().forEach(cert -> {
        Certificacoes c = new Certificacoes();
        c.setNomeCurso(cert.getNome());
        c.setDataConclusao((cert.getDataCertificacao()));
        c.setUsuario(usuario);

        certificacoesRepository.save(c);
      });
    }

    // =========================
    // 5️⃣ Salvar FOTO (opcional)
    // =========================
    if (foto != null && !foto.isEmpty()) {
      // você pode salvar no disco, Cloud ou banco
    }

    // =========================
    // 6️⃣ Salvar ARQUIVOS DE CERTIFICADO (opcional)
    // =========================
    if (certificados != null) {
      certificados.forEach(arquivo -> {
        if (!arquivo.isEmpty()) {
          // salvar arquivo
        }
      });
    }
  }
}
