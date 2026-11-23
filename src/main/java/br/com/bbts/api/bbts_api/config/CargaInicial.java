package br.com.bbts.api.bbts_api.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.bbts.api.bbts_api.constants.NivelHabilidades;
import br.com.bbts.api.bbts_api.constants.NivelIdioma;
import br.com.bbts.api.bbts_api.constants.Perfil;
import br.com.bbts.api.bbts_api.constants.StatusFormacao;
import br.com.bbts.api.bbts_api.constants.TipoFeedback;
import br.com.bbts.api.bbts_api.models.Competencia;
import br.com.bbts.api.bbts_api.models.CompetenciaMinima;
import br.com.bbts.api.bbts_api.models.CompetenciaUsuario;
import br.com.bbts.api.bbts_api.models.Curso;
import br.com.bbts.api.bbts_api.models.Equipe;
import br.com.bbts.api.bbts_api.models.ExperienciaProfissional;
import br.com.bbts.api.bbts_api.models.Feedback;
import br.com.bbts.api.bbts_api.models.Formacao;
import br.com.bbts.api.bbts_api.models.Idioma;
import br.com.bbts.api.bbts_api.models.Tecnologia;
import br.com.bbts.api.bbts_api.models.Usuario;
import br.com.bbts.api.bbts_api.repository.CompetenciaMinimaRepository;
import br.com.bbts.api.bbts_api.repository.CompetenciaRepository;
import br.com.bbts.api.bbts_api.repository.CompetenciaUsuarioRepository;
import br.com.bbts.api.bbts_api.repository.CursoRepository;
import br.com.bbts.api.bbts_api.repository.EquipeRepository;
import br.com.bbts.api.bbts_api.repository.ExperienciaProfissionalRepository;
import br.com.bbts.api.bbts_api.repository.FeedbackRepository;
import br.com.bbts.api.bbts_api.repository.FormacaoRepository;
import br.com.bbts.api.bbts_api.repository.IdiomaRepository;
import br.com.bbts.api.bbts_api.repository.TecnologiaRepository;
import br.com.bbts.api.bbts_api.repository.UsuarioRepository;

/**
 * Carga inicial do banco, idempotente:
 * - o catálogo SFIA e os cursos entram quando o catálogo está vazio;
 * - equipes, perfis de gestor, níveis, idiomas, tecnologias, experiências, formação e feedbacks
 *   entram uma vez só, quando ainda não há nenhuma equipe. Usuários que já existem (pelo e-mail)
 *   são reaproveitados; os que faltam são criados. Nada que já estava preenchido é sobrescrito.
 *
 * Desligue com `bbts.carga-inicial=false`.
 */
@Component
@ConditionalOnProperty(name = "bbts.carga-inicial", havingValue = "true", matchIfMissing = true)
public class CargaInicial implements CommandLineRunner {

  private static final Logger LOG = LoggerFactory.getLogger(CargaInicial.class);

  private final CompetenciaRepository competencias;
  private final CursoRepository cursos;
  private final EquipeRepository equipes;
  private final CompetenciaMinimaRepository minimos;
  private final CompetenciaUsuarioRepository niveis;
  private final UsuarioRepository usuarios;
  private final IdiomaRepository idiomas;
  private final TecnologiaRepository tecnologias;
  private final ExperienciaProfissionalRepository experiencias;
  private final FormacaoRepository formacoes;
  private final FeedbackRepository feedbacks;

  public CargaInicial(
      CompetenciaRepository competencias,
      CursoRepository cursos,
      EquipeRepository equipes,
      CompetenciaMinimaRepository minimos,
      CompetenciaUsuarioRepository niveis,
      UsuarioRepository usuarios,
      IdiomaRepository idiomas,
      TecnologiaRepository tecnologias,
      ExperienciaProfissionalRepository experiencias,
      FormacaoRepository formacoes,
      FeedbackRepository feedbacks) {
    this.competencias = competencias;
    this.cursos = cursos;
    this.equipes = equipes;
    this.minimos = minimos;
    this.niveis = niveis;
    this.usuarios = usuarios;
    this.idiomas = idiomas;
    this.tecnologias = tecnologias;
    this.experiencias = experiencias;
    this.formacoes = formacoes;
    this.feedbacks = feedbacks;
  }

  @Override
  @Transactional
  public void run(String... args) {
    if (competencias.count() == 0) {
      carregarCatalogo();
    }
    if (equipes.count() == 0) {
      carregarEquipes();
    }
  }

  /* ───────────── Catálogo SFIA e cursos ───────────── */

