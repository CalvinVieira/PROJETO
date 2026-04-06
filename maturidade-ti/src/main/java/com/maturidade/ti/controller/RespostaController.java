package com.maturidade.ti.controller;

import com.maturidade.ti.dto.RespostaRequestDTO;
import com.maturidade.ti.dto.RespostaResponseDTO;
import com.maturidade.ti.service.RespostaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/respostas")
// @CrossOrigin(origins = "*")
public class RespostaController {
    private final RespostaService service;
    public RespostaController(RespostaService service) { this.service = service; }
    @PostMapping("/lote") public List<RespostaResponseDTO> salvarLote(@RequestBody List<RespostaRequestDTO> dtos) { return service.salvarLote(dtos); }
    @GetMapping("/empresa/{empresaId}") public List<RespostaResponseDTO> listar(@PathVariable Long empresaId) { return service.listarPorEmpresa(empresaId); }
}
