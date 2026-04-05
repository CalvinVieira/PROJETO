package com.maturidade.ti.service;

import com.maturidade.ti.dto.DashboardResponseDTO;
import com.maturidade.ti.repository.RespostaRepository;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {
    private final EmpresaService empresaService;
    private final QuestaoService questaoService;
    private final RespostaRepository respostaRepository;

    public DashboardService(EmpresaService empresaService, QuestaoService questaoService, RespostaRepository respostaRepository) {
        this.empresaService = empresaService;
        this.questaoService = questaoService;
        this.respostaRepository = respostaRepository;
    }

    public DashboardResponseDTO montar(Long usuarioId) {
        DashboardResponseDTO dto = new DashboardResponseDTO();
        long totalAvaliacoes = respostaRepository.countAvaliacoesByUsuarioId(usuarioId);

        dto.setTotalEmpresas(empresaService.contarPorUsuario(usuarioId));
        dto.setTotalQuestoes(questaoService.contar());
        dto.setTotalAvaliacoes(totalAvaliacoes);
        dto.setTotalRelatorios(totalAvaliacoes);
        dto.setUltimaAvaliacao(totalAvaliacoes > 0 ? totalAvaliacoes + " avaliação(ões) concluída(s)" : "Nenhuma avaliação concluída");
        return dto;
    }
}
