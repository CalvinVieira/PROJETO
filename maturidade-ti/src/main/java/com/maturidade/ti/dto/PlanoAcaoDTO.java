package com.maturidade.ti.dto;

public class PlanoAcaoDTO {
    private String titulo;
    private String descricao;
    public PlanoAcaoDTO() {}
    public PlanoAcaoDTO(String titulo, String descricao) { this.titulo = titulo; this.descricao = descricao; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}
