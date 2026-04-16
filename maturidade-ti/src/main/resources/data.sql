INSERT INTO usuario (nome, email, senha, perfil) VALUES
('Administrador Stratec', 'admin@stratec.com', '123456', 'ADMIN'),
('Ana Avaliadora', 'avaliador@stratec.com', '123456', 'AVALIADOR')
ON CONFLICT (email) DO NOTHING;

INSERT INTO empresa (nome, segmento, porte, usuario_id)
SELECT 'Empresa Exemplo', 'Serviços', 'Médio', id
FROM usuario
WHERE email = 'avaliador@stratec.com'
ON CONFLICT DO NOTHING;

ALTER TABLE questao
ADD COLUMN IF NOT EXISTS tipo_avaliacao VARCHAR(30);

UPDATE questao
SET tipo_avaliacao = CASE
    WHEN categoria IN ('Governança', 'Estratégia') THEN 'Governança'
    ELSE 'Gestão'
END
WHERE tipo_avaliacao IS NULL OR TRIM(tipo_avaliacao) = '';

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'Existe um planejamento estratégico de TI alinhado ao negócio?', 'Governança', 'Governança', 5
WHERE NOT EXISTS (
    SELECT 1 FROM questao WHERE pergunta = 'Existe um planejamento estratégico de TI alinhado ao negócio?'
);

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'A TI participa das decisões estratégicas da empresa?', 'Governança', 'Governança', 4
WHERE NOT EXISTS (
    SELECT 1 FROM questao WHERE pergunta = 'A TI participa das decisões estratégicas da empresa?'
);

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'Há definição clara de papéis e responsabilidades na governança de TI?', 'Governança', 'Governança', 4
WHERE NOT EXISTS (
    SELECT 1 FROM questao WHERE pergunta = 'Há definição clara de papéis e responsabilidades na governança de TI?'
);

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'Existem KPIs para medir o desempenho estratégico da TI?', 'Governança', 'Governança', 4
WHERE NOT EXISTS (
    SELECT 1 FROM questao WHERE pergunta = 'Existem KPIs para medir o desempenho estratégico da TI?'
);

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'Há revisão periódica da estratégia de TI?', 'Governança', 'Governança', 3
WHERE NOT EXISTS (
    SELECT 1 FROM questao WHERE pergunta = 'Há revisão periódica da estratégia de TI?'
);

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'A TI possui portfólio priorizado de iniciativas?', 'Estratégia', 'Governança', 4
WHERE NOT EXISTS (
    SELECT 1 FROM questao WHERE pergunta = 'A TI possui portfólio priorizado de iniciativas?'
);

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'Os investimentos de TI são avaliados por valor e risco?', 'Estratégia', 'Governança', 4
WHERE NOT EXISTS (
    SELECT 1 FROM questao WHERE pergunta = 'Os investimentos de TI são avaliados por valor e risco?'
);

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'Há alinhamento entre orçamento de TI e objetivos do negócio?', 'Estratégia', 'Governança', 4
WHERE NOT EXISTS (
    SELECT 1 FROM questao WHERE pergunta = 'Há alinhamento entre orçamento de TI e objetivos do negócio?'
);

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'Existe roadmap de evolução tecnológica?', 'Estratégia', 'Governança', 3
WHERE NOT EXISTS (
    SELECT 1 FROM questao WHERE pergunta = 'Existe roadmap de evolução tecnológica?'
);

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'A TI apoia inovação e transformação digital?', 'Estratégia', 'Governança', 3
WHERE NOT EXISTS (
    SELECT 1 FROM questao WHERE pergunta = 'A TI apoia inovação e transformação digital?'
);

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'Existe política formal de segurança da informação?', 'Segurança', 'Gestão', 5
WHERE NOT EXISTS (
    SELECT 1 FROM questao WHERE pergunta = 'Existe política formal de segurança da informação?'
);

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'São realizados backups periódicos e testados?', 'Segurança', 'Gestão', 5
WHERE NOT EXISTS (
    SELECT 1 FROM questao WHERE pergunta = 'São realizados backups periódicos e testados?'
);

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'Há controle de acesso com segregação de privilégios?', 'Segurança', 'Gestão', 5
WHERE NOT EXISTS (
    SELECT 1 FROM questao WHERE pergunta = 'Há controle de acesso com segregação de privilégios?'
);

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'Existe gestão de vulnerabilidades e patches?', 'Segurança', 'Gestão', 4
WHERE NOT EXISTS (
    SELECT 1 FROM questao WHERE pergunta = 'Existe gestão de vulnerabilidades e patches?'
);

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'Há processo de resposta a incidentes?', 'Segurança', 'Gestão', 4
WHERE NOT EXISTS (
    SELECT 1 FROM questao WHERE pergunta = 'Há processo de resposta a incidentes?'
);

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'Existe monitoramento de servidores e rede?', 'Infraestrutura', 'Gestão', 4
WHERE NOT EXISTS (
    SELECT 1 FROM questao WHERE pergunta = 'Existe monitoramento de servidores e rede?'
);

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'A infraestrutura é documentada?', 'Infraestrutura', 'Gestão', 3
WHERE NOT EXISTS (
    SELECT 1 FROM questao WHERE pergunta = 'A infraestrutura é documentada?'
);

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'Existe plano de continuidade e recuperação?', 'Infraestrutura', 'Gestão', 5
WHERE NOT EXISTS (
    SELECT 1 FROM questao WHERE pergunta = 'Existe plano de continuidade e recuperação?'
);

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'Os ativos críticos possuem redundância mínima?', 'Infraestrutura', 'Gestão', 4
WHERE NOT EXISTS (
    SELECT 1 FROM questao WHERE pergunta = 'Os ativos críticos possuem redundância mínima?'
);

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'A disponibilidade dos serviços é medida?', 'Infraestrutura', 'Gestão', 4
WHERE NOT EXISTS (
    SELECT 1 FROM questao WHERE pergunta = 'A disponibilidade dos serviços é medida?'
);

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'Existe gerenciamento de incidentes?', 'Processos', 'Gestão', 5
WHERE NOT EXISTS (
    SELECT 1 FROM questao WHERE pergunta = 'Existe gerenciamento de incidentes?'
);

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'Existe gerenciamento de problemas recorrentes?', 'Processos', 'Gestão', 4
WHERE NOT EXISTS (
    SELECT 1 FROM questao WHERE pergunta = 'Existe gerenciamento de problemas recorrentes?'
);

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'Há processo formal de mudanças?', 'Processos', 'Gestão', 4
WHERE NOT EXISTS (
    SELECT 1 FROM questao WHERE pergunta = 'Há processo formal de mudanças?'
);

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'Existe gestão de ativos e configuração?', 'Processos', 'Gestão', 3
WHERE NOT EXISTS (
    SELECT 1 FROM questao WHERE pergunta = 'Existe gestão de ativos e configuração?'
);

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'As rotinas operacionais são padronizadas?', 'Processos', 'Gestão', 4
WHERE NOT EXISTS (
    SELECT 1 FROM questao WHERE pergunta = 'As rotinas operacionais são padronizadas?'
);

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'A equipe de TI possui plano de capacitação?', 'Pessoas', 'Gestão', 4
WHERE NOT EXISTS (
    SELECT 1 FROM questao WHERE pergunta = 'A equipe de TI possui plano de capacitação?'
);

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'As competências necessárias estão mapeadas?', 'Pessoas', 'Gestão', 4
WHERE NOT EXISTS (
    SELECT 1 FROM questao WHERE pergunta = 'As competências necessárias estão mapeadas?'
);

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'Há avaliação de desempenho da equipe de TI?', 'Pessoas', 'Gestão', 3
WHERE NOT EXISTS (
    SELECT 1 FROM questao WHERE pergunta = 'Há avaliação de desempenho da equipe de TI?'
);

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'Existe retenção e sucessão para funções críticas?', 'Pessoas', 'Gestão', 3
WHERE NOT EXISTS (
    SELECT 1 FROM questao WHERE pergunta = 'Existe retenção e sucessão para funções críticas?'
);

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'A liderança reconhece a TI como área estratégica?', 'Pessoas', 'Gestão', 4
WHERE NOT EXISTS (
    SELECT 1 FROM questao WHERE pergunta = 'A liderança reconhece a TI como área estratégica?'
);

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'Os dados são organizados e possuem qualidade mínima?', 'Dados', 'Gestão', 4
WHERE NOT EXISTS (
    SELECT 1 FROM questao WHERE pergunta = 'Os dados são organizados e possuem qualidade mínima?'
);

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'Há política de proteção e privacidade de dados?', 'Dados', 'Gestão', 5
WHERE NOT EXISTS (
    SELECT 1 FROM questao WHERE pergunta = 'Há política de proteção e privacidade de dados?'
);

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'Existe controle de acesso a dados sensíveis?', 'Dados', 'Gestão', 5
WHERE NOT EXISTS (
    SELECT 1 FROM questao WHERE pergunta = 'Existe controle de acesso a dados sensíveis?'
);

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'Os dados suportam tomada de decisão?', 'Dados', 'Gestão', 4
WHERE NOT EXISTS (
    SELECT 1 FROM questao WHERE pergunta = 'Os dados suportam tomada de decisão?'
);

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'Existe classificação da informação?', 'Dados', 'Gestão', 4
WHERE NOT EXISTS (
    SELECT 1 FROM questao WHERE pergunta = 'Existe classificação da informação?'
);

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'Existe catálogo de serviços de TI?', 'Serviços', 'Gestão', 4
WHERE NOT EXISTS (
    SELECT 1 FROM questao WHERE pergunta = 'Existe catálogo de serviços de TI?'
);

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'Há SLA/ANS formal com áreas clientes?', 'Serviços', 'Gestão', 4
WHERE NOT EXISTS (
    SELECT 1 FROM questao WHERE pergunta = 'Há SLA/ANS formal com áreas clientes?'
);

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'A satisfação do usuário é medida?', 'Serviços', 'Gestão', 3
WHERE NOT EXISTS (
    SELECT 1 FROM questao WHERE pergunta = 'A satisfação do usuário é medida?'
);

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'Os serviços são monitorados com indicadores?', 'Serviços', 'Gestão', 4
WHERE NOT EXISTS (
    SELECT 1 FROM questao WHERE pergunta = 'Os serviços são monitorados com indicadores?'
);

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'Existe comunicação periódica dos níveis de serviço?', 'Serviços', 'Gestão', 3
WHERE NOT EXISTS (
    SELECT 1 FROM questao WHERE pergunta = 'Existe comunicação periódica dos níveis de serviço?'
);