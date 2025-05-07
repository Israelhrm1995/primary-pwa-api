# Etapa 1: Build com Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copia os arquivos do projeto (exceto o .dockerignore)
COPY . .

# Executa o build do projeto
RUN mvn clean package -DskipTests

# Etapa 2: Runtime com Java 21
FROM eclipse-temurin:21-jre

# Define o diretório de trabalho no container final
WORKDIR /app

# Copia o JAR gerado da etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta padrão do Spring Boot (ajuste se necessário)
EXPOSE 8080

# Comando para iniciar o aplicativo
ENTRYPOINT ["java", "-jar", "app.jar"]
