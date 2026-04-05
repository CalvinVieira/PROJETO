package com.maturidade.ti.service;

import com.maturidade.ti.dto.RespostaRequestDTO;
import com.maturidade.ti.dto.RespostaResponseDTO;
import com.maturidade.ti.model.Resposta;
import com.maturidade.ti.repository.RespostaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RespostaService {
    private final RespostaRepository repository;
    private final EmpresaService empresaService;
    private final QuestaoService questaoService;

    public RespostaService(RespostaRepository repository, EmpresaService empresaService, QuestaoService questaoService) {
        this.repository = repository;
        this.empresaService = empresaService;
        this.questaoService = questaoService;
    }

    public List<RespostaResponseDTO> salvarLote(List<RespostaRequestDTO> dtos) {
        List<Resposta> respostas = dtos.stream().map(dto -> {
            Resposta r = new Resposta();
            r.setEmpresa(empresaService.getEntity(dto.getEmpresaId()));
            r.setQuestao(questaoService.getEntity(dto.getQuestaoId()));
            r.setValor(dto.getValor());
            r.setEvidencia(dto.getEvidencia());
            r.setPlanoAcao(dto.getPlanoAcao());
            r.setDimensao(dto.getDimensao());
            return r;
        }).toList();
        return repository.saveAll(respostas).stream().map(this::toDTO).toList();
    }

    public List<RespostaResponseDTO> listarPorEmpresa(Long empresaId) {
        return repository.findByEmpresaId(empresaId).stream().map(this::toDTO).toList();
    }

    private RespostaResponseDTO toDTO(Resposta r) {
        RespostaResponseDTO dto = new RespostaResponseDTO();
        dto.setId(r.getId());
        dto.setEmpresaId(r.getEmpresa().getId());
        dto.setQuestaoId(r.getQuestao().getId());
        dto.setPergunta(r.getQuestao().getPergunta());
        dto.setCategoria(r.getQuestao().getCategoria());
        dto.setValor(r.getValor());
        dto.setEvidencia(r.getEvidencia());
        dto.setPlanoAcao(r.getPlanoAcao());
        dto.setDimensao(r.getDimensao());
        return dto;
    }
}
