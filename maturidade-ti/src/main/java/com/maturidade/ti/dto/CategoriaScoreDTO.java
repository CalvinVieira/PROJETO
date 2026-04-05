package com.maturidade.ti.dto;

public class CategoriaScoreDTO {
    private String categoria;
    private double score;
    public CategoriaScoreDTO() {}
    public CategoriaScoreDTO(String categoria, double score) { this.categoria = categoria; this.score = score; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }
}
