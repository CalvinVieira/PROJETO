package com.maturidade.ti.dto;

import java.util.List;
import java.util.Map;

public class RelatorioResponseDTO {
    private String empresa;
    private double scoreGeral;
    private String nivel;
    private String justificativa;
    private String conclusao;
    private String recomendacao;
    private String melhorCategoria;
    private String piorCategoria;
    private List<CategoriaScoreDTO> scorePorCategoria;
    private Map<String, Double> scorePorTipoAvaliacao;
    private List<String> pontosFortes;
    private List<String> pontosCriticos;
    private List<String> evidencias;
    private List<String> planosManuais;
    private List<PlanoAcaoDTO> planoAcaoAutomatico;
    private List<String> roadmap;
    private Map<String, Long> distribuicaoDimensoes;
    private Boolean possuiDadosOperacionais;
    private Long totalServicos;
    private Long totalIncidentes;
    private Long incidentesAbertos;
    private Long incidentesResolvidos;
    private Long incidentesDentroSla;
    private Long incidentesForaSla;
    private Double percentualCumprimentoSla;
    private String analiseOperacional;

    public String getEmpresa() { return empresa; }
    public void setEmpresa(String empresa) { this.empresa = empresa; }
    public double getScoreGeral() { return scoreGeral; }
    public void setScoreGeral(double scoreGeral) { this.scoreGeral = scoreGeral; }
    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }
    public String getJustificativa() { return justificativa; }
    public void setJustificativa(String justificativa) { this.justificativa = justificativa; }
    public String getConclusao() { return conclusao; }
    public void setConclusao(String conclusao) { this.conclusao = conclusao; }
    public String getRecomendacao() { return recomendacao; }
    public void setRecomendacao(String recomendacao) { this.recomendacao = recomendacao; }
    public String getMelhorCategoria() { return melhorCategoria; }
    public void setMelhorCategoria(String melhorCategoria) { this.melhorCategoria = melhorCategoria; }
    public String getPiorCategoria() { return piorCategoria; }
    public void setPiorCategoria(String piorCategoria) { this.piorCategoria = piorCategoria; }
    public List<CategoriaScoreDTO> getScorePorCategoria() { return scorePorCategoria; }
    public void setScorePorCategoria(List<CategoriaScoreDTO> scorePorCategoria) { this.scorePorCategoria = scorePorCategoria; }
    public Map<String, Double> getScorePorTipoAvaliacao() { return scorePorTipoAvaliacao; }
    public void setScorePorTipoAvaliacao(Map<String, Double> scorePorTipoAvaliacao) { this.scorePorTipoAvaliacao = scorePorTipoAvaliacao; }
    public List<String> getPontosFortes() { return pontosFortes; }
    public void setPontosFortes(List<String> pontosFortes) { this.pontosFortes = pontosFortes; }
    public List<String> getPontosCriticos() { return pontosCriticos; }
    public void setPontosCriticos(List<String> pontosCriticos) { this.pontosCriticos = pontosCriticos; }
    public List<String> getEvidencias() { return evidencias; }
    public void setEvidencias(List<String> evidencias) { this.evidencias = evidencias; }
    public List<String> getPlanosManuais() { return planosManuais; }
    public void setPlanosManuais(List<String> planosManuais) { this.planosManuais = planosManuais; }
    public List<PlanoAcaoDTO> getPlanoAcaoAutomatico() { return planoAcaoAutomatico; }
    public void setPlanoAcaoAutomatico(List<PlanoAcaoDTO> planoAcaoAutomatico) { this.planoAcaoAutomatico = planoAcaoAutomatico; }
    public List<String> getRoadmap() { return roadmap; }
    public void setRoadmap(List<String> roadmap) { this.roadmap = roadmap; }
    public Map<String, Long> getDistribuicaoDimensoes() { return distribuicaoDimensoes; }
    public void setDistribuicaoDimensoes(Map<String, Long> distribuicaoDimensoes) { this.distribuicaoDimensoes = distribuicaoDimensoes; }
    public Boolean getPossuiDadosOperacionais() { return possuiDadosOperacionais; }
    public void setPossuiDadosOperacionais(Boolean possuiDadosOperacionais) { this.possuiDadosOperacionais = possuiDadosOperacionais; }
    public Long getTotalServicos() { return totalServicos; }
    public void setTotalServicos(Long totalServicos) { this.totalServicos = totalServicos; }
    public Long getTotalIncidentes() { return totalIncidentes; }
    public void setTotalIncidentes(Long totalIncidentes) { this.totalIncidentes = totalIncidentes; }
    public Long getIncidentesAbertos() { return incidentesAbertos; }
    public void setIncidentesAbertos(Long incidentesAbertos) { this.incidentesAbertos = incidentesAbertos; }
    public Long getIncidentesResolvidos() { return incidentesResolvidos; }
    public void setIncidentesResolvidos(Long incidentesResolvidos) { this.incidentesResolvidos = incidentesResolvidos; }
    public Long getIncidentesDentroSla() { return incidentesDentroSla; }
    public void setIncidentesDentroSla(Long incidentesDentroSla) { this.incidentesDentroSla = incidentesDentroSla; }
    public Long getIncidentesForaSla() { return incidentesForaSla; }
    public void setIncidentesForaSla(Long incidentesForaSla) { this.incidentesForaSla = incidentesForaSla; }
    public Double getPercentualCumprimentoSla() { return percentualCumprimentoSla; }
    public void setPercentualCumprimentoSla(Double percentualCumprimentoSla) { this.percentualCumprimentoSla = percentualCumprimentoSla; }
    public String getAnaliseOperacional() { return analiseOperacional; }
    public void setAnaliseOperacional(String analiseOperacional) { this.analiseOperacional = analiseOperacional; }
}
