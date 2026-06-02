package com.maturidade.ti.controller;

import com.maturidade.ti.dto.*;
import com.maturidade.ti.service.GovernancaService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/governanca")
public class GovernancaController {

    private final GovernancaService governancaService;

    public GovernancaController(GovernancaService governancaService) {
        this.governancaService = governancaService;
    }

    // Gera toda a governança automaticamente a partir do questionário
    @PostMapping("/gerar/{empresaId}")
    public GovernancaResponseDTO gerarGovernanca(@PathVariable Long empresaId) {
        return governancaService.gerarGovernancaCompleta(empresaId);
    }

    // PDTI Config
    @GetMapping("/pdti/{empresaId}")
    public PdtiConfigDTO getPdtiConfig(@PathVariable Long empresaId) {
        return governancaService.getPdtiConfig(empresaId);
    }

    @PostMapping("/pdti/{empresaId}")
    public PdtiConfigDTO salvarPdtiConfig(@PathVariable Long empresaId, @RequestBody PdtiConfigDTO dto) {
        return governancaService.salvarPdtiConfig(empresaId, dto);
    }

    // Plano 5W2H
    @GetMapping("/plano/{empresaId}")
    public List<PlanoAcao5w2hDTO> getPlano(@PathVariable Long empresaId) {
        return governancaService.getPlanoAcao(empresaId);
    }

    @PostMapping("/plano/{empresaId}")
    public PlanoAcao5w2hDTO salvarPlano(@PathVariable Long empresaId, @RequestBody PlanoAcao5w2hDTO dto) {
        return governancaService.salvarPlano(empresaId, dto);
    }

    @DeleteMapping("/plano/{id}")
    public void deletarPlano(@PathVariable Long id) {
        governancaService.deletarPlano(id);
    }

    // Gestão de Riscos
    @GetMapping("/riscos/{empresaId}")
    public List<GestaoRiscoDTO> getRiscos(@PathVariable Long empresaId) {
        return governancaService.getRiscos(empresaId);
    }

    @PostMapping("/riscos/{empresaId}")
    public GestaoRiscoDTO salvarRisco(@PathVariable Long empresaId, @RequestBody GestaoRiscoDTO dto) {
        return governancaService.salvarRisco(empresaId, dto);
    }

    @DeleteMapping("/riscos/{id}")
    public void deletarRisco(@PathVariable Long id) {
        governancaService.deletarRisco(id);
    }
}
