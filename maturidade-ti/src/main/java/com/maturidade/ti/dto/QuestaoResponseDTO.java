package com.maturidade.ti.dto;

public class QuestaoResponseDTO {

    private Long id;
    private String pergunta;
    private String categoria;
    private String tipoAvaliacao;
    private Integer peso;

    public QuestaoResponseDTO() {
    }

    public QuestaoResponseDTO(Long id, String pergunta, String categoria, String tipoAvaliacao, Integer peso) {
        this.id = id;
        this.pergunta = pergunta;
        this.categoria = categoria;
        this.tipoAvaliacao = tipoAvaliacao;
        this.peso = peso;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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