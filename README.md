# Stratec TI

Sistema web para **avaliação da maturidade da Tecnologia da Informação em empresas**, desenvolvido com foco em **governança, gestão, segurança, processos e apoio à tomada de decisão**.

O objetivo da aplicação é permitir que uma empresa responda a um questionário estruturado, obtenha um **score de maturidade segmentado e geral**, visualize os resultados em **gráficos e relatórios**, e utilize essas informações para apoiar a evolução da sua área de TI.

Acesse em: https://stratec-ti-frontend.onrender.com/
---

## 📌 Visão Geral

O **Stratec TI** foi desenvolvido como uma solução acadêmica e prática para apoiar o diagnóstico da maturidade de TI em organizações, com base em pilares como:

- Governança de TI
- Segurança da Informação
- Infraestrutura
- Gestão de Serviços
- Desenvolvimento de Software
- Gestão de Dados
- Processos
- Pessoas
- Tecnologia

A aplicação transforma respostas estruturadas em um **diagnóstico claro, visual e interpretativo**, permitindo que a empresa identifique:

- seus pontos fortes
- áreas vulneráveis
- nível atual de maturidade
- prioridades de melhoria

---

## 🎯 Objetivo do Projeto

Permitir que empresas realizem uma **autoavaliação da sua maturidade em TI** por meio de um sistema web simples, intuitivo e visual, gerando:

- **score por categoria**
- **score geral**
- **nível de maturidade**
- **gráficos de apoio**
- **relatório executivo**
- **plano de ação orientativo**

---

## 🧠 Problema que o Sistema Resolve

Muitas empresas utilizam tecnologia diariamente, mas **não sabem o quão madura está sua área de TI**.

Isso gera cenários como:

- TI atuando apenas de forma reativa
- ausência de políticas e processos definidos
- baixa segurança da informação
- falta de indicadores e controle
- infraestrutura sem planejamento
- decisões sem base em diagnóstico real

O **Stratec TI** foi criado justamente para transformar esse cenário em uma análise prática, acessível e orientada à melhoria contínua.

---

## 🚀 Funcionalidades

### 👤 Acesso e Usuários
- Cadastro de usuário
- Login
- Logout
- Persistência de sessão local
- Navegação autenticada

### 🏢 Gestão de Empresas
- Cadastro de empresas
- Associação de avaliações por empresa
- Organização dos dados por contexto empresarial

### 📝 Questionário de Maturidade
- Aplicação de questionário estruturado
- Respostas em escala de maturidade (0 a 5)
- Campo obrigatório de **categoria percebida da resposta**
- Destaque visual da categoria selecionada
- Validação de preenchimento antes da finalização

### 📊 Dashboard
- Visualização de indicadores principais
- Quantidade de avaliações realizadas
- Quantidade de relatórios gerados
- Acesso rápido às áreas do sistema

### 📄 Relatório de Maturidade
- Score geral da empresa
- Score por categoria
- Classificação do nível de maturidade
- Categoria mais vulnerável
- Conclusão interpretativa
- Recomendações
- Evidências registradas
- Plano de ação

### 📈 Visualização Gráfica
- Gráfico Radar de Maturidade
- Gráfico de Barras por Categoria
- Indicadores visuais para apoio à leitura do diagnóstico

### 🧾 Exportação PDF
- Geração de relatório em PDF
- Layout visual refinado
- Captura em alta resolução
- Compatível com uso acadêmico e apresentação

### 🌐 Publicação Online
- Compatível com execução local via Docker
- Compatível com publicação online via Render
- Acesso via navegador e QR Code
- Responsivo para mobile

---

## 🏗️ Arquitetura do Projeto

O projeto foi dividido em 3 partes principais:

- **Frontend**: interface web (HTML, CSS, JavaScript)
- **Backend**: API REST (Java + Spring Boot)
- **Banco de Dados**: PostgreSQL

---

## 🧰 Tecnologias Utilizadas

### Frontend
- HTML5
- CSS3
- JavaScript (Vanilla JS)
- Chart.js
- html2canvas
- jsPDF

### Backend
- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Bean Validation

### Banco de Dados
- PostgreSQL

### Infraestrutura / Deploy
- Docker
- Docker Compose
- Render

### Ferramentas de Apoio
- VS Code
- DBeaver
- Git
- GitHub

---

## 📁 Estrutura do Projeto

```bash
STRATEC-TI/
├── docker-compose.yml
├── README.md
├── db/
│   ├── Dockerfile
│   └── init.sql
├── maturidade-ti/
│   ├── Dockerfile
│   ├── pom.xml
│   ├── src/
│   │   └── main/
│   │       ├── java/com/maturidade/ti/
│   │       │   ├── config/
│   │       │   ├── controller/
│   │       │   ├── dto/
│   │       │   ├── model/
│   │       │   ├── repository/
│   │       │   ├── service/
│   │       │   └── MaturidadeTiApplication.java
│   │       └── resources/
│   │           ├── application.properties
│   │           └── data.sql
├── frontend/
│   ├── index.html
│   ├── login.html
│   ├── cadastro.html
│   ├── dashboard.html
│   ├── empresas.html
│   ├── questionario.html
│   ├── relatorio.html
│   ├── style.css
│   ├── auth.js
│   ├── app.js
│   ├── relatorio.js
│   └── assets/
│       ├── logo.png
│       ├── favicon.png
│       └── ...
