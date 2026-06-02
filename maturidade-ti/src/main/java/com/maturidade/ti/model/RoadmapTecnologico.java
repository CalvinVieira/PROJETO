package com.maturidade.ti.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "roadmap_tecnologico")
public class RoadmapTecnologico {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @Column(length = 300, nullable = false)
    private String iniciativa;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    private String trimestre;
    private String prioridade = "MEDIA";
    private String dependencias;
    private String status = "PLANEJADO";
    private String categoria;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }
    public String getIniciativa() { return iniciativa; }
    public void setIniciativa(String iniciativa) { this.iniciativa = iniciativa; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getTrimestre() { return trimestre; }
    public void setTrimestre(String trimestre) { this.trimestre = trimestre; }
    public String getPrioridade() { return prioridade; }
    public void setPrioridade(String prioridade) { this.prioridade = prioridade; }
    public String getDependencias() { return dependencias; }
    public void setDependencias(String dependencias) { this.dependencias = dependencias; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
}
