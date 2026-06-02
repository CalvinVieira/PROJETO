package com.maturidade.ti.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "gestao_riscos")
public class GestaoRisco {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @Column(length = 500, nullable = false)
    private String descricao;

    private String tipo = "Ameaça";
    private String ativo;
    private String ameaca;

    @Column(columnDefinition = "TEXT")
    private String vulnerabilidade;

    @Column(columnDefinition = "TEXT")
    private String causa;

    @Column(columnDefinition = "TEXT")
    private String consequencia;

    private Integer impacto = 3;
    private String probabilidade = "Moderado";

    @Column(name = "nivel_risco")
    private String nivelRisco;

    @Column(name = "nivel_risco_num")
    private Integer nivelRiscoNum;

    private String tratamento = "Mitigar";
    private String responsavel;
    private String status = "Identificado";

    @Column(name = "categoria_origem")
    private String categoriaOrigem;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getAtivo() { return ativo; }
    public void setAtivo(String ativo) { this.ativo = ativo; }
    public String getAmeaca() { return ameaca; }
    public void setAmeaca(String ameaca) { this.ameaca = ameaca; }
    public String getVulnerabilidade() { return vulnerabilidade; }
    public void setVulnerabilidade(String vulnerabilidade) { this.vulnerabilidade = vulnerabilidade; }
    public String getCausa() { return causa; }
    public void setCausa(String causa) { this.causa = causa; }
    public String getConsequencia() { return consequencia; }
    public void setConsequencia(String consequencia) { this.consequencia = consequencia; }
    public Integer getImpacto() { return impacto; }
    public void setImpacto(Integer impacto) { this.impacto = impacto; }
    public String getProbabilidade() { return probabilidade; }
    public void setProbabilidade(String probabilidade) { this.probabilidade = probabilidade; }
    public String getNivelRisco() { return nivelRisco; }
    public void setNivelRisco(String nivelRisco) { this.nivelRisco = nivelRisco; }
    public Integer getNivelRiscoNum() { return nivelRiscoNum; }
    public void setNivelRiscoNum(Integer nivelRiscoNum) { this.nivelRiscoNum = nivelRiscoNum; }
    public String getTratamento() { return tratamento; }
    public void setTratamento(String tratamento) { this.tratamento = tratamento; }
    public String getResponsavel() { return responsavel; }
    public void setResponsavel(String responsavel) { this.responsavel = responsavel; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCategoriaOrigem() { return categoriaOrigem; }
    public void setCategoriaOrigem(String categoriaOrigem) { this.categoriaOrigem = categoriaOrigem; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
}
