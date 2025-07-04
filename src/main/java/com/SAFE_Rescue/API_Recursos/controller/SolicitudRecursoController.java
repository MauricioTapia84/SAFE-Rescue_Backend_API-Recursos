package com.SAFE_Rescue.API_Recursos.controller;

import com.SAFE_Rescue.API_Recursos.modelo.SolicitudRecurso;
import com.SAFE_Rescue.API_Recursos.service.SolicitudRecursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * Controlador REST para la gestión de solicitudes de recursos.
 * Proporciona endpoints para operaciones CRUD de solicitudes de recursos.
 */
@RestController
@RequestMapping("/api-recursos/v1/solicitudes-recursos")
public class SolicitudRecursoController {

    // SERVICIOS INYECTADOS
    @Autowired
    private SolicitudRecursoService solicitudRecursoService;

    // OPERACIONES CRUD BÁSICAS

    /**
     * Obtiene todas las solicitudes de recursos registradas en el sistema.
     * @return ResponseEntity con lista de solicitudes de recursos o estado NO_CONTENT si no hay registros
     */
    @GetMapping
    @Operation(summary = "Obtener todas las solicitudes de recursos", description = "Devuelve una lista de todas las solicitudes de recursos registradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de solicitudes de recursos encontrada"),
            @ApiResponse(responseCode = "204", description = "No hay solicitudes de recursos registradas")
    })
    public ResponseEntity<List<SolicitudRecurso>> listarsolicitud() {
        List<SolicitudRecurso> solicitudRecurso = solicitudRecursoService.findAll();
        if (solicitudRecurso.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(solicitudRecurso);
    }

    /**
     * Busca una solicitud de recurso por su ID.
     * @param id ID de la solicitud a buscar
     * @return ResponseEntity con la solicitud encontrada o mensaje de error
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar solicitud por ID", description = "Devuelve una solicitud específica dada su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud de recurso encontrada"),
            @ApiResponse(responseCode = "404", description = "Solicitud de recurso no encontrada")
    })
    public ResponseEntity<?> buscarSolicitud(@PathVariable Integer id) {
        SolicitudRecurso solicitudRecurso;
        try {
            solicitudRecurso = solicitudRecursoService.findById(id);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<String>("Solicitud Recurso no encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(solicitudRecurso);
    }

    /**
     * Crea una nueva solicitud de recurso.
     * @param solicitudRecurso Datos de la solicitud a crear
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PostMapping
    @Operation(summary = "Crear nueva solicitud de recurso", description = "Crea una nueva solicitud de recurso en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Solicitud de recurso creada con éxito"),
            @ApiResponse(responseCode = "400", description = "Error de validación"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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
     * Actualiza una solicitud existente.
     * @param id ID de la solicitud a actualizar
     * @param solicitudRecurso Datos actualizados de la solicitud
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar solicitud de recurso", description = "Actualiza la información de una solicitud existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud de recurso actualizada con éxito"),
            @ApiResponse(responseCode = "404", description = "Solicitud de recurso no encontrada"),
            @ApiResponse(responseCode = "400", description = "Error de validación"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<String> actualizarSolicitud(@PathVariable Integer id, @RequestBody SolicitudRecurso solicitudRecurso) {
        try {
            SolicitudRecurso nuevoSolicitudRecurso = solicitudRecursoService.update(solicitudRecurso, id);
            return ResponseEntity.ok("Actualizado con éxito");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Solicitud Recurso no encontrada");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }

    /**
     * Elimina una solicitud del sistema.
     * @param id ID de la solicitud a eliminar
     * @return ResponseEntity con mensaje de confirmación
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar solicitud de recurso", description = "Elimina una solicitud de recurso del sistema dado su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud de recurso eliminada con éxito"),
            @ApiResponse(responseCode = "404", description = "Solicitud de recurso no encontrada"),
            @ApiResponse(responseCode = "400", description = "Error de validación"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<String> eliminarSolicitud(@PathVariable Integer id) {
        try {
            solicitudRecursoService.delete(id);
            return ResponseEntity.ok("Solicitud Recurso eliminada con éxito.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Solicitud Recurso no encontrada");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }

    // GESTIÓN DE RELACIONES

    /**
     * Asigna un recurso a una solicitud de recurso.
     * @param solicitudRecursoId ID de la solicitud
     * @param recursoId ID del recurso a asignar
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PostMapping("/{solicitudRecursoId}/asignar-recurso/{recursoId}")
    @Operation(summary = "Asignar recurso a solicitud", description = "Asigna un recurso a una solicitud de recurso específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recurso asignado a la solicitud exitosamente"),
            @ApiResponse(responseCode = "404", description = "Solicitud de recurso no encontrada o recurso no encontrado"),
            @ApiResponse(responseCode = "400", description = "Error de validación")
    })
    public ResponseEntity<String> asignarRecurso(@PathVariable Integer solicitudRecursoId, @PathVariable Integer recursoId) {
        try {
            solicitudRecursoService.asignarRecurso(solicitudRecursoId, recursoId);
            return ResponseEntity.ok("Recurso asignado a la Solicitud Recurso exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Asigna un bombero a una solicitud de recurso.
     * @param solicitudRecursoId ID de la solicitud
     * @param bomberoId ID del bombero a asignar
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PostMapping("/{solicitudRecursoId}/asignar-bombero/{bomberoId}")
    @Operation(summary = "Asignar bombero a solicitud", description = "Asigna un bombero a una solicitud de recurso específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bombero asignado a la solicitud exitosamente"),
            @ApiResponse(responseCode = "404", description = "Solicitud de recurso no encontrada o bombero no encontrado"),
            @ApiResponse(responseCode = "400", description = "Error de validación")
    })
    public ResponseEntity<String> asignarBombero(@PathVariable Integer solicitudRecursoId, @PathVariable Integer bomberoId) {
        try {
            solicitudRecursoService.asignarBombero(solicitudRecursoId, bomberoId);
            return ResponseEntity.ok("Bombero asignado a la Solicitud Recurso exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}