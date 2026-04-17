package com.maturidade.ti.service;

import com.maturidade.ti.dto.IncidenteTIRequestDTO;
import com.maturidade.ti.dto.IncidenteTIResponseDTO;
import com.maturidade.ti.model.IncidenteTI;
import com.maturidade.ti.model.ServicoTI;
import com.maturidade.ti.repository.IncidenteTIRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class IncidenteTIService {

    private final IncidenteTIRepository repository;
    private final EmpresaService empresaService;
    private final ServicoTIService servicoTIService;

    public IncidenteTIService(IncidenteTIRepository repository, EmpresaService empresaService, ServicoTIService servicoTIService) {
        this.repository = repository;
        this.empresaService = empresaService;
        this.servicoTIService = servicoTIService;
    }

    public IncidenteTIResponseDTO criar(IncidenteTIRequestDTO dto) {
        validar(dto);
        IncidenteTI incidente = new IncidenteTI();
        incidente.setDataAbertura(LocalDateTime.now());
        preencher(incidente, dto);
        return toDTO(repository.save(incidente));
    }

    public List<IncidenteTIResponseDTO> listarPorEmpresa(Long empresaId) {
        return repository.findByEmpresaIdOrderByDataAberturaDesc(empresaId).stream().map(this::toDTO).toList();
    }

    public IncidenteTIResponseDTO atualizar(Long id, IncidenteTIRequestDTO dto) {
        validar(dto);
        IncidenteTI incidente = getEntity(id);
        preencher(incidente, dto);
        return toDTO(repository.save(incidente));
    }

    public void excluir(Long id) {
        repository.delete(getEntity(id));
    }

    public IncidenteTI getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Incidente não encontrado"));
    }

    private void preencher(IncidenteTI incidente, IncidenteTIRequestDTO dto) {
        incidente.setEmpresa(empresaService.getEntity(dto.getEmpresaId()));
        incidente.setTitulo(dto.getTitulo().trim());
        incidente.setDescricao(dto.getDescricao() != null ? dto.getDescricao().trim() : null);
        incidente.setPrioridade(dto.getPrioridade().trim());

        String status = (dto.getStatus() == null || dto.getStatus().isBlank()) ? "Aberto" : dto.getStatus().trim();
        incidente.setStatus(status);

        ServicoTI servico = null;
        if (dto.getServicoId() != null) {
            servico = servicoTIService.getEntity(dto.getServicoId());
            if (!servico.getEmpresa().getId().equals(dto.getEmpresaId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O serviço informado não pertence à empresa selecionada");
            }
        }
        incidente.setServico(servico);

        Integer sla = dto.getSlaHoras();
        if (sla == null && servico != null) {
            sla = servico.getSlaHoras();
        }
        incidente.setSlaHoras(sla);

        if (status.equalsIgnoreCase("Resolvido") || status.equalsIgnoreCase("Fechado")) {
            if (incidente.getDataFechamento() == null) {
                incidente.setDataFechamento(LocalDateTime.now());
            }
        } else {
            incidente.setDataFechamento(null);
        }
    }

    private IncidenteTIResponseDTO toDTO(IncidenteTI incidente) {
        IncidenteTIResponseDTO dto = new IncidenteTIResponseDTO();
        dto.setId(incidente.getId());
        dto.setEmpresaId(incidente.getEmpresa().getId());
        dto.setServicoId(incidente.getServico() != null ? incidente.getServico().getId() : null);
        dto.setServicoNome(incidente.getServico() != null ? incidente.getServico().getNome() : null);
        dto.setTitulo(incidente.getTitulo());
        dto.setDescricao(incidente.getDescricao());
        dto.setPrioridade(incidente.getPrioridade());
        dto.setStatus(incidente.getStatus());
        dto.setSlaHoras(incidente.getSlaHoras());
        dto.setDataAbertura(incidente.getDataAbertura());
        dto.setDataFechamento(incidente.getDataFechamento());
        dto.setDentroSla(calcularDentroSla(incidente));
        return dto;
    }

    private Boolean calcularDentroSla(IncidenteTI incidente) {
        if (incidente.getSlaHoras() == null || incidente.getDataFechamento() == null) {
            return null;
        }
        long horas = Duration.between(incidente.getDataAbertura(), incidente.getDataFechamento()).toHours();
        return horas <= incidente.getSlaHoras();
    }

    private void validar(IncidenteTIRequestDTO dto) {
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados do incidente não informados");
        }
        if (dto.getEmpresaId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A empresa é obrigatória");
        }
        if (dto.getTitulo() == null || dto.getTitulo().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O título do incidente é obrigatório");
        }
        if (dto.getPrioridade() == null || dto.getPrioridade().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A prioridade do incidente é obrigatória");
        }
        if (dto.getSlaHoras() != null && dto.getSlaHoras() < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O SLA do incidente deve ser maior que zero");
        }
    }
}