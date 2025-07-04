package com.SAFE_Rescue.API_Recursos.repository;

import com.SAFE_Rescue.API_Recursos.modelo.TipoRecurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la gestión de Tipo Recursos
 * Maneja operaciones CRUD desde la base de datos usando Jakarta
 */
@Repository
public interface TipoRecursoRepository extends JpaRepository<TipoRecurso, Integer> {

}
