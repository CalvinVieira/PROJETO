package com.maturidade.ti.repository;

import com.maturidade.ti.model.ServicoTI;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServicoTIRepository extends JpaRepository<ServicoTI, Long> {
    List<ServicoTI> findByEmpresaIdOrderByNomeAsc(Long empresaId);
    long countByEmpresaId(Long empresaId);
}