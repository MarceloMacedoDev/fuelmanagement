# Gerenciamento de Abastecimentos - Frontend

Este repositório contém o frontend de um aplicativo para gerenciamento de abastecimentos de veículos, desenvolvido com Angular 16.20 e Angular Material. O projeto utiliza a arquitetura Clean Architecture para organização e manutenabilidade do código.

## Pré-requisitos

Antes de começar, certifique-se de ter instalado o seguinte:

* **Node.js:** Versão 16 ou superior. Verifique sua versão executando `node -v` no seu terminal.  Caso necessário, baixe e instale a versão correta em [https://nodejs.org/](https://nodejs.org/).  É **essencial** usar a versão 16 para compatibilidade com o projeto.
* **npm (ou yarn):** O gerenciador de pacotes Node.js. Geralmente instalado junto com o Node.js. Verifique sua versão com `npm -v` ou `yarn -v`. Recomendamos usar `npm` para este projeto.
* **Git:** Para clonar o repositório. Baixe e instale em [https://git-scm.com/](https://git-scm.com/).


## Instalação e Execução

**Passo 1: Clonando o Repositório**

Abra seu terminal ou Git Bash e execute os seguintes comandos:

```bash
git clone https://github.com/MarceloMacedoDev/fuelmanagement.git
cd fuelmanagement/abastecimento-front
```

Este comando clona o repositório do GitHub para sua máquina local e navega para a pasta do projeto frontend.

**Passo 2: Instalando as Dependências**

Após clonar o repositório, navegue até a pasta `abastecimento-front` e instale as dependências do projeto usando o npm:

```bash
npm install
```

Este comando irá baixar e instalar todas as bibliotecas e pacotes necessários para o funcionamento do aplicativo Angular.  Certifique-se de ter uma conexão estável com a internet durante este processo.


**Passo 3: Executando o Aplicativo**

Após a instalação das dependências, execute o aplicativo com o seguinte comando:

```bash
npm start
```

Este comando irá iniciar um servidor de desenvolvimento local. O aplicativo estará acessível através do navegador em `http://localhost:4200/`.  Se o servidor não iniciar, verifique se não há conflitos de porta ou problemas com as dependências instaladas.


**Resolvendo Problemas Comuns**

* **Erro de Versão do Node.js:** Se você receber um erro relacionado à versão do Node.js, certifique-se de ter a versão 16 ou superior instalada.  Use um gerenciador de versões como `nvm` (Node Version Manager) para facilitar a gestão de diferentes versões do Node.js.

* **Erros de Dependências:** Se ocorrerem erros durante a instalação das dependências, verifique a sua conexão com a internet e tente executar `npm install` novamente.  Se o problema persistir, examine o arquivo `package.json` e `package-lock.json` para identificar possíveis conflitos.

* **Erro de Porta:** Se a porta 4200 estiver em uso, você pode alterar a porta no arquivo `angular.json` e reiniciar o servidor.


## Estrutura do Projeto

O projeto utiliza uma estrutura baseada na arquitetura Clean, com separação clara de responsabilidades:

```
src/
├── app/
│   ├── core/ // Módulos e serviços centrais, independentes de funcionalidades específicas.
│   │   ├── models/ // Modelos de dados.
│   │   └── services/ // Serviços para acesso a dados e lógica de negócio.
│   ├── features/ // Módulos contendo funcionalidades específicas.
│   │   ├── abastecimentos/ // Módulo para gerenciamento de abastecimentos.
│   │   │   ├── components/ // Componentes específicos para o módulo de abastecimentos.
│   │   │   │   ├── abastecimento-list/ // Componente para listar abastecimentos.
│   │   │   │   │   ├── abastecimento-list.component.html
│   │   │   │   │   └── abastecimento-list.component.ts
│   │   │   │   └── add-abastecimento/ // Componente para adicionar abastecimentos.
│   │   │   │       ├── add-abastecimento.component.html
│   │   │   │       └── add-abastecimento.component.ts
│   │   └── shared/ // Componentes, diretivas e pipes compartilhados entre módulos.
│   │       ├── components/
│   │       ├── directives/
│   │       └── pipes/
│   ├── app-routing.module.ts // Módulo de rotas.
│   ├── app.component.html // Template principal da aplicação.
│   ├── app.component.ts // Componente principal da aplicação.
│   └── app.module.ts // Módulo principal da aplicação.
├── assets/ // Recursos estáticos (imagens, ícones etc.).
├── environments/ // Configurações de ambiente (development, production).
├── index.html // Arquivo HTML principal.
├── main.ts // Ponto de entrada da aplicação.
├── polyfills.ts // Polyfills para compatibilidade com navegadores antigos.
├── styles.css // Estilos CSS globais.
├── tsconfig.app.json // Configurações do TypeScript para a aplicação.
├── tsconfig.json // Configurações do TypeScript para o projeto.
├── angular.json // Arquivo de configuração do Angular CLI.
├── package.json // Arquivo de gerenciamento de dependências.
└── README.md // Este arquivo.
```

## Funcionalidades

* **Tela Principal de Abastecimentos:** Lista os abastecimentos com paginação (5, 10 e 15 registros por página), um filtro de pesquisa dinâmica por placa do veículo, opção de remoção com confirmação e um link para adicionar novos abastecimentos.

* **Página de Adição de Abastecimento:** Campos para data/hora (obrigatório, com validação para datas futuras), placa do veículo (obrigatório, com validação de formato), quilometragem (obrigatório, maior que o último abastecimento do mesmo veículo) e valor total (obrigatório, maior que zero). Validações visuais são exibidas abaixo de cada campo, e o botão "Salvar" é habilitado apenas quando todas as validações são atendidas.

## Tecnologias Utilizadas

* Angular 16.20
* Angular Material
* Node.js 16 (**essencial**)


## Contribuições

Contribuições são bem-vindas!  Crie um *pull request* para propor alterações.  Antes de contribuir, por favor, leia as [Diretrizes de Contribuição](CONTRIBUTING.md) (crie este arquivo se ele não existir, com as suas próprias diretrizes).

