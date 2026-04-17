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
    WHEN categoria IN ('Governança', 'Estratégia', 'Riscos', 'Desempenho e Valor') THEN 'Governança'
    ELSE 'Gestão'
END
WHERE tipo_avaliacao IS NULL OR TRIM(tipo_avaliacao) = '';

-- Reestruturação da Etapa 2: questionário mais detalhado com base em COBIT, ITIL e ISO 27000
-- Atualiza perguntas já existentes da Etapa 1 para evitar duplicidade e preservar histórico.

UPDATE questao
SET categoria = 'Governança', tipo_avaliacao = 'Governança', peso = 5,
    pergunta = 'Existe um modelo formal de governança de TI alinhado aos objetivos do negócio?'
WHERE pergunta = 'Existe um planejamento estratégico de TI alinhado ao negócio?';

UPDATE questao
SET categoria = 'Governança', tipo_avaliacao = 'Governança', peso = 4,
    pergunta = 'A alta administração participa das decisões estratégicas de tecnologia da informação?'
WHERE pergunta = 'A TI participa das decisões estratégicas da empresa?';

UPDATE questao
SET categoria = 'Governança', tipo_avaliacao = 'Governança', peso = 4,
    pergunta = 'Há definição formal de papéis, responsabilidades e mecanismos de decisão para governança de TI?'
WHERE pergunta = 'Há definição clara de papéis e responsabilidades na governança de TI?';

UPDATE questao
SET categoria = 'Desempenho e Valor', tipo_avaliacao = 'Governança', peso = 4,
    pergunta = 'Existem indicadores para medir valor entregue, desempenho e contribuição da TI ao negócio?'
WHERE pergunta = 'Existem KPIs para medir o desempenho estratégico da TI?';

UPDATE questao
SET categoria = 'Estratégia', tipo_avaliacao = 'Governança', peso = 3,
    pergunta = 'A estratégia de TI é revisada periodicamente com base em mudanças do negócio e do ambiente externo?'
WHERE pergunta = 'Há revisão periódica da estratégia de TI?';

UPDATE questao
SET categoria = 'Estratégia', tipo_avaliacao = 'Governança', peso = 4,
    pergunta = 'A organização mantém portfólio priorizado de iniciativas de TI com critérios claros de valor e urgência?'
WHERE pergunta = 'A TI possui portfólio priorizado de iniciativas?';

UPDATE questao
SET categoria = 'Riscos', tipo_avaliacao = 'Governança', peso = 4,
    pergunta = 'Os investimentos e iniciativas de TI são avaliados considerando riscos, custos e benefícios esperados?'
WHERE pergunta = 'Os investimentos de TI são avaliados por valor e risco?';

UPDATE questao
SET categoria = 'Estratégia', tipo_avaliacao = 'Governança', peso = 4,
    pergunta = 'Há alinhamento formal entre orçamento de TI e metas estratégicas da organização?'
WHERE pergunta = 'Há alinhamento entre orçamento de TI e objetivos do negócio?';

UPDATE questao
SET categoria = 'Estratégia', tipo_avaliacao = 'Governança', peso = 3,
    pergunta = 'Existe roadmap de evolução tecnológica com prioridades, dependências e horizonte de execução?'
WHERE pergunta = 'Existe roadmap de evolução tecnológica?';

UPDATE questao
SET categoria = 'Desempenho e Valor', tipo_avaliacao = 'Governança', peso = 3,
    pergunta = 'A TI contribui ativamente para inovação, transformação digital e geração de valor para o negócio?'
WHERE pergunta = 'A TI apoia inovação e transformação digital?';

UPDATE questao
SET categoria = 'Segurança', tipo_avaliacao = 'Gestão', peso = 5,
    pergunta = 'Existe política formal de segurança da informação aprovada, divulgada e revisada periodicamente?'
WHERE pergunta = 'Existe política formal de segurança da informação?';

UPDATE questao
SET categoria = 'Continuidade e Backup', tipo_avaliacao = 'Gestão', peso = 5,
    pergunta = 'São realizados backups periódicos com testes de restauração e evidências de execução?'
WHERE pergunta = 'São realizados backups periódicos e testados?';

