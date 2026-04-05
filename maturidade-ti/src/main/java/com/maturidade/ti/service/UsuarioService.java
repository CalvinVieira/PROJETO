package com.maturidade.ti.service;

import com.maturidade.ti.dto.UsuarioResponseDTO;
import com.maturidade.ti.model.Usuario;
import com.maturidade.ti.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository repository;
    public UsuarioService(UsuarioRepository repository) { this.repository = repository; }
    public List<UsuarioResponseDTO> listar() {
        return repository.findAll().stream().map(this::toDTO).toList();
    }
    public UsuarioResponseDTO buscar(Long id) {
        return toDTO(repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado")));
    }
    public Usuario getEntity(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }
    private UsuarioResponseDTO toDTO(Usuario u) { return new UsuarioResponseDTO(u.getId(), u.getNome(), u.getEmail(), u.getPerfil()); }
}
