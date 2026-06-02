package com.maturidade.ti.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pdti_config")
public class PdtiConfig {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @Column(columnDefinition = "TEXT")
    private String missao;

    @Column(columnDefinition = "TEXT")
    private String visao;

    @Column(name = "objetivos_estrategicos", columnDefinition = "TEXT")
    private String objetivosEstrategicos;

    @Column(name = "metas_estrategicas", columnDefinition = "TEXT")
    private String metasEstrategicas;

    @Column(name = "periodo_vigencia_inicio")
    private String periodoVigenciaInicio;

    @Column(name = "periodo_vigencia_fim")
    private String periodoVigenciaFim;

    private String responsavel;

    @Column(name = "patrocinador_executivo")
    private String patrocinadorExecutivo;

    @Column(name = "contexto_organizacional", columnDefinition = "TEXT")
    private String contextoOrganizacional;

    @Column(name = "contexto_tecnologico", columnDefinition = "TEXT")
    private String contextoTecnologico;

    @Column(name = "analise_situacional", columnDefinition = "TEXT")
    private String analiseSituacional;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm = LocalDateTime.now();

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }
    public String getMissao() { return missao; }
    public void setMissao(String missao) { this.missao = missao; }
    public String getVisao() { return visao; }
    public void setVisao(String visao) { this.visao = visao; }
    public String getObjetivosEstrategicos() { return objetivosEstrategicos; }
    public void setObjetivosEstrategicos(String objetivosEstrategicos) { this.objetivosEstrategicos = objetivosEstrategicos; }
    public String getMetasEstrategicas() { return metasEstrategicas; }
    public void setMetasEstrategicas(String metasEstrategicas) { this.metasEstrategicas = metasEstrategicas; }
    public String getPeriodoVigenciaInicio() { return periodoVigenciaInicio; }
    public void setPeriodoVigenciaInicio(String periodoVigenciaInicio) { this.periodoVigenciaInicio = periodoVigenciaInicio; }
    public String getPeriodoVigenciaFim() { return periodoVigenciaFim; }
    public void setPeriodoVigenciaFim(String periodoVigenciaFim) { this.periodoVigenciaFim = periodoVigenciaFim; }
    public String getResponsavel() { return responsavel; }
    public void setResponsavel(String responsavel) { this.responsavel = responsavel; }
    public String getPatrocinadorExecutivo() { return patrocinadorExecutivo; }
    public void setPatrocinadorExecutivo(String patrocinadorExecutivo) { this.patrocinadorExecutivo = patrocinadorExecutivo; }
    public String getContextoOrganizacional() { return contextoOrganizacional; }
    public void setContextoOrganizacional(String contextoOrganizacional) { this.contextoOrganizacional = contextoOrganizacional; }
    public String getContextoTecnologico() { return contextoTecnologico; }
    public void setContextoTecnologico(String contextoTecnologico) { this.contextoTecnologico = contextoTecnologico; }
    public String getAnaliseSituacional() { return analiseSituacional; }
    public void setAnaliseSituacional(String analiseSituacional) { this.analiseSituacional = analiseSituacional; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
    public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }
}
