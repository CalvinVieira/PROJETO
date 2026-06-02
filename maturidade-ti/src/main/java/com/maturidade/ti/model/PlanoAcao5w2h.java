package com.maturidade.ti.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "plano_acao_5w2h")
public class PlanoAcao5w2h {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @Column(name = "o_que", length = 500, nullable = false)
    private String oQue;

    @Column(name = "por_que", columnDefinition = "TEXT")
    private String porQue;

    private String onde;
    private String quando;
    private String quem;

    @Column(columnDefinition = "TEXT")
    private String como;

    private String quanto;
    private String prioridade = "MEDIA";
    private String status = "PENDENTE";

    @Column(name = "categoria_origem")
    private String categoriaOrigem;

    @Column(name = "score_origem")
    private Double scoreOrigem;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }
    public String getOQue() { return oQue; }
    public void setOQue(String oQue) { this.oQue = oQue; }
    public String getPorQue() { return porQue; }
    public void setPorQue(String porQue) { this.porQue = porQue; }
    public String getOnde() { return onde; }
    public void setOnde(String onde) { this.onde = onde; }
    public String getQuando() { return quando; }
    public void setQuando(String quando) { this.quando = quando; }
    public String getQuem() { return quem; }
    public void setQuem(String quem) { this.quem = quem; }
    public String getComo() { return como; }
    public void setComo(String como) { this.como = como; }
    public String getQuanto() { return quanto; }
    public void setQuanto(String quanto) { this.quanto = quanto; }
    public String getPrioridade() { return prioridade; }
    public void setPrioridade(String prioridade) { this.prioridade = prioridade; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCategoriaOrigem() { return categoriaOrigem; }
    public void setCategoriaOrigem(String categoriaOrigem) { this.categoriaOrigem = categoriaOrigem; }
    public Double getScoreOrigem() { return scoreOrigem; }
    public void setScoreOrigem(Double scoreOrigem) { this.scoreOrigem = scoreOrigem; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
}
