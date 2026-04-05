package com.maturidade.ti.service;

import com.maturidade.ti.dto.EmpresaRequestDTO;
import com.maturidade.ti.dto.EmpresaResponseDTO;
import com.maturidade.ti.model.Empresa;
import com.maturidade.ti.repository.EmpresaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EmpresaService {
    private final EmpresaRepository repository;
    private final UsuarioService usuarioService;
    public EmpresaService(EmpresaRepository repository, UsuarioService usuarioService) {
        this.repository = repository; this.usuarioService = usuarioService;
    }
    public EmpresaResponseDTO criar(EmpresaRequestDTO dto) {
        Empresa e = new Empresa();
        e.setNome(dto.getNome()); e.setSegmento(dto.getSegmento()); e.setPorte(dto.getPorte());
        e.setUsuario(usuarioService.getEntity(dto.getUsuarioId()));
        return toDTO(repository.save(e));
    }
    public List<EmpresaResponseDTO> listarPorUsuario(Long usuarioId) { return repository.findByUsuarioId(usuarioId).stream().map(this::toDTO).toList(); }
    public EmpresaResponseDTO atualizar(Long id, EmpresaRequestDTO dto) {
        Empresa e = getEntity(id);
        if (!e.getUsuario().getId().equals(dto.getUsuarioId())) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Empresa não pertence ao usuário informado");
        e.setNome(dto.getNome()); e.setSegmento(dto.getSegmento()); e.setPorte(dto.getPorte());
        return toDTO(repository.save(e));
    }
    public void excluir(Long id, Long usuarioId) {
        Empresa e = getEntity(id);
        if (!e.getUsuario().getId().equals(usuarioId)) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Operação não permitida");
        repository.delete(e);
    }
    public Empresa getEntity(Long id) { return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada")); }
    public long contarPorUsuario(Long usuarioId) { return repository.countByUsuarioId(usuarioId); }
    private EmpresaResponseDTO toDTO(Empresa e) { return new EmpresaResponseDTO(e.getId(), e.getNome(), e.getSegmento(), e.getPorte(), e.getUsuario().getId()); }
}
