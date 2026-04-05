package com.maturidade.ti.repository;

import com.maturidade.ti.model.Questao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestaoRepository extends JpaRepository<Questao, Long> {
    List<Questao> findByCategoriaOrderByIdAsc(String categoria);
    List<Questao> findAllByOrderByCategoriaAscIdAsc();
}
