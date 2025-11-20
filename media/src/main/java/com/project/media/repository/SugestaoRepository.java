package com.project.media.repository;

import com.project.media.entity.Sugestao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SugestaoRepository extends JpaRepository<Sugestao, Long> {

    @Query("SELECT s, COUNT(DISTINCT sin.id) as matchCount FROM Sugestao s " +
           "JOIN s.sintomas sin WHERE s.ativo = true AND " +
           "LOWER(sin.nome) IN (:sintomasNomes) " +
           "GROUP BY s " +
           "ORDER BY COUNT(DISTINCT sin.id) DESC, s.prioridade DESC, s.dataCriacao DESC")
    List<Object[]> findMelhoresSugestoesPorSintomas(@Param("sintomasNomes") List<String> sintomasNomes);
}

