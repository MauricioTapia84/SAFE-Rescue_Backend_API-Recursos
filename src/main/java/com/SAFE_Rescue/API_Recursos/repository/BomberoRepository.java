package com.SAFE_Rescue.API_Recursos.repository;

import com.SAFE_Rescue.API_Recursos.modelo.Bombero;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BomberoRepository extends JpaRepository<Bombero, Long>{

    boolean existsByTelefono(Long telefono);
}



