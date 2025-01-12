# API REST de Gerenciamento de Abastecimentos de Veículos

Este projeto implementa uma API REST para gerenciar abastecimentos de veículos, utilizando Spring Boot, Clean Architecture, e um banco de dados H2.  O projeto inclui validações, tratamento de erros, logs, e um filtro de pesquisa por placa do veículo.

## Tecnologias Utilizadas

* **Backend:** Spring Boot 3.3.7 com Spring JPA
* **Banco de Dados:** H2
* **Arquitetura:** Clean Architecture
* **Java:** 17
* **Mapeamento de Objetos:** MapStruct
* **Validação:** Bean Validation (anotações)


## Pré-requisitos

* Java 17 instalado e configurado.  Verifique com `java -version`.
* Maven ou Gradle instalado.  Verifique com `mvn -version` ou `gradle -version`.
* IDE Java (ex: IntelliJ IDEA, Eclipse).  Recomendado: IntelliJ IDEA com Spring Boot plugin.
* Docker (opcional, mas recomendado para execução simplificada).  Verifique com `docker version`.
* Docker Compose (opcional, mas recomendado para execução simplificada, especialmente se o frontend estiver incluído). Verifique com `docker-compose version`.


## Instalação e Execução

**1. Clonando o Repositório:**

Clone o repositório Git:

```bash
git clone https://github.com/MarceloMacedoDev/fuelmanagement.git
cd fuelmanagement/abastecimento-api
```

**2. Executando com IDE (IntelliJ IDEA recomendado):**

* Importe o projeto como um projeto Maven ou Gradle na sua IDE.  (File -> New -> Project from Existing Sources... -> selecione o arquivo `pom.xml` ou `build.gradle`).
* Certifique-se de que o JDK 17 esteja configurado como o JDK do projeto. (File -> Project Structure -> Project -> Project SDK).
* Execute a aplicação `AbastecimentoApiApplication`.  (Procure por `AbastecimentoApiApplication.main()` e execute).
* A API estará disponível em `http://localhost:8088`.  O console H2 estará disponível em `http://localhost:8088/h2-console`.  As credenciais para o H2 são o usuário `sa` e senha em branco.


**3. Executando com Docker:**

* Navegue até a pasta `abastecimento-api`.
* Execute o comando: `mvn clean package -DskipTests` para compilar o projeto (a opção `-DskipTests` ignora os testes para acelerar a compilação, execute os testes separadamente).
* Construa a imagem Docker:
```bash
docker build -t abastecimento-api .
```
* Execute a imagem Docker:
```bash
docker run -p 8088:8088 abastecimento-api
```
* A API estará disponível em `http://localhost:8088`.  Acesse o H2 console pelo navegador no endereço `http://localhost:8088/h2-console` com as credenciais padrão do H2 (usuário `sa`, senha em branco).  Note que o H2 está configurado como um banco de dados em memória, os dados serão perdidos após a parada do container.


**4. Executando com Docker Compose (recomendado):**

Este método é recomendado especialmente se um frontend também estiver incluído no repositório `fuelmanagement`.

* Certifique-se de ter o Docker Compose instalado.
* Navegue até a pasta raiz do projeto `fuelmanagement`.
* Execute o comando: `docker-compose up -d` para iniciar os serviços (`api` e `front`).
* A API estará disponível em `http://localhost:8088` e o frontend em `http://localhost:4520` (assumindo que o frontend esteja configurado para funcionar nessa porta). Pare os contêineres com `docker-compose down`.


## Endpoints da API

* **GET /abastecimentos:** Retorna uma lista de abastecimentos.  Suporta paginação e filtro por placa (parâmetros de query `pagina`, `tamanhoPagina`, e `placa`). Exemplo: `/abastecimentos?pagina=0&tamanhoPagina=10&placa=ABC-1234`

* **POST /abastecimentos:** Adiciona um novo abastecimento. O corpo da requisição deve conter os dados do abastecimento em formato JSON, incluindo `veiculoPlaca`, `quilometragem`, `dataHora`, e `valorTotal`.

* **DELETE /abastecimentos/{id}:** Remove um abastecimento pelo seu ID.


## Validações

As seguintes validações são aplicadas ao criar um novo abastecimento:

* **Quilometragem:** Deve ser maior que a quilometragem do último abastecimento para o mesmo veículo.
* **Placa:** Deve seguir o formato AAA-1234 ou ABC1D23 (validação de regex).
* **Data e Hora:** Não pode ser uma data futura.
* **Valor Total:** Deve ser maior que zero.
* **Campos Obrigatórios:** Todos os campos são obrigatórios.


## Tratamento de Erros

A API retorna códigos de status HTTP apropriados para indicar sucesso ou falha nas requisições.  Mensagens de erro detalhadas são incluídas no corpo da resposta em caso de falha.  Um `GlobalExceptionHandler` centralizado trata as exceções.
 
##  Estrutura do Projeto (Clean Architecture)

A aplicação segue a arquitetura limpa (Clean Architecture), separando as camadas de domínio, aplicação e infraestrutura:

* **Domain:** Contém as entidades (`Abastecimento`) e as regras de negócio (lógica de validação).
* **Application:** Contém os use cases (`GerenciarAbastecimentoUseCase`) que orquestram a lógica de negócio.
* **Infrastructure:** Contém os repositórios (`AbastecimentoRepository`), mapeadores (`AbastecimentoMapper`), e implementações de acesso ao banco de dados.
* **Web:** Contém os controllers (`AbastecimentoController`) e DTOs (`AbastecimentoDTO`).


##  `application.properties`

```properties
spring.application.name=abastecimento-api
server.port=8088
spring.datasource.url=jdbc:h2:mem:abastecimento-api
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.datasource.initialize=true
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect
spring.mvc.throw-exception-if-no-handler-found=true #Para retornar 404 em rotas inexistentes
```


## Contribuições

Contribuições são bem-vindas!  Por favor, abra issues ou pull requests no repositório do GitHub.


## Considerações
 
* Para uma melhor experiência de desenvolvimento, é altamente recomendável usar uma IDE com suporte a Spring Boot.
* Lembre-se de executar os testes unitários e de integração antes de fazer deploy da aplicação.

 