UPDATE questao
SET categoria = 'Controle de Acesso', tipo_avaliacao = 'Gestão', peso = 5,
    pergunta = 'Há controle de acesso com segregação de funções, revisão de perfis e gestão de privilégios?'
WHERE pergunta = 'Há controle de acesso com segregação de privilégios?';

UPDATE questao
SET categoria = 'Segurança', tipo_avaliacao = 'Gestão', peso = 4,
    pergunta = 'Existe processo de gestão de vulnerabilidades, correções e atualização segura dos ativos de TI?'
WHERE pergunta = 'Existe gestão de vulnerabilidades e patches?';

UPDATE questao
SET categoria = 'Segurança', tipo_avaliacao = 'Gestão', peso = 4,
    pergunta = 'Há processo formal de detecção, resposta, registro e lições aprendidas para incidentes de segurança?'
WHERE pergunta = 'Há processo de resposta a incidentes?';

UPDATE questao
SET categoria = 'Infraestrutura', tipo_avaliacao = 'Gestão', peso = 4,
    pergunta = 'Existe monitoramento contínuo de servidores, rede, disponibilidade e capacidade dos recursos críticos?'
WHERE pergunta = 'Existe monitoramento de servidores e rede?';

UPDATE questao
SET categoria = 'Infraestrutura', tipo_avaliacao = 'Gestão', peso = 3,
    pergunta = 'A infraestrutura de TI é documentada, inventariada e mantida atualizada?'
WHERE pergunta = 'A infraestrutura é documentada?';

UPDATE questao
SET categoria = 'Continuidade e Backup', tipo_avaliacao = 'Gestão', peso = 5,
    pergunta = 'Existe plano de continuidade e recuperação de desastres com testes periódicos e responsáveis definidos?'
WHERE pergunta = 'Existe plano de continuidade e recuperação?';

UPDATE questao
SET categoria = 'Infraestrutura', tipo_avaliacao = 'Gestão', peso = 4,
    pergunta = 'Os ativos e serviços críticos possuem redundância mínima para sustentar a operação?'
WHERE pergunta = 'Os ativos críticos possuem redundância mínima?';

UPDATE questao
SET categoria = 'Infraestrutura', tipo_avaliacao = 'Gestão', peso = 4,
    pergunta = 'A disponibilidade dos serviços de TI é medida com metas e indicadores definidos?'
WHERE pergunta = 'A disponibilidade dos serviços é medida?';

UPDATE questao
SET categoria = 'Incidentes e Problemas', tipo_avaliacao = 'Gestão', peso = 5,
    pergunta = 'Existe processo formal de gerenciamento de incidentes com registro, classificação, priorização e escalonamento?'
WHERE pergunta = 'Existe gerenciamento de incidentes?';

UPDATE questao
SET categoria = 'Incidentes e Problemas', tipo_avaliacao = 'Gestão', peso = 4,
    pergunta = 'Existe processo de gerenciamento de problemas para identificar causa raiz e evitar recorrências?'
WHERE pergunta = 'Existe gerenciamento de problemas recorrentes?';

UPDATE questao
SET categoria = 'Mudanças e Liberação', tipo_avaliacao = 'Gestão', peso = 4,
    pergunta = 'Há processo formal de mudanças com avaliação de impacto, aprovação e registro das alterações?'
WHERE pergunta = 'Há processo formal de mudanças?';

UPDATE questao
SET categoria = 'Mudanças e Liberação', tipo_avaliacao = 'Gestão', peso = 3,
    pergunta = 'Existe gestão de ativos e configuração para identificar versões, dependências e itens críticos?'
WHERE pergunta = 'Existe gestão de ativos e configuração?';

UPDATE questao
SET categoria = 'Processos', tipo_avaliacao = 'Gestão', peso = 4,
    pergunta = 'As rotinas operacionais de TI são padronizadas, documentadas e acompanhadas por responsáveis definidos?'
WHERE pergunta = 'As rotinas operacionais são padronizadas?';

UPDATE questao
SET categoria = 'Pessoas', tipo_avaliacao = 'Gestão', peso = 4,
    pergunta = 'A equipe de TI possui plano de capacitação compatível com as necessidades técnicas e gerenciais da área?'
