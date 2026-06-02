# Stratec TI – Plataforma de Maturidade e Governança de TI

Sistema web para **avaliação da maturidade de TI** e **geração automática de documentos de governança corporativa**.

Acesse em: https://stratec-ti-frontend.onrender.com/

---

## 🚀 Novidades – Módulo de Governança (v2.0)

A versão 2.0 adiciona uma camada completa de **Governança e Planejamento de TI**, transformando o diagnóstico em documentos corporativos prontos para uso:

### Fluxo de Trabalho
```
QUESTIONÁRIO → DIAGNÓSTICO → PLANO 5W2H → MATRIZ DE RISCOS → PDTI
```

### Documentos Gerados Automaticamente
| Documento | Formato |
|---|---|
| Diagnóstico Completo de Maturidade | PDF |
| PDTI – Plano Diretor de TI (padrão ABNT) | PDF |
| Plano de Ação 5W2H | Excel |
| Matriz de Gestão de Riscos | Excel |

---

## 📌 Funcionalidades

- ✅ Autenticação e cadastro de usuários
- ✅ Cadastro de empresas
- ✅ Questionário de maturidade (32 questões, 7 categorias)
- ✅ Score geral e por categoria
- ✅ Dashboard executivo
- ✅ Relatório de diagnóstico com gráficos (Radar, Bar)
- ✅ **[NOVO]** Geração automática do Plano 5W2H
- ✅ **[NOVO]** Geração automática da Matriz de Riscos
- ✅ **[NOVO]** Motor de recomendações por nível de maturidade
- ✅ **[NOVO]** PDTI completo com estrutura ABNT
- ✅ **[NOVO]** Exportação PDF Diagnóstico Completo
- ✅ **[NOVO]** Exportação PDF PDTI formal
- ✅ **[NOVO]** Exportação Excel 5W2H
- ✅ **[NOVO]** Exportação Excel Matriz de Riscos

---

## 🛠️ Stack Tecnológica

- **Backend:** Java 17 + Spring Boot 3.3.5 + Spring Data JPA
- **Banco de Dados:** PostgreSQL 16
- **Frontend:** HTML5 + CSS3 + JavaScript (Vanilla)
- **Bibliotecas frontend:** Chart.js, jsPDF, html2canvas, SheetJS
- **Deploy:** Render (backend + frontend separados) ou Docker Compose

---

## 🗄️ Banco de Dados

### Tabelas originais
- `usuario`, `empresa`, `questao`, `resposta`, `servico_ti`, `incidente_ti`

### Tabelas adicionadas (v2.0)
- `pdti_config` – Configuração do PDTI (missão, visão, objetivos, vigência)
- `plano_acao_5w2h` – Plano de Ação 5W2H automático e manual
- `gestao_riscos` – Matriz de Gestão de Riscos
- `roadmap_tecnologico` – Roadmap tecnológico por trimestre

---

## 🔧 Variáveis de Ambiente (Render)

```
SPRING_DATASOURCE_URL=jdbc:postgresql://<host>:<port>/<database>
SPRING_DATASOURCE_USERNAME=<usuario>
SPRING_DATASOURCE_PASSWORD=<senha>
PORT=8080
```

---

## 🐳 Executar com Docker Compose (local)

```bash
docker-compose up --build
```

- Frontend: http://localhost
- Backend: http://localhost:8080
- Banco: localhost:5432

**Credenciais padrão:**
- admin@stratec.com / 123456
- avaliador@stratec.com / 123456

---

## 📡 Endpoints de API

### Existentes (preservados)
- `POST /api/auth/login`
- `GET/POST /api/empresas`
- `GET /api/questoes`
- `POST /api/respostas`
- `GET /api/relatorios/empresa/{id}`
- `GET /api/dashboard`

### Novos – Módulo de Governança
- `POST /api/governanca/gerar/{empresaId}` – Geração automática completa
- `GET/POST /api/governanca/pdti/{empresaId}` – PDTI config
- `GET/POST /api/governanca/plano/{empresaId}` – Plano 5W2H
- `DELETE /api/governanca/plano/{id}` – Remover item do plano
- `GET/POST /api/governanca/riscos/{empresaId}` – Matriz de riscos
- `DELETE /api/governanca/riscos/{id}` – Remover risco

---

## 📁 Estrutura do Projeto

```
PROJETO/
├── maturidade-ti/          # Backend Spring Boot
│   └── src/main/java/com/maturidade/ti/
│       ├── controller/     # REST Controllers
│       ├── service/        # Lógica de negócio
│       ├── repository/     # JPA Repositories
│       ├── model/          # Entidades JPA
│       └── dto/            # Data Transfer Objects
├── frontend/               # Frontend estático (nginx)
│   ├── governanca.html     # [NOVO] Módulo de Governança
│   ├── governanca.js       # [NOVO] Lógica + exportações
│   └── ...
├── db/
│   └── init.sql            # Schema + dados iniciais
└── docker-compose.yml
```
