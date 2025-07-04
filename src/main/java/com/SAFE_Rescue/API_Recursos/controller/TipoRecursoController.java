package com.SAFE_Rescue.API_Recursos.controller;

import com.SAFE_Rescue.API_Recursos.modelo.TipoRecurso;
import com.SAFE_Rescue.API_Recursos.service.TipoRecursoService;
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
 * Controlador REST para la gestión de tipos de recursos.
 * Proporciona endpoints para operaciones CRUD de tipos de recursos.
 */
@RestController
@RequestMapping("/api-recursos/v1/tipos-recursos")
public class TipoRecursoController {

    // SERVICIOS INYECTADOS
    @Autowired
    private TipoRecursoService tipoRecursoService;

    // OPERACIONES CRUD BÁSICAS

    /**
     * Obtiene todos los tipos de recursos registrados en el sistema.
     * @return ResponseEntity con lista de tipos de recursos o estado NO_CONTENT si no hay registros
     */
    @GetMapping
    @Operation(summary = "Obtener todos los tipos de recursos", description = "Devuelve una lista de todos los tipos de recursos registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tipos de recursos encontrada"),
            @ApiResponse(responseCode = "204", description = "No hay tipos de recursos registrados")
    })
    public ResponseEntity<List<TipoRecurso>> listarTiposRecursos() {
        List<TipoRecurso> tipoRecurso = tipoRecursoService.findAll();
        if (tipoRecurso.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(tipoRecurso);
    }

    /**
     * Busca un tipo de recurso por su ID.
     * @param id ID del tipo de recurso a buscar
     * @return ResponseEntity con el tipo de recurso encontrado o mensaje de error
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar tipo de recurso por ID", description = "Devuelve un tipo de recurso específico dado su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de recurso encontrado"),
            @ApiResponse(responseCode = "404", description = "Tipo de recurso no encontrado")
    })
    public ResponseEntity<?> buscarTipoRecurso(@PathVariable Integer id) {
        TipoRecurso tipoRecurso;
        try {
            tipoRecurso = tipoRecursoService.findById(id);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<String>("Tipo Recurso no encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(tipoRecurso);
    }

    /**
     * Crea un nuevo tipo de recurso.
     * @param tipoRecurso Datos del tipo de recurso a crear
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PostMapping
    @Operation(summary = "Crear nuevo tipo de recurso", description = "Crea un nuevo tipo de recurso en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tipo de recurso creado con éxito"),
            @ApiResponse(responseCode = "400", description = "Error de validación"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<String> agregarTipoRecurso(@RequestBody TipoRecurso tipoRecurso) {
        try {
            tipoRecursoService.save(tipoRecurso);
            return ResponseEntity.status(HttpStatus.CREATED).body("Tipo Recurso creado con éxito.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }

    /**
     * Actualiza un tipo de recurso existente.
     * @param id ID del tipo de recurso a actualizar
     * @param tipoRecurso Datos actualizados del tipo de recurso
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar tipo de recurso", description = "Actualiza la información de un tipo de recurso existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de recurso actualizado con éxito"),
            @ApiResponse(responseCode = "404", description = "Tipo de recurso no encontrado"),
            @ApiResponse(responseCode = "400", description = "Error de validación"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<String> actualizarTipoRecurso(@PathVariable Integer id, @RequestBody TipoRecurso tipoRecurso) {
        try {
            TipoRecurso nuevoTipoRecurso = tipoRecursoService.update(tipoRecurso, id);
            return ResponseEntity.ok("Actualizado con éxito");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tipo Recurso no encontrado");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }

    /**
     * Elimina un tipo de recurso del sistema.
     * @param id ID del tipo de recurso a eliminar
     * @return ResponseEntity con mensaje de confirmación
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar tipo de recurso", description = "Elimina un tipo de recurso del sistema dado su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de recurso eliminado con éxito"),
            @ApiResponse(responseCode = "404", description = "Tipo de recurso no encontrado"),
            @ApiResponse(responseCode = "400", description = "Error de validación"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<String> eliminarTipoEquipo(@PathVariable Integer id) {
        try {
            tipoRecursoService.delete(id);
            return ResponseEntity.ok("Tipo Recurso eliminado con éxito.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tipo Recurso no encontrado");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }
}