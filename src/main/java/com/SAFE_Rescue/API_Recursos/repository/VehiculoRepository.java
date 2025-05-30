package com.SAFE_Rescue.API_Recursos.repository;

import com.SAFE_Rescue.API_Recursos.modelo.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

    boolean existsByPatente(String Patente);
}
