package com.maturidade.ti.dto;
import java.util.List;

public class GovernancaResponseDTO {
    private PdtiConfigDTO pdtiConfig;
    private List<PlanoAcao5w2hDTO> planoAcao;
    private List<GestaoRiscoDTO> riscos;
    private String empresa;
    private double scoreGeral;
    private String nivel;

    public PdtiConfigDTO getPdtiConfig() { return pdtiConfig; }
    public void setPdtiConfig(PdtiConfigDTO pdtiConfig) { this.pdtiConfig = pdtiConfig; }
    public List<PlanoAcao5w2hDTO> getPlanoAcao() { return planoAcao; }
    public void setPlanoAcao(List<PlanoAcao5w2hDTO> planoAcao) { this.planoAcao = planoAcao; }
    public List<GestaoRiscoDTO> getRiscos() { return riscos; }
    public void setRiscos(List<GestaoRiscoDTO> riscos) { this.riscos = riscos; }
    public String getEmpresa() { return empresa; }
    public void setEmpresa(String empresa) { this.empresa = empresa; }
    public double getScoreGeral() { return scoreGeral; }
    public void setScoreGeral(double scoreGeral) { this.scoreGeral = scoreGeral; }
    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }
}