  private record ItemCatalogo(String sigla, String nome, String categoria, int nivelMax, String curso) {
  }

  private static final List<ItemCatalogo> CATALOGO = List.of(
      new ItemCatalogo("PRMG", "Gestão de Projetos", "Mudança e transformação", 7, "Gestão de projetos ágeis"),
      new ItemCatalogo("PROG", "Programação/Desenvolvimento de Software", "Desenvolvimento e implementação", 6, "Boas práticas de desenvolvimento de software"),
      new ItemCatalogo("TEST", "Testes Funcionais", "Desenvolvimento e implementação", 6, "Testes automatizados na prática"),
      new ItemCatalogo("DESN", "Projeto de Sistemas", "Desenvolvimento e implementação", 6, "Arquitetura e projeto de sistemas"),
      new ItemCatalogo("DBDS", "Projeto de Banco de Dados", "Desenvolvimento e implementação", 6, "Modelagem de banco de dados"),
      new ItemCatalogo("URCH", "Pesquisa de Usuário", "Experiência do usuário", 6, "Pesquisa com usuários: do roteiro à síntese"),
      new ItemCatalogo("UNAN", "Análise da Experiência do Usuário", "Experiência do usuário", 5, "Análise de experiência do usuário"),
      new ItemCatalogo("GRDN", "Design Gráfico", "Experiência do usuário", 5, "Fundamentos de design gráfico"),
      new ItemCatalogo("CFMG", "Gestão de Configuração", "Entrega e operação", 6, "Versionamento e gestão de configuração com Git"),
      new ItemCatalogo("ITOP", "Operação de Infraestrutura", "Entrega e operação", 5, "Operação de infraestrutura em nuvem"),
      new ItemCatalogo("SCTY", "Segurança da Informação", "Segurança e privacidade", 7, "Segurança da informação essencial"));

  private void carregarCatalogo() {
    for (ItemCatalogo item : CATALOGO) {
      Competencia c = competencias.save(new Competencia(null, item.sigla(), item.nome(), item.categoria(), item.nivelMax()));
      cursos.save(new Curso(null, item.curso(), c));
    }
    LOG.info("Carga inicial: {} competências SFIA e cursos cadastrados.", CATALOGO.size());
  }

  /* ───────────── Pessoas, equipes e níveis ───────────── */

  private record Pessoa(String nome, String cpf, LocalDate nascimento, String email, String telefone, String area, Perfil perfil) {
  }

  private Usuario garantir(Pessoa p) {
    Usuario usuario = usuarios.findByEmailIgnoreCase(p.email()).orElseGet(() -> {
      Usuario novo = new Usuario();
      novo.setNome(p.nome());
      novo.setCpf(p.cpf());
      novo.setDataNascimento(p.nascimento());
      novo.setEmail(p.email());
      novo.setTelefone(p.telefone());
      novo.setArea(p.area());
      return novo;
    });
    if (usuario.getPerfil() == null || p.perfil() == Perfil.GESTOR) {
      usuario.setPerfil(p.perfil());
    }
    if (usuario.getArea() == null) {
      usuario.setArea(p.area());
    }
    return usuarios.save(usuario);
  }

  private Equipe equipe(String nome, String foco, String descricao, Usuario gestor, Map<String, Integer> minimosPorSigla) {
    Equipe equipe = equipes.save(new Equipe(null, nome, foco, descricao, gestor));
    minimosPorSigla.forEach((sigla, nivel) -> minimos.save(new CompetenciaMinima(null, equipe, competencia(sigla), nivel)));
    return equipe;
  }

  private Competencia competencia(String sigla) {
    return competencias.findBySiglaIgnoreCase(sigla).orElseThrow();
  }

  private void membro(Usuario usuario, Equipe equipe, Map<String, Integer> niveisPorSigla) {
    if (usuario.getEquipe() == null) {
      usuario.setEquipe(equipe);
      usuarios.save(usuario);
    }
    if (niveis.findByUsuarioIdOrderById(usuario.getId()).isEmpty()) {
      niveisPorSigla.forEach((sigla, nivel) -> niveis.save(new CompetenciaUsuario(null, usuario, competencia(sigla), nivel)));
    }
  }

  private void idiomas(Usuario u, Object... pares) {
    if (!idiomas.findByUsuarioIdOrderById(u.getId()).isEmpty()) {
      return;
    }
    for (int i = 0; i < pares.length; i += 2) {
      idiomas.save(new Idioma(null, (String) pares[i], (NivelIdioma) pares[i + 1], u));
    }
  }

