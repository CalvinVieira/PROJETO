package com.maturidade.ti.service;

import com.maturidade.ti.dto.CategoriaScoreDTO;
import com.maturidade.ti.dto.PlanoAcaoDTO;
import com.maturidade.ti.dto.RelatorioResponseDTO;
import com.maturidade.ti.model.IncidenteTI;
import com.maturidade.ti.model.Resposta;
import com.maturidade.ti.repository.IncidenteTIRepository;
import com.maturidade.ti.repository.RespostaRepository;
import com.maturidade.ti.repository.ServicoTIRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class RelatorioService {
    private final EmpresaService empresaService;
    private final RespostaRepository respostaRepository;
    private final ServicoTIRepository servicoTIRepository;
    private final IncidenteTIRepository incidenteTIRepository;

    public RelatorioService(EmpresaService empresaService, RespostaRepository respostaRepository, ServicoTIRepository servicoTIRepository, IncidenteTIRepository incidenteTIRepository) {
        this.empresaService = empresaService;
        this.respostaRepository = respostaRepository;
        this.servicoTIRepository = servicoTIRepository;
        this.incidenteTIRepository = incidenteTIRepository;
    }

    public RelatorioResponseDTO gerar(Long empresaId) {
        var empresa = empresaService.getEntity(empresaId);
        List<Resposta> respostas = respostaRepository.findByEmpresaId(empresaId);
        if (respostas.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A empresa ainda não possui respostas");
        }

        double scoreGeral = calcularScore(respostas);
        Map<String, List<Resposta>> porCategoria = respostas.stream()
                .collect(Collectors.groupingBy(r -> r.getQuestao().getCategoria(), TreeMap::new, Collectors.toList()));

        List<CategoriaScoreDTO> scores = porCategoria.entrySet().stream()
                .map(e -> new CategoriaScoreDTO(e.getKey(), round(calcularScore(e.getValue()))))
                .sorted(Comparator.comparing(CategoriaScoreDTO::getScore).reversed())
                .toList();

        CategoriaScoreDTO melhor = scores.stream().findFirst().orElse(null);
        CategoriaScoreDTO pior = scores.stream().sorted(Comparator.comparing(CategoriaScoreDTO::getScore)).findFirst().orElse(null);

        Map<String, Long> distribuicaoDimensoes = respostas.stream()
                .map(Resposta::getDimensao)
                .filter(Objects::nonNull)
                .filter(s -> !s.isBlank())
                .collect(Collectors.groupingBy(s -> s, LinkedHashMap::new, Collectors.counting()));

        Map<String, Double> scorePorTipoAvaliacao = respostas.stream()
                .filter(r -> r.getQuestao().getTipoAvaliacao() != null && !r.getQuestao().getTipoAvaliacao().isBlank())
                .collect(Collectors.groupingBy(
                        r -> r.getQuestao().getTipoAvaliacao(),
                        LinkedHashMap::new,
                        Collectors.collectingAndThen(Collectors.toList(), this::calcularScore)
                ));
        scorePorTipoAvaliacao.replaceAll((k, v) -> round(v));

        List<String> pontosFortes = scores.stream().filter(s -> s.getScore() >= 4.0).limit(3)
                .map(s -> s.getCategoria() + " (" + fmt(s.getScore()) + ")")
                .toList();

        List<String> pontosCriticos = scores.stream().sorted(Comparator.comparing(CategoriaScoreDTO::getScore)).limit(3)
                .map(s -> s.getCategoria() + " (" + fmt(s.getScore()) + ")")
                .toList();

        RelatorioResponseDTO dto = new RelatorioResponseDTO();
        dto.setEmpresa(empresa.getNome());
        dto.setScoreGeral(round(scoreGeral));
        dto.setScorePorCategoria(scores.stream().sorted(Comparator.comparing(CategoriaScoreDTO::getCategoria)).toList());
        dto.setScorePorTipoAvaliacao(scorePorTipoAvaliacao);
        dto.setPontosFortes(pontosFortes);
        dto.setPontosCriticos(pontosCriticos);
        dto.setMelhorCategoria(melhor != null ? melhor.getCategoria() : "-");
        dto.setPiorCategoria(pior != null ? pior.getCategoria() : "-");
        dto.setNivel(nivel(scoreGeral));
        dto.setJustificativa(montarJustificativa(scoreGeral, scorePorTipoAvaliacao, melhor, pior));
        dto.setConclusao(montarConclusao(scoreGeral, pior));
        dto.setRecomendacao(montarRecomendacao(scoreGeral, pior));
        dto.setEvidencias(respostas.stream().map(Resposta::getEvidencia).filter(Objects::nonNull).map(String::trim).filter(s -> !s.isBlank()).distinct().toList());
        dto.setPlanosManuais(respostas.stream().map(Resposta::getPlanoAcao).filter(Objects::nonNull).map(String::trim).filter(s -> !s.isBlank()).distinct().toList());
        dto.setPlanoAcaoAutomatico(gerarPlano(respostas));
        dto.setRoadmap(montarRoadmap(scoreGeral, pior != null ? pior.getCategoria() : null));
        dto.setDistribuicaoDimensoes(distribuicaoDimensoes);

        preencherBlocoOperacional(dto, empresaId, pior != null ? pior.getCategoria() : null);
        return dto;
    }

    private void preencherBlocoOperacional(RelatorioResponseDTO dto, Long empresaId, String piorCategoria) {
        long totalServicos = servicoTIRepository.countByEmpresaId(empresaId);
        List<IncidenteTI> incidentes = incidenteTIRepository.findByEmpresaIdOrderByDataAberturaDesc(empresaId);
        dto.setTotalServicos(totalServicos);
        dto.setTotalIncidentes((long) incidentes.size());

        long abertos = incidentes.stream().filter(i -> i.getStatus() != null)
                .filter(i -> !i.getStatus().equalsIgnoreCase("Resolvido") && !i.getStatus().equalsIgnoreCase("Fechado")).count();
        long resolvidos = incidentes.stream().filter(i -> i.getStatus() != null)
                .filter(i -> i.getStatus().equalsIgnoreCase("Resolvido") || i.getStatus().equalsIgnoreCase("Fechado")).count();
        long dentroSla = incidentes.stream().filter(this::estaDentroSla).count();
        long foraSla = incidentes.stream().filter(this::estaForaSla).count();
        long comSlaFechado = dentroSla + foraSla;

        dto.setIncidentesAbertos(abertos);
        dto.setIncidentesResolvidos(resolvidos);
        dto.setIncidentesDentroSla(dentroSla);
        dto.setIncidentesForaSla(foraSla);
        dto.setPercentualCumprimentoSla(comSlaFechado > 0 ? round((dentroSla * 100.0) / comSlaFechado) : null);
        boolean possui = totalServicos > 0 || !incidentes.isEmpty();
        dto.setPossuiDadosOperacionais(possui);
        dto.setAnaliseOperacional(possui ? montarAnaliseOperacional(totalServicos, incidentes.size(), abertos, resolvidos, dentroSla, foraSla, dto.getPercentualCumprimentoSla(), piorCategoria) : null);
    }

    private boolean estaDentroSla(IncidenteTI incidente) {
        if (incidente.getSlaHoras() == null || incidente.getDataAbertura() == null || incidente.getDataFechamento() == null) return false;
        return Duration.between(incidente.getDataAbertura(), incidente.getDataFechamento()).toHours() <= incidente.getSlaHoras();
    }

    private boolean estaForaSla(IncidenteTI incidente) {
        if (incidente.getSlaHoras() == null || incidente.getDataAbertura() == null || incidente.getDataFechamento() == null) return false;
        return Duration.between(incidente.getDataAbertura(), incidente.getDataFechamento()).toHours() > incidente.getSlaHoras();
    }

    private double calcularScore(List<Resposta> respostas) {
        double somaPesos = respostas.stream().mapToDouble(r -> r.getQuestao().getPeso()).sum();
        double somaPonderada = respostas.stream().mapToDouble(r -> r.getValor() * r.getQuestao().getPeso()).sum();
        return somaPesos == 0 ? 0 : somaPonderada / somaPesos;
    }

    private double round(double d) { return Math.round(d * 100.0) / 100.0; }
    private String fmt(double d) { return String.format(java.util.Locale.US, "%.2f", d); }

    private String nivel(double s) {
        if (s <= 1.0) return "Nível 1 - Inicial (Caótico)";
        if (s <= 2.0) return "Nível 2 - Repetível (Reativo)";
        if (s <= 3.5) return "Nível 3 - Definido (Proativo)";
        if (s <= 4.5) return "Nível 4 - Gerenciado (Mensurável)";
        return "Nível 5 - Otimizado (Estratégico)";
    }

    private String montarJustificativa(double score, Map<String, Double> scorePorTipo, CategoriaScoreDTO melhor, CategoriaScoreDTO pior) {
        String governanca = scorePorTipo.get("Governança") != null ? fmt(scorePorTipo.get("Governança")) : "-";
        String gestao = scorePorTipo.get("Gestão") != null ? fmt(scorePorTipo.get("Gestão")) : "-";
        return "Com score geral de " + fmt(score) + ", a organização se encontra em " + nivel(score) + ". A leitura estratégica mostra Governança em " + governanca + " e Gestão em " + gestao + ". O melhor desempenho apareceu em " + (melhor != null ? melhor.getCategoria() : "-") + ", enquanto " + (pior != null ? pior.getCategoria() : "-") + " concentra as lacunas mais relevantes para a evolução da TI.";
    }

    private String montarConclusao(double score, CategoriaScoreDTO pior) {
        if (score <= 2.0) return "A maturidade da TI ainda é baixa e depende de práticas pouco padronizadas. O foco imediato deve ser estruturar processos mínimos, reduzir dependência de pessoas-chave e tratar fragilidades críticas em " + (pior != null ? pior.getCategoria() : "categorias prioritárias") + ".";
        if (score <= 3.5) return "A empresa já possui fundamentos importantes, mas precisa consolidar disciplina operacional, mensuração e governança para alcançar maior previsibilidade, confiabilidade e alinhamento com o negócio.";
        if (score <= 4.5) return "A TI demonstra maturidade gerenciada, com base consistente para ganhos de eficiência, conformidade e resiliência. O próximo passo é fortalecer automação, indicadores executivos e melhoria contínua.";
        return "A TI opera em nível estratégico, com forte capacidade de sustentar valor para o negócio. O desafio agora é ampliar inovação, inteligência analítica e otimização contínua.";
    }

    private String montarRecomendacao(double score, CategoriaScoreDTO pior) {
        String foco = pior != null ? pior.getCategoria() : "categoria prioritária";
        if (score <= 2.0) return "Prioridade crítica: formalizar políticas, rotinas e controles em " + foco + ", registrar evidências de execução e implantar acompanhamento gerencial mínimo.";
        if (score <= 3.5) return "Priorize a padronização e a mensuração em " + foco + ", com indicadores claros, responsáveis definidos, cronograma de melhoria e revisão executiva periódica.";
        if (score <= 4.5) return "Avance em " + foco + " com automação, painéis gerenciais e metas de desempenho. Isso reduzirá risco operacional e aumentará previsibilidade para a empresa.";
        return "Mantenha o ciclo de melhoria contínua em " + foco + " e fortaleça iniciativas de inovação, analytics e governança orientada a valor.";
    }

    private List<String> montarRoadmap(double score, String piorCategoria) {
        List<String> roadmap = new ArrayList<>();
        if (score <= 2.0) {
            roadmap.add("Formalizar políticas, papéis e responsáveis da TI.");
            roadmap.add("Padronizar rotinas críticas e criar acompanhamento mínimo de execução.");
            roadmap.add("Atacar primeiro a categoria mais vulnerável: " + (piorCategoria != null ? piorCategoria : "frente prioritária") + ".");
            roadmap.add("Definir indicadores básicos de desempenho e confiabilidade.");
            roadmap.add("Criar disciplina de revisão executiva da evolução da TI.");
            return roadmap;
        }
        if (score <= 3.5) {
            roadmap.add("Consolidar processos e controles já existentes.");
            roadmap.add("Definir KPIs por categoria e por tipo de avaliação.");
            roadmap.add("Elevar maturidade da frente mais fraca: " + (piorCategoria != null ? piorCategoria : "frente prioritária") + ".");
            roadmap.add("Aprimorar segurança, continuidade e prestação de serviços.");
            roadmap.add("Fortalecer alinhamento entre TI e metas do negócio.");
            return roadmap;
        }
        if (score <= 4.5) {
            roadmap.add("Expandir automação e monitoramento gerencial.");
            roadmap.add("Transformar indicadores em rituais de decisão executiva.");
            roadmap.add("Otimizar experiência dos usuários e qualidade dos serviços.");
            roadmap.add("Integrar riscos, conformidade e melhoria contínua.");
            roadmap.add("Aumentar previsibilidade e capacidade de resposta da TI.");
            return roadmap;
        }
        roadmap.add("Sustentar governança orientada a valor e desempenho.");
        roadmap.add("Aprofundar inovação, analytics e inteligência operacional.");
        roadmap.add("Reforçar resiliência, escalabilidade e excelência de serviços.");
        roadmap.add("Expandir o uso de dados para decisões estratégicas.");
        roadmap.add("Promover melhoria contínua com visão executiva e de negócio.");
        return roadmap;
    }

    private String montarAnaliseOperacional(long totalServicos, long totalIncidentes, long abertos, long resolvidos, long dentroSla, long foraSla, Double percentualSla, String piorCategoria) {
        if (totalIncidentes == 0) {
            return "Há " + totalServicos + " serviço(s) cadastrado(s), mas ainda não existem incidentes registrados. Isso limita a leitura operacional do suporte e do cumprimento de SLA, embora o relatório estratégico continue válido.";
        }
        String base = "Foram identificados " + totalServicos + " serviço(s) de TI e " + totalIncidentes + " incidente(s), sendo " + abertos + " aberto(s) e " + resolvidos + " resolvido(s).";
        if (percentualSla == null) return base + " Ainda não há volume suficiente de incidentes fechados com SLA definido para medir com segurança o desempenho operacional.";
        if (percentualSla < 70) return base + " O cumprimento de SLA está em " + fmt(percentualSla) + "%, com " + foraSla + " incidente(s) fora do prazo. Isso sinaliza fragilidade operacional e reforça a urgência de melhorar " + (piorCategoria != null ? piorCategoria : "a principal frente crítica") + ".";
        if (percentualSla < 90) return base + " O cumprimento de SLA está em " + fmt(percentualSla) + "%, indicando uma operação razoável, mas ainda com espaço para ganho de previsibilidade e disciplina na gestão dos serviços.";
        return base + " O cumprimento de SLA está em " + fmt(percentualSla) + "%, sugerindo boa capacidade operacional e maior aderência entre processo e entrega dos serviços.";
    }

    private List<PlanoAcaoDTO> gerarPlano(List<Resposta> respostas) {
        Map<String, PlanoAcaoDTO> acoes = new LinkedHashMap<>();
        for (Resposta r : respostas) {
            if (r.getValor() <= 1) {
                String cat = r.getQuestao().getCategoria();
                String pergunta = r.getQuestao().getPergunta();
                PlanoAcaoDTO plano = switch (cat) {
                    case "Governança" -> new PlanoAcaoDTO("Estruturar Governança", "Definir papéis, fóruns de decisão, critérios de priorização e mecanismos formais de acompanhamento da governança de TI. Ponto crítico: " + pergunta);
                    case "Estratégia" -> new PlanoAcaoDTO("Alinhar Estratégia", "Conectar planejamento, orçamento e roadmap de TI aos objetivos do negócio com revisão executiva periódica. Ponto crítico: " + pergunta);
                    case "Riscos", "Gestão de Riscos", "Ativos, Ameaças e Vulnerabilidades" -> new PlanoAcaoDTO("Fortalecer Gestão de Riscos", "Mapear ativos críticos, ameaças, vulnerabilidades, impacto e probabilidade para reduzir exposição e apoiar a tomada de decisão. Ponto crítico: " + pergunta);
                    case "Desempenho e Valor" -> new PlanoAcaoDTO("Mensurar Valor e Desempenho", "Implantar indicadores para valor entregue, desempenho dos serviços, conformidade e retorno dos investimentos em TI. Ponto crítico: " + pergunta);
                    case "Segurança" -> new PlanoAcaoDTO("Fortalecer Segurança", "Formalizar controles de segurança, gestão de vulnerabilidades, monitoramento e resposta a incidentes. Ponto crítico: " + pergunta);
                    case "Controle de Acesso" -> new PlanoAcaoDTO("Reforçar Controle de Acesso", "Revisar perfis, privilégios, segregação de funções e controles de acesso a sistemas e dados críticos. Ponto crítico: " + pergunta);
                    case "Continuidade e Backup" -> new PlanoAcaoDTO("Garantir Continuidade", "Estruturar backup, restauração, continuidade e testes periódicos para sustentar a operação em cenários de falha. Ponto crítico: " + pergunta);
                    case "Infraestrutura" -> new PlanoAcaoDTO("Elevar Infraestrutura", "Ampliar monitoramento, documentação, disponibilidade e resiliência da infraestrutura crítica. Ponto crítico: " + pergunta);
                    case "Incidentes e Problemas" -> new PlanoAcaoDTO("Aprimorar Suporte Operacional", "Padronizar registro, escalonamento, causa raiz e base de conhecimento para incidentes e problemas. Ponto crítico: " + pergunta);
                    case "Mudanças e Liberação" -> new PlanoAcaoDTO("Controlar Mudanças", "Formalizar avaliação de impacto, aprovação, rollback e rastreabilidade das mudanças e liberações. Ponto crítico: " + pergunta);
                    case "Serviços e SLA", "Serviços" -> new PlanoAcaoDTO("Gerir Serviços e SLA", "Implantar catálogo de serviços, metas de atendimento, monitoramento e comunicação periódica dos níveis de serviço. Ponto crítico: " + pergunta);
                    case "Processos" -> new PlanoAcaoDTO("Padronizar Processos", "Documentar rotinas críticas, definir responsáveis e reforçar a execução padronizada dos processos de TI. Ponto crítico: " + pergunta);
                    case "Pessoas" -> new PlanoAcaoDTO("Desenvolver Competências", "Estruturar capacitação, avaliação de desempenho e cobertura para funções críticas da equipe de TI. Ponto crítico: " + pergunta);
                    case "Dados" -> new PlanoAcaoDTO("Governar Dados", "Melhorar qualidade, responsabilidade e uso gerencial dos dados para apoiar decisões e controles. Ponto crítico: " + pergunta);
                    case "Proteção de Dados" -> new PlanoAcaoDTO("Proteger Dados", "Fortalecer classificação, privacidade, inventário e proteção dos dados sensíveis e regulados. Ponto crítico: " + pergunta);
                    default -> new PlanoAcaoDTO("Melhorar " + cat, "Executar ação corretiva e padronizar controles da categoria " + cat + ". Ponto crítico: " + pergunta);
                };
                acoes.putIfAbsent(plano.getTitulo(), plano);
            }
        }
        return new ArrayList<>(acoes.values());
    }
}
