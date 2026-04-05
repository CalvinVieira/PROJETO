package com.maturidade.ti.repository;

import com.maturidade.ti.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    List<Empresa> findByUsuarioId(Long usuarioId);
    long countByUsuarioId(Long usuarioId);
}
