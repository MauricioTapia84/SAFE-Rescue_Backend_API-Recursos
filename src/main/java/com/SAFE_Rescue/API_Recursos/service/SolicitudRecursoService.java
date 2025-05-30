package com.SAFE_Rescue.API_Recursos.service;

import com.SAFE_Rescue.API_Recursos.modelo.Bombero;
import com.SAFE_Rescue.API_Recursos.modelo.Recurso;
import com.SAFE_Rescue.API_Recursos.modelo.SolicitudRecurso;
import com.SAFE_Rescue.API_Recursos.modelo.TipoRecurso;
import com.SAFE_Rescue.API_Recursos.repository.BomberoRepository;
import com.SAFE_Rescue.API_Recursos.repository.RecursoRepository;
import com.SAFE_Rescue.API_Recursos.repository.SolicitudRecursoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Servicio para gestionar operaciones relacionadas con solicitudes de recursos
 * <p>
 * Proporciona métodos para CRUD de compañías, validación de reglas de negocio
 * </p>
 */
@Service
public class SolicitudRecursoService {

    @Autowired
    private SolicitudRecursoRepository solicitudRecursoRepository;

    @Autowired
    private BomberoRepository bomberoRepository;

    @Autowired
    private RecursoRepository recursoRepository;


    // MÉTODOS CRUD PRINCIPALES

    /**
     * Obtiene todas las solicitudes registradas.
     *
     * @return Lista de todas las solicitudes
     */
    public List<SolicitudRecurso> findAll() {
        return solicitudRecursoRepository.findAll();
    }

    /**
     * Busca una solicitud por su ID.
     *
     * @param id Identificador único de la solicitud
     * @return La solicitud encontrada
     * @throws NoSuchElementException Si no se encuentra la solicitud
     */
    public SolicitudRecurso findByID(long id) {
        return solicitudRecursoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Solicitud de recurso no encontrada con ID: " + id));
    }

    /**
     * Guarda una nueva solicitud en el sistema
     *
     * @param solicitudRecurso solicitud a guardar
     * @return solicitud guardada con ID generado
     * @throws IllegalArgumentException Si la solicitud no pasa las validaciones
     * @throws RuntimeException         Si ocurre un error al guardar o validar la ubicación
     */
    public SolicitudRecurso save(SolicitudRecurso solicitudRecurso) {
        try {
            validarSolicitud(solicitudRecurso);
            return solicitudRecursoRepository.save(solicitudRecurso);
        } catch (EntityNotFoundException e) {
            throw new RuntimeException("Error al guardar la Solicitud Recurso: " + e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error inesperado: " + ex.getMessage());
        }
    }

    /**
     * Actualiza una solicitud existente con validación de datos.
     *
     * @param solicitudRecurso Datos actualizados de la solicitud
     * @param id               ID de la solicitud a actualizar
     * @return solicitud actualizada
     * @throws NoSuchElementException   Si no se encuentra la solicitud con el ID especificado
     * @throws IllegalArgumentException Si los datos no pasan las validaciones
     */
    public SolicitudRecurso update(SolicitudRecurso solicitudRecurso, long id) {
        SolicitudRecurso antiguaSolicitudRecurso = solicitudRecursoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Solicitud de recurso no encontrada con ID: " + id));


        if (solicitudRecurso.getTitulo() != null) {
            if (solicitudRecurso.getTitulo().length() > 50) {
                throw new IllegalArgumentException("El titulo no puede exceder los 50 caracteres");
            } else {
                antiguaSolicitudRecurso.setTitulo(solicitudRecurso.getTitulo());
            }
        }

        if (solicitudRecurso.getDetalle() != null) {
            if (solicitudRecurso.getDetalle().length() > 400) {
                throw new IllegalArgumentException("El Detalle no puede exceder los 400 caracteres");
            } else {
                antiguaSolicitudRecurso.setDetalle(solicitudRecurso.getDetalle());
            }
        }

        // Actualizar clases asociadas
        if (solicitudRecurso.getBombero() != null) {
            antiguaSolicitudRecurso.setBombero(solicitudRecurso.getBombero());
        }

        if (solicitudRecurso.getRecurso() != null) {
            antiguaSolicitudRecurso.setRecurso(solicitudRecurso.getRecurso());
        }

        return solicitudRecursoRepository.save(antiguaSolicitudRecurso);
    }

    /**
     * Elimina una solicitud por su ID.
     *
     * @param id ID de la solicitud a eliminar
     * @throws NoSuchElementException Si no se encuentra la solicitud
     */
    public void delete(long id) {
        if (!solicitudRecursoRepository.existsById(id)) {
            throw new NoSuchElementException("Solicitud de recurso no encontrada con ID: " + id);
        }
        solicitudRecursoRepository.deleteById(id);
    }


    //Validaciones

    /**
     * Valida los datos básicos de una solicitud
     *
     * @param solicitudRecurso solicitud a validar
     * @throws IllegalArgumentException Si la solicitud no cumple con las reglas de validación
     */
    private void validarSolicitud(SolicitudRecurso solicitudRecurso) {
        if (solicitudRecurso.getTitulo() == null || solicitudRecurso.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El Titulo de la solicitud recurso es requerido");
        }

        if (solicitudRecurso.getDetalle() == null || solicitudRecurso.getDetalle().trim().isEmpty()) {
            throw new IllegalArgumentException("El Detalle de la solicitud recurso es requerido");
        }

        if (solicitudRecurso.getDetalle().length() > 400) {
            throw new IllegalArgumentException("El Detalle no puede exceder los 400 caracteres");
        }

        if (solicitudRecurso.getTitulo().length() > 50) {
            throw new IllegalArgumentException("El Titulo no puede exceder los 50 caracteres");
        }

        validarBombero(solicitudRecurso.getBombero());
        validarRecurso(solicitudRecurso.getRecurso());
    }

    /**
     * Valida los datos de un Bombero
     *
     * @param bombero Bombero a validar
     * @throws IllegalArgumentException Si la ubicación no cumple con las reglas de validación
     */
    private void validarBombero(Bombero bombero) {

        if (bomberoRepository.existsByTelefono(bombero.getTelefono())) {
            throw new RuntimeException("El Telefono ya existe");
        }

        if (bombero.getTelefono() <= 0) {
            throw new IllegalArgumentException("El Telefono debe ser un número positivo");
        } else {
            if (String.valueOf(bombero.getTelefono()).length() > 9) {
                throw new RuntimeException("El valor telefono excede máximo de caracteres (9)");
            }
        }

        if (bombero.getNombre() != null) {
            if (bombero.getNombre().length() > 50) {
                throw new RuntimeException("El valor nombre excede máximo de caracteres (50)");
            }
        } else {
            throw new IllegalArgumentException("El nombre es requerido");
        }

        if (bombero.getAPaterno() != null) {
            if (bombero.getAPaterno().length() > 50) {
                throw new RuntimeException("El valor Apellido Paterno excede máximo de caracteres (50)");
            }
        } else {
            throw new IllegalArgumentException("El Apellido Paterno es requerido");
        }

        if (bombero.getAMaterno() != null) {
            if (bombero.getAMaterno().length() > 50) {
                throw new RuntimeException("El valor Apellido Materno excede máximo de caracteres (50)");
            }
        } else {
            throw new IllegalArgumentException("El Apellido Materno es requerido");
        }

    }

    private void validarRecurso(Recurso recurso) {

        if (recurso.getCantidad() <= 0) {
            throw new IllegalArgumentException("La Cantidad debe ser un número positivo");
        } else {
            if (String.valueOf(recurso.getCantidad()).length() > 9) {
                throw new RuntimeException("El valor cantidad excede máximo de caracteres (9)");
            }
        }

        if (recurso.getNombre() != null) {
            if (recurso.getNombre().length() > 50) {
                throw new RuntimeException("El valor nombre del recurso excede máximo de caracteres (50)");
            }
        } else {
            throw new IllegalArgumentException("El nombre del recurso es requerido");
        }

    }

    /**
     * Asigna un recurso a una Solicitud de recurso
     * @param solicitudRecursoId ID de la Solicitud Recurso
     * @param recursoId ID del recurso
     */
    public void asignarRecurso(long solicitudRecursoId, long recursoId) {
        SolicitudRecurso solicitudRecurso = solicitudRecursoRepository.findById(solicitudRecursoId)
                .orElseThrow(() -> new RuntimeException("Solicitud Recurso no encontrada"));
        Recurso recurso = recursoRepository.findById(recursoId)
                .orElseThrow(() -> new RuntimeException("Recurso no encontrado"));
        solicitudRecurso.setRecurso(recurso);
        solicitudRecursoRepository.save(solicitudRecurso);
    }

    /**
     * Asigna un Bombero a una Solicitud de recurso
     * @param solicitudRecursoId ID de la Solicitud Recurso
     * @param bomberoId ID del bombero
     */
    public void asignarBombero(long solicitudRecursoId, long bomberoId) {
        SolicitudRecurso solicitudRecurso = solicitudRecursoRepository.findById(solicitudRecursoId)
                .orElseThrow(() -> new RuntimeException("Solicitud Recurso no encontrada"));
        Bombero bombero = bomberoRepository.findById(bomberoId)
                .orElseThrow(() -> new RuntimeException("Bombero no encontrado"));
        solicitudRecurso.setBombero(bombero);
        solicitudRecursoRepository.save(solicitudRecurso);
    }




}


