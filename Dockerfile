# Usa imagem oficial com Java 21 pré-instalado
FROM eclipse-temurin:21-jdk

# Define diretório de trabalho
WORKDIR /app

# Copia arquivos para dentro do container
COPY . .

# Dá permissão de execução ao mvnw
RUN chmod +x mvnw

# Faz o build do projeto
RUN ./mvnw clean package -DskipTests

# Define a porta (Render injeta via variável $PORT)
ENV PORT=8080

# Expõe a porta para fora do container
EXPOSE 8080

# Comando para rodar o app
CMD ["java", "-jar", "target/portalpdv-0.0.1-SNAPSHOT.jar"]
