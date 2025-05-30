package com.SAFE_Rescue.API_Recursos.repository;

import com.SAFE_Rescue.API_Recursos.modelo.TipoVehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoVehiculoRepository extends JpaRepository<TipoVehiculo, Long> {

}
