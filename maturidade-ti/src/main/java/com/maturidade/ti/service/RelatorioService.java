package com.maturidade.ti.service;

import com.maturidade.ti.dto.CategoriaScoreDTO;
import com.maturidade.ti.dto.PlanoAcaoDTO;
import com.maturidade.ti.dto.RelatorioResponseDTO;
import com.maturidade.ti.model.Resposta;
import com.maturidade.ti.repository.RespostaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public RelatorioService(EmpresaService empresaService, RespostaRepository respostaRepository) {
        this.empresaService = empresaService;
        this.respostaRepository = respostaRepository;
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
                .sorted(Comparator.comparing(CategoriaScoreDTO::getCategoria))
                .toList();

        CategoriaScoreDTO melhor = scores.stream().max(Comparator.comparing(CategoriaScoreDTO::getScore)).orElse(null);
        CategoriaScoreDTO pior = scores.stream().min(Comparator.comparing(CategoriaScoreDTO::getScore)).orElse(null);

        Map<String, Long> distribuicaoDimensoes = respostas.stream()
                .map(Resposta::getDimensao)
                .filter(Objects::nonNull)
                .filter(s -> !s.isBlank())
                .collect(Collectors.groupingBy(s -> s, LinkedHashMap::new, Collectors.counting()));

        double scoreArredondado = round(scoreGeral);

        RelatorioResponseDTO dto = new RelatorioResponseDTO();
        dto.setEmpresa(empresa.getNome());
        dto.setScoreGeral(scoreArredondado);
        dto.setScorePorCategoria(scores);
        dto.setMelhorCategoria(melhor != null ? melhor.getCategoria() : "-");
        dto.setPiorCategoria(pior != null ? pior.getCategoria() : "-");
        dto.setNivel(nivel(scoreGeral));
        dto.setJustificativa(montarJustificativa(scoreArredondado, melhor, pior));
        dto.setConclusao(montarConclusao(scoreGeral, pior));
        dto.setRecomendacao(montarRecomendacao(scoreGeral, pior));
        dto.setEvidencias(respostas.stream().map(Resposta::getEvidencia).filter(Objects::nonNull).map(String::trim).filter(s -> !s.isBlank()).distinct().toList());
        dto.setPlanosManuais(respostas.stream().map(Resposta::getPlanoAcao).filter(Objects::nonNull).map(String::trim).filter(s -> !s.isBlank()).distinct().toList());
        dto.setPlanoAcaoAutomatico(gerarPlano(respostas));
        dto.setRoadmap(montarRoadmap(scoreGeral));
        dto.setDistribuicaoDimensoes(distribuicaoDimensoes);
        return dto;
    }

    private double calcularScore(List<Resposta> respostas) {
        double somaPesos = respostas.stream().mapToDouble(r -> r.getQuestao().getPeso()).sum();
        double somaPonderada = respostas.stream().mapToDouble(r -> r.getValor() * r.getQuestao().getPeso()).sum();
        return somaPesos == 0 ? 0 : somaPonderada / somaPesos;
    }

    private double round(double d) {
        return Math.round(d * 100.0) / 100.0;
    }

    private String nivel(double s) {
        if (s <= 1.0) return "Nível 1 - Inicial (Caótico)";
        if (s <= 2.0) return "Nível 2 - Repetível (Reativo)";
        if (s <= 3.5) return "Nível 3 - Definido (Proativo)";
        if (s <= 4.5) return "Nível 4 - Gerenciado (Mensurável)";
        return "Nível 5 - Otimizado (Estratégico)";
    }

    private String montarJustificativa(double score, CategoriaScoreDTO melhor, CategoriaScoreDTO pior) {
        return "Com score geral de " + score + ", a organização se encontra em " + nivel(score) +
                ". O melhor desempenho apareceu em " + (melhor != null ? melhor.getCategoria() : "-") +
                ", enquanto " + (pior != null ? pior.getCategoria() : "-") +
                " concentra as lacunas mais relevantes para a continuidade da evolução.";
    }

    private String montarConclusao(double score, CategoriaScoreDTO pior) {
        if (score <= 2.0) {
            return "A operação de TI ainda depende de práticas pouco padronizadas. O foco imediato deve ser estruturar processos básicos, reduzir dependência de pessoas-chave e tratar fragilidades críticas em " + (pior != null ? pior.getCategoria() : "categorias prioritárias") + ".";
        }
        if (score <= 3.5) {
            return "A empresa já possui fundamentos importantes, mas precisa consolidar disciplina operacional, medição e governança para sair de um estágio proativo básico e alcançar maior previsibilidade.";
        }
        if (score <= 4.5) {
            return "A TI demonstra maturidade gerenciada, com boa base para ganhos de eficiência, conformidade e confiabilidade. O próximo passo é aprofundar indicadores, automação e melhoria contínua.";
        }
        return "A TI opera em nível estratégico, com forte capacidade de sustentar valor para o negócio. O desafio agora é ampliar inovação, inteligência de dados e otimização contínua.";
    }

    private String montarRecomendacao(double score, CategoriaScoreDTO pior) {
        String foco = pior != null ? pior.getCategoria() : "categoria prioritária";
        if (score <= 2.0) {
            return "Prioridade crítica: formalizar políticas, rotinas e controles em " + foco + ", registrar evidências operacionais e implantar ações mínimas de segurança, continuidade e acompanhamento.";
        }
        if (score <= 3.5) {
            return "Priorize a padronização e a mensuração em " + foco + ", com indicadores claros, responsáveis definidos, cronograma de melhoria e revisão executiva periódica.";
        }
        if (score <= 4.5) {
            return "Avance em " + foco + " com automação, painéis gerenciais e metas de desempenho. Isso reduzirá risco operacional e aumentará previsibilidade para o negócio.";
        }
        return "Mantenha o ciclo de melhoria contínua em " + foco + " e fortaleça iniciativas de inovação, analytics e governança orientada a valor.";
    }

    private List<String> montarRoadmap(double score) {
        if (score <= 2.0) {
            return List.of("Diagnóstico detalhado", "Correções críticas", "Padronização mínima", "Formalização de controles", "Indicadores iniciais", "Revisão executiva");
        }
        if (score <= 3.5) {
            return List.of("Consolidar processos", "Definir KPIs", "Automatizar rotinas críticas", "Fortalecer segurança", "Revisar portfólio", "Expandir governança");
        }
        if (score <= 4.5) {
            return List.of("Aprimorar métricas", "Ampliar automação", "Otimizar experiência dos usuários", "Integrar gestão de riscos", "Aumentar previsibilidade", "Promover inovação");
        }
        return List.of("Melhoria contínua", "Governança orientada a valor", "Analytics e IA", "Resiliência avançada", "Escala operacional", "Inovação estratégica");
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
                    case "Riscos" -> new PlanoAcaoDTO("Fortalecer Gestão de Riscos", "Mapear riscos críticos, definir apetite a risco, acompanhar exposição e formalizar tratamento dos riscos de TI. Ponto crítico: " + pergunta);
                    case "Desempenho e Valor" -> new PlanoAcaoDTO("Mensurar Valor e Desempenho", "Implantar indicadores para valor entregue, desempenho dos serviços, conformidade e retorno dos investimentos em TI. Ponto crítico: " + pergunta);
                    case "Segurança" -> new PlanoAcaoDTO("Fortalecer Segurança", "Formalizar controles de segurança, gestão de vulnerabilidades, monitoramento e resposta a incidentes. Ponto crítico: " + pergunta);
                    case "Controle de Acesso" -> new PlanoAcaoDTO("Reforçar Controle de Acesso", "Revisar perfis, privilégios, segregação de funções e controles de acesso a sistemas e dados críticos. Ponto crítico: " + pergunta);
                    case "Continuidade e Backup" -> new PlanoAcaoDTO("Garantir Continuidade", "Estruturar backup, restauração, continuidade e testes periódicos para sustentar a operação em cenários de falha. Ponto crítico: " + pergunta);
                    case "Infraestrutura" -> new PlanoAcaoDTO("Elevar Infraestrutura", "Ampliar monitoramento, documentação, disponibilidade e resiliência da infraestrutura crítica. Ponto crítico: " + pergunta);
                    case "Incidentes e Problemas" -> new PlanoAcaoDTO("Aprimorar Suporte Operacional", "Padronizar registro, escalonamento, causa raiz e base de conhecimento para incidentes e problemas. Ponto crítico: " + pergunta);
                    case "Mudanças e Liberação" -> new PlanoAcaoDTO("Controlar Mudanças", "Formalizar avaliação de impacto, aprovação, rollback e rastreabilidade das mudanças e liberações. Ponto crítico: " + pergunta);
                    case "Serviços e SLA" -> new PlanoAcaoDTO("Gerir Serviços e SLA", "Implantar catálogo de serviços, metas de atendimento, monitoramento e comunicação periódica dos níveis de serviço. Ponto crítico: " + pergunta);
                    case "Serviços" -> new PlanoAcaoDTO("Gerir Serviços e SLA", "Implantar catálogo de serviços, metas de atendimento, monitoramento e comunicação periódica dos níveis de serviço. Ponto crítico: " + pergunta);
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