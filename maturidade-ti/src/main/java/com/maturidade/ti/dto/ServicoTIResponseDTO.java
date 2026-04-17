package com.maturidade.ti.dto;

public class ServicoTIResponseDTO {

    private Long id;
    private Long empresaId;
    private String nome;
    private String descricao;
    private String categoria;
    private String responsavel;
    private Integer slaHoras;
    private String status;

    public ServicoTIResponseDTO() {
    }

    public ServicoTIResponseDTO(Long id, Long empresaId, String nome, String descricao, String categoria, String responsavel, Integer slaHoras, String status) {
        this.id = id;
        this.empresaId = empresaId;
        this.nome = nome;
        this.descricao = descricao;
        this.categoria = categoria;
        this.responsavel = responsavel;
        this.slaHoras = slaHoras;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public Integer getSlaHoras() {
        return slaHoras;
    }

    public void setSlaHoras(Integer slaHoras) {
        this.slaHoras = slaHoras;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}