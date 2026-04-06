package com.maturidade.ti.controller;

import com.maturidade.ti.dto.EmpresaRequestDTO;
import com.maturidade.ti.dto.EmpresaResponseDTO;
import com.maturidade.ti.service.EmpresaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empresas")
// @CrossOrigin(origins = "*")
public class EmpresaController {
    private final EmpresaService service;
    public EmpresaController(EmpresaService service) { this.service = service; }
    @GetMapping public List<EmpresaResponseDTO> listar(@RequestParam Long usuarioId) { return service.listarPorUsuario(usuarioId); }
    @PostMapping public EmpresaResponseDTO criar(@RequestBody EmpresaRequestDTO dto) { return service.criar(dto); }
    @PutMapping("/{id}") public EmpresaResponseDTO atualizar(@PathVariable Long id, @RequestBody EmpresaRequestDTO dto) { return service.atualizar(id, dto); }
    @DeleteMapping("/{id}") public void excluir(@PathVariable Long id, @RequestParam Long usuarioId) { service.excluir(id, usuarioId); }
}
