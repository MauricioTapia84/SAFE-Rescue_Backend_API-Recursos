package com.SAFE_Rescue.API_Recursos.repository;

import com.SAFE_Rescue.API_Recursos.modelo.SolicitudRecurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitudRecursoRepository extends JpaRepository<SolicitudRecurso, Long> {


}