  private void tecnologias(Usuario u, Object... pares) {
    if (!tecnologias.findByUsuarioIdOrderById(u.getId()).isEmpty()) {
      return;
    }
    for (int i = 0; i < pares.length; i += 2) {
      tecnologias.save(new Tecnologia(null, (String) pares[i], (NivelHabilidades) pares[i + 1], u));
    }
  }

  private void experiencia(Usuario u, String empresa, String cargo, LocalDate inicio, LocalDate fim, String descricao) {
    experiencias.save(new ExperienciaProfissional(null, empresa, cargo, inicio, fim, fim == null, descricao, u));
  }

  private boolean semExperiencias(Usuario u) {
    return experiencias.findByUsuarioIdOrderByDataInicioDesc(u.getId()).isEmpty();
  }

  private void formacao(Usuario u, String instituicao, String curso, int inicio, Integer fim, StatusFormacao status) {
    formacoes.save(new Formacao(null, instituicao, curso, inicio, fim, status, u));
  }

  private boolean semFormacao(Usuario u) {
    return formacoes.findByUsuarioIdOrderByAnoInicioDesc(u.getId()).isEmpty();
  }

  private void carregarEquipes() {
    Usuario mariana = garantir(new Pessoa("Mariana Costa Almeida", "11122233396", LocalDate.of(1984, 3, 12),
        "mariana.almeida@bbts.com.br", "11988011001", "Gestão de Tecnologia", Perfil.GESTOR));
    Usuario ricardo = garantir(new Pessoa("Ricardo Nogueira Lima", "22233344405", LocalDate.of(1979, 8, 25),
        "ricardo.lima@bbts.com.br", "11988011002", "Gestão de Projetos", Perfil.GESTOR));
    Usuario camila = garantir(new Pessoa("Camila Ferreira Souza", "33344455514", LocalDate.of(1990, 1, 30),
        "camila.souza@bbts.com.br", "11988022001", "Desenvolvimento Front-End", Perfil.COLABORADOR));
    Usuario lucas = garantir(new Pessoa("Lucas Oliveira Santos", "44455566623", LocalDate.of(1995, 6, 18),
        "lucas.santos@bbts.com.br", "11988022002", "Desenvolvimento Back-End", Perfil.COLABORADOR));
    Usuario beatriz = garantir(new Pessoa("Beatriz Martins Rocha", "55566677732", LocalDate.of(1998, 11, 5),
        "beatriz.rocha@bbts.com.br", "11988022003", "Qualidade de Software", Perfil.COLABORADOR));
    Usuario felipe = garantir(new Pessoa("Felipe Araújo Pereira", "66677788841", LocalDate.of(1993, 9, 14),
        "felipe.pereira@bbts.com.br", "11988022004", "Infraestrutura e DevOps", Perfil.COLABORADOR));

    Equipe desenvolvimento = equipe("Desenvolvimento", "Front-End e Back-End das soluções BBTS",
        "Construímos as aplicações web da BBTS de ponta a ponta: interfaces acessíveis e responsivas no front-end e serviços "
            + "seguros e performáticos no back-end, sempre com testes e versionamento em dia.",
        mariana, Map.of("PROG", 4, "TEST", 3, "DESN", 3, "CFMG", 3));
    Equipe qualidade = equipe("Qualidade e Infraestrutura", "Testes, DevOps e operação",
        "Garantimos que cada entrega chegue estável à produção: automação de testes, pipelines de integração contínua, "
            + "observabilidade e operação da infraestrutura.",
        mariana, Map.of("TEST", 4, "CFMG", 3, "ITOP", 3, "SCTY", 3));
    equipe("Projetos Estratégicos", "Planejamento e gestão de portfólio",
        "Organizamos prioridades, prazos e recursos dos projetos da BBTS, conectando as metas do negócio ao trabalho das equipes técnicas.",
        ricardo, Map.of("PRMG", 5, "DESN", 3));

    membro(camila, desenvolvimento, Map.of("PROG", 4, "TEST", 3, "DESN", 3, "CFMG", 2, "URCH", 3, "UNAN", 3, "GRDN", 3));
    membro(lucas, desenvolvimento, Map.of("PROG", 5, "TEST", 2, "DESN", 4, "CFMG", 3, "DBDS", 4));
    membro(beatriz, qualidade, Map.of("TEST", 5, "CFMG", 3, "ITOP", 2, "SCTY", 2, "PROG", 3));
    membro(felipe, qualidade, Map.of("ITOP", 4, "CFMG", 4, "SCTY", 3, "TEST", 3, "PROG", 3));

    idiomas(camila, "Português", NivelIdioma.NATIVO, "Inglês", NivelIdioma.AVANCADO);
    idiomas(lucas, "Português", NivelIdioma.NATIVO, "Inglês", NivelIdioma.INTERMEDIARIO);
    idiomas(beatriz, "Português", NivelIdioma.NATIVO, "Inglês", NivelIdioma.AVANCADO, "Espanhol", NivelIdioma.BASICO);
    idiomas(felipe, "Português", NivelIdioma.NATIVO, "Inglês", NivelIdioma.FLUENTE);

    tecnologias(camila, "React", NivelHabilidades.SENIOR, "TypeScript", NivelHabilidades.PLENO, "Figma", NivelHabilidades.PLENO);
    tecnologias(lucas, "Java", NivelHabilidades.SENIOR, "Spring Boot", NivelHabilidades.PLENO, "MySQL", NivelHabilidades.PLENO);
    tecnologias(beatriz, "Cypress", NivelHabilidades.PLENO, "Selenium", NivelHabilidades.PLENO, "Postman", NivelHabilidades.SENIOR);
    tecnologias(felipe, "Docker", NivelHabilidades.SENIOR, "Kubernetes", NivelHabilidades.PLENO, "Linux", NivelHabilidades.SENIOR);

    if (semExperiencias(camila)) {
      experiencia(camila, "BBTS", "Desenvolvedora Front-End", LocalDate.of(2021, 3, 1), null, "Interfaces do portal de competências.");
      experiencia(camila, "Agência Ponto Digital", "Desenvolvedora Web", LocalDate.of(2017, 2, 1), LocalDate.of(2021, 2, 28), null);
    }
    if (semExperiencias(lucas)) {
      experiencia(lucas, "BBTS", "Desenvolvedor Back-End", LocalDate.of(2020, 8, 3), null, "APIs REST em Java e Spring Boot.");
    }
    if (semExperiencias(beatriz)) {
      experiencia(beatriz, "BBTS", "Analista de Qualidade", LocalDate.of(2022, 1, 10), null, "Automação de testes de ponta a ponta.");
    }
    if (semExperiencias(felipe)) {
      experiencia(felipe, "BBTS", "Engenheiro DevOps", LocalDate.of(2019, 5, 2), null, "Pipelines de CI/CD e observabilidade.");
      experiencia(felipe, "Datacenter Sul", "Analista de Infraestrutura", LocalDate.of(2015, 3, 1), LocalDate.of(2019, 4, 30), null);
    }

    if (semFormacao(camila)) {
      formacao(camila, "FIAP", "Tecnólogo em Análise e Desenvolvimento de Sistemas", 2010, 2012, StatusFormacao.CONCLUIDO);
    }
    if (semFormacao(lucas)) {
      formacao(lucas, "Universidade Presbiteriana Mackenzie", "Ciência da Computação", 2013, 2017, StatusFormacao.CONCLUIDO);
    }
    if (semFormacao(beatriz)) {
      formacao(beatriz, "Faculdade Senac", "Pós-graduação em Engenharia de Software", 2025, 2026, StatusFormacao.CURSANDO);
      formacao(beatriz, "Faculdade Senac", "Tecnólogo em Análise e Desenvolvimento de Sistemas", 2017, 2019, StatusFormacao.CONCLUIDO);
    }
    if (semFormacao(felipe)) {
      formacao(felipe, "Universidade Federal do ABC", "Engenharia de Computação", 2011, 2015, StatusFormacao.CONCLUIDO);
    }

    LocalDateTime agora = LocalDateTime.now();
    feedbacks.save(new Feedback(null, mariana, camila, TipoFeedback.POSITIVO,
        "Excelente entrega do novo painel de competências: acessível, rápido e bem testado. Obrigada pelo capricho!",
        agora.minusDays(2)));
    feedbacks.save(new Feedback(null, mariana, beatriz, TipoFeedback.DESENVOLVIMENTO,
        "Vamos reforçar Segurança da Informação e Operação de Infraestrutura nos próximos meses. A trilha sugerida no seu painel é um bom começo.",
        agora.minusDays(5)));
    feedbacks.save(new Feedback(null, mariana, lucas, TipoFeedback.DESENVOLVIMENTO,
        "Suas APIs estão sólidas. O próximo passo é aumentar a cobertura de testes automatizados.",
        agora.minusDays(9)));

    LOG.info("Carga inicial: 3 equipes, perfis, níveis e feedbacks de exemplo cadastrados.");
  }
}
