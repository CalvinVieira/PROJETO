package com.maturidade.ti.controller;

import com.maturidade.ti.dto.DashboardResponseDTO;
import com.maturidade.ti.dto.RelatorioResponseDTO;
import com.maturidade.ti.service.DashboardService;
import com.maturidade.ti.service.RelatorioService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
// @CrossOrigin(origins = "*")
public class RelatorioController {
    private final RelatorioService relatorioService;
    private final DashboardService dashboardService;
    public RelatorioController(RelatorioService relatorioService, DashboardService dashboardService) {
        this.relatorioService = relatorioService; this.dashboardService = dashboardService;
    }
    @GetMapping("/relatorios/empresa/{empresaId}") public RelatorioResponseDTO relatorio(@PathVariable Long empresaId) { return relatorioService.gerar(empresaId); }
    @GetMapping("/dashboard") public DashboardResponseDTO dashboard(@RequestParam Long usuarioId) { return dashboardService.montar(usuarioId); }
}
