# Calculadora Canabinoide - Projeto Full-Stack

![Java](https://img.shields.io/badge/Java-17-blue)![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-green)![React](https://img.shields.io/badge/React-18-blue?logo=react)![MySQL](https://img.shields.io/badge/MySQL-8.0-orange?logo=mysql)

Este é um projeto de portfólio que demonstra a criação de uma aplicação web full-stack, utilizando uma arquitetura moderna com Java/Spring Boot no backend e React no frontend.

## 🚀 Sobre o Projeto

A **Calculadora Canabinoide** é uma ferramenta desenvolvida para auxiliar médicos e pacientes no controle da dosagem de tratamentos à base de cannabis medicinal. A aplicação permite que o usuário selecione um produto com composição específica, informe a quantidade de gotas desejada e receba como retorno a dosagem exata em miligramas de cada canabinoide (CBD, THC, etc.).

O objetivo é automatizar e simplificar um processo que hoje é feito manualmente, aumentando a segurança e precisão das prescrições médicas.

### 🤖 Novo: Assistente de IA Integrado

Agora a aplicação conta com um **Chat Assistant baseado em GPT-3.5-turbo** que responde perguntas sobre cannabis medicinal. O assistente é acessível via um widget flutuante no canto inferior direito e oferece informações educacionais sobre:
- Tipos de canabinoides (CBD, THC, etc.)
- Efeitos terapêuticos e colaterais
- Interações medicamentosas
- Uso medicinal responsável

**Features:**
- ✅ Chat flutuante discreto (não interfere com calculadora)
- ✅ Histórico de conversa
- ✅ Cache de respostas (24h) para economizar custos
- ✅ Error handling robusto
- ✅ Integração OpenFeign + OpenAI API

## 🏛️ Estrutura do Repositório (Monorepo)

Este repositório utiliza uma abordagem de monorepo para gerenciar os dois componentes principais da aplicação:

-   `./backend`: Contém a API RESTful construída com **Java e Spring Boot**. É responsável por todas as regras de negócio, lógica de cálculo, comunicação com o banco de dados e gerenciamento dos produtos.
-   `./frontend`: Contém a interface do usuário (UI) construída com **React**. É uma Single Page Application (SPA) que consome os endpoints da API do backend para fornecer uma experiência interativa e responsiva ao usuário.

## 💻 Tecnologias Utilizadas

| Camada  | Tecnologia                                           |
| :------ | :--------------------------------------------------- |
| **Backend** | Java 17, Spring Boot 3, Spring Data JPA, Hibernate |
| **IA Integration** | OpenFeign, OpenAI API (GPT-3.5-turbo), Spring Cache |
| **Frontend**| React 18, JavaScript (ES6+), HTML5, CSS3          |
| **Banco de Dados**| MySQL com migrações gerenciadas por Flyway         |
| **Build**   | Maven (Backend) e npm (Frontend)                   |

## ⚙️ Como Executar o Projeto

Siga os passos abaixo para executar a aplicação completa em seu ambiente local.

### Pré-requisitos
- JDK 17 ou superior
- Apache Maven 3.8+
- Node.js 16+ e npm
- MySQL Server 8.0+
- **OpenAI API Key** (obtida em https://platform.openai.com/account/api-keys)

### ⚡ Configuração Rápida (10 minutos)

Para uma abordagem mais rápida com todos os passos, consulte [GUIA_EXECUCCAO_COMPLETO.md](./GUIA_EXECUCCAO_COMPLETO.md)

### 1. Configurando o Banco de Dados
- Crie um banco de dados no MySQL com o nome `calculadora_db`.
- Atualize as credenciais do banco no arquivo `/backend/src/main/resources/application.properties` se necessário:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/calculadora_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

### 2. Configurando a Chave OpenAI API (Novo!)

**Obtenha sua chave em:** https://platform.openai.com/account/api-keys

**Windows (PowerShell) - Recomendado:**
```powershell
[Environment]::SetEnvironmentVariable("OPENAI_API_KEY", "sk-seu-token-aqui", "User")
# Feche e reabra o terminal para aplicar
```

**Linux/macOS (Bash):**
```bash
export OPENAI_API_KEY="sk-seu-token-aqui"
# Para permanente, adicione ao ~/.bashrc ou ~/.zshrc
```

### 3. Executando o Backend (API)
Abra um terminal na raiz do projeto e execute os seguintes comandos:

```bash
# Navegue para a pasta do backend
cd backend

# Use o Maven Wrapper para iniciar a aplicação
./mvnw spring-boot:run
```
O servidor do backend estará rodando em `http://localhost:8080`. O Flyway criará as tabelas automaticamente na primeira inicialização.

### 3. Executando o Frontend (UI)
Abra um **novo terminal** na raiz do projeto e execute os seguintes comandos:

```bash
# Navegue para a pasta do frontend
cd frontend

# Instale as dependências do projeto (apenas na primeira vez)
npm install

# Inicie o servidor de desenvolvimento do React
npm start
```
A interface da aplicação estará acessível em `http://localhost:3000` no seu navegador.

---
<<<<<<< HEAD
*Projeto desenvolvido como parte do meu portfólio de programação. Sinta-se à vontade para explorar o código!*
=======

## 📚 Documentação Completa

| Documento | Descrição |
|-----------|-----------|
| [GUIA_EXECUCCAO_COMPLETO.md](./GUIA_EXECUCCAO_COMPLETO.md) | Guia passo-a-passo para rodar tudo em 10 minutos |
| [CHAT_AGENT_SETUP.md](./backend/CHAT_AGENT_SETUP.md) | Setup detalhado da integração OpenAI no backend |
| [FASE_3_CHAT_WIDGET.md](./FASE_3_CHAT_WIDGET.md) | Arquitetura e componentes do chat widget frontend |
| [ANALISE_CODIGO_COMPLETA.md](./ANALISE_CODIGO_COMPLETA.md) | Análise completa do projeto (backend + frontend) |
| [ANALISE_COMPATIBILIDADE.md](./ANALISE_COMPATIBILIDADE.md) | Verificação de compatibilidade e versões |
| [ROADMAP_FASES_4_5_6.md](./ROADMAP_FASES_4_5_6.md) | Roadmap futuro: testes, segurança e deployment |

---

## 🧪 Testando a Aplicação

### Teste da Calculadora
1. Abra http://localhost:3000
2. Selecione um produto no dropdown
3. Digite a quantidade de gotas
4. Clique em "Calcular Dosagem"

### Teste do Chat Assistant (NOVO!)
1. Clique no botão 💬 no canto inferior direito
2. Digite: "O que é CBD?"
3. Aguarde a resposta da IA

### Teste via Postman
```
POST http://localhost:8080/api/v1/chat/ask
Content-Type: application/json

{
  "pergunta": "Qual é a diferença entre CBD e THC?"
}
```

---

*Projeto desenvolvido como parte do meu portfólio de programação. Sinta-se à vontade para explorar o código!*
>>>>>>> 2252df4 (doc: Atualizar README e adicionar documentação completa da feature IA)
