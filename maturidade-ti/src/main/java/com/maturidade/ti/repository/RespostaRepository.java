package com.maturidade.ti.repository;

import com.maturidade.ti.model.Resposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RespostaRepository extends JpaRepository<Resposta, Long> {
    List<Resposta> findByEmpresaId(Long empresaId);
    long countByEmpresaId(Long empresaId);

    @Query("select count(distinct r.empresa.id) from Resposta r where r.empresa.usuario.id = :usuarioId")
    long countAvaliacoesByUsuarioId(@Param("usuarioId") Long usuarioId);
}
