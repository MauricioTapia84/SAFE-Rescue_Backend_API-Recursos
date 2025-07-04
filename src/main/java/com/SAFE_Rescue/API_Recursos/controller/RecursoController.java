package com.SAFE_Rescue.API_Recursos.controller;

import com.SAFE_Rescue.API_Recursos.modelo.Recurso;
import com.SAFE_Rescue.API_Recursos.service.RecursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Controlador REST para la gestión de recursos.
 * Proporciona endpoints para operaciones CRUD y gestión de relaciones de recursos.
 */
@RestController
@RequestMapping("/api-recursos/v1/recursos")
public class RecursoController {

    // SERVICIOS INYECTADOS
    @Autowired
    private RecursoService recursoService;

    // OPERACIONES CRUD BÁSICAS

    /**
     * Obtiene todos los recursos registrados en el sistema.
     * @return ResponseEntity con lista de recursos o estado NO_CONTENT si no hay registros
     */
    @GetMapping
    public ResponseEntity<List<Recurso>> listar() {
        List<Recurso> recursos = recursoService.findAll();
        if (recursos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(recursos);
    }

    /**
     * Busca un recurso por su ID.
     * @param id ID del recurso a buscar
     * @return ResponseEntity con el recurso encontrado o mensaje de error
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarRecurso(@PathVariable Integer id) {
        Recurso recurso;
        try {
            recurso = recursoService.findById(id);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<String>("Recurso no encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(recurso);
    }

    /**
     * Crea un nuevo recurso.
     * @param recurso Datos del recurso a crear
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PostMapping
    public ResponseEntity<String> agregarRecurso(@RequestBody Recurso recurso) {
        try {
            recursoService.save(recurso);
            return ResponseEntity.status(HttpStatus.CREATED).body("Recurso creado con éxito.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }

    /**
     * Actualiza un recurso existente.
     * @param id ID del recurso a actualizar
     * @param recurso Datos actualizados del recurso
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarRecurso(@PathVariable Integer id, @RequestBody Recurso recurso) {
        try {
            Recurso nuevoRecurso = recursoService.update(recurso, id);
            return ResponseEntity.ok("Actualizado con éxito");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Recurso no encontrado");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor.");
        }
    }

    /**
     * Elimina un recurso del sistema.
     * @param id ID del recurso a eliminar
     * @return ResponseEntity con mensaje de confirmación
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarRecurso(@PathVariable Integer id) {
        try {
            recursoService.delete(id);
            return ResponseEntity.ok("Recurso eliminado con éxito.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Recurso no encontrado");
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
     * Asigna un tipo de recurso a un recurso.
     * @param recursoId ID del recurso
     * @param tipoRecursoId ID del tipo de recurso a asignar
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PostMapping("/{recursoId}/asignar-tipo-recurso/{tipoRecursoId}")
    public ResponseEntity<String> asignarTipoRecurso(@PathVariable int recursoId, @PathVariable int tipoRecursoId) {
        try {
            recursoService.asignarTipoRecurso(recursoId, tipoRecursoId);
            return ResponseEntity.ok("Tipo Recurso asignado al Recurso exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}