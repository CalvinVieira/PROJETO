package com.maturidade.ti.repository;
import com.maturidade.ti.model.PdtiConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface PdtiConfigRepository extends JpaRepository<PdtiConfig, Long> {
    Optional<PdtiConfig> findByEmpresaId(Long empresaId);
}
