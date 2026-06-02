package com.maturidade.ti.repository;
import com.maturidade.ti.model.RoadmapTecnologico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
public interface RoadmapTecnologicoRepository extends JpaRepository<RoadmapTecnologico, Long> {
    List<RoadmapTecnologico> findByEmpresaIdOrderByTrimestreAsc(Long empresaId);
    @Modifying
    @Query("DELETE FROM RoadmapTecnologico r WHERE r.empresa.id = :empresaId")
    void deleteByEmpresaId(@Param("empresaId") Long empresaId);
}
