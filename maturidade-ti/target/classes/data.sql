INSERT INTO usuario (nome, email, senha, perfil) VALUES
('Administrador Stratec', 'admin@stratec.com', '123456', 'ADMIN'),
('Ana Avaliadora', 'avaliador@stratec.com', '123456', 'AVALIADOR')
ON CONFLICT (email) DO NOTHING;

INSERT INTO empresa (nome, segmento, porte, usuario_id)
SELECT 'Empresa Exemplo', 'Serviços', 'Médio', id FROM usuario WHERE email = 'avaliador@stratec.com'
ON CONFLICT DO NOTHING;

INSERT INTO questao (pergunta, categoria, peso) VALUES
('Existe um planejamento estratégico de TI alinhado ao negócio?', 'Governança', 5),
('A TI participa das decisões estratégicas da empresa?', 'Governança', 4),
('Existem indicadores para medir desempenho da TI?', 'Governança', 4),
('Há definição clara de papéis e responsabilidades na TI?', 'Governança', 3),
('A estratégia de TI é revisada periodicamente?', 'Governança', 3),
('Existe política formal de segurança da informação?', 'Segurança', 5),
('São realizados backups periódicos?', 'Segurança', 5),
('Há controle de acesso com login, senha e perfis?', 'Segurança', 4),
('Existe proteção contra ataques?', 'Segurança', 4),
('Os dados sensíveis possuem proteção adequada?', 'Segurança', 3),
('Existe monitoramento de servidores e rede?', 'Infraestrutura', 4),
('A infraestrutura é documentada?', 'Infraestrutura', 3),
('Existe plano de continuidade?', 'Infraestrutura', 5),
('Os sistemas são atualizados regularmente?', 'Infraestrutura', 4),
('Há redundância mínima para serviços críticos?', 'Infraestrutura', 3),
('Existe gestão de incidentes?', 'Serviços', 5),
('Existe controle de chamados?', 'Serviços', 4),
('Há SLA definido?', 'Serviços', 4),
('Existe catálogo de serviços?', 'Serviços', 3),
('Problemas recorrentes são analisados?', 'Serviços', 4),
('A empresa gerencia riscos de TI?', 'Riscos', 5),
('Há formalização de políticas e processos?', 'Processos', 4),
('A equipe recebe capacitação contínua?', 'Cultura', 3),
('A TI gera valor estratégico ao negócio?', 'Estratégia', 5),
('Existe cultura de melhoria contínua?', 'Cultura', 4),
('Existe controle de versão no desenvolvimento?', 'Processos', 4),
('Há testes automatizados?', 'Processos', 3),
('Existe processo definido de desenvolvimento?', 'Processos', 4),
('Os sistemas passam por homologação antes de produção?', 'Processos', 4),
('Existe integração contínua ou deploy automatizado?', 'Processos', 3)
ON CONFLICT DO NOTHING;

