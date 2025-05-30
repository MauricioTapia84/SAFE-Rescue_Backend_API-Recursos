package com.SAFE_Rescue.API_Recursos.controller;

import com.SAFE_Rescue.API_Recursos.modelo.SolicitudRecurso;
import com.SAFE_Rescue.API_Recursos.service.SolicitudRecursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Controlador REST para la gestión de solicitudes recursos
 * Proporciona endpoints para operaciones CRUD de solicitudes recursos
 */
@RestController
@RequestMapping("/api-recursos/v1/solicitudes-recursos")
public class SolicitudRecursoController {

    // SERVICIOS INYECTADOS

    @Autowired
    private SolicitudRecursoService solicitudRecursoService;

    // OPERACIONES CRUD BÁSICAS

    /**
     * Obtiene todas las solicitudes recursos registradas en el sistema.
     * @return ResponseEntity con lista de solicitudes recursos o estado NO_CONTENT si no hay registros
     */
    @GetMapping
    public ResponseEntity<List<SolicitudRecurso>> listarsolicitud(){

        List<SolicitudRecurso> solicitudRecurso = solicitudRecursoService.findAll();
        if(solicitudRecurso.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(solicitudRecurso);
    }

    /**
     * Busca una solicitud por su ID.
     * @param id ID de la solicitud a buscar
     * @return ResponseEntity con la solicitud encontrada o mensaje de error
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarSolicitud(@PathVariable int id) {
        SolicitudRecurso solicitudRecurso;

        try {
            solicitudRecurso = solicitudRecursoService.findByID(id);
        }catch(NoSuchElementException e){
            return new ResponseEntity<String>("Solicitud Recurso no encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(solicitudRecurso);

    }

    /**
     * Crea una nueva Solicitud
     * @param solicitudRecurso Datos de la Solicitud a crear
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PostMapping
    public ResponseEntity<String> agregarSolicitud(@RequestBody SolicitudRecurso solicitudRecurso) {
        try {
            solicitudRecursoService.save(solicitudRecurso);
            return ResponseEntity.status(HttpStatus.CREATED).body("Solicitud Recurso creada con éxito.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }

    /**
     * Actualiza una Solicitud existente.
     * @param id ID de la Solicitud a actualizar
     * @param solicitudRecurso Datos actualizados de la Solicitud
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarSolicitud(@PathVariable long id, @RequestBody SolicitudRecurso solicitudRecurso) {
        try {
            SolicitudRecurso nuevoSolicitudRecurso = solicitudRecursoService.update(solicitudRecurso, id);
            return ResponseEntity.ok("Actualizado con éxito");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Solicitud Recurso no encontrada");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor.");
        }
    }

    /**
     * Elimina una Solicitud del sistema.
     * @param id ID de la Solicitud a eliminar
     * @return ResponseEntity con mensaje de confirmación
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarSolicitud(@PathVariable long id) {
        try {
            solicitudRecursoService.delete(id);
            return ResponseEntity.ok("Solicitud Recurso eliminada con éxito.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Solicitud Recurso no encontrada");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor.");
        }
    }

    // GESTIÓN DE RELACIONES

    /**
     * Asigna un Recurso a una SolicitudRecurso
     * @param solicitudRecursoId ID del Recurso
     * @param recursoId ID de Recurso
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PostMapping("/{solicitudRecursoId}/asignar-recurso/{recursoId}")
    public ResponseEntity<String> asignarRecurso(@PathVariable Long solicitudRecursoId, @PathVariable Long recursoId) {
        try {
            solicitudRecursoService.asignarRecurso(solicitudRecursoId, recursoId);
            return ResponseEntity.ok("Recurso asignado a la Solicitud Recurso exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Asigna un Bombero a una SolicitudRecurso
     * @param solicitudRecursoId ID del Recurso
     * @param bomberoId ID del Bombero
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PostMapping("/{solicitudRecursoId}/asignar-bombero/{bomberoId}")
    public ResponseEntity<String> asignarBombero(@PathVariable Long solicitudRecursoId, @PathVariable Long bomberoId) {
        try {
            solicitudRecursoService.asignarBombero(solicitudRecursoId, bomberoId);
            return ResponseEntity.ok("Bombero asignado a la Solicitud Recurso exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
