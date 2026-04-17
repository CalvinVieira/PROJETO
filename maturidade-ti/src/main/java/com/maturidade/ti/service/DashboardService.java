package com.maturidade.ti.service;

import com.maturidade.ti.dto.DashboardResponseDTO;
import com.maturidade.ti.model.IncidenteTI;
import com.maturidade.ti.repository.IncidenteTIRepository;
import com.maturidade.ti.repository.RespostaRepository;
import com.maturidade.ti.repository.ServicoTIRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class DashboardService {
    private final EmpresaService empresaService;
    private final QuestaoService questaoService;
    private final RespostaRepository respostaRepository;
    private final ServicoTIRepository servicoTIRepository;
    private final IncidenteTIRepository incidenteTIRepository;

    public DashboardService(
            EmpresaService empresaService,
            QuestaoService questaoService,
            RespostaRepository respostaRepository,
            ServicoTIRepository servicoTIRepository,
            IncidenteTIRepository incidenteTIRepository
    ) {
        this.empresaService = empresaService;
        this.questaoService = questaoService;
        this.respostaRepository = respostaRepository;
        this.servicoTIRepository = servicoTIRepository;
        this.incidenteTIRepository = incidenteTIRepository;
    }

    public DashboardResponseDTO montar(Long usuarioId) {
        DashboardResponseDTO dto = new DashboardResponseDTO();
        long totalAvaliacoes = respostaRepository.countAvaliacoesByUsuarioId(usuarioId);

        long totalServicos = 0;
        long totalIncidentes = 0;
        long incidentesAbertos = 0;
        long incidentesForaSla = 0;

        var empresas = empresaService.listarPorUsuario(usuarioId);
        for (var empresa : empresas) {
            totalServicos += servicoTIRepository.countByEmpresaId(empresa.getId());
            List<IncidenteTI> incidentes = incidenteTIRepository.findByEmpresaIdOrderByDataAberturaDesc(empresa.getId());
            totalIncidentes += incidentes.size();
            incidentesAbertos += incidentes.stream()
                    .filter(i -> i.getStatus() != null)
                    .filter(i -> !i.getStatus().equalsIgnoreCase("Resolvido") && !i.getStatus().equalsIgnoreCase("Fechado"))
                    .count();
            incidentesForaSla += incidentes.stream().filter(this::estaForaSla).count();
        }

        dto.setTotalEmpresas(empresaService.contarPorUsuario(usuarioId));
        dto.setTotalQuestoes(questaoService.contar());
        dto.setTotalAvaliacoes(totalAvaliacoes);
        dto.setTotalRelatorios(totalAvaliacoes);
        dto.setUltimaAvaliacao(totalAvaliacoes > 0 ? totalAvaliacoes + " avaliação(ões) concluída(s)" : "Nenhuma avaliação concluída");
        dto.setTotalServicosTI(totalServicos);
        dto.setTotalIncidentes(totalIncidentes);
        dto.setIncidentesAbertos(incidentesAbertos);
        dto.setIncidentesForaSla(incidentesForaSla);
        return dto;
    }

    private boolean estaForaSla(IncidenteTI incidente) {
        if (incidente.getSlaHoras() == null || incidente.getDataAbertura() == null || incidente.getDataFechamento() == null) {
            return false;
        }
        long horas = Duration.between(incidente.getDataAbertura(), incidente.getDataFechamento()).toHours();
        return horas > incidente.getSlaHoras();
    }
}
