## API de Gerenciamento de Abastecimentos - Documentação

Este documento descreve como baixar, instalar e executar a API REST para gerenciamento de abastecimentos de veículos. A API foi desenvolvida utilizando Spring Boot, JPA, Clean Architecture e outras tecnologias.


**1. Tecnologias Utilizadas:**

* **Backend:** Spring Boot 3.3.7 com Spring JPA
* **Banco de Dados:** H2 (in-memory)
* **Arquitetura:** Clean Architecture
* **Linguagem:** Java 17
* **Gestão de Dependências:** Maven
* **Mapeamento:** MapStruct
* **Validação:** Validações customizadas com anotações do Bean Validation e tratamento personalizado de exceções. 
* **Teste:** JUnit (ou similar, dependendo da configuração do projeto)


**2. Pré-requisitos:**

* Java Development Kit (JDK) 17 instalado e configurado na variável de ambiente `JAVA_HOME`.
* Maven instalado e configurado.
* Git instalado (para clonar o repositório).
* IDE Java (e.g., IntelliJ IDEA, Eclipse) (Recomendado)
* Docker e Docker Compose (opcional, para execução em contêiner).


**3. Clonando o Repositório:**

Clone o repositório do GitHub usando o seguinte comando:

```bash
git clone https://github.com/MarceloMacedoDev/fuelmanagement.git
cd fuelmanagement/abastecimento-api
```

**4. Execução com IDE (Recomendado):**

1. **Importe o projeto:** Importe o projeto `abastecimento-api` em sua IDE favorita. Certifique-se de que o JDK 17 esteja configurado.  O projeto utiliza Maven para gerenciamento de dependências.
2. **Configure o `application.properties` (se necessário):** O arquivo `src/main/resources/application.properties` contém as configurações do aplicativo. As configurações padrão usam um banco de dados H2 em memória.  Para um banco de dados persistente, você precisará alterar a URL. Exemplo:


```properties
spring.application.name=abastecimento-api
server.port=8088
spring.datasource.url=jdbc:h2:mem:abastecimento-api  # Alterar para sua URL do banco de dados (ex: jdbc:postgresql://localhost:5432/seu_banco)
spring.datasource.username=sa                     # Alterar para seu username do banco de dados
spring.datasource.password=                       # Alterar para sua senha do banco de dados
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.datasource.initialize=true
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update # Importante para criação automática das tabelas (use com cuidado em produção)
```

3. **Execute o aplicativo:** Execute a classe `AbastecimentoApiApplication`. A API estará disponível em `http://localhost:8088`.  Verifique o console para mensagens de log e erros.


**5. Execução com Docker e Docker Compose:**

Este método requer o arquivo `Dockerfile` e `docker-compose.yml`  na raiz do projeto `abastecimento-api`.


1. **Construa a imagem Docker:**  Execute o comando `docker-compose up --build` na pasta raiz do projeto.  Isso constroi a imagem Docker baseada no `Dockerfile` e inicia o contêiner usando o `docker-compose.yml`.
2. **Acessando a API:** A API estará acessível em `http://localhost:8088` após a inicialização do contêiner.
        
**Dockerfile (Exemplo):**

```dockerfile
# Use a slim base image to reduce the size of the final image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml file first to optimize the build process
COPY pom.xml .

# Install Maven.  Using a specific version is recommended for reproducibility.
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Copy the rest of the application code
COPY . .

# Copy the application.properties file
COPY src/main/resources/application.properties .

# Build the Spring Boot application. Skip tests during the image build to speed up the process.
# Tests should be run separately in a CI/CD pipeline or locally during development.
RUN mvn clean package -DskipTests

# Expose the port used by the Spring Boot application
EXPOSE 8088
ENV SPRING_DATASOURCE_URL=jdbc:h2:mem:abastecimento-api

# Define the command to run the application 

# Define the command to run the application
# CMD ["java", "-jar", "target/abastecimento-api-0.0.1-SNAPSHOT.jar"]
CMD ["java", "-jar", "-Dspring.datasource.url=jdbc:h2:mem:abastecimento-api", "target/abastecimento-api-0.0.1-SNAPSHOT.jar"]
```

**docker-compose.yml (Exemplo):**

```yaml
version: "3.9"

  api:
    build: ./abastecimento-api
    ports:
      - "8088:8088"
    networks:
      - abastecimento-net

    networks:
    abastecimento-net:
        driver: bridge
```

**6. Endpoints da API:**

* **GET /abastecimentos:** Retorna uma lista de todos os abastecimentos. Suporta paginação e filtro por placa do veículo através de parâmetros de query: `?pagina=0&tamanhoPagina=5&placa=AAA-1234`.  A paginação e o tamanho da página devem ser tratados pela API.
* **POST /abastecimentos:** Adiciona um novo abastecimento. O corpo da requisição deve ser um JSON com os dados do abastecimento (ver DTO abaixo).
* **DELETE /abastecimentos/{id}:** Remove um abastecimento específico pelo seu ID.


**7. DTO (Data Transfer Object) - Exemplo:**

```json
{
  "placa": "AAA-1234",
  "quilometragem": 10000,
  "dataHora": "2024-10-27T10:00:00",
  "valorTotal": 100.00
}
```

**8. Validações:**

* **Quilometragem:** Deve ser maior que a quilometragem do último abastecimento para o mesmo veículo.
* **Placa:** Deve ser um formato válido (AAA-1234 ou ABC1D23) -  Validação de formato de placa deve ser implementada.
* **Data e Hora:** Não pode ser uma data futura.
* **Valor Total:** Deve ser maior que zero.
* **Campos Obrigatórios:** Todos os campos são obrigatórios.


**9.  Estrutura do Projeto (Clean Architecture):**

A estrutura do projeto se baseia em Clean Architecture.

* **domain:** Entidades de negócio (`Abastecimento`) e regras de negócio.
* **application:** Casos de uso (`GerenciarAbastecimentoUseCase`) que orquestram a lógica de negócio.
* **infrastructure:** Camada de acesso a dados (`AbastecimentoRepository`) e mapeadores (`AbastecimentoMapper`).
* **web:** Controladores REST (`AbastecimentoController`), DTOs (`AbastecimentoDTO`), e filtros (`CorsFilter`).


**10. Tratamento de Erros:**

A API retorna códigos HTTP apropriados (ex: 400 Bad Request para validações, 500 Internal Server Error para erros internos).  Mensagens de erro detalhadas devem ser incluídas nas respostas.  Um `GlobalExceptionHandler` pode ser implementado para lidar com exceções e retornar respostas padronizadas.


**11. Considerações Adicionais:**

* Para acessar o console do H2 (apenas para o banco em memória), acesse `http://localhost:8088/h2-console` após executar a aplicação.  Para um banco de dados externo, utilize o console de administração do seu banco de dados.
* Testes unitários e de integração são altamente recomendados para garantir a qualidade do código.

 
