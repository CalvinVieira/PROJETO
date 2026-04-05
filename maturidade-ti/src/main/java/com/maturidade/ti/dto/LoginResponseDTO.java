package com.maturidade.ti.dto;

import com.maturidade.ti.model.Perfil;

public class LoginResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private Perfil perfil;
    public LoginResponseDTO() {}
    public LoginResponseDTO(Long id, String nome, String email, Perfil perfil) {
        this.id = id; this.nome = nome; this.email = email; this.perfil = perfil;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Perfil getPerfil() { return perfil; }
    public void setPerfil(Perfil perfil) { this.perfil = perfil; }
}