WHERE pergunta = 'A equipe de TI possui plano de capacitação?';

UPDATE questao
SET categoria = 'Pessoas', tipo_avaliacao = 'Gestão', peso = 4,
    pergunta = 'As competências necessárias para operação, segurança e evolução da TI estão mapeadas?'
WHERE pergunta = 'As competências necessárias estão mapeadas?';

UPDATE questao
SET categoria = 'Pessoas', tipo_avaliacao = 'Gestão', peso = 3,
    pergunta = 'Há avaliação periódica de desempenho da equipe de TI com critérios claros?'
WHERE pergunta = 'Há avaliação de desempenho da equipe de TI?';

UPDATE questao
SET categoria = 'Pessoas', tipo_avaliacao = 'Gestão', peso = 3,
    pergunta = 'Existe plano de retenção, sucessão ou contingência para funções críticas da área de TI?'
WHERE pergunta = 'Existe retenção e sucessão para funções críticas?';

UPDATE questao
SET categoria = 'Pessoas', tipo_avaliacao = 'Gestão', peso = 4,
    pergunta = 'A liderança reconhece a TI como função essencial para operação, conformidade e resultados do negócio?'
WHERE pergunta = 'A liderança reconhece a TI como área estratégica?';

UPDATE questao
SET categoria = 'Dados', tipo_avaliacao = 'Gestão', peso = 4,
    pergunta = 'Os dados organizacionais são estruturados, mantidos com qualidade mínima e possuem responsáveis definidos?'
WHERE pergunta = 'Os dados são organizados e possuem qualidade mínima?';

UPDATE questao
SET categoria = 'Proteção de Dados', tipo_avaliacao = 'Gestão', peso = 5,
    pergunta = 'Existe política de proteção, privacidade e tratamento de dados alinhada à LGPD?'
WHERE pergunta = 'Há política de proteção e privacidade de dados?';

UPDATE questao
SET categoria = 'Controle de Acesso', tipo_avaliacao = 'Gestão', peso = 5,
    pergunta = 'Existe controle de acesso específico para dados sensíveis, críticos e restritos?'
WHERE pergunta = 'Existe controle de acesso a dados sensíveis?';

UPDATE questao
SET categoria = 'Dados', tipo_avaliacao = 'Gestão', peso = 4,
    pergunta = 'Os dados são utilizados como apoio consistente à tomada de decisão e acompanhamento de resultados?'
WHERE pergunta = 'Os dados suportam tomada de decisão?';

UPDATE questao
SET categoria = 'Proteção de Dados', tipo_avaliacao = 'Gestão', peso = 4,
    pergunta = 'Existe classificação da informação para orientar proteção, retenção e acesso adequado aos dados?'
WHERE pergunta = 'Existe classificação da informação?';

UPDATE questao
SET categoria = 'Serviços e SLA', tipo_avaliacao = 'Gestão', peso = 4,
    pergunta = 'Existe catálogo formal e atualizado dos serviços de TI oferecidos às áreas clientes?'
WHERE pergunta = 'Existe catálogo de serviços de TI?';

UPDATE questao
SET categoria = 'Serviços e SLA', tipo_avaliacao = 'Gestão', peso = 4,
    pergunta = 'Há acordos de nível de serviço formalizados com metas, indicadores e responsabilidades definidas?'
WHERE pergunta = 'Há SLA/ANS formal com áreas clientes?';

UPDATE questao
SET categoria = 'Serviços e SLA', tipo_avaliacao = 'Gestão', peso = 3,
    pergunta = 'A satisfação dos usuários com os serviços de TI é medida periodicamente e analisada pela gestão?'
WHERE pergunta = 'A satisfação do usuário é medida?';

UPDATE questao
SET categoria = 'Serviços e SLA', tipo_avaliacao = 'Gestão', peso = 4,
    pergunta = 'Os serviços de TI são monitorados com indicadores de desempenho, capacidade e qualidade?'
WHERE pergunta = 'Os serviços são monitorados com indicadores?';

UPDATE questao
SET categoria = 'Serviços e SLA', tipo_avaliacao = 'Gestão', peso = 3,
    pergunta = 'Os resultados dos níveis de serviço são comunicados regularmente às áreas atendidas?'
WHERE pergunta = 'Existe comunicação periódica dos níveis de serviço?';

-- Inserções complementares da Etapa 2 (novas perguntas, sem duplicar o que já existe)
INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'Existe comitê, fórum ou instância equivalente para priorizar temas relevantes de TI com participação das áreas de negócio?', 'Governança', 'Governança', 4
WHERE NOT EXISTS (SELECT 1 FROM questao WHERE pergunta = 'Existe comitê, fórum ou instância equivalente para priorizar temas relevantes de TI com participação das áreas de negócio?');

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'A organização define apetite a risco e critérios para aceitação de riscos relacionados à tecnologia?', 'Riscos', 'Governança', 4
WHERE NOT EXISTS (SELECT 1 FROM questao WHERE pergunta = 'A organização define apetite a risco e critérios para aceitação de riscos relacionados à tecnologia?');

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'Há acompanhamento executivo dos principais riscos de TI, segurança e continuidade do negócio?', 'Riscos', 'Governança', 4
WHERE NOT EXISTS (SELECT 1 FROM questao WHERE pergunta = 'Há acompanhamento executivo dos principais riscos de TI, segurança e continuidade do negócio?');

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'Existe monitoramento de conformidade com políticas, normas e controles de TI?', 'Desempenho e Valor', 'Governança', 3
WHERE NOT EXISTS (SELECT 1 FROM questao WHERE pergunta = 'Existe monitoramento de conformidade com políticas, normas e controles de TI?');

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'Há revisão periódica de acessos de usuários, contas privilegiadas e contas inativas?', 'Controle de Acesso', 'Gestão', 5
WHERE NOT EXISTS (SELECT 1 FROM questao WHERE pergunta = 'Há revisão periódica de acessos de usuários, contas privilegiadas e contas inativas?');

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'As mudanças em produção possuem plano de rollback, comunicação e registro de aprovação?', 'Mudanças e Liberação', 'Gestão', 4
WHERE NOT EXISTS (SELECT 1 FROM questao WHERE pergunta = 'As mudanças em produção possuem plano de rollback, comunicação e registro de aprovação?');

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'Existe base de conhecimento, histórico ou documentação de soluções para agilizar atendimento e suporte?', 'Incidentes e Problemas', 'Gestão', 3
WHERE NOT EXISTS (SELECT 1 FROM questao WHERE pergunta = 'Existe base de conhecimento, histórico ou documentação de soluções para agilizar atendimento e suporte?');

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'Existe inventário dos ativos de informação e definição de proprietários dos dados mais críticos?', 'Proteção de Dados', 'Gestão', 4
WHERE NOT EXISTS (SELECT 1 FROM questao WHERE pergunta = 'Existe inventário dos ativos de informação e definição de proprietários dos dados mais críticos?');

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'Há testes periódicos do plano de continuidade e recuperação com registro de resultados e melhorias?', 'Continuidade e Backup', 'Gestão', 5
WHERE NOT EXISTS (SELECT 1 FROM questao WHERE pergunta = 'Há testes periódicos do plano de continuidade e recuperação com registro de resultados e melhorias?');

INSERT INTO servico_ti (empresa_id, nome, descricao, categoria, responsavel, sla_horas, status)
SELECT e.id, 'Service Desk', 'Atendimento inicial e tratamento de chamados dos usuários.', 'Suporte', 'Coordenação de Suporte', 8, 'Ativo'
FROM empresa e
WHERE e.nome = 'Empresa Exemplo'
  AND NOT EXISTS (
      SELECT 1 FROM servico_ti s WHERE s.empresa_id = e.id AND s.nome = 'Service Desk'
  );

INSERT INTO servico_ti (empresa_id, nome, descricao, categoria, responsavel, sla_horas, status)
SELECT e.id, 'Correio Corporativo', 'Serviço de e-mail institucional e colaboração.', 'Comunicação', 'Infraestrutura', 12, 'Ativo'
FROM empresa e
WHERE e.nome = 'Empresa Exemplo'
  AND NOT EXISTS (
      SELECT 1 FROM servico_ti s WHERE s.empresa_id = e.id AND s.nome = 'Correio Corporativo'
  );

INSERT INTO servico_ti (empresa_id, nome, descricao, categoria, responsavel, sla_horas, status)
SELECT e.id, 'ERP Corporativo', 'Sustentação do sistema central de gestão empresarial.', 'Negócio', 'Sistemas Corporativos', 6, 'Ativo'
FROM empresa e
WHERE e.nome = 'Empresa Exemplo'
  AND NOT EXISTS (
      SELECT 1 FROM servico_ti s WHERE s.empresa_id = e.id AND s.nome = 'ERP Corporativo'
  );

-- Etapa 2.0: módulo de riscos
INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'A organização mantém inventário atualizado dos ativos de TI mais críticos para o negócio?', 'Ativos, Ameaças e Vulnerabilidades', 'Governança', 4
WHERE NOT EXISTS (SELECT 1 FROM questao WHERE pergunta = 'A organização mantém inventário atualizado dos ativos de TI mais críticos para o negócio?');

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'As principais ameaças aos ativos críticos de TI são identificadas e revisadas periodicamente?', 'Ativos, Ameaças e Vulnerabilidades', 'Governança', 4
WHERE NOT EXISTS (SELECT 1 FROM questao WHERE pergunta = 'As principais ameaças aos ativos críticos de TI são identificadas e revisadas periodicamente?');

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'As vulnerabilidades técnicas e processuais mais relevantes são avaliadas e priorizadas pela organização?', 'Ativos, Ameaças e Vulnerabilidades', 'Gestão', 4
WHERE NOT EXISTS (SELECT 1 FROM questao WHERE pergunta = 'As vulnerabilidades técnicas e processuais mais relevantes são avaliadas e priorizadas pela organização?');

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'A organização avalia impacto e probabilidade para classificar riscos de TI e definir prioridades de tratamento?', 'Gestão de Riscos', 'Governança', 5
WHERE NOT EXISTS (SELECT 1 FROM questao WHERE pergunta = 'A organização avalia impacto e probabilidade para classificar riscos de TI e definir prioridades de tratamento?');

INSERT INTO questao (pergunta, categoria, tipo_avaliacao, peso)
SELECT 'Existe plano de tratamento de riscos com responsáveis, prazos e monitoramento da execução?', 'Gestão de Riscos', 'Gestão', 5
WHERE NOT EXISTS (SELECT 1 FROM questao WHERE pergunta = 'Existe plano de tratamento de riscos com responsáveis, prazos e monitoramento da execução?');

-- Etapa 3: módulo de serviços de TI baseado em ITIL
INSERT INTO servico_ti (empresa_id, nome, descricao, categoria, responsavel, sla_horas, status)
SELECT e.id, 'Service Desk', 'Atendimento inicial e tratamento de chamados dos usuários.', 'Suporte', 'Coordenação de Suporte', 8, 'Ativo'
FROM empresa e
WHERE e.nome = 'Empresa Exemplo'
  AND NOT EXISTS (SELECT 1 FROM servico_ti s WHERE s.empresa_id = e.id AND s.nome = 'Service Desk');

INSERT INTO servico_ti (empresa_id, nome, descricao, categoria, responsavel, sla_horas, status)
SELECT e.id, 'Correio Corporativo', 'Serviço de e-mail institucional e colaboração.', 'Comunicação', 'Infraestrutura', 12, 'Ativo'
FROM empresa e
WHERE e.nome = 'Empresa Exemplo'
  AND NOT EXISTS (SELECT 1 FROM servico_ti s WHERE s.empresa_id = e.id AND s.nome = 'Correio Corporativo');

INSERT INTO servico_ti (empresa_id, nome, descricao, categoria, responsavel, sla_horas, status)
SELECT e.id, 'ERP Corporativo', 'Sustentação do sistema central de gestão empresarial.', 'Negócio', 'Sistemas Corporativos', 6, 'Ativo'
FROM empresa e
WHERE e.nome = 'Empresa Exemplo'
  AND NOT EXISTS (SELECT 1 FROM servico_ti s WHERE s.empresa_id = e.id AND s.nome = 'ERP Corporativo');
