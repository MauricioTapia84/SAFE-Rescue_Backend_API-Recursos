package com.SAFE_Rescue.API_Recursos.service;

import com.SAFE_Rescue.API_Recursos.modelo.TipoRecurso;
import com.SAFE_Rescue.API_Recursos.repository.TipoRecursoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Servicio para gestionar operaciones relacionadas con tipos de recursos
 * Proporciona métodos para CRUD de tipos de recursos y validación de reglas de negocio.
 */
@Service
public class TipoRecursoService {

    // REPOSITORIOS INYECTADOS

    @Autowired
    private TipoRecursoRepository tipoRecursoRepository;

    // MÉTODOS CRUD PRINCIPALES
    /**
     * Obtiene todos los tipos de recursos registrados.
     * @return Lista de todos los tipos de recursos
     */
    public List<TipoRecurso> findAll() {
        return tipoRecursoRepository.findAll();
    }

    /**
     * Busca un tipo de recursos por su ID.
     * @param id Identificador único del tipo de recursos
     * @return El tipo de recursos encontrado
     * @throws NoSuchElementException Si no se encuentra el tipo de recursos
     */
    public TipoRecurso findByID(long id) {
        return tipoRecursoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Tipo de recursos no encontrado con ID: " + id));
    }

    /**
     * Guarda un nuevo tipo de recursos
     * @param tipoRecurso Tipo de recursos a guardar
     * @return Tipo de recursos guardado
     * @throws IllegalArgumentException Si el tipo de recursos no pasa las validaciones
     */
    public TipoRecurso save(TipoRecurso tipoRecurso) {
        try{
            validarTipoRecurso(tipoRecurso);
            return tipoRecursoRepository.save(tipoRecurso);
        } catch (EntityNotFoundException e) {
            throw new RuntimeException("Error al guardar el Tipo recursos: " + e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error inesperado: " + ex.getMessage());
        }
    }

    /**
     * Actualiza un tipo de recursos existente.
     * @param tipoRecurso Datos actualizados del tipo de recursos
     * @param id ID del tipo de recursos a actualizar
     * @return Tipo de recursos actualizado
     * @throws NoSuchElementException Si no se encuentra el tipo de recursos
     * @throws IllegalArgumentException Si los datos no pasan las validaciones
     */
    public TipoRecurso update(TipoRecurso tipoRecurso, long id) {
        TipoRecurso tipoExistente = tipoRecursoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Tipo de recursos no encontrado con ID: " + id));

        if (tipoRecurso.getNombre() != null) {
            if (tipoRecurso.getNombre().length() > 50) {
                throw new IllegalArgumentException("El nombre no puede exceder los 50 caracteres");
            } else {
                tipoExistente.setNombre(tipoRecurso.getNombre());
            }
        }

        return tipoRecursoRepository.save(tipoExistente);
    }

    /**
     * Elimina un tipo de recurso por su ID.
     * @param id ID del tipo de recurso a eliminar
     * @throws NoSuchElementException Si no se encuentra el tipo de recurso
     */
    public void delete(long id) {
        if (!tipoRecursoRepository.existsById(id)) {
            throw new NoSuchElementException("Tipo de recurso no encontrado con ID: " + id);
        }
        tipoRecursoRepository.deleteById(id);
    }

    // MÉTODOS PRIVADOS DE VALIDACIÓN Y UTILIDADES

    /**
     * Valida los datos de un tipo de recurso
     * @param tipoRecurso Tipo de recurso a validar
     * @throws IllegalArgumentException Si el tipo de recurso no cumple con las reglas de validación
     */
    private void validarTipoRecurso(TipoRecurso tipoRecurso) {
        if (tipoRecurso.getNombre() == null || tipoRecurso.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del Tipo recurso es requerido");
        }
        if (tipoRecurso.getNombre().length() > 50) {
            throw new IllegalArgumentException("El nombre no puede exceder los 50 caracteres");
        }
    }

}