package com.maturidade.ti.model;

import jakarta.persistence.*;

@Entity
@Table(name = "questao")
public class Questao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String pergunta;

    @Column(nullable = false)
    private String categoria;

    @Column(name = "tipo_avaliacao", length = 30)
    private String tipoAvaliacao;

    @Column(nullable = false)
    private Integer peso;

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