package com.maturidade.ti.controller;

import com.maturidade.ti.dto.QuestaoRequestDTO;
import com.maturidade.ti.dto.QuestaoResponseDTO;
import com.maturidade.ti.service.QuestaoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questoes")
@CrossOrigin(origins = "*")
public class QuestaoController {
    private final QuestaoService service;
    public QuestaoController(QuestaoService service) { this.service = service; }
    @GetMapping public List<QuestaoResponseDTO> listar() { return service.listar(); }
    @PostMapping public QuestaoResponseDTO criar(@RequestBody QuestaoRequestDTO dto) { return service.criar(dto); }
    @PutMapping("/{id}") public QuestaoResponseDTO atualizar(@PathVariable Long id, @RequestBody QuestaoRequestDTO dto) { return service.atualizar(id, dto); }
    @DeleteMapping("/{id}") public void excluir(@PathVariable Long id) { service.excluir(id); }
}
