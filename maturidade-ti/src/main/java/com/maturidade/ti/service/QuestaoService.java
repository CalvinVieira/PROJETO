package com.maturidade.ti.service;

import com.maturidade.ti.dto.QuestaoRequestDTO;
import com.maturidade.ti.dto.QuestaoResponseDTO;
import com.maturidade.ti.model.Questao;
import com.maturidade.ti.repository.QuestaoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class QuestaoService {
    private final QuestaoRepository repository;
    public QuestaoService(QuestaoRepository repository) { this.repository = repository; }
    public QuestaoResponseDTO criar(QuestaoRequestDTO dto) {
        Questao q = new Questao(); q.setPergunta(dto.getPergunta()); q.setCategoria(dto.getCategoria()); q.setPeso(dto.getPeso());
        return toDTO(repository.save(q));
    }
    public List<QuestaoResponseDTO> listar() { return repository.findAllByOrderByCategoriaAscIdAsc().stream().map(this::toDTO).toList(); }
    public QuestaoResponseDTO atualizar(Long id, QuestaoRequestDTO dto) {
        Questao q = getEntity(id); q.setPergunta(dto.getPergunta()); q.setCategoria(dto.getCategoria()); q.setPeso(dto.getPeso());
        return toDTO(repository.save(q));
    }
    public void excluir(Long id) { repository.delete(getEntity(id)); }
    public long contar() { return repository.count(); }
    public Questao getEntity(Long id) { return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Questão não encontrada")); }
    private QuestaoResponseDTO toDTO(Questao q) { return new QuestaoResponseDTO(q.getId(), q.getPergunta(), q.getCategoria(), q.getPeso()); }
}
