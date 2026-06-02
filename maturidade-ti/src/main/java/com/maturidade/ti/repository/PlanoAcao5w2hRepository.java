package com.maturidade.ti.repository;
import com.maturidade.ti.model.PlanoAcao5w2h;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
public interface PlanoAcao5w2hRepository extends JpaRepository<PlanoAcao5w2h, Long> {
    List<PlanoAcao5w2h> findByEmpresaIdOrderByCriadoEmDesc(Long empresaId);
    @Modifying
    @Query("DELETE FROM PlanoAcao5w2h p WHERE p.empresa.id = :empresaId")
    void deleteByEmpresaId(@Param("empresaId") Long empresaId);
}
