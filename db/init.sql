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