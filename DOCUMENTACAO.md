# Documentação do Stratec TI

## Objetivo
Avaliar a maturidade da TI de empresas com base em governança, segurança, infraestrutura, processos, pessoas, dados, estratégia e serviços.

## Arquitetura
Cliente (HTML/CSS/JS) -> API REST (Spring Boot) -> PostgreSQL

## Requisitos funcionais
- Cadastro e autenticação
- Cadastro de empresa
- Cadastro de questões
- Aplicação de questionário
- Geração de relatório
- Exportação em PDF

## Requisitos não funcionais
- Responsividade
- Organização MVC
- Persistência relacional
- Execução em Docker
- Preparado para deploy em nuvem

## Critérios de avaliação
Escala de 0 a 5 por pergunta, ponderada por peso.

## Níveis de maturidade
- 0.0 a 1.0: Inicial (Caótico)
- 1.1 a 2.0: Repetível (Reativo)
- 2.1 a 3.5: Definido (Proativo)
- 3.6 a 4.5: Gerenciado (Mensurável)
- 4.6 a 5.0: Otimizado (Estratégico)


## Deploy online
A aplicação pode ser executada localmente com Docker Compose e também publicada em nuvem.

### Estrutura recomendada
- backend Spring Boot como Web Service
- banco PostgreSQL gerenciado
- frontend como site estático ou serviço Nginx

### Ajustes já preparados
- `server.port=${PORT:8080}`
- endpoint `/api/health`
- frontend com `API_BASE` alternando entre localhost e URL pública

### Observação
Ao publicar, ajuste `frontend/auth.js` com a URL real do backend.
