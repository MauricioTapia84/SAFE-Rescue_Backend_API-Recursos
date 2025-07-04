package com.SAFE_Rescue.API_Recursos.service;

import com.SAFE_Rescue.API_Recursos.modelo.Recurso;
import com.SAFE_Rescue.API_Recursos.modelo.TipoRecurso;
import com.SAFE_Rescue.API_Recursos.repository.RecursoRepository;
import com.SAFE_Rescue.API_Recursos.repository.TipoRecursoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.NoSuchElementException;

/**
 * Servicio para la gestión integral de recursos
 * Maneja operaciones CRUD, asignación de tipo recurso
 * y validación de datos para recursos
 */
@Service
@Transactional
public class RecursoService {

    // REPOSITORIOS INYECTADOS
    @Autowired private RecursoRepository recursoRepository;
    @Autowired private TipoRecursoRepository tipoRecursoRepository;

    // SERVICIOS INYECTADOS
    @Autowired private TipoRecursoService tipoRecursoService;


    // MÉTODOS CRUD PRINCIPALES

    /**
     * Obtiene todos los recursos registrados en el sistema.
     * @return Lista completa de recursos
     */
    public List<Recurso> findAll() {
        return recursoRepository.findAll();
    }

    /**
     * Busca un recurso por su ID único.
     * @param id Identificador del recurso
     * @return Recurso encontrado
     * @throws NoSuchElementException Si no se encuentra el recurso
     */
    public Recurso findById(Integer id) {
        return recursoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró recurso con ID: " + id));
    }

    /**
     * Guarda un nuevo recurso en el sistema.
     * Realiza validaciones y guarda relaciones con otros componentes.
     * @param recurso Datos del recurso a guardar
     * @return Recurso guardado con ID generado
     * @throws RuntimeException Si ocurre algún error durante el proceso
     */
    public Recurso save(Recurso recurso) {
        try {
            // Validación y persistencia de relaciones principales
            TipoRecurso tipoRecursoGuardado = tipoRecursoService.save(recurso.getTipoRecurso());

            recurso.setTipoRecurso(tipoRecursoGuardado);

            validarRecurso(recurso);

            return recursoRepository.save(recurso);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el recurso: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza los datos de un recurso existente.
     * @param recurso Datos actualizados del recurso
     * @param id Identificador del recurso a actualizar
     * @return Recurso actualizado
     * @throws IllegalArgumentException Si el recurso proporcionado es nulo
     * @throws NoSuchElementException Si no se encuentra el recurso a actualizar
     * @throws RuntimeException Si ocurre algún error durante la actualización
     */
    public Recurso update(Recurso recurso, Integer id) {
        if (recurso == null) {
            throw new IllegalArgumentException("El recurso no puede ser nulo");
        }

        Recurso recursoExistente = recursoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Recurso no encontrado con ID: " + id));

        try {
            if (recurso.getTipoRecurso() != null) {
                recursoExistente.setTipoRecurso(recurso.getTipoRecurso());
            }

            if (recurso.getNombre() != null) {
                if (recurso.getNombre().length() > 50) {
                    throw new IllegalArgumentException("El Nombre no puede exceder los 50 caracteres");
                } else {
                    recursoExistente.setNombre(recurso.getNombre());
                }
            }

            if (recurso.getCantidad() != 0) {
                if (String.valueOf(recurso.getCantidad()).length() > 9) {
                    throw new RuntimeException("El valor cantidad excede máximo de caracteres (9)");
                } else {
                    recursoExistente.setCantidad(recurso.getCantidad());
                }
            }

            if (recurso.getEstado() != null) {
                if (recurso.getEstado().length() > 50) {
                    throw new IllegalArgumentException("El Estado no puede exceder los 50 caracteres");
                } else {
                    recursoExistente.setEstado(recurso.getEstado());
                }
            }

            validarRecurso(recursoExistente);
            return recursoRepository.save(recursoExistente);
        }catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Error al actualizar el recurso: " + e.getMessage());
        } catch (NoSuchElementException  f) {
            throw new NoSuchElementException("Error al actualizar el recurso: " + f.getMessage());
        } catch (Exception g) {
            throw new RuntimeException("Error al actualizar el recurso: " + g.getMessage());
        }
    }

    /**
     * Elimina un recurso del sistema.
     * @param id Identificador del recurso a eliminar
     * @throws NoSuchElementException Si no se encuentra el recurso
     */
    public void delete(Integer id) {
        if (!recursoRepository.existsById(id)) {
            throw new NoSuchElementException("No se encontró recurso con ID: " + id);
        }
        recursoRepository.deleteById(id);
    }


    // MÉTODOS PRIVADOS DE VALIDACIÓN Y UTILIDADES

    /**
     * Valida el recurso
     * @param recurso Recurso
     * @throws IllegalArgumentException Si el recurso no cumple con las reglas de validación
     */
    public void validarRecurso(Recurso recurso) {

        try{
            if (recurso.getCantidad() <= 0) {
                throw new IllegalArgumentException("La Cantidad debe ser un número positivo");
            } else {
                if (String.valueOf(recurso.getCantidad()).length() > 9) {
                    throw new IllegalArgumentException("El valor cantidad excede máximo de caracteres (9)");
                }
            }

            if (recurso.getNombre() != null) {
                if (recurso.getNombre().length() > 50) {
                    throw new IllegalArgumentException("El valor nombre del recurso excede máximo de caracteres (50)");
                }
            } else {
                throw new IllegalArgumentException("El nombre del recurso es requerido");
            }

            if (recurso.getEstado() != null) {
                if (recurso.getEstado().length() > 50) {
                    throw new IllegalArgumentException("El nombre Estado del recurso excede máximo de caracteres (50)");
                }
            } else {
                throw new IllegalArgumentException("El nombre del Estado es requerido");
            }

            if (recurso.getTipoRecurso() == null) {
                throw new IllegalArgumentException("El tipo de Recurso no puede ser nulo");
            }else{
                validarTipoRecurso(recurso.getTipoRecurso());
            }

        }catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Error al validar el vehículo: " + e.getMessage());
        }catch (Exception f) {
            throw new RuntimeException("Error al validar el vehículo: " + f.getMessage());
        }
    }

    /**
     * Valida los datos de un tipo de recurso
     * @param tipoRecurso Tipo de recurso a validar
     * @throws IllegalArgumentException Si el tipo de recurso no cumple con las reglas de validación
     */
    public void validarTipoRecurso(TipoRecurso tipoRecurso) {

        try{
            if (tipoRecurso.getNombre() == null || tipoRecurso.getNombre().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del Tipo recurso es requerido");
            }
            if (tipoRecurso.getNombre().length() > 50) {
                throw new IllegalArgumentException("El nombre no puede exceder los 50 caracteres");
            }

        }catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Error al validar el Tipo Recurso: " + e.getMessage());
        }catch (Exception f) {
            throw new RuntimeException("Error al validar el Tipo Recurso: " + f.getMessage());
        }
    }

    // MÉTODOS DE ASIGNACIÓN DE RELACIONES

    /**
     * Asigna un tipo de recurso a un recurso
     * @param recursoId ID del recurso
     * @param tipoRecursoId ID del tipo de recurso
     */
    public void asignarTipoRecurso(Integer recursoId, Integer tipoRecursoId) {
        Recurso recurso = recursoRepository.findById(recursoId)
                .orElseThrow(() -> new RuntimeException("Recurso no encontrado"));
        TipoRecurso tipoRecurso = tipoRecursoRepository.findById(tipoRecursoId)
                .orElseThrow(() -> new RuntimeException("Tipo Recurso no encontrado"));
        recurso.setTipoRecurso(tipoRecurso);
        recursoRepository.save(recurso);
    }

}