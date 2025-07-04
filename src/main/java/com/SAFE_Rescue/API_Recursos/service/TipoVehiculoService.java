package com.SAFE_Rescue.API_Recursos.service;

import com.SAFE_Rescue.API_Recursos.modelo.TipoVehiculo;
import com.SAFE_Rescue.API_Recursos.repository.TipoVehiculoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Servicio para gestionar operaciones relacionadas con tipos de Vehiculos
 * Proporciona métodos para CRUD de tipos de Vehiculos y validación de reglas de negocio.
 */
@Service
public class TipoVehiculoService {

    // REPOSITORIOS INYECTADOS

    @Autowired
    private TipoVehiculoRepository tipoVehiculoRepository;

    // MÉTODOS CRUD PRINCIPALES
    /**
     * Obtiene todos los tipos de Vehiculo registrados.
     * @return Lista de todos los tipos de Vehiculo
     */
    public List<TipoVehiculo> findAll() {
        return tipoVehiculoRepository.findAll();
    }

    /**
     * Busca un tipo de Vehiculos por su ID.
     * @param id Identificador único del tipo de Vehiculos
     * @return El tipo de Vehiculos encontrado
     * @throws NoSuchElementException Si no se encuentra el tipo de Vehiculos
     */
    public TipoVehiculo findById(Integer id) {
        return tipoVehiculoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Tipo de Vehiculos no encontrado con ID: " + id));
    }

    /**
     * Guarda un nuevo tipo de Vehiculo
     * @param tipoVehiculo Tipo de Vehiculo a guardar
     * @return Tipo de Vehiculo guardado
     * @throws IllegalArgumentException Si el tipo de Vehiculo no pasa las validaciones
     */
    public TipoVehiculo save(TipoVehiculo tipoVehiculo) {
        try{
            validarTipoVehiculo(tipoVehiculo);
            return tipoVehiculoRepository.save(tipoVehiculo);
        } catch (EntityNotFoundException e) {
            throw new RuntimeException("Error al guardar el Tipo Vehiculo: " + e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error inesperado: " + ex.getMessage());
        }
    }

    /**
     * Actualiza un tipo de Vehiculo existente.
     * @param tipoVehiculo Datos actualizados del tipo de Vehiculo
     * @param id ID del tipo de Vehiculo a actualizar
     * @return Tipo de Vehiculo actualizado
     * @throws NoSuchElementException Si no se encuentra el tipo de Vehiculo
     * @throws IllegalArgumentException Si los datos no pasan las validaciones
     */
    public TipoVehiculo update(TipoVehiculo tipoVehiculo, Integer id) {
        TipoVehiculo tipoExistente = tipoVehiculoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Tipo de Vehiculo no encontrado con ID: " + id));

        if (tipoVehiculo.getNombre() != null) {
            if (tipoVehiculo.getNombre().length() > 50) {
                throw new IllegalArgumentException("El nombre no puede exceder los 50 caracteres");
            } else {
                tipoExistente.setNombre(tipoVehiculo.getNombre());
            }
        }

        return tipoVehiculoRepository.save(tipoExistente);
    }

    /**
     * Elimina un tipo de Vehiculo por su ID.
     * @param id ID del tipo de Vehiculo a eliminar
     * @throws NoSuchElementException Si no se encuentra el tipo de Vehiculo
     */
    public void delete(Integer id) {
        if (!tipoVehiculoRepository.existsById(id)) {
            throw new NoSuchElementException("Tipo de Vehiculo no encontrado con ID: " + id);
        }
        tipoVehiculoRepository.deleteById(id);
    }

    // MÉTODOS PRIVADOS DE VALIDACIÓN Y UTILIDADES

    /**
     * Valida los datos de un tipo de Vehiculo
     * @param tipoVehiculo Tipo de Vehiculo a validar
     * @throws IllegalArgumentException Si el tipo de Vehiculo no cumple con las reglas de validación
     */
    public void validarTipoVehiculo(TipoVehiculo tipoVehiculo) {
        if (tipoVehiculo.getNombre() == null || tipoVehiculo.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del Tipo Vehiculo es requerido");
        }
        if (tipoVehiculo.getNombre().length() > 50) {
            throw new IllegalArgumentException("El nombre no puede exceder los 50 caracteres");
        }
    }

}
