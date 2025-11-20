package com.project.media.repository;

import com.project.media.entity.Sintoma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SintomaRepository extends JpaRepository<Sintoma, Long> {

    @Query("SELECT DISTINCT s FROM Sintoma s WHERE s.ativo = true AND LOWER(s.nome) IN (:sintomas)")
    List<Sintoma> findByNomesContaining(@Param("sintomas") List<String> sintomas);
}
