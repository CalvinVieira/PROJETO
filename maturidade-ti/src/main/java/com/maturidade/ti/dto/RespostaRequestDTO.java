package com.maturidade.ti.dto;

public class RespostaRequestDTO {
    private Long empresaId;
    private Long questaoId;
    private Integer valor;
    private String evidencia;
    private String planoAcao;
    private String dimensao;
    public Long getEmpresaId() { return empresaId; }
    public void setEmpresaId(Long empresaId) { this.empresaId = empresaId; }
    public Long getQuestaoId() { return questaoId; }
    public void setQuestaoId(Long questaoId) { this.questaoId = questaoId; }
    public Integer getValor() { return valor; }
    public void setValor(Integer valor) { this.valor = valor; }
    public String getEvidencia() { return evidencia; }
    public void setEvidencia(String evidencia) { this.evidencia = evidencia; }
    public String getPlanoAcao() { return planoAcao; }
    public void setPlanoAcao(String planoAcao) { this.planoAcao = planoAcao; }
    public String getDimensao() { return dimensao; }
    public void setDimensao(String dimensao) { this.dimensao = dimensao; }
}
