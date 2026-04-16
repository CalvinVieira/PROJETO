package com.maturidade.ti.dto;

public class QuestaoRequestDTO {

    private String pergunta;
    private String categoria;
    private String tipoAvaliacao;
    private Integer peso;

    public String getPergunta() {
        return pergunta;
    }

    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTipoAvaliacao() {
        return tipoAvaliacao;
    }

    public void setTipoAvaliacao(String tipoAvaliacao) {
        this.tipoAvaliacao = tipoAvaliacao;
    }

    public Integer getPeso() {
        return peso;
    }

    public void setPeso(Integer peso) {
        this.peso = peso;
    }
}