# BBTS API

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

API REST desenvolvida com Spring Boot para gerenciamento completo de currículos profissionais, permitindo cadastro e organização de informações curriculares de forma estruturada e segura.

## 📋 Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Funcionalidades](#funcionalidades)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Arquitetura](#arquitetura)
- [Pré-requisitos](#pré-requisitos)
- [Instalação](#instalação)
- [Configuração](#configuração)
- [Executando a Aplicação](#executando-a-aplicação)
- [Documentação da API](#documentação-da-api)
- [Endpoints](#endpoints)
- [Exemplos de Uso](#exemplos-de-uso)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Contribuindo](#contribuindo)
- [Licença](#licença)
- [Contato](#contato)

## 🎯 Sobre o Projeto

O **BBTS API** é um sistema backend robusto para gerenciamento completo de informações curriculares. A aplicação permite que usuários cadastrem e organizem seus dados profissionais de forma eficiente, incluindo:

- **Dados Pessoais**: informações básicas do usuário
- **Experiências Profissionais**: histórico completo de empregos
- **Certificações**: cursos e certificados obtidos
- **Idiomas**: registro de idiomas com níveis de proficiência
- **Tecnologias**: habilidades técnicas com níveis de domínio

O projeto foi desenvolvido seguindo os padrões REST e boas práticas do Spring Framework, com arquitetura em camadas (Controller, Service, Repository) e uso de DTOs para proteção de dados sensíveis.

## ✨ Funcionalidades

- ✅ Cadastro completo de usuários com validação de dados
- ✅ Gerenciamento de experiências profissionais (emprego atual/anterior)
- ✅ Registro de certificações com suporte a arquivos
- ✅ Cadastro de idiomas com níveis de proficiência (INICIANTE até NATIVO)
- ✅ Registro de tecnologias com níveis de habilidade (JUNIOR, PLENO, SENIOR)
- ✅ Upload de foto de perfil e certificados
- ✅ Cadastro completo em uma única requisição (multipart/form-data)
- ✅ Validação de dados com Bean Validation
- ✅ Proteção de dados sensíveis através de DTOs
- ✅ Documentação interativa com Swagger/OpenAPI
- ✅ Configuração de CORS para integração com frontend

## 🚀 Tecnologias Utilizadas

### Backend
- **Java 17** - Linguagem de programação
- **Spring Boot 3.5.7** - Framework principal
- **Spring Data JPA** - Persistência de dados
- **Spring Validation** - Validação de dados
- **Hibernate** - ORM (Object-Relational Mapping)

### Banco de Dados
- **MySQL 8.0+** - Sistema de gerenciamento de banco de dados

### Ferramentas e Bibliotecas
- **Lombok** - Redução de código boilerplate
- **SpringDoc OpenAPI 2.6.0** - Documentação automática da API
- **Maven** - Gerenciamento de dependências
- **Spring Boot DevTools** - Ferramentas de desenvolvimento

## 🏗️ Arquitetura

O projeto segue uma arquitetura em camadas bem definida:

```
┌─────────────────────────────────────┐
│         Controller Layer            │  ← Requisições HTTP
├─────────────────────────────────────┤
│          Service Layer              │  ← Lógica de negócio
├─────────────────────────────────────┤
│        Repository Layer             │  ← Acesso a dados
├─────────────────────────────────────┤
│         Database (MySQL)            │  ← Persistência
└─────────────────────────────────────┘
```

### Camadas:

- **Controller**: Recebe requisições HTTP, valida entrada e retorna respostas
- **Service**: Contém a lógica de negócio da aplicação
- **Repository**: Interface com o banco de dados usando Spring Data JPA
- **Model**: Entidades JPA que representam as tabelas do banco
- **DTO**: Objetos de transferência de dados para proteção de informações sensíveis

## 📋 Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- **Java JDK 17** ou superior
- **Maven 3.6+** (ou use o wrapper incluído no projeto)
- **MySQL 8.0+**
- **IDE** (IntelliJ IDEA, Eclipse, VS Code, etc.)
- **Git** (para clonar o repositório)

## 🔧 Instalação

### 1. Clone o repositório

```bash
git clone https://github.com/seu-usuario/bbts-api.git
cd bbts-api
```

### 2. Crie o banco de dados

Conecte-se ao MySQL e execute:

```sql
CREATE DATABASE bbts_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. Configure as credenciais

Edite o arquivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bbts_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

## ⚙️ Configuração

### application.properties

Configurações disponíveis:

```properties
# Nome da aplicação
spring.application.name=bbts-api

# Configuração do banco de dados
spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:mysql://localhost:3306/seu_banco
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Mostrar SQL no console (opcional - descomente para debug)
#spring.jpa.show-sql=true
#spring.jpa.properties.hibernate.format_sql=true
```

### CORS

A API está configurada para aceitar requisições de:
- `http://localhost:5500`
- `http://127.0.0.1:5500`
- `http://localhost:3000`

Para adicionar novas origens, edite `CorsConfig.java`:

```java
.allowedOrigins(
    "http://localhost:5500",
    "http://127.0.0.1:5500",
    "http://localhost:3000",
    "https://seu-frontend.com"  // Adicione aqui
)
```

## ▶️ Executando a Aplicação

### Usando Maven Wrapper (Recomendado)

**Linux/Mac:**
```bash
./mvnw spring-boot:run
```

**Windows:**
```bash
mvnw.cmd spring-boot:run
```

### Usando Maven instalado

```bash
mvn spring-boot:run
```

### Gerando o JAR

```bash
./mvnw clean package
java -jar target/bbts-api-0.0.1-SNAPSHOT.jar
```

A aplicação estará disponível em: `http://localhost:8080`

## 📚 Documentação da API

Após iniciar a aplicação, acesse a documentação interativa:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

## 🔌 Endpoints

### Usuários

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/usuarios` | Lista todos os usuários |
| GET | `/usuarios/{id}` | Busca usuário por ID |
| POST | `/usuarios` | Cadastra novo usuário |
| PUT | `/usuarios/{id}` | Atualiza usuário |
| DELETE | `/usuarios/{id}` | Remove usuário |

### Experiências Profissionais

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/experienciaProfissional` | Lista todas as experiências |
| GET | `/experienciaProfissional/{id}` | Busca experiência por ID |
| POST | `/experienciaProfissional` | Cadastra nova experiência |
| PUT | `/experienciaProfissional/{id}` | Atualiza experiência |
| DELETE | `/experienciaProfissional/{id}` | Remove experiência |

### Certificações

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/certificacoes` | Lista todas as certificações |
| GET | `/certificacoes/{id}` | Busca certificação por ID |
| POST | `/certificacoes` | Cadastra nova certificação |
| PUT | `/certificacoes/{id}` | Atualiza certificação |
| DELETE | `/certificacoes/{id}` | Remove certificação |

### Idiomas

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/idiomas` | Lista todos os idiomas |
| GET | `/idiomas/{id}` | Busca idioma por ID |
| POST | `/idiomas` | Cadastra novo idioma |
| PUT | `/idiomas/{id}` | Atualiza idioma |
| DELETE | `/idiomas/{id}` | Remove idioma |

### Tecnologias

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/tecnologias` | Lista todas as tecnologias |
| GET | `/tecnologias/{id}` | Busca tecnologia por ID |
| POST | `/tecnologias` | Cadastra nova tecnologia |
| PUT | `/tecnologias/{id}` | Atualiza tecnologia |
| DELETE | `/tecnologias/{id}` | Remove tecnologia |

### Cadastro Completo

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/cadastro/salvar-completo` | Cadastro completo com upload de arquivos |

## 💡 Exemplos de Uso

### Cadastrar Usuário

```bash
curl -X POST http://localhost:8080/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "cpf": "12345678900",
    "dataNascimento": "1990-05-15",
    "email": "joao.silva@email.com",
    "senha": "senha123",
    "telefone": "(81) 98765-4321"
  }'
```

### Cadastrar Experiência Profissional

```bash
curl -X POST http://localhost:8080/experienciaProfissional \
  -H "Content-Type: application/json" \
  -d '{
    "empresa": "Tech Solutions",
    "cargo": "Desenvolvedor Full Stack",
    "dataInicio": "2020-01-15",
    "dataFim": null,
    "empregoAtual": true,
    "descricao": "Desenvolvimento de aplicações web com Spring Boot e React",
    "usuario": {
      "id": 1
    }
  }'
```

### Cadastrar Idioma

```bash
curl -X POST http://localhost:8080/idiomas \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Inglês",
    "nivel": "AVANCADO",
    "usuario": {
      "id": 1
    }
  }'
```

**Níveis de idioma disponíveis:**
- INICIANTE
- BASICO
- INTERMEDIARIO
- AVANCADO
- FLUENTE
- NATIVO

### Cadastrar Tecnologia

```bash
curl -X POST http://localhost:8080/tecnologias \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Spring Boot",
    "nivel": "SENIOR",
    "usuario": {
      "id": 1
    }
  }'
```

**Níveis de habilidade disponíveis:**
- JUNIOR
- PLENO
- SENIOR

### Cadastro Completo (Multipart)

```bash
curl -X POST http://localhost:8080/api/cadastro/salvar-completo \
  -F 'dados={
    "usuario": {
      "nome": "Maria Santos",
      "cpf": "98765432100",
      "dataNascimento": "1992-08-20",
      "email": "maria@email.com",
      "telefone": "(81) 99999-8888"
    },
    "idiomas": [
      {"nome": "Inglês", "nivel": "FLUENTE"},
      {"nome": "Espanhol", "nivel": "INTERMEDIARIO"}
    ],
    "experiencias": [
      {
        "empresa": "Tech Corp",
        "cargo": "Analista de Sistemas",
        "dataInicio": "2019-03-01",
        "dataFim": "2021-12-31",
        "empregoAtual": false
      }
    ],
    "certificacoes": [
      {
        "nome": "AWS Certified Developer",
        "dataCertificacao": "2021-06-15"
      }
    ]
  };type=application/json' \
  -F 'foto=@/caminho/para/foto.jpg' \
  -F 'certificados=@/caminho/para/certificado1.pdf' \
  -F 'certificados=@/caminho/para/certificado2.pdf'
```

## 📁 Estrutura do Projeto

```
bbts-api/
│
├── src/
│   ├── main/
│   │   ├── java/br/com/bbts/api/bbts_api/
│   │   │   ├── config/
│   │   │   │   └── CorsConfig.java
│   │   │   ├── constants/
│   │   │   │   ├── NivelHabilidades.java
│   │   │   │   └── NivelIdioma.java
│   │   │   ├── controller/
│   │   │   │   ├── CadastroController.java
│   │   │   │   ├── CertificacoesController.java
│   │   │   │   ├── ExperienciaProfissionalController.java
│   │   │   │   ├── IdiomaController.java
│   │   │   │   ├── TecnologiaController.java
│   │   │   │   └── UsuarioController.java
│   │   │   ├── dto/
│   │   │   │   ├── CadastroCompletoDto.java
│   │   │   │   ├── CertificacaoDto.java
│   │   │   │   ├── ExperienciaDto.java
│   │   │   │   ├── IdiomaDto.java
│   │   │   │   └── UsuarioDto.java
│   │   │   ├── models/
│   │   │   │   ├── Certificacoes.java
│   │   │   │   ├── ExperienciaProfissional.java
│   │   │   │   ├── Idioma.java
│   │   │   │   ├── Tecnologia.java
│   │   │   │   └── Usuario.java
│   │   │   ├── repository/
│   │   │   │   ├── CertificacoesRepository.java
│   │   │   │   ├── ExperienciaProfissionalRepository.java
│   │   │   │   ├── IdiomaRepository.java
│   │   │   │   ├── TecnologiaRepository.java
│   │   │   │   └── UsuarioRepository.java
│   │   │   ├── services/
│   │   │   │   ├── CadastroService.java
│   │   │   │   ├── CertificacoesService.java
│   │   │   │   ├── ExperienciaProfissionalService.java
│   │   │   │   ├── IdiomaService.java
│   │   │   │   ├── TecnologiaService.java
│   │   │   │   └── UsuarioService.java
│   │   │   └── BbtsApiApplication.java
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│       └── java/br/com/bbts/api/bbts_api/
│           └── BbtsApiApplicationTests.java
│
├── .gitignore
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

### Descrição das Camadas

**config/**
- Configurações gerais da aplicação (CORS, etc.)

**constants/**
- Enumerações e constantes utilizadas no projeto

**controller/**
- Controllers REST que recebem requisições HTTP

**dto/**
- Data Transfer Objects para transferência segura de dados

**models/**
- Entidades JPA que representam tabelas do banco

**repository/**
- Interfaces de acesso ao banco de dados

**services/**
- Camada de lógica de negócio

## 🤝 Contribuindo

Contribuições são sempre bem-vindas! Para contribuir:

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona MinhaFeature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

### Padrões de Código

- Siga as convenções de código Java
- Utilize Lombok para reduzir boilerplate
- Adicione validações com Bean Validation
- Documente métodos públicos importantes
- Escreva testes unitários para novas funcionalidades

## 👥 Autor
Vitor Santos - Desenvolvedor Principal

## 📞 Contato

**BBTS → Squad 22**
- Email: vitorrsantos.dev@gmail.com
- Website: https://www.bbts.com.br

---

⭐ Se este projeto foi útil para você, considere dar uma estrela no repositório!
