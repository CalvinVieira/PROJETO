package com.maturidade.ti.controller;

import com.maturidade.ti.dto.IncidenteTIRequestDTO;
import com.maturidade.ti.dto.IncidenteTIResponseDTO;
import com.maturidade.ti.service.IncidenteTIService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidentes")
public class IncidenteTIController {

    private final IncidenteTIService service;

    public IncidenteTIController(IncidenteTIService service) {
        this.service = service;
    }

    @GetMapping
    public List<IncidenteTIResponseDTO> listar(@RequestParam Long empresaId) {
        return service.listarPorEmpresa(empresaId);
    }

    @PostMapping
    public IncidenteTIResponseDTO criar(@RequestBody IncidenteTIRequestDTO dto) {
        return service.criar(dto);
    }

    @PutMapping("/{id}")
    public IncidenteTIResponseDTO atualizar(@PathVariable Long id, @RequestBody IncidenteTIRequestDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }
}