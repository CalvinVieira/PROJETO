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

DELETE FROM questao;
ALTER SEQUENCE questao_id_seq RESTART WITH 1;

INSERT INTO questao (id, pergunta, categoria, tipo_avaliacao, peso) VALUES
(1, 'Existe um planejamento estratégico de TI alinhado ao negócio?', 'Governança', 'Governança', 5),
(2, 'A TI participa das decisões estratégicas da empresa?', 'Governança', 'Governança', 4),
(3, 'Há definição clara de papéis e responsabilidades na governança de TI?', 'Governança', 'Governança', 4),
(4, 'Existem KPIs para medir o desempenho estratégico da TI?', 'Governança', 'Governança', 4),
(5, 'Há revisão periódica da estratégia de TI?', 'Governança', 'Governança', 3),

(6, 'A TI possui portfólio priorizado de iniciativas?', 'Estratégia', 'Governança', 4),
(7, 'Os investimentos de TI são avaliados por valor e risco?', 'Estratégia', 'Governança', 4),
(8, 'Há alinhamento entre orçamento de TI e objetivos do negócio?', 'Estratégia', 'Governança', 4),
(9, 'Existe roadmap de evolução tecnológica?', 'Estratégia', 'Governança', 3),
(10, 'A TI apoia inovação e transformação digital?', 'Estratégia', 'Governança', 3),

(11, 'Existe política formal de segurança da informação?', 'Segurança', 'Gestão', 5),
(12, 'São realizados backups periódicos e testados?', 'Segurança', 'Gestão', 5),
(13, 'Há controle de acesso com segregação de privilégios?', 'Segurança', 'Gestão', 5),
(14, 'Existe gestão de vulnerabilidades e patches?', 'Segurança', 'Gestão', 4),
(15, 'Há processo de resposta a incidentes?', 'Segurança', 'Gestão', 4),

(16, 'Existe monitoramento de servidores e rede?', 'Infraestrutura', 'Gestão', 4),
(17, 'A infraestrutura é documentada?', 'Infraestrutura', 'Gestão', 3),
(18, 'Existe plano de continuidade e recuperação?', 'Infraestrutura', 'Gestão', 5),
(19, 'Os ativos críticos possuem redundância mínima?', 'Infraestrutura', 'Gestão', 4),
(20, 'A disponibilidade dos serviços é medida?', 'Infraestrutura', 'Gestão', 4),

(21, 'Existe gerenciamento de incidentes?', 'Processos', 'Gestão', 5),
(22, 'Existe gerenciamento de problemas recorrentes?', 'Processos', 'Gestão', 4),
(23, 'Há processo formal de mudanças?', 'Processos', 'Gestão', 4),
(24, 'Existe gestão de ativos e configuração?', 'Processos', 'Gestão', 3),
(25, 'As rotinas operacionais são padronizadas?', 'Processos', 'Gestão', 4),

(26, 'A equipe de TI possui plano de capacitação?', 'Pessoas', 'Gestão', 4),
(27, 'As competências necessárias estão mapeadas?', 'Pessoas', 'Gestão', 4),
(28, 'Há avaliação de desempenho da equipe de TI?', 'Pessoas', 'Gestão', 3),
(29, 'Existe retenção e sucessão para funções críticas?', 'Pessoas', 'Gestão', 3),
(30, 'A liderança reconhece a TI como área estratégica?', 'Pessoas', 'Gestão', 4),

(31, 'Os dados são organizados e possuem qualidade mínima?', 'Dados', 'Gestão', 4),
(32, 'Há política de proteção e privacidade de dados?', 'Dados', 'Gestão', 5),
(33, 'Existe controle de acesso a dados sensíveis?', 'Dados', 'Gestão', 5),
(34, 'Os dados suportam tomada de decisão?', 'Dados', 'Gestão', 4),
(35, 'Existe classificação da informação?', 'Dados', 'Gestão', 4),

(36, 'Existe catálogo de serviços de TI?', 'Serviços', 'Gestão', 4),
(37, 'Há SLA/ANS formal com áreas clientes?', 'Serviços', 'Gestão', 4),
(38, 'A satisfação do usuário é medida?', 'Serviços', 'Gestão', 3),
(39, 'Os serviços são monitorados com indicadores?', 'Serviços', 'Gestão', 4),
(40, 'Existe comunicação periódica dos níveis de serviço?', 'Serviços', 'Gestão', 3);