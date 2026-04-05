package com.maturidade.ti.dto;

public class EmpresaRequestDTO {
    private String nome;
    private String segmento;
    private String porte;
    private Long usuarioId;
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getSegmento() { return segmento; }
    public void setSegmento(String segmento) { this.segmento = segmento; }
    public String getPorte() { return porte; }
    public void setPorte(String porte) { this.porte = porte; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
}
