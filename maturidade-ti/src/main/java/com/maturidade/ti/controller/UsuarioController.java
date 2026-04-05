package com.maturidade.ti.controller;

import com.maturidade.ti.dto.UsuarioResponseDTO;
import com.maturidade.ti.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {
    private final UsuarioService service;
    public UsuarioController(UsuarioService service) { this.service = service; }
    @GetMapping public List<UsuarioResponseDTO> listar() { return service.listar(); }
    @GetMapping("/{id}") public UsuarioResponseDTO buscar(@PathVariable Long id) { return service.buscar(id); }
}
