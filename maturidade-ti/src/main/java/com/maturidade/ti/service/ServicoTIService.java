package com.maturidade.ti.service;

import com.maturidade.ti.dto.ServicoTIRequestDTO;
import com.maturidade.ti.dto.ServicoTIResponseDTO;
import com.maturidade.ti.model.ServicoTI;
import com.maturidade.ti.repository.ServicoTIRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ServicoTIService {

    private final ServicoTIRepository repository;
    private final EmpresaService empresaService;

    public ServicoTIService(ServicoTIRepository repository, EmpresaService empresaService) {
        this.repository = repository;
        this.empresaService = empresaService;
    }

    public ServicoTIResponseDTO criar(ServicoTIRequestDTO dto) {
        validar(dto);
        ServicoTI servico = new ServicoTI();
        preencher(servico, dto);
        return toDTO(repository.save(servico));
    }

    public List<ServicoTIResponseDTO> listarPorEmpresa(Long empresaId) {
        return repository.findByEmpresaIdOrderByNomeAsc(empresaId).stream().map(this::toDTO).toList();
    }

    public ServicoTIResponseDTO atualizar(Long id, ServicoTIRequestDTO dto) {
        validar(dto);
        ServicoTI servico = getEntity(id);
        preencher(servico, dto);
        return toDTO(repository.save(servico));
    }

    public void excluir(Long id) {
        repository.delete(getEntity(id));
    }

    public ServicoTI getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço não encontrado"));
    }

    private void preencher(ServicoTI servico, ServicoTIRequestDTO dto) {
        servico.setEmpresa(empresaService.getEntity(dto.getEmpresaId()));
        servico.setNome(dto.getNome().trim());
        servico.setDescricao(dto.getDescricao() != null ? dto.getDescricao().trim() : null);
        servico.setCategoria(dto.getCategoria().trim());
        servico.setResponsavel(dto.getResponsavel() != null ? dto.getResponsavel().trim() : null);
        servico.setSlaHoras(dto.getSlaHoras());
        servico.setStatus((dto.getStatus() == null || dto.getStatus().isBlank()) ? "Ativo" : dto.getStatus().trim());
    }

    private ServicoTIResponseDTO toDTO(ServicoTI servico) {
        return new ServicoTIResponseDTO(
                servico.getId(),
                servico.getEmpresa().getId(),
                servico.getNome(),
                servico.getDescricao(),
                servico.getCategoria(),
                servico.getResponsavel(),
                servico.getSlaHoras(),
                servico.getStatus()
        );
    }

    private void validar(ServicoTIRequestDTO dto) {
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados do serviço não informados");
        }
        if (dto.getEmpresaId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A empresa é obrigatória");
        }
        if (dto.getNome() == null || dto.getNome().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome do serviço é obrigatório");
        }
        if (dto.getCategoria() == null || dto.getCategoria().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A categoria do serviço é obrigatória");
        }
        if (dto.getSlaHoras() != null && dto.getSlaHoras() < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O SLA deve ser maior que zero");
        }
    }
}