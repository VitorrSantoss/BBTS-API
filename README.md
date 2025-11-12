# BBTS API

API REST desenvolvida com Spring Boot para gerenciamento de currículos profissionais.

## Sobre o Projeto

O BBTS API é um sistema backend para gerenciamento completo de informações curriculares. A aplicação permite que usuários cadastrem e organizem seus dados profissionais, incluindo experiências de trabalho, certificações obtidas e idiomas dominados. O projeto foi desenvolvido seguindo os padrões REST e boas práticas do Spring Framework, com arquitetura em camadas (Controller, Service, Repository) e uso de DTOs para proteção de dados sensíveis.

## Tecnologias

- Java 17
- Spring Boot 3.5.7
- Spring Data JPA
- MySQL
- Lombok
- SpringDoc OpenAPI (Swagger)
- Maven

## Funcionalidades

A API permite gerenciar:

- **Usuários**: cadastro completo com dados pessoais
- **Experiências Profissionais**: histórico de empregos
- **Certificações**: cursos e certificados
- **Idiomas**: registro de idiomas com níveis de proficiência

## Endpoints

Todos os endpoints seguem o padrão REST com operações CRUD:

### Usuários
- `GET /usuarios` - Listar todos
- `GET /usuarios/{id}` - Buscar por ID
- `POST /usuarios` - Cadastrar
- `PUT /usuarios/{id}` - Atualizar
- `DELETE /usuarios/{id}` - Excluir

### Experiências Profissionais
- `GET /experienciaProfissional` - Listar todas
- `GET /experienciaProfissional/{id}` - Buscar por ID
- `POST /experienciaProfissional` - Cadastrar
- `PUT /experienciaProfissional/{id}` - Atualizar
- `DELETE /experienciaProfissional/{id}` - Excluir

### Certificações
- `GET /certificacoes` - Listar todas
- `GET /certificacoes/{id}` - Buscar por ID
- `POST /certificacoes` - Cadastrar
- `PUT /certificacoes/{id}` - Atualizar
- `DELETE /certificacoes/{id}` - Excluir

### Idiomas
- `GET /idiomas` - Listar todos
- `GET /idiomas/{id}` - Buscar por ID
- `POST /idiomas` - Cadastrar
- `PUT /idiomas/{id}` - Atualizar
- `DELETE /idiomas/{id}` - Excluir

## Configuração

1. **Banco de Dados**

Crie um banco MySQL e configure em `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/seu_banco
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

2. **Executar**

```bash
./mvnw spring-boot:run
```

## Documentação API

Acesse o Swagger UI após iniciar a aplicação:

```
http://localhost:8080/swagger-ui.html
```

## Estrutura do Projeto

```
src/main/java/br/com/bbts/api/bbts_api/
├── constants/      # Enums e constantes
├── controller/     # Controllers REST
├── dto/           # Data Transfer Objects
├── models/        # Entidades JPA
├── repository/    # Repositórios
└── services/      # Lógica de negócio
```