package com.maturidade.ti.dto;

public class EmpresaResponseDTO {
    private Long id;
    private String nome;
    private String segmento;
    private String porte;
    private Long usuarioId;
    public EmpresaResponseDTO() {}
    public EmpresaResponseDTO(Long id, String nome, String segmento, String porte, Long usuarioId) {
        this.id=id; this.nome=nome; this.segmento=segmento; this.porte=porte; this.usuarioId=usuarioId;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getSegmento() { return segmento; }
    public void setSegmento(String segmento) { this.segmento = segmento; }
    public String getPorte() { return porte; }
    public void setPorte(String porte) { this.porte = porte; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
}
