CREATE TABLE IF NOT EXISTS usuario (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    perfil VARCHAR(50) NOT NULL DEFAULT 'CLIENTE'
);

CREATE TABLE IF NOT EXISTS empresa (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    segmento VARCHAR(255),
    porte VARCHAR(255),
    usuario_id BIGINT NOT NULL REFERENCES usuario(id)
);

CREATE TABLE IF NOT EXISTS questao (
    id BIGSERIAL PRIMARY KEY,
    pergunta VARCHAR(500) NOT NULL,
    categoria VARCHAR(255) NOT NULL,
    tipo_avaliacao VARCHAR(30),
    peso INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS resposta (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL REFERENCES empresa(id) ON DELETE CASCADE,
    questao_id BIGINT NOT NULL REFERENCES questao(id),
    valor INTEGER NOT NULL,
    evidencia VARCHAR(1000),
    plano_acao VARCHAR(1000),
    dimensao VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS servico_ti (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    descricao VARCHAR(1000),
    categoria VARCHAR(80) NOT NULL,
    responsavel VARCHAR(120),
    sla_horas INTEGER,
    status VARCHAR(30) NOT NULL,
    empresa_id BIGINT NOT NULL REFERENCES empresa(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS incidente_ti (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    descricao VARCHAR(2000),
    prioridade VARCHAR(30) NOT NULL,
    status VARCHAR(30) NOT NULL,
    data_abertura TIMESTAMP NOT NULL,
    data_fechamento TIMESTAMP,
    sla_horas INTEGER,
    empresa_id BIGINT NOT NULL REFERENCES empresa(id) ON DELETE CASCADE,
    servico_id BIGINT REFERENCES servico_ti(id)
);

INSERT INTO usuario (nome, email, senha, perfil) VALUES
('Administrador Stratec', 'admin@stratec.com', '123456', 'ADMIN'),
('Ana Avaliadora', 'avaliador@stratec.com', '123456', 'AVALIADOR')
ON CONFLICT (email) DO NOTHING;

INSERT INTO empresa (nome, segmento, porte, usuario_id)
SELECT 'Empresa Exemplo', 'Serviços', 'Médio', id
FROM usuario
WHERE email = 'avaliador@stratec.com'
ON CONFLICT DO NOTHING;

-- Inserção já com separação Governança x Gestão
INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso) VALUES
('Existe um planejamento estratégico de TI alinhado ao negócio?', 'Governança', 'Governança', 5),
('A TI participa das decisões estratégicas da empresa?', 'Governança', 'Governança', 4),
('Há definição clara de papéis e responsabilidades na TI?', 'Governança', 'Governança', 4),
('Existem KPIs para medir o desempenho da TI?', 'Governança', 'Governança', 4),
('A estratégia de TI é revisada periodicamente?', 'Governança', 'Governança', 3),

('Existe política formal de segurança da informação?', 'Segurança', 'Gestão', 5),
('São realizados backups periódicos?', 'Segurança', 'Gestão', 5),
('Há controle de acesso com login, senha e perfis?', 'Segurança', 'Gestão', 4),
('Existe proteção contra ataques?', 'Segurança', 'Gestão', 4),
('Os dados sensíveis possuem proteção adequada?', 'Segurança', 'Gestão', 3),

('Existe monitoramento de servidores e rede?', 'Infraestrutura', 'Gestão', 4),
('A infraestrutura é documentada?', 'Infraestrutura', 'Gestão', 3),
('Existe plano de continuidade?', 'Infraestrutura', 'Gestão', 5),
('Os sistemas são atualizados regularmente?', 'Infraestrutura', 'Gestão', 4),
('Há redundância mínima para serviços críticos?', 'Infraestrutura', 'Gestão', 3),

('Existe gestão de incidentes?', 'Serviços', 'Gestão', 5),
('Existe controle de chamados?', 'Serviços', 'Gestão', 4),
('Há SLA definido?', 'Serviços', 'Gestão', 4),
('Existe catálogo de serviços?', 'Serviços', 'Gestão', 3),
('Problemas recorrentes são analisados?', 'Serviços', 'Gestão', 4),

('A empresa gerencia riscos de TI?', 'Processos', 'Gestão', 5),
('Há formalização de políticas e processos?', 'Processos', 'Gestão', 4),
('Existe controle de versão no desenvolvimento?', 'Processos', 'Gestão', 4),
('Há testes automatizados?', 'Processos', 'Gestão', 3),
('Existe processo definido de desenvolvimento?', 'Processos', 'Gestão', 4),
('Os sistemas passam por homologação antes de produção?', 'Processos', 'Gestão', 4),
('Existe integração contínua ou deploy automatizado?', 'Processos', 'Gestão', 3),

('A TI gera valor estratégico ao negócio?', 'Estratégia', 'Governança', 5),
('Existe alinhamento entre TI e objetivos de negócio?', 'Estratégia', 'Governança', 4),
('A empresa mede retorno sobre investimento em TI?', 'Estratégia', 'Governança', 4),

('A equipe recebe capacitação contínua?', 'Pessoas', 'Gestão', 3),
('Existe cultura de melhoria contínua?', 'Pessoas', 'Gestão', 4);
-- ============================================================
-- UPGRADE: MÓDULO DE GOVERNANÇA DE TI - STRATEC TI
-- ============================================================

-- Tabela de configuração do PDTI
CREATE TABLE IF NOT EXISTS pdti_config (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL REFERENCES empresa(id) ON DELETE CASCADE,
    missao TEXT,
    visao TEXT,
    objetivos_estrategicos TEXT,
    metas_estrategicas TEXT,
    periodo_vigencia_inicio VARCHAR(20),
    periodo_vigencia_fim VARCHAR(20),
    responsavel VARCHAR(255),
    patrocinador_executivo VARCHAR(255),
    contexto_organizacional TEXT,
    contexto_tecnologico TEXT,
    analise_situacional TEXT,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela do Plano de Ação 5W2H
CREATE TABLE IF NOT EXISTS plano_acao_5w2h (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL REFERENCES empresa(id) ON DELETE CASCADE,
    o_que VARCHAR(500) NOT NULL,
    por_que TEXT,
    onde VARCHAR(300),
    quando VARCHAR(100),
    quem VARCHAR(200),
    como TEXT,
    quanto VARCHAR(100),
    prioridade VARCHAR(30) DEFAULT 'MEDIA',
    status VARCHAR(30) DEFAULT 'PENDENTE',
    categoria_origem VARCHAR(100),
    score_origem DECIMAL(5,2),
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela da Matriz de Gestão de Riscos
CREATE TABLE IF NOT EXISTS gestao_riscos (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL REFERENCES empresa(id) ON DELETE CASCADE,
    descricao VARCHAR(500) NOT NULL,
    tipo VARCHAR(30) DEFAULT 'Ameaça',
    ativo VARCHAR(200),
    ameaca VARCHAR(300),
    vulnerabilidade TEXT,
    causa TEXT,
    consequencia TEXT,
    impacto INTEGER DEFAULT 3,
    probabilidade VARCHAR(30) DEFAULT 'Moderado',
    nivel_risco VARCHAR(30),
    nivel_risco_num INTEGER,
    tratamento VARCHAR(30) DEFAULT 'Mitigar',
    responsavel VARCHAR(200),
    status VARCHAR(30) DEFAULT 'Identificado',
    categoria_origem VARCHAR(100),
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela do Roadmap Tecnológico
CREATE TABLE IF NOT EXISTS roadmap_tecnologico (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL REFERENCES empresa(id) ON DELETE CASCADE,
    iniciativa VARCHAR(300) NOT NULL,
    descricao TEXT,
    trimestre VARCHAR(20),
    prioridade VARCHAR(30) DEFAULT 'MEDIA',
    dependencias VARCHAR(500),
    status VARCHAR(30) DEFAULT 'PLANEJADO',
    categoria VARCHAR(100),
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

