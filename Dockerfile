# -------------------------------------------------------------
# ETAPA 1: Build (Construção do .jar)
# -------------------------------------------------------------
FROM eclipse-temurin:17-jdk-alpine AS build

# Define a pasta de trabalho dentro do container
WORKDIR /app

# Copia os arquivos do projeto para dentro do container
COPY . .

# Dá permissão de execução para o Maven Wrapper
RUN chmod +x mvnw

# Roda o comando para gerar o .jar (pula os testes para agilizar o deploy)
RUN ./mvnw clean package -DskipTests

# -------------------------------------------------------------
# ETAPA 2: Run (Execução da aplicação)
# -------------------------------------------------------------
FROM eclipse-temurin:17-jre-alpine

# Define a pasta de trabalho
WORKDIR /app

# Copia APENAS o arquivo .jar gerado na etapa anterior (mais leve)
# O wildcard *.jar pega qualquer nome que o Maven gerar
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta padrão do Spring Boot
EXPOSE 8080

# Comando que roda a aplicação quando o container inicia
ENTRYPOINT ["java", "-jar", "app.jar"]