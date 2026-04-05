package com.maturidade.ti.service;

import com.maturidade.ti.dto.LoginRequestDTO;
import com.maturidade.ti.dto.LoginResponseDTO;
import com.maturidade.ti.dto.UsuarioRequestDTO;
import com.maturidade.ti.model.Usuario;
import com.maturidade.ti.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    public AuthService(UsuarioRepository usuarioRepository) { this.usuarioRepository = usuarioRepository; }

    public LoginResponseDTO login(LoginRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas"));
        if (!usuario.getSenha().equals(dto.getSenha())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas");
        }
        return new LoginResponseDTO(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getPerfil());
    }

    public LoginResponseDTO register(UsuarioRequestDTO dto) {
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "E-mail já cadastrado");
        }
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());
        usuario.setPerfil(dto.getPerfil() != null ? dto.getPerfil() : com.maturidade.ti.model.Perfil.CLIENTE);
        Usuario salvo = usuarioRepository.save(usuario);
        return new LoginResponseDTO(salvo.getId(), salvo.getNome(), salvo.getEmail(), salvo.getPerfil());
    }
}
