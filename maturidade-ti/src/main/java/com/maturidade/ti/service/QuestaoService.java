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

    public QuestaoService(QuestaoRepository repository) {
        this.repository = repository;
    }

    public QuestaoResponseDTO criar(QuestaoRequestDTO dto) {
        validar(dto);

        Questao q = new Questao();
        q.setPergunta(dto.getPergunta().trim());
        q.setCategoria(dto.getCategoria().trim());
        q.setTipoAvaliacao(dto.getTipoAvaliacao().trim());
        q.setPeso(dto.getPeso());

        return toDTO(repository.save(q));
    }

    public List<QuestaoResponseDTO> listar() {
        return repository.findAllByOrderByCategoriaAscIdAsc()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public QuestaoResponseDTO atualizar(Long id, QuestaoRequestDTO dto) {
        validar(dto);

        Questao q = getEntity(id);
        q.setPergunta(dto.getPergunta().trim());
        q.setCategoria(dto.getCategoria().trim());
        q.setTipoAvaliacao(dto.getTipoAvaliacao().trim());
        q.setPeso(dto.getPeso());

        return toDTO(repository.save(q));
    }

    public void excluir(Long id) {
        repository.delete(getEntity(id));
    }

    public long contar() {
        return repository.count();
    }

    public Questao getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Questão não encontrada"));
    }

    private QuestaoResponseDTO toDTO(Questao q) {
        return new QuestaoResponseDTO(
                q.getId(),
                q.getPergunta(),
                q.getCategoria(),
                q.getTipoAvaliacao(),
                q.getPeso()
        );
    }

    private void validar(QuestaoRequestDTO dto) {
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados da questão não informados");
        }

        if (dto.getPergunta() == null || dto.getPergunta().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A pergunta é obrigatória");
        }

        if (dto.getCategoria() == null || dto.getCategoria().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A categoria é obrigatória");
        }

        if (dto.getTipoAvaliacao() == null || dto.getTipoAvaliacao().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O tipo de avaliação é obrigatório");
        }

        if (!dto.getTipoAvaliacao().equalsIgnoreCase("Governança")
                && !dto.getTipoAvaliacao().equalsIgnoreCase("Gestão")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de avaliação inválido");
        }

        if (dto.getPeso() == null || dto.getPeso() < 1 || dto.getPeso() > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O peso deve estar entre 1 e 5");
        }
    }
}