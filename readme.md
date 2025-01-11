# Gerenciamento de Abastecimentos - Fuel Management

Este projeto consiste em um sistema web para gerenciar abastecimentos de veículos, utilizando uma arquitetura *Clean Architecture*, com validações robustas no frontend e backend, e um filtro de pesquisa dinâmica por placa do veículo.

## Visão Geral

O sistema foi desenvolvido utilizando as seguintes tecnologias:

* **Frontend:** Angular 16.20 com Angular Material
* **Backend:** Spring Boot 3.x com Spring JPA, Spring Web, Spring Data JPA, Hibernate (implícito pelo Spring Data JPA), Jackson (implícito pelo Spring Boot para serialização/deserialização JSON), H2 Database
* **Banco de Dados:** H2 Database
* **Arquitetura:** Clean Architecture

O projeto está disponível no GitHub: [https://github.com/MarceloMacedoDev/fuelmanagement.git](https://github.com/MarceloMacedoDev/fuelmanagement.git)

## Funcionalidades

### Frontend

* **Tela Principal de Abastecimentos:**
    * Lista todos os abastecimentos cadastrados com paginação (5, 10 e 15 registros).  A paginação é implementada via requisições ao backend, enviando parâmetros `pagina` e `tamanhoPagina`.
    * Filtro de pesquisa dinâmica por placa do veículo. A pesquisa é realizada via requisições assíncronas ao backend, atualizando a lista em tempo real conforme o usuário digita.
    * Opção para remover abastecimentos com confirmação utilizando um modal de confirmação do Angular Material. A remoção é feita via requisição DELETE ao backend.
    * Link ou botão para navegar até a página de adição de abastecimento utilizando o Angular Router.

* **Página de Adição de Abastecimento:**
    * Campos para:
        * Data e Hora (obrigatório, validação para datas futuras utilizando Angular Reactive Forms e validações customizadas).
        * Placa do Veículo (obrigatório, validação de formato AAA-1234 ou ABC1D23 utilizando Angular Reactive Forms e expressões regulares).
        * Quilometragem (obrigatório, maior que o último abastecimento para o mesmo veículo. Esta validação é feita via requisição ao backend antes de salvar).
        * Valor Total do Abastecimento (obrigatório, maior que zero utilizando Angular Reactive Forms e validações customizadas).
    * Validações visuais com mensagens de erro utilizando Angular Material's `errorStateMatcher`.
    * Botão de salvar desabilitado até que todas as validações sejam atendidas utilizando Angular Reactive Forms' `valid` property.


### Backend

* **Endpoints REST:**
    * `GET /abastecimentos`: Lista todos os abastecimentos, com suporte a filtro por placa e paginação (parâmetros de query: `pagina`, `tamanhoPagina`, `placa`).  A paginação é implementada utilizando `Pageable` do Spring Data JPA. O filtro por placa é aplicado utilizando `Specification` ou `Querydsl`.
    * `POST /abastecimentos`: Adiciona um novo abastecimento.  Inclui validações de dados (quilometragem, placa, data/hora, valor total) antes de persistir no banco de dados.
    * `DELETE /abastecimentos/{id}`: Remove um abastecimento.

* **Validações (Backend):**
    * Quilometragem: Maior que a do abastecimento anterior para o mesmo veículo.  Esta validação é feita no `AbastecimentoService`.
    * Placa: Formato válido (AAA-1234 ou ABC1D23).  Esta validação é feita utilizando expressões regulares no `AbastecimentoService` ou com anotações de validação do Bean Validation.
    * Data e Hora: Não pode ser futura.  Esta validação é feita no `AbastecimentoService`.
    * Valor Total: Maior que zero. Esta validação é feita no `AbastecimentoService`.
    * Todos os campos obrigatórios devem ser preenchidos.  Esta validação é feita no `AbastecimentoService` ou com anotações de validação do Bean Validation.

## Arquitetura (Clean Architecture)

O projeto segue o padrão Clean Architecture, dividido em três camadas:

1. **Domain Layer (Core):** Entidades de domínio e regras de negócio.  `Abastecimento.java` (entidade), `AbastecimentoService.java` (regras de negócio).  A entidade `Abastecimento` contém os atributos e métodos relacionados aos abastecimentos. O `AbastecimentoService` encapsula a lógica de negócio relacionada aos abastecimentos, como validações e regras de negócio.

2. **Application Layer:** Use cases que implementam a lógica de aplicação. `GerenciarAbastecimentoUseCase.java`.  Este use case coordena as operações relacionadas à manipulação de abastecimentos, interagindo com o `AbastecimentoService`.

3. **Infrastructure Layer:** Acesso ao banco de dados usando Spring Data JPA. `AbastecimentoRepository.java` (interface), `AbastecimentoRepositoryImpl.java` (implementação), `AbastecimentoMapper.java` (mapeamento DTO/Entidade). `AbastecimentoRepository` define a interface de acesso aos dados. `AbastecimentoRepositoryImpl` implementa a interface, utilizando Spring Data JPA para interagir com o banco de dados. `AbastecimentoMapper` mapeia os objetos de domínio (`Abastecimento`) para os objetos de transferência de dados (`AbastecimentoDTO`) usados na comunicação com o frontend.

## Docker Compose

O arquivo `docker-compose.yaml` define os serviços para o frontend e o backend, gerenciando seus containers e dependências.  A rede `abastecimento-net` permite a comunicação entre os containers. `depends_on` garante que o frontend só seja iniciado depois que o backend estiver disponível.

```yaml
version: '3'

services:
  front:
    build: ./abastecimento-front
    ports:
      - "4520:4520"
    networks:
      - abastecimento-net
    depends_on:
      - api

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

## Instalação e Execução

*(Instruções detalhadas, expandidas e melhoradas)*

### Pré-requisitos

* Java 17 JDK (ou superior)
* Node.js 16.20.2 (ou superior) e npm (ou yarn)
* Git
* Maven (para o backend)
* Docker e Docker Compose (opcional, mas recomendado)

### Sem Docker

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/MarceloMacedoDev/fuelmanagement.git
   ```

2. **Backend:**
    * Navegue até o diretório `abastecimento-api`.
    * Execute `mvn clean install` para construir o projeto. Isso irá baixar as dependências, compilar o código e gerar o arquivo JAR executável.
    * Execute `java -jar target/abastecimento-api-*.jar` para iniciar o Spring Boot.  O console H2 estará disponível em `http://localhost:8088/h2-console` (acesso com usuário `sa` e senha vazia).

3. **Frontend:**
    * Navegue até o diretório `abastecimento-front`.
    * Execute `npm install` para instalar as dependências.
    * Execute `ng serve` para iniciar o Angular. O aplicativo estará acessível em `http://localhost:4200`.


### Com Docker

1. **Clone o repositório:** (como acima)

2. **Construção e execução com Docker Compose:**
   ```bash
   cd fuelmanagement
   docker-compose up -d --build
   ```
   Isso irá construir as imagens Docker para o frontend e o backend e iniciar os containers em background.

3. **Acesso:**
   * Backend: `http://localhost:8088`
   * Frontend: `http://localhost:4520`
   * Console H2: `http://localhost:8088/h2-console` (usuário `sa`, senha vazia)

4. **Parar os containers:**
   ```bash
   docker-compose down
   ```
   
## Estrutura de Diretórios (detalhada com responsabilidades)

### Frontend (`abastecimento-front`)

*(Estrutura já detalhada anteriormente, mas adicionando responsabilidades)*

```
src/
├── app/
│   ├── core/          (Componentes, serviços e modelos compartilhados)
│   │   ├── models/    (Modelos de dados compartilhados)
│   │   └── services/  (Serviços para acessar a API do backend)
│   ├── features/      (Componentes específicos de funcionalidades)
│   │   └── abastecimentos/ (Módulo para gerenciar abastecimentos)
│   │       ├── components/ (Componentes específicos do módulo de abastecimentos)
│   │       │   ├── abastecimento-list/ (Componente para listar abastecimentos)
│   │       │   │   ├── abastecimento-list.component.html (Template)
│   │       │   │   └── abastecimento-list.component.ts (Lógica do componente)
│   │       │   └── add-abastecimento/ (Componente para adicionar abastecimentos)
│   │       │       ├── add-abastecimento.component.html (Template)
│   │       │       └── add-abastecimento.component.ts (Lógica do componente)
│   │       └── shared/ (Componentes, diretivas e pipes compartilhados dentro do módulo de abastecimentos)
│   │           ├── components/
│   │           ├── directives/
│   │           └── pipes/
│   ├── app-routing.module.ts (Configuração das rotas do aplicativo)
│   ├── app.component.html (Template do componente principal)
│   ├── app.component.ts (Lógica do componente principal)
│   └── app.module.ts (Módulo principal do aplicativo)
├── assets/ (Arquivos estáticos como imagens e ícones)
├── environments/ (Configurações de ambiente)
├── index.html (Arquivo HTML principal)
├── main.ts (Arquivo de inicialização do aplicativo)
├── polyfills.ts (Polyfills para compatibilidade)
├── styles.css (Estilos CSS do aplicativo)
├── tsconfig.app.json (Configurações do TypeScript)
├── tsconfig.json (Configurações do TypeScript)
└── tslint.json (Regras do TSLint)
```

### Backend (`abastecimento-api`)

*(Estrutura já detalhada anteriormente, mas adicionando responsabilidades)*

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── example/
│   │           ├── domain/       (Camada de domínio - regras de negócio)
│   │           │   ├── entity/     (Entidades - representam os dados do domínio)
│   │           │   │   └── Abastecimento.java (Entidade Abastecimento)
│   │           │   ├── repository/ (Interfaces para acesso a dados)
│   │           │   │   └── AbastecimentoRepository.java (Interface para acesso a dados de Abastecimento)
│   │           │   └── service/   (Serviços de domínio - lógica de negócio)
│   │           │       └── AbastecimentoService.java (Serviços para manipular abastecimentos)
│   │           ├── application/  (Camada de aplicação - orquestração de use cases)
│   │           │   └── usecase/    (Use Cases - casos de uso)
│   │           │       └── GerenciarAbastecimentoUseCase.java (Caso de uso para gerenciar abastecimentos)
│   │           ├── infrastructure/ (Camada de infraestrutura - implementação de acesso a dados e outros recursos externos)
│   │           │   ├── config/     (Configurações da aplicação)
│   │           │   │   └── AppConfig.java (Configuração do Spring)
│   │           │   ├── repository/ (Implementação do acesso a dados)
│   │           │   │   └── AbastecimentoRepositoryImpl.java (Implementação do repositório de Abastecimentos)
│   │           │   └── mapper/     (Mapeamento entre entidades e DTOs)
│   │           │       └── AbastecimentoMapper.java (Mapeamento entre Abastecimento e AbastecimentoDTO)
│   │           └── web/           (Camada web - controladores REST)
│   │               ├── controller/ (Controladores REST - endpoints)
│   │               │   └── AbastecimentoController.java (Controlador REST para Abastecimentos)
│   │               ├── dto/        (DTOs - objetos de transferência de dados)
│   │               │   └── AbastecimentoDTO.java (DTO para Abastecimento)
│   │               └── filter/     (Filtros para requisições)
│   │                   └── AbastecimentoFilter.java (Filtro para Abastecimentos)
│   └── resources/ (Recursos da aplicação)
│       └── application.properties (Propriedades de configuração)
└── pom.xml (Arquivo de configuração do Maven)
```


## Considerações Adicionais

* **Testes:** A inclusão de testes unitários e de integração (tanto no frontend quanto no backend) é fortemente recomendada para garantir a qualidade do software.
* **Segurança:** Em um ambiente de produção, é crucial implementar medidas de segurança adequadas, incluindo autenticação e autorização.
* **Escalabilidade:** Para maior escalabilidade, considere o uso de um banco de dados relacional mais robusto e soluções de balanceamento de carga.

 