DELETE FROM questao;
ALTER SEQUENCE questao_id_seq RESTART WITH 1;
INSERT INTO questao (id, pergunta, categoria, peso) VALUES (1, 'Existe um planejamento estratégico de TI alinhado ao negócio?', 'Governança', 5);
INSERT INTO questao (id, pergunta, categoria, peso) VALUES (2, 'A TI participa das decisões estratégicas da empresa?', 'Governança', 4);
INSERT INTO questao (id, pergunta, categoria, peso) VALUES (3, 'Há definição clara de papéis e responsabilidades na TI?', 'Governança', 4);
INSERT INTO questao (id, pergunta, categoria, peso) VALUES (4, 'Existem KPIs para medir o desempenho da TI?', 'Governança', 4);
INSERT INTO questao (id, pergunta, categoria, peso) VALUES (5, 'Há revisão periódica da estratégia de TI?', 'Governança', 3);
INSERT INTO questao (id, pergunta, categoria, peso) VALUES (6, 'Existe política formal de segurança da informação?', 'Segurança', 5);
INSERT INTO questao (id, pergunta, categoria, peso) VALUES (7, 'São realizados backups periódicos e testados?', 'Segurança', 5);
INSERT INTO questao (id, pergunta, categoria, peso) VALUES (8, 'Há controle de acesso com segregação de privilégios?', 'Segurança', 5);
INSERT INTO questao (id, pergunta, categoria, peso) VALUES (9, 'Existe gestão de vulnerabilidades e patches?', 'Segurança', 4);
INSERT INTO questao (id, pergunta, categoria, peso) VALUES (10, 'Há processo de resposta a incidentes?', 'Segurança', 4);
INSERT INTO questao (id, pergunta, categoria, peso) VALUES (11, 'Existe monitoramento de servidores e rede?', 'Infraestrutura', 4);
INSERT INTO questao (id, pergunta, categoria, peso) VALUES (12, 'A infraestrutura é documentada?', 'Infraestrutura', 3);
INSERT INTO questao (id, pergunta, categoria, peso) VALUES (13, 'Existe plano de continuidade e recuperação?', 'Infraestrutura', 5);
INSERT INTO questao (id, pergunta, categoria, peso) VALUES (14, 'Os ativos críticos possuem redundância mínima?', 'Infraestrutura', 4);
INSERT INTO questao (id, pergunta, categoria, peso) VALUES (15, 'A disponibilidade dos serviços é medida?', 'Infraestrutura', 4);
INSERT INTO questao (id, pergunta, categoria, peso) VALUES (16, 'Existe gerenciamento de incidentes?', 'Processos', 5);
INSERT INTO questao (id, pergunta, categoria, peso) VALUES (17, 'Existe gerenciamento de problemas recorrentes?', 'Processos', 4);
INSERT INTO questao (id, pergunta, categoria, peso) VALUES (18, 'Há processo formal de mudanças?', 'Processos', 4);
INSERT INTO questao (id, pergunta, categoria, peso) VALUES (19, 'Existe gestão de ativos e configuração?', 'Processos', 3);
INSERT INTO questao (id, pergunta, categoria, peso) VALUES (20, 'As rotinas operacionais são padronizadas?', 'Processos', 4);
INSERT INTO questao (id, pergunta, categoria, peso) VALUES (21, 'A TI possui portfólio priorizado de iniciativas?', 'Estratégia', 4);
INSERT INTO questao (id, pergunta, categoria, peso) VALUES (22, 'Os investimentos de TI são avaliados por valor e risco?', 'Estratégia', 4);
INSERT INTO questao (id, pergunta, categoria, peso) VALUES (23, 'Há alinhamento entre orçamento de TI e objetivos do negócio?', 'Estratégia', 4);
INSERT INTO questao (id, pergunta, categoria, peso) VALUES (24, 'Existe roadmap de evolução tecnológica?', 'Estratégia', 3);
INSERT INTO questao (id, pergunta, categoria, peso) VALUES (25, 'A TI apoia inovação e transformação digital?', 'Estratégia', 3);
INSERT INTO questao (id, pergunta, categoria, peso) VALUES (26, 'A equipe de TI possui plano de capacitação?', 'Pessoas', 4);
INSERT INTO questao (id, pergunta, categoria, peso) VALUES (27, 'As competências necessárias estão mapeadas?', 'Pessoas', 4);
INSERT INTO questao (id, pergunta, categoria, peso) VALUES (28, 'Há avaliação de desempenho da equipe de TI?', 'Pessoas', 3);
INSERT INTO questao (id, pergunta, categoria, peso) VALUES (29, 'Existe retenção e sucessão para funções críticas?', 'Pessoas', 3);
INSERT INTO questao (id, pergunta, categoria, peso) VALUES (30, 'A liderança reconhece a TI como área estratégica?', 'Pessoas', 4);
INSERT INTO questao (id, pergunta, categoria, peso) VALUES (31, 'Os dados são organizados e possuem qualidade mínima?', 'Dados', 4);
INSERT INTO questao (id, pergunta, categoria, peso) VALUES (32, 'Há política de proteção e privacidade de dados?', 'Dados', 5);
INSERT INTO questao (id, pergunta, categoria, peso) VALUES (33, 'Existe controle de acesso a dados sensíveis?', 'Dados', 5);
INSERT INTO questao (id, pergunta, categoria, peso) VALUES (34, 'Os dados suportam tomada de decisão?', 'Dados', 4);
INSERT INTO questao (id, pergunta, categoria, peso) VALUES (35, 'Existe classificação da informação?', 'Dados', 4);
INSERT INTO questao (id, pergunta, categoria, peso) VALUES (36, 'Existe catálogo de serviços de TI?', 'Serviços', 4);
INSERT INTO questao (id, pergunta, categoria, peso) VALUES (37, 'Há SLA/ANS formal com áreas clientes?', 'Serviços', 4);
INSERT INTO questao (id, pergunta, categoria, peso) VALUES (38, 'A satisfação do usuário é medida?', 'Serviços', 3);
INSERT INTO questao (id, pergunta, categoria, peso) VALUES (39, 'Os serviços são monitorados com indicadores?', 'Serviços', 4);
INSERT INTO questao (id, pergunta, categoria, peso) VALUES (40, 'Existe comunicação periódica dos níveis de serviço?', 'Serviços', 3);
