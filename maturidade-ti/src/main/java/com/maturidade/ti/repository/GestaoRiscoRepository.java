package com.maturidade.ti.repository;
import com.maturidade.ti.model.GestaoRisco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
public interface GestaoRiscoRepository extends JpaRepository<GestaoRisco, Long> {
    List<GestaoRisco> findByEmpresaIdOrderByCriadoEmDesc(Long empresaId);
    @Modifying
    @Query("DELETE FROM GestaoRisco g WHERE g.empresa.id = :empresaId")
    void deleteByEmpresaId(@Param("empresaId") Long empresaId);
}
