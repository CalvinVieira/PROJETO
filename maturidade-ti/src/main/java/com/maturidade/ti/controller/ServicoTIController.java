package com.maturidade.ti.controller;

import com.maturidade.ti.dto.ServicoTIRequestDTO;
import com.maturidade.ti.dto.ServicoTIResponseDTO;
import com.maturidade.ti.service.ServicoTIService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicos")
public class ServicoTIController {

    private final ServicoTIService service;

    public ServicoTIController(ServicoTIService service) {
        this.service = service;
    }

    @GetMapping
    public List<ServicoTIResponseDTO> listar(@RequestParam Long empresaId) {
        return service.listarPorEmpresa(empresaId);
    }

    @PostMapping
    public ServicoTIResponseDTO criar(@RequestBody ServicoTIRequestDTO dto) {
        return service.criar(dto);
    }

    @PutMapping("/{id}")
    public ServicoTIResponseDTO atualizar(@PathVariable Long id, @RequestBody ServicoTIRequestDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }
}