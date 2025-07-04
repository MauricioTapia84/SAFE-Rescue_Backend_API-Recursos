package com.SAFE_Rescue.API_Recursos.service;

import com.SAFE_Rescue.API_Recursos.modelo.TipoRecurso;
import com.SAFE_Rescue.API_Recursos.modelo.TipoVehiculo;
import com.SAFE_Rescue.API_Recursos.modelo.Vehiculo;
import com.SAFE_Rescue.API_Recursos.repository.TipoVehiculoRepository;
import com.SAFE_Rescue.API_Recursos.repository.VehiculoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.NoSuchElementException;

/**
 * Servicio para la gestión integral de vehiculos
 * Maneja operaciones CRUD, asignación de tipo Vehiculo
 * y validación de datos para vehiculos
 */
@Service
@Transactional
public class VehiculoService {

    // REPOSITORIOS INYECTADOS
    @Autowired private VehiculoRepository vehiculoRepository;
    @Autowired private TipoVehiculoRepository tipoVehiculoRepository;

    // SERVICIOS INYECTADOS
    @Autowired private TipoVehiculoService tipoVehiculoService;

    // MÉTODOS CRUD PRINCIPALES

    /**
     * Obtiene todos los vehiculos registrados en el sistema.
     * @return Lista completa de vehiculos
     */
    public List<Vehiculo> findAll() {
        return vehiculoRepository.findAll();
    }

    /**
     * Busca un vehiculo por su ID único.
     * @param id Identificador del vehiculo
     * @return Vehiculo encontrado
     * @throws NoSuchElementException Si no se encuentra el vehiculo
     */
    public Vehiculo findById(Integer id) {
        return vehiculoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró vehiculo con ID: " + id));
    }

    /**
     * Guarda un nuevo vehiculo en el sistema.
     * Realiza validaciones y guarda relaciones con otros componentes.
     * @param vehiculo Datos del vehiculo a guardar
     * @return Vehiculo guardado con ID generado
     * @throws RuntimeException Si ocurre algún error durante el proceso
     */
    public Vehiculo save(Vehiculo vehiculo) {
        try {

            // Validación y persistencia de relaciones principales
            TipoVehiculo tipoVehiculoGuardado = tipoVehiculoService.save(vehiculo.getTipoVehiculo());

            vehiculo.setTipoVehiculo(tipoVehiculoGuardado);

            validarVehiculo(vehiculo);

            return vehiculoRepository.save(vehiculo);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el vehiculo: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza los datos de un vehiculo existente.
     * @param vehiculo Datos actualizados del vehiculo
     * @param id Identificador del vehiculo a actualizar
     * @return Vehiculo actualizado
     * @throws IllegalArgumentException Si el vehiculo proporcionado es nulo
     * @throws NoSuchElementException Si no se encuentra el vehiculo a actualizar
     * @throws RuntimeException Si ocurre algún error durante la actualización
     */
    public Vehiculo update(Vehiculo vehiculo, Integer id) {
        if (vehiculo == null) {
            throw new IllegalArgumentException("El vehiculo no puede ser nulo");
        }

        Vehiculo vehiculoExistente = vehiculoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Vehiculo no encontrado con ID: " + id));

        try {

            if (vehiculo.getTipoVehiculo() != null) {
                vehiculoExistente.setTipoVehiculo(vehiculo.getTipoVehiculo());
            }

            if (vehiculo.getMarca() != null) {
                if (vehiculo.getMarca().length() > 50) {
                    throw new IllegalArgumentException("El Marca no puede exceder los 50 caracteres");
                } else {
                    vehiculoExistente.setMarca(vehiculo.getMarca());
                }
            }

            if (vehiculo.getModelo() != null) {
                if (vehiculo.getModelo().length() > 50) {
                    throw new IllegalArgumentException("El Modelo no puede exceder los 50 caracteres");
                } else {
                    vehiculoExistente.setModelo(vehiculo.getModelo());
                }
            }

            if (vehiculo.getEstado() != null) {
                if (vehiculo.getEstado().length() > 50) {
                    throw new IllegalArgumentException("El Estado no puede exceder los 50 caracteres");
                } else {
                    vehiculoExistente.setEstado(vehiculo.getEstado());
                }
            }

            if (vehiculo.getConductor() != null) {
                if (vehiculo.getConductor().length() > 50) {
                    throw new IllegalArgumentException("El Conductor no puede exceder los 50 caracteres");
                } else {
                    vehiculoExistente.setConductor(vehiculo.getConductor());
                }
            }

            if (vehiculo.getPatente() != null) {
                if (vehiculo.getPatente().length() > 6) {
                    throw new IllegalArgumentException("La patente no puede exceder los 6 caracteres");
                } else {
                    if (vehiculoRepository.existsByPatente(vehiculo.getPatente())) {
                        throw new RuntimeException("La Patente ya existe");
                    }else{
                    vehiculoExistente.setPatente(vehiculo.getPatente());
                    }
                }
            }

            if (vehiculo.getTipoVehiculo() != null) {
                validarTipoVehiculo(vehiculo.getTipoVehiculo());
            }

            return vehiculoRepository.save(vehiculoExistente);
        }catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Error al actualizar el vehículo: " + e.getMessage());
        } catch (NoSuchElementException  f) {
            throw new NoSuchElementException("Error al actualizar el vehículo: " + f.getMessage());
        } catch (Exception g) {
            throw new RuntimeException("Error al actualizar el vehículo: " + g.getMessage());
        }
    }

    /**
     * Elimina un vehiculo del sistema.
     * @param id Identificador del vehiculo a eliminar
     * @throws NoSuchElementException Si no se encuentra el vehiculo
     */
    public void delete(Integer id) {
        if (!vehiculoRepository.existsById(id)) {
            throw new NoSuchElementException("No se encontró vehiculo con ID: " + id);
        }
        vehiculoRepository.deleteById(id);
    }


    // MÉTODOS PRIVADOS DE VALIDACIÓN Y UTILIDADES

    /**
     * Valida los datos de un  de Vehiculo
     * @param vehiculo Vehiculo a validar
     * @throws IllegalArgumentException Si el Vehiculo no cumple con las reglas de validación
     */
    public void validarVehiculo(Vehiculo vehiculo) {

        try {
            if (vehiculoRepository.existsByPatente(vehiculo.getPatente())) {
                throw new RuntimeException("La Patente ya existe");
            }

            if (vehiculo.getMarca() != null) {
                if (vehiculo.getMarca().length() > 50) {
                    throw new IllegalArgumentException("La Marca no puede exceder los 50 caracteres");
                }
            }else{
                throw new IllegalArgumentException("La marca no puede ser nula");
            }

            if (vehiculo.getModelo() != null) {
                if (vehiculo.getModelo().length() > 50) {
                    throw new IllegalArgumentException("El Modelo no puede exceder los 50 caracteres");
                }
            }else{
                throw new IllegalArgumentException("El modelo no puede ser nulo");
            }

            if (vehiculo.getEstado() != null) {
                if (vehiculo.getEstado().length() > 50) {
                    throw new IllegalArgumentException("El Estado no puede exceder los 50 caracteres");
                }
            }else{
                throw new IllegalArgumentException("El estado no puede ser nulo");
            }

            if (vehiculo.getConductor() != null) {
                if (vehiculo.getConductor().length() > 50) {
                    throw new IllegalArgumentException("El Conductor no puede exceder los 50 caracteres");
                }
            }else{
                throw new IllegalArgumentException("El conductor no pude ser nulo");
            }

            if (vehiculo.getPatente() != null) {
                if (vehiculo.getPatente().length() > 6) {
                    throw new IllegalArgumentException("La patente no puede exceder los 6 caracteres");
                }
            }else{
                throw new IllegalArgumentException("La patente no pude ser nula");
            }

            validarTipoVehiculo(vehiculo.getTipoVehiculo());
        }catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Error al validar el vehículo: " + e.getMessage());
        }catch (Exception f) {
            throw new RuntimeException("Error al validar el vehículo: " + f.getMessage());
        }
    }

    /**
     * Valida los datos de un tipo de Vehiculo
     * @param tipoVehiculo Tipo de Vehiculo a validar
     * @throws IllegalArgumentException Si el tipo de Vehiculo no cumple con las reglas de validación
     */
    private void validarTipoVehiculo(TipoVehiculo tipoVehiculo) {
        if (tipoVehiculo.getNombre() == null || tipoVehiculo.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del Tipo Vehiculo es requerido");
        }
        if (tipoVehiculo.getNombre().length() > 50) {
            throw new IllegalArgumentException("El nombre no puede exceder los 50 caracteres");
        }


    }

    // MÉTODOS DE ASIGNACIÓN DE RELACIONES

    /**
     * Asigna un tipo de vehiculoa un vehiculo
     * @param vehiculoId ID del rvehiculo
     * @param tipoVehiculoId ID del tipo de vehiculo
     */
    public void asignarTipoVehiculo(Integer vehiculoId, Integer tipoVehiculoId) {
        Vehiculo vehiculo = vehiculoRepository.findById(vehiculoId)
                .orElseThrow(() -> new RuntimeException("Vehiculo no encontrado"));
        TipoVehiculo tipoVehiculo = tipoVehiculoRepository.findById(tipoVehiculoId)
                .orElseThrow(() -> new RuntimeException("Tipo vehiculo no encontrado"));
        vehiculo.setTipoVehiculo(tipoVehiculo);
        vehiculoRepository.save(vehiculo);
    }

}

