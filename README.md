# Stratec TI

Plataforma web para avaliação de maturidade da área de TI em empresas, com questionário estruturado, score geral e por categoria, relatório executivo, plano de ação e roadmap.

## Stack
- Frontend: HTML, CSS, JavaScript (Vanilla)
- Backend: Java 17 + Spring Boot
- Banco: PostgreSQL
- Infra: Docker / Docker Compose / Nginx

## Funcionalidades
- Cadastro e login de usuários
- Cadastro de empresas
- Cadastro e gestão de questões
- Aplicação de avaliação por categoria
- Registro de evidências e plano de ação manual
- Geração de relatório executivo com radar, score por categoria e roadmap
- Exportação em PDF

## Executar localmente
```bash
docker compose up -d --build
```

### URLs
- Frontend: http://localhost:8081
- Backend: http://localhost:8080

## Publicar online (Render)
### Backend
1. Suba o projeto para o GitHub.
2. No Render, crie um **Web Service** apontando para a pasta `maturidade-ti`.
3. Use o `Dockerfile` do backend.
4. Configure variáveis:
   - `SPRING_DATASOURCE_URL`
   - `SPRING_DATASOURCE_USERNAME`
   - `SPRING_DATASOURCE_PASSWORD`

### Banco
Crie um PostgreSQL no Render ou Railway e use as credenciais acima.

### Frontend
Publique a pasta `frontend` como Static Site ou Web Service.
No `frontend/auth.js`, troque `https://SEU-BACKEND.onrender.com/api` pela URL real do backend.

## QR Code
Depois do deploy, gere um QR Code da URL do frontend e use na apresentação.

## Estrutura
- `frontend/`: telas, estilos, scripts e assets
- `maturidade-ti/`: API REST em Spring Boot
- `db/`: inicialização do banco e perguntas


## Deploy online
O projeto continua compatível com `docker compose` local e também pode ser publicado em plataformas como Render ou Railway.

### Backend
- publique a pasta `maturidade-ti/` como Web Service
- configure `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`
- a aplicação já usa `server.port=${PORT:8080}`

### Frontend
- publique `frontend/` como site estático ou serviço com Nginx
- ajuste `frontend/auth.js` com a URL pública do backend

### Health check
- endpoint recomendado: `/api/health`
