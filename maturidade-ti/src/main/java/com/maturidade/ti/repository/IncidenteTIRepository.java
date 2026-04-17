package com.maturidade.ti.repository;

import com.maturidade.ti.model.IncidenteTI;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncidenteTIRepository extends JpaRepository<IncidenteTI, Long> {
    List<IncidenteTI> findByEmpresaIdOrderByDataAberturaDesc(Long empresaId);
    long countByEmpresaId(Long empresaId);
}