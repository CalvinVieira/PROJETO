package com.maturidade.ti.dto;

public class RespostaResponseDTO {
    private Long id;
    private Long empresaId;
    private Long questaoId;
    private String pergunta;
    private String categoria;
    private Integer valor;
    private String evidencia;
    private String planoAcao;
    private String dimensao;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getEmpresaId() { return empresaId; }
    public void setEmpresaId(Long empresaId) { this.empresaId = empresaId; }
    public Long getQuestaoId() { return questaoId; }
    public void setQuestaoId(Long questaoId) { this.questaoId = questaoId; }
    public String getPergunta() { return pergunta; }
    public void setPergunta(String pergunta) { this.pergunta = pergunta; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public Integer getValor() { return valor; }
    public void setValor(Integer valor) { this.valor = valor; }
    public String getEvidencia() { return evidencia; }
    public void setEvidencia(String evidencia) { this.evidencia = evidencia; }
    public String getPlanoAcao() { return planoAcao; }
    public void setPlanoAcao(String planoAcao) { this.planoAcao = planoAcao; }
    public String getDimensao() { return dimensao; }
    public void setDimensao(String dimensao) { this.dimensao = dimensao; }
}
