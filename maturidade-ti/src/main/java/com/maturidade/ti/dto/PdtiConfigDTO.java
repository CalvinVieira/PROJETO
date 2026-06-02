package com.maturidade.ti.dto;

public class PdtiConfigDTO {
    private Long id;
    private String missao;
    private String visao;
    private String objetivosEstrategicos;
    private String metasEstrategicas;
    private String periodoVigenciaInicio;
    private String periodoVigenciaFim;
    private String responsavel;
    private String patrocinadorExecutivo;
    private String contextoOrganizacional;
    private String contextoTecnologico;
    private String analiseSituacional;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMissao() { return missao; }
    public void setMissao(String missao) { this.missao = missao; }
    public String getVisao() { return visao; }
    public void setVisao(String visao) { this.visao = visao; }
    public String getObjetivosEstrategicos() { return objetivosEstrategicos; }
    public void setObjetivosEstrategicos(String v) { this.objetivosEstrategicos = v; }
    public String getMetasEstrategicas() { return metasEstrategicas; }
    public void setMetasEstrategicas(String v) { this.metasEstrategicas = v; }
    public String getPeriodoVigenciaInicio() { return periodoVigenciaInicio; }
    public void setPeriodoVigenciaInicio(String v) { this.periodoVigenciaInicio = v; }
    public String getPeriodoVigenciaFim() { return periodoVigenciaFim; }
    public void setPeriodoVigenciaFim(String v) { this.periodoVigenciaFim = v; }
    public String getResponsavel() { return responsavel; }
    public void setResponsavel(String responsavel) { this.responsavel = responsavel; }
    public String getPatrocinadorExecutivo() { return patrocinadorExecutivo; }
    public void setPatrocinadorExecutivo(String v) { this.patrocinadorExecutivo = v; }
    public String getContextoOrganizacional() { return contextoOrganizacional; }
    public void setContextoOrganizacional(String v) { this.contextoOrganizacional = v; }
    public String getContextoTecnologico() { return contextoTecnologico; }
    public void setContextoTecnologico(String v) { this.contextoTecnologico = v; }
    public String getAnaliseSituacional() { return analiseSituacional; }
    public void setAnaliseSituacional(String v) { this.analiseSituacional = v; }
}
