package com.maturidade.ti.service;

import com.maturidade.ti.dto.*;
import com.maturidade.ti.model.*;
import com.maturidade.ti.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GovernancaService {

    private final EmpresaService empresaService;
    private final RespostaRepository respostaRepository;
    private final PdtiConfigRepository pdtiConfigRepository;
    private final PlanoAcao5w2hRepository planoAcaoRepository;
    private final GestaoRiscoRepository gestaoRiscoRepository;
    private final RoadmapTecnologicoRepository roadmapRepository;

    public GovernancaService(EmpresaService empresaService, RespostaRepository respostaRepository,
            PdtiConfigRepository pdtiConfigRepository, PlanoAcao5w2hRepository planoAcaoRepository,
            GestaoRiscoRepository gestaoRiscoRepository, RoadmapTecnologicoRepository roadmapRepository) {
        this.empresaService = empresaService;
        this.respostaRepository = respostaRepository;
        this.pdtiConfigRepository = pdtiConfigRepository;
        this.planoAcaoRepository = planoAcaoRepository;
        this.gestaoRiscoRepository = gestaoRiscoRepository;
        this.roadmapRepository = roadmapRepository;
    }

    // ===========================================================
    // GERAÇÃO AUTOMÁTICA COMPLETA
    // ===========================================================

    @Transactional
    public GovernancaResponseDTO gerarGovernancaCompleta(Long empresaId) {
        var empresa = empresaService.getEntity(empresaId);
        List<Resposta> respostas = respostaRepository.findByEmpresaId(empresaId);
        if (respostas.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A empresa ainda não possui respostas do questionário.");

        double scoreGeral = calcularScore(respostas);
        Map<String, Double> scoresPorCategoria = calcularScoresPorCategoria(respostas);

        // Gerar e salvar plano 5W2H automático
        planoAcaoRepository.deleteByEmpresaId(empresaId);
        List<PlanoAcao5w2h> planos = gerarPlano5w2h(empresa, respostas, scoresPorCategoria);
        planoAcaoRepository.saveAll(planos);

        // Gerar e salvar matriz de riscos automática
        gestaoRiscoRepository.deleteByEmpresaId(empresaId);
        List<GestaoRisco> riscos = gerarMatrizRiscos(empresa, scoresPorCategoria);
        gestaoRiscoRepository.saveAll(riscos);

        // Gerar roadmap
        roadmapRepository.deleteByEmpresaId(empresaId);
        List<RoadmapTecnologico> roadmap = gerarRoadmap(empresa, scoreGeral, scoresPorCategoria);
        roadmapRepository.saveAll(roadmap);

        // Criar/atualizar PDTI config automático
        gerarPdtiConfigAutomatico(empresa, scoreGeral, scoresPorCategoria);

        return montarResponse(empresaId, empresa.getNome(), scoreGeral);
    }

    // ===========================================================
    // MOTOR DE RECOMENDAÇÕES - PLANO 5W2H
    // ===========================================================

    private List<PlanoAcao5w2h> gerarPlano5w2h(Empresa empresa, List<Resposta> respostas, Map<String, Double> scoresPorCategoria) {
        List<PlanoAcao5w2h> planos = new ArrayList<>();

        for (Map.Entry<String, Double> entry : scoresPorCategoria.entrySet()) {
            String categoria = entry.getKey();
            double score = entry.getValue();
            PlanoAcao5w2h plano = buildPlano5w2h(empresa, categoria, score);
            planos.add(plano);
        }

        // Adicionar planos para questões com score <= 2 individualmente
        respostas.stream()
            .filter(r -> r.getValor() <= 2)
            .forEach(r -> {
                Double scoreCateg = scoresPorCategoria.getOrDefault(r.getQuestao().getCategoria(), 3.0);
                if (scoreCateg <= 2.0) return; // já coberto acima como crítica
                PlanoAcao5w2h p = buildPlanoEspecifico(empresa, r, scoreCateg);
                if (p != null) planos.add(p);
            });

        return planos;
    }

    private PlanoAcao5w2h buildPlano5w2h(Empresa empresa, String categoria, double score) {
        PlanoAcao5w2h p = new PlanoAcao5w2h();
        p.setEmpresa(empresa);
        p.setCategoriaOrigem(categoria);
        p.setScoreOrigem(Math.round(score * 100.0) / 100.0);
        p.setOnde("Ambiente corporativo de TI");

        if (score < 1.0) {
            p.setPrioridade("CRITICA");
            configurarPlanoCategoria(p, categoria, "CRITICA");
        } else if (score < 2.0) {
            p.setPrioridade("ALTA");
            configurarPlanoCategoria(p, categoria, "ALTA");
        } else if (score < 3.0) {
            p.setPrioridade("MEDIA");
            configurarPlanoCategoria(p, categoria, "MEDIA");
        } else {
            p.setPrioridade("BAIXA");
            configurarPlanoCategoria(p, categoria, "BAIXA");
        }

        return p;
    }

    private void configurarPlanoCategoria(PlanoAcao5w2h p, String categoria, String nivel) {
        switch (categoria) {
            case "Segurança" -> {
                p.setOQue("Implantar Programa de Segurança da Informação");
                p.setPorQue("Eliminar vulnerabilidades críticas e garantir conformidade com LGPD");
                p.setQuando(nivel.equals("CRITICA") ? "30 dias" : nivel.equals("ALTA") ? "60 dias" : "90 dias");
                p.setQuem("Gerente de TI / CISO");
                p.setComo("Implementar política formal de segurança, controle de acesso MFA, criptografia de dados sensíveis e treinamento de usuários");
                p.setQuanto(nivel.equals("CRITICA") ? "R$ 25.000" : "R$ 15.000");
            }
            case "Infraestrutura" -> {
                p.setOQue("Modernizar e Documentar Infraestrutura de TI");
                p.setPorQue("Aumentar disponibilidade, resiliência e rastreabilidade operacional");
                p.setQuando(nivel.equals("CRITICA") ? "45 dias" : "90 dias");
                p.setQuem("Gerente de Infraestrutura");
                p.setComo("Mapear ativos, implantar monitoramento automatizado, criar plano de continuidade e backup offsite com testes periódicos");
                p.setQuanto(nivel.equals("CRITICA") ? "R$ 40.000" : "R$ 20.000");
            }
            case "Governança" -> {
                p.setOQue("Estruturar Governança de TI com base COBIT");
                p.setPorQue("Alinhar TI à estratégia corporativa e criar mecanismos formais de decisão");
                p.setQuando(nivel.equals("CRITICA") ? "30 dias" : "60 dias");
                p.setQuem("CIO / Comitê de TI");
                p.setComo("Criar comitê estratégico de TI, definir papéis e responsabilidades, implantar rituais de governança mensais");
                p.setQuanto("R$ 10.000");
            }
            case "Processos" -> {
                p.setOQue("Formalizar e Padronizar Processos de TI");
                p.setPorQue("Reduzir dependência de pessoas-chave e aumentar previsibilidade operacional");
                p.setQuando("90 dias");
                p.setQuem("Gerente de TI");
                p.setComo("Documentar POPs, criar fluxos formais de mudança, homologação e versionamento de sistemas");
                p.setQuanto("R$ 8.000");
            }
            case "Serviços" -> {
                p.setOQue("Implantar Catálogo de Serviços e Gestão de SLA");
                p.setPorQue("Garantir visibilidade e previsibilidade dos serviços de TI prestados");
                p.setQuando("60 dias");
                p.setQuem("Analista de ITSM");
                p.setComo("Criar catálogo formal de serviços, definir SLAs, implantar ferramenta ITSM de abertura de chamados");
                p.setQuanto("R$ 12.000");
            }
            case "Estratégia" -> {
                p.setOQue("Elaborar PDTI Alinhado ao Planejamento Estratégico");
                p.setPorQue("Conectar investimentos em TI aos objetivos de negócio com métricas claras");
                p.setQuando("45 dias");
                p.setQuem("CIO / CEO");
                p.setComo("Conduzir workshops estratégicos, definir roadmap tecnológico e KPIs alinhados ao negócio");
                p.setQuanto("R$ 15.000");
            }
            case "Pessoas" -> {
                p.setOQue("Desenvolver Competências e Cultura de TI");
                p.setPorQue("Reduzir gaps técnicos e criar capacidade de resposta às demandas tecnológicas");
                p.setQuando("90 dias");
                p.setQuem("RH / Gerente de TI");
                p.setComo("Plano de capacitação em ITIL, COBIT e cibersegurança, programa de certificações e avaliação de desempenho");
                p.setQuanto("R$ 20.000");
            }
            default -> {
                p.setOQue("Elevar Maturidade em " + categoria);
                p.setPorQue("Corrigir deficiências identificadas no diagnóstico de TI");
                p.setQuando(nivel.equals("CRITICA") ? "30 dias" : nivel.equals("ALTA") ? "60 dias" : "90 dias");
                p.setQuem("Gerente de TI");
                p.setComo("Implementar práticas de mercado, treinar equipe e monitorar evolução com indicadores");
                p.setQuanto("R$ 10.000");
            }
        }
    }

    private PlanoAcao5w2h buildPlanoEspecifico(Empresa empresa, Resposta r, double score) {
        PlanoAcao5w2h p = new PlanoAcao5w2h();
        p.setEmpresa(empresa);
        p.setCategoriaOrigem(r.getQuestao().getCategoria());
        p.setScoreOrigem(score);
        p.setPrioridade("ALTA");
        p.setOQue("Corrigir: " + truncar(r.getQuestao().getPergunta(), 120));
        p.setPorQue("Questão crítica identificada no diagnóstico com score baixo");
        p.setOnde("Ambiente corporativo de TI");
        p.setQuando("60 dias");
        p.setQuem("Gerente de TI");
        p.setComo("Implementar controle específico, treinar responsáveis e monitorar evolução");
        p.setQuanto("A definir conforme escopo");
        return p;
    }

    // ===========================================================
    // MOTOR DE RISCOS AUTOMÁTICO
    // ===========================================================

    private List<GestaoRisco> gerarMatrizRiscos(Empresa empresa, Map<String, Double> scores) {
        List<GestaoRisco> riscos = new ArrayList<>();

        for (Map.Entry<String, Double> entry : scores.entrySet()) {
            String cat = entry.getKey();
            double score = entry.getValue();
            if (score < 3.0) {
                GestaoRisco r = buildRisco(empresa, cat, score);
                riscos.add(r);
            }
        }

        // Riscos transversais sempre presentes
        riscos.add(buildRiscoTransversal(empresa, "Descontinuidade Operacional",
                "Infraestrutura", "Falha de hardware crítico",
                "Ausência de plano de recuperação de desastres", 5, "Alto",
                "Paralisação total das operações por falha no datacenter ou ataque cibernético",
                "Interrupção do negócio com impacto financeiro severo",
                "Mitigar", "Gerente de Infraestrutura"));

        riscos.add(buildRiscoTransversal(empresa, "Violação de Dados Pessoais (LGPD)",
                "Segurança", "Vazamento de dados",
                "Ausência de controles de acesso adequados e criptografia", 5, "Moderado",
                "Exposição de dados pessoais de colaboradores, clientes ou parceiros",
                "Multas regulatórias e dano reputacional",
                "Eliminar", "DPO / Jurídico"));

        riscos.add(buildRiscoTransversal(empresa, "Dependência de Pessoas-Chave",
                "Pessoas", "Saída de colaboradores críticos",
                "Processos não documentados e conhecimento concentrado", 4, "Alto",
                "Perda de funcionário com conhecimento único sobre sistemas críticos",
                "Paralisação de processos e elevação de custos de substituição",
                "Reduzir", "RH / Gerente de TI"));

        return riscos;
    }

    private GestaoRisco buildRisco(Empresa empresa, String categoria, double score) {
        GestaoRisco r = new GestaoRisco();
        r.setEmpresa(empresa);
        r.setCategoriaOrigem(categoria);
        r.setTipo("Ameaça");
        r.setStatus("Identificado");

        int probNum = score < 1.0 ? 5 : score < 2.0 ? 4 : 3;
        int impactoNum = score < 1.0 ? 5 : score < 2.0 ? 4 : 3;

        r.setImpacto(impactoNum);
        r.setNivelRiscoNum(probNum * impactoNum);

        switch (categoria) {
            case "Segurança" -> {
                r.setDescricao("Falha crítica nos controles de segurança da informação");
                r.setAtivo("Sistemas e Dados Corporativos");
                r.setAmeaca("Ataque cibernético, acesso não autorizado, ransomware");
                r.setVulnerabilidade("Ausência de política formal de segurança e controles básicos");
                r.setCausa("Investimento insuficiente e falta de governança de segurança");
                r.setConsequencia("Vazamento de dados, fraudes, multas LGPD e paralisação de sistemas");
                r.setProbabilidade(score < 1.0 ? "Muito Alto" : "Alto");
                r.setNivelRisco(nivelRisco(r.getNivelRiscoNum()));
                r.setTratamento("Mitigar");
                r.setResponsavel("CISO / Gerente de TI");
            }
            case "Infraestrutura" -> {
                r.setDescricao("Falha de infraestrutura sem capacidade de recuperação");
                r.setAtivo("Datacenter e Servidores");
                r.setAmeaca("Falha de hardware, desastre físico, perda de conectividade");
                r.setVulnerabilidade("Ausência de redundância, backup não testado e plano de contingência");
                r.setCausa("Infraestrutura obsoleta e sem monitoramento adequado");
                r.setConsequencia("Indisponibilidade de sistemas críticos por período prolongado");
                r.setProbabilidade(score < 1.5 ? "Alto" : "Moderado");
                r.setNivelRisco(nivelRisco(r.getNivelRiscoNum()));
                r.setTratamento("Mitigar");
                r.setResponsavel("Gerente de Infraestrutura");
            }
            case "Governança" -> {
                r.setDescricao("Ausência de governança de TI alinhada ao negócio");
                r.setAtivo("Processos de Decisão de TI");
                r.setAmeaca("Investimentos mal direcionados, projetos sem ROI");
                r.setVulnerabilidade("Falta de estrutura formal de governança e KPIs");
                r.setCausa("TI tratada como suporte operacional sem visão estratégica");
                r.setConsequencia("Perda de competitividade e desperdício de recursos tecnológicos");
                r.setProbabilidade("Moderado");
                r.setNivelRisco(nivelRisco(r.getNivelRiscoNum()));
                r.setTratamento("Reduzir");
                r.setResponsavel("CIO / Board");
            }
            case "Processos" -> {
                r.setDescricao("Processos de TI não documentados e inconsistentes");
                r.setAtivo("Processos Operacionais de TI");
                r.setAmeaca("Erros operacionais, indisponibilidade por mudanças mal gerenciadas");
                r.setVulnerabilidade("Ausência de gestão de mudanças e ambientes de homologação");
                r.setCausa("Cultura reativa sem padronização de processos");
                r.setConsequencia("Incidentes causados por mudanças em produção sem controle");
                r.setProbabilidade("Alto");
                r.setNivelRisco(nivelRisco(r.getNivelRiscoNum()));
                r.setTratamento("Reduzir");
                r.setResponsavel("Gerente de TI");
            }
            default -> {
                r.setDescricao("Risco crítico em " + categoria);
                r.setAtivo("Área de " + categoria);
                r.setAmeaca("Falhas operacionais e conformidade");
                r.setVulnerabilidade("Controles insuficientes identificados no diagnóstico");
                r.setCausa("Score abaixo do esperado: " + String.format("%.2f", score));
                r.setConsequencia("Impacto operacional e estratégico na organização");
                r.setProbabilidade(score < 1.5 ? "Alto" : "Moderado");
                r.setNivelRisco(nivelRisco(r.getNivelRiscoNum()));
                r.setTratamento("Mitigar");
                r.setResponsavel("Gerente de TI");
            }
        }
        return r;
    }

    private GestaoRisco buildRiscoTransversal(Empresa empresa, String descricao, String categoria,
            String ameaca, String vulnerabilidade, int impacto, String probabilidade,
            String causa, String consequencia, String tratamento, String responsavel) {
        GestaoRisco r = new GestaoRisco();
        r.setEmpresa(empresa);
        r.setCategoriaOrigem(categoria);
        r.setDescricao(descricao);
        r.setTipo("Ameaça");
        r.setAtivo("Operações Corporativas");
        r.setAmeaca(ameaca);
        r.setVulnerabilidade(vulnerabilidade);
        r.setCausa(causa);
        r.setConsequencia(consequencia);
        r.setImpacto(impacto);
        r.setProbabilidade(probabilidade);
        int probNum = switch (probabilidade) { case "Muito Alto" -> 5; case "Alto" -> 4; case "Moderado" -> 3; case "Baixo" -> 2; default -> 1; };
        r.setNivelRiscoNum(impacto * probNum);
        r.setNivelRisco(nivelRisco(r.getNivelRiscoNum()));
        r.setTratamento(tratamento);
        r.setResponsavel(responsavel);
        r.setStatus("Identificado");
        return r;
    }

    // ===========================================================
    // GERAÇÃO DE ROADMAP
    // ===========================================================

    private List<RoadmapTecnologico> gerarRoadmap(Empresa empresa, double score, Map<String, Double> scores) {
        List<RoadmapTecnologico> roadmap = new ArrayList<>();

        // T1 - 90 dias (crítico)
        scores.entrySet().stream()
            .filter(e -> e.getValue() < 2.0)
            .forEach(e -> {
                RoadmapTecnologico item = new RoadmapTecnologico();
                item.setEmpresa(empresa);
                item.setIniciativa("Estabilizar " + e.getKey());
                item.setDescricao("Ação emergencial para corrigir falhas críticas em " + e.getKey());
                item.setTrimestre("T1 - 90 dias");
                item.setPrioridade("CRITICA");
                item.setCategoria(e.getKey());
                item.setStatus("PLANEJADO");
                roadmap.add(item);
            });

        // T2 - 180 dias (evolutivo)
        scores.entrySet().stream()
            .filter(e -> e.getValue() >= 2.0 && e.getValue() < 3.0)
            .forEach(e -> {
                RoadmapTecnologico item = new RoadmapTecnologico();
                item.setEmpresa(empresa);
                item.setIniciativa("Evoluir " + e.getKey());
                item.setDescricao("Elevar maturidade em " + e.getKey() + " para nível gerenciado");
                item.setTrimestre("T2 - 180 dias");
                item.setPrioridade("ALTA");
                item.setCategoria(e.getKey());
                item.setStatus("PLANEJADO");
                roadmap.add(item);
            });

        // T3 - 365 dias (otimização)
        RoadmapTecnologico pdti = new RoadmapTecnologico();
        pdti.setEmpresa(empresa);
        pdti.setIniciativa("Consolidar Governança e PDTI");
        pdti.setDescricao("Implementar ciclo completo de governança de TI com revisão anual do PDTI");
        pdti.setTrimestre("T3 - 365 dias");
        pdti.setPrioridade("MEDIA");
        pdti.setCategoria("Governança");
        pdti.setStatus("PLANEJADO");
        roadmap.add(pdti);

        return roadmap;
    }

    // ===========================================================
    // PDTI CONFIG AUTOMÁTICO
    // ===========================================================

    private void gerarPdtiConfigAutomatico(Empresa empresa, double score, Map<String, Double> scores) {
        PdtiConfig config = pdtiConfigRepository.findByEmpresaId(empresa.getId())
                .orElse(new PdtiConfig());
        config.setEmpresa(empresa);

        if (config.getMissao() == null || config.getMissao().isBlank()) {
            config.setMissao("Prover soluções tecnológicas seguras, eficientes e alinhadas à estratégia do negócio, garantindo disponibilidade, conformidade e valor sustentável para " + empresa.getNome() + ".");
        }
        if (config.getVisao() == null || config.getVisao().isBlank()) {
            config.setVisao("Ser reconhecida como referência em maturidade e governança de TI no segmento de atuação até 2027, sustentando a expansão e a inovação de " + empresa.getNome() + ".");
        }
        if (config.getObjetivosEstrategicos() == null || config.getObjetivosEstrategicos().isBlank()) {
            config.setObjetivosEstrategicos("1. Elevar a disponibilidade dos sistemas críticos para acima de 99,5%\n2. Implantar governança de segurança da informação com conformidade LGPD\n3. Estruturar processos formais de TI baseados em ITIL e COBIT\n4. Alinhar o roadmap tecnológico aos objetivos estratégicos do negócio\n5. Desenvolver competências técnicas e gerenciais da equipe de TI");
        }
        if (config.getMetasEstrategicas() == null || config.getMetasEstrategicas().isBlank()) {
            config.setMetasEstrategicas("• Reduzir incidentes críticos em 60% nos primeiros 90 dias\n• Implementar MFA e revisão de acessos em 30 dias\n• Criar catálogo de serviços e SLAs em 60 dias\n• Atingir score de maturidade ≥ 3,5 em 12 meses\n• Conformidade LGPD em 90 dias");
        }

        String piorCategoria = scores.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse("TI em geral");

        config.setAnaliseSituacional("A análise situacional revela score geral de maturidade de TI de " +
                String.format("%.2f", score) + "/5,00 (" + nivel(score) + "). " +
                "A área mais crítica identificada é " + piorCategoria + ", que requer ação imediata. " +
                "O diagnóstico aponta necessidade de estruturação de governança, modernização de processos " +
                "e fortalecimento da segurança da informação para suportar os objetivos estratégicos da organização.");

        pdtiConfigRepository.save(config);
    }

    // ===========================================================
    // CRUD - PDTI CONFIG
    // ===========================================================

    public PdtiConfigDTO getPdtiConfig(Long empresaId) {
        empresaService.getEntity(empresaId);
        PdtiConfig config = pdtiConfigRepository.findByEmpresaId(empresaId)
                .orElse(new PdtiConfig());
        return toDTO(config);
    }

    @Transactional
    public PdtiConfigDTO salvarPdtiConfig(Long empresaId, PdtiConfigDTO dto) {
        var empresa = empresaService.getEntity(empresaId);
        PdtiConfig config = pdtiConfigRepository.findByEmpresaId(empresaId).orElse(new PdtiConfig());
        config.setEmpresa(empresa);
        config.setMissao(dto.getMissao());
        config.setVisao(dto.getVisao());
        config.setObjetivosEstrategicos(dto.getObjetivosEstrategicos());
        config.setMetasEstrategicas(dto.getMetasEstrategicas());
        config.setPeriodoVigenciaInicio(dto.getPeriodoVigenciaInicio());
        config.setPeriodoVigenciaFim(dto.getPeriodoVigenciaFim());
        config.setResponsavel(dto.getResponsavel());
        config.setPatrocinadorExecutivo(dto.getPatrocinadorExecutivo());
        config.setContextoOrganizacional(dto.getContextoOrganizacional());
        config.setContextoTecnologico(dto.getContextoTecnologico());
        config.setAnaliseSituacional(dto.getAnaliseSituacional());
        config.setAtualizadoEm(java.time.LocalDateTime.now());
        return toDTO(pdtiConfigRepository.save(config));
    }

    // ===========================================================
    // CRUD - PLANO 5W2H
    // ===========================================================

    public List<PlanoAcao5w2hDTO> getPlanoAcao(Long empresaId) {
        empresaService.getEntity(empresaId);
        return planoAcaoRepository.findByEmpresaIdOrderByCriadoEmDesc(empresaId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public PlanoAcao5w2hDTO salvarPlano(Long empresaId, PlanoAcao5w2hDTO dto) {
        var empresa = empresaService.getEntity(empresaId);
        PlanoAcao5w2h p = dto.getId() != null
                ? planoAcaoRepository.findById(dto.getId()).orElse(new PlanoAcao5w2h())
                : new PlanoAcao5w2h();
        p.setEmpresa(empresa);
        p.setOQue(dto.getOQue()); p.setPorQue(dto.getPorQue()); p.setOnde(dto.getOnde());
        p.setQuando(dto.getQuando()); p.setQuem(dto.getQuem()); p.setComo(dto.getComo());
        p.setQuanto(dto.getQuanto()); p.setPrioridade(dto.getPrioridade()); p.setStatus(dto.getStatus());
        p.setCategoriaOrigem(dto.getCategoriaOrigem()); p.setScoreOrigem(dto.getScoreOrigem());
        return toDTO(planoAcaoRepository.save(p));
    }

    @Transactional
    public void deletarPlano(Long id) { planoAcaoRepository.deleteById(id); }

    // ===========================================================
    // CRUD - GESTÃO DE RISCOS
    // ===========================================================

    public List<GestaoRiscoDTO> getRiscos(Long empresaId) {
        empresaService.getEntity(empresaId);
        return gestaoRiscoRepository.findByEmpresaIdOrderByCriadoEmDesc(empresaId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public GestaoRiscoDTO salvarRisco(Long empresaId, GestaoRiscoDTO dto) {
        var empresa = empresaService.getEntity(empresaId);
        GestaoRisco r = dto.getId() != null
                ? gestaoRiscoRepository.findById(dto.getId()).orElse(new GestaoRisco())
                : new GestaoRisco();
        r.setEmpresa(empresa);
        r.setDescricao(dto.getDescricao()); r.setTipo(dto.getTipo()); r.setAtivo(dto.getAtivo());
        r.setAmeaca(dto.getAmeaca()); r.setVulnerabilidade(dto.getVulnerabilidade());
        r.setCausa(dto.getCausa()); r.setConsequencia(dto.getConsequencia());
        r.setImpacto(dto.getImpacto()); r.setProbabilidade(dto.getProbabilidade());
        r.setTratamento(dto.getTratamento()); r.setResponsavel(dto.getResponsavel());
        r.setStatus(dto.getStatus()); r.setCategoriaOrigem(dto.getCategoriaOrigem());
        // Calcular nível de risco
        int probNum = switch (r.getProbabilidade() != null ? r.getProbabilidade() : "") {
            case "Muito Alto" -> 5; case "Alto" -> 4; case "Moderado" -> 3; case "Baixo" -> 2; default -> 1;
        };
        r.setNivelRiscoNum((r.getImpacto() != null ? r.getImpacto() : 3) * probNum);
        r.setNivelRisco(nivelRisco(r.getNivelRiscoNum()));
        return toDTO(gestaoRiscoRepository.save(r));
    }

    @Transactional
    public void deletarRisco(Long id) { gestaoRiscoRepository.deleteById(id); }

    // ===========================================================
    // MONTAGEM DE RESPONSE
    // ===========================================================

    private GovernancaResponseDTO montarResponse(Long empresaId, String nomeEmpresa, double score) {
        GovernancaResponseDTO dto = new GovernancaResponseDTO();
        dto.setEmpresa(nomeEmpresa);
        dto.setScoreGeral(Math.round(score * 100.0) / 100.0);
        dto.setNivel(nivel(score));
        dto.setPdtiConfig(getPdtiConfig(empresaId));
        dto.setPlanoAcao(getPlanoAcao(empresaId));
        dto.setRiscos(getRiscos(empresaId));
        return dto;
    }

    // ===========================================================
    // HELPERS
    // ===========================================================

    private double calcularScore(List<Resposta> respostas) {
        double somaPesos = respostas.stream().mapToDouble(r -> r.getQuestao().getPeso()).sum();
        double somaPonderada = respostas.stream().mapToDouble(r -> r.getValor() * r.getQuestao().getPeso()).sum();
        return somaPesos == 0 ? 0 : somaPonderada / somaPesos;
    }

    private Map<String, Double> calcularScoresPorCategoria(List<Resposta> respostas) {
        return respostas.stream()
                .collect(Collectors.groupingBy(r -> r.getQuestao().getCategoria(),
                        Collectors.collectingAndThen(Collectors.toList(), this::calcularScore)));
    }

    private String nivel(double s) {
        if (s <= 1.0) return "Nível 1 - Inicial (Caótico)";
        if (s <= 2.0) return "Nível 2 - Repetível (Reativo)";
        if (s <= 3.5) return "Nível 3 - Definido (Proativo)";
        if (s <= 4.5) return "Nível 4 - Gerenciado (Mensurável)";
        return "Nível 5 - Otimizado (Estratégico)";
    }

    private String nivelRisco(int n) {
        if (n >= 20) return "Crítico";
        if (n >= 12) return "Alto";
        if (n >= 6) return "Moderado";
        if (n >= 3) return "Baixo";
        return "Desprezível";
    }

    private String truncar(String s, int max) {
        return s != null && s.length() > max ? s.substring(0, max) + "..." : s;
    }

    private PdtiConfigDTO toDTO(PdtiConfig c) {
        PdtiConfigDTO dto = new PdtiConfigDTO();
        dto.setId(c.getId());
        dto.setMissao(c.getMissao()); dto.setVisao(c.getVisao());
        dto.setObjetivosEstrategicos(c.getObjetivosEstrategicos());
        dto.setMetasEstrategicas(c.getMetasEstrategicas());
        dto.setPeriodoVigenciaInicio(c.getPeriodoVigenciaInicio());
        dto.setPeriodoVigenciaFim(c.getPeriodoVigenciaFim());
        dto.setResponsavel(c.getResponsavel());
        dto.setPatrocinadorExecutivo(c.getPatrocinadorExecutivo());
        dto.setContextoOrganizacional(c.getContextoOrganizacional());
        dto.setContextoTecnologico(c.getContextoTecnologico());
        dto.setAnaliseSituacional(c.getAnaliseSituacional());
        return dto;
    }

    private PlanoAcao5w2hDTO toDTO(PlanoAcao5w2h p) {
        PlanoAcao5w2hDTO dto = new PlanoAcao5w2hDTO();
        dto.setId(p.getId()); dto.setOQue(p.getOQue()); dto.setPorQue(p.getPorQue());
        dto.setOnde(p.getOnde()); dto.setQuando(p.getQuando()); dto.setQuem(p.getQuem());
        dto.setComo(p.getComo()); dto.setQuanto(p.getQuanto()); dto.setPrioridade(p.getPrioridade());
        dto.setStatus(p.getStatus()); dto.setCategoriaOrigem(p.getCategoriaOrigem());
        dto.setScoreOrigem(p.getScoreOrigem());
        return dto;
    }

    private GestaoRiscoDTO toDTO(GestaoRisco r) {
        GestaoRiscoDTO dto = new GestaoRiscoDTO();
        dto.setId(r.getId()); dto.setDescricao(r.getDescricao()); dto.setTipo(r.getTipo());
        dto.setAtivo(r.getAtivo()); dto.setAmeaca(r.getAmeaca()); dto.setVulnerabilidade(r.getVulnerabilidade());
        dto.setCausa(r.getCausa()); dto.setConsequencia(r.getConsequencia()); dto.setImpacto(r.getImpacto());
        dto.setProbabilidade(r.getProbabilidade()); dto.setNivelRisco(r.getNivelRisco());
        dto.setNivelRiscoNum(r.getNivelRiscoNum()); dto.setTratamento(r.getTratamento());
        dto.setResponsavel(r.getResponsavel()); dto.setStatus(r.getStatus());
        dto.setCategoriaOrigem(r.getCategoriaOrigem());
        return dto;
    }
}
