package com.project.media.repository;

import com.project.media.entity.HistoricoConsulta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface HistoricoConsultaRepository extends JpaRepository<HistoricoConsulta, Long> {

    @Query("SELECT h FROM HistoricoConsulta h JOIN h.sugestao s WHERE " +
           "(:cpfPaciente IS NULL OR h.cpfPaciente = :cpfPaciente) AND " +
           "(:dataInicio IS NULL OR h.dataConsulta >= :dataInicio) AND " +
           "(:dataFim IS NULL OR h.dataConsulta <= :dataFim) " +
           "ORDER BY h.dataConsulta DESC")
    Page<HistoricoConsulta> findComFiltros(@Param("cpfPaciente") String cpfPaciente,
                                          @Param("dataInicio") LocalDateTime dataInicio,
                                          @Param("dataFim") LocalDateTime dataFim,
                                          Pageable pageable);
}
