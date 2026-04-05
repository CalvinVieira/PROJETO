package com.maturidade.ti.dto;

public class DashboardResponseDTO {
    private Long totalEmpresas;
    private Long totalQuestoes;
    private String ultimaAvaliacao;
    private Long totalAvaliacoes;
    private Long totalRelatorios;

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
}
