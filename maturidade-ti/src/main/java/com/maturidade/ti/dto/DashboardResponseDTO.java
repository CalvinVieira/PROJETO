package com.maturidade.ti.dto;

public class DashboardResponseDTO {
    private Long totalEmpresas;
    private Long totalQuestoes;
    private String ultimaAvaliacao;
    private Long totalAvaliacoes;
    private Long totalRelatorios;
    private Long totalServicosTI;
    private Long totalIncidentes;
    private Long incidentesAbertos;
    private Long incidentesForaSla;

    public Long getTotalEmpresas() { return totalEmpresas; }
    public void setTotalEmpresas(Long totalEmpresas) { this.totalEmpresas = totalEmpresas; }
    public Long getTotalQuestoes() { return totalQuestoes; }
    public void setTotalQuestoes(Long totalQuestoes) { this.totalQuestoes = totalQuestoes; }
    public String getUltimaAvaliacao() { return ultimaAvaliacao; }
    public void setUltimaAvaliacao(String ultimaAvaliacao) { this.ultimaAvaliacao = ultimaAvaliacao; }
    public Long getTotalAvaliacoes() { return totalAvaliacoes; }
    public void setTotalAvaliacoes(Long totalAvaliacoes) { this.totalAvaliacoes = totalAvaliacoes; }
    public Long getTotalRelatorios() { return totalRelatorios; }
    public void setTotalRelatorios(Long totalRelatorios) { this.totalRelatorios = totalRelatorios; }
    public Long getTotalServicosTI() { return totalServicosTI; }
    public void setTotalServicosTI(Long totalServicosTI) { this.totalServicosTI = totalServicosTI; }
    public Long getTotalIncidentes() { return totalIncidentes; }
    public void setTotalIncidentes(Long totalIncidentes) { this.totalIncidentes = totalIncidentes; }
    public Long getIncidentesAbertos() { return incidentesAbertos; }
    public void setIncidentesAbertos(Long incidentesAbertos) { this.incidentesAbertos = incidentesAbertos; }
    public Long getIncidentesForaSla() { return incidentesForaSla; }
    public void setIncidentesForaSla(Long incidentesForaSla) { this.incidentesForaSla = incidentesForaSla; }
}
