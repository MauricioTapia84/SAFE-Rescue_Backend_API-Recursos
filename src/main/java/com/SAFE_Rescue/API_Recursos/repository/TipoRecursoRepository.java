package com.SAFE_Rescue.API_Recursos.repository;

import com.SAFE_Rescue.API_Recursos.modelo.TipoRecurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoRecursoRepository extends JpaRepository<TipoRecurso, Long> {

}
