package com.SAFE_Rescue.API_Recursos.controller;

import com.SAFE_Rescue.API_Recursos.modelo.TipoVehiculo;
import com.SAFE_Rescue.API_Recursos.service.TipoVehiculoService;
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
 * Controlador REST para la gestión de tipos de vehículos.
 * Proporciona endpoints para operaciones CRUD de tipos de vehículos.
 */
@RestController
@RequestMapping("/api-recursos/v1/tipos-vehiculos")
public class TipoVehiculoController {

    // SERVICIOS INYECTADOS
    @Autowired
    private TipoVehiculoService tipoVehiculoService;

    // OPERACIONES CRUD BÁSICAS

    /**
     * Obtiene todos los tipos de vehículos registrados en el sistema.
     * @return ResponseEntity con lista de tipos de vehículos o estado NO_CONTENT si no hay registros
     */
    @GetMapping
    @Operation(summary = "Obtener todos los tipos de vehículos", description = "Devuelve una lista de todos los tipos de vehículos registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tipos de vehículos encontrada"),
            @ApiResponse(responseCode = "204", description = "No hay tipos de vehículos registrados")
    })
    public ResponseEntity<List<TipoVehiculo>> listarTiposVehiculos() {
        List<TipoVehiculo> tipoVehiculo = tipoVehiculoService.findAll();
        if (tipoVehiculo.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(tipoVehiculo);
    }

    /**
     * Busca un tipo de vehículo por su ID.
     * @param id ID del tipo de vehículo a buscar
     * @return ResponseEntity con el tipo de vehículo encontrado o mensaje de error
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar tipo de vehículo por ID", description = "Devuelve un tipo de vehículo específico dado su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de vehículo encontrado"),
            @ApiResponse(responseCode = "404", description = "Tipo de vehículo no encontrado")
    })
    public ResponseEntity<?> buscarTipoVehiculo(@PathVariable Integer id) {
        TipoVehiculo tipoVehiculo;
        try {
            tipoVehiculo = tipoVehiculoService.findById(id);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<String>("Tipo Vehiculo no encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(tipoVehiculo);
    }

    /**
     * Crea un nuevo tipo de vehículo.
     * @param tipoVehiculo Datos del tipo de vehículo a crear
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PostMapping
    @Operation(summary = "Crear nuevo tipo de vehículo", description = "Crea un nuevo tipo de vehículo en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tipo de vehículo creado con éxito"),
            @ApiResponse(responseCode = "400", description = "Error de validación"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<String> agregarTipoVehiculo(@RequestBody TipoVehiculo tipoVehiculo) {
        try {
            tipoVehiculoService.save(tipoVehiculo);
            return ResponseEntity.status(HttpStatus.CREATED).body("Tipo Vehiculo creado con éxito.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }

    /**
     * Actualiza un tipo de vehículo existente.
     * @param id ID del tipo de vehículo a actualizar
     * @param tipoVehiculo Datos actualizados del tipo de vehículo
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar tipo de vehículo", description = "Actualiza la información de un tipo de vehículo existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de vehículo actualizado con éxito"),
            @ApiResponse(responseCode = "404", description = "Tipo de vehículo no encontrado"),
            @ApiResponse(responseCode = "400", description = "Error de validación"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<String> actualizarTipoVehiculo(@PathVariable Integer id, @RequestBody TipoVehiculo tipoVehiculo) {
        try {
            TipoVehiculo nuevoTipoVehiculo = tipoVehiculoService.update(tipoVehiculo, id);
            return ResponseEntity.ok("Actualizado con éxito");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tipo Vehiculo no encontrado");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }

    /**
     * Elimina un tipo de vehículo del sistema.
     * @param id ID del tipo de vehículo a eliminar
     * @return ResponseEntity con mensaje de confirmación
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar tipo de vehículo", description = "Elimina un tipo de vehículo del sistema dado su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de vehículo eliminado con éxito"),
            @ApiResponse(responseCode = "404", description = "Tipo de vehículo no encontrado"),
            @ApiResponse(responseCode = "400", description = "Error de validación"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<String> eliminarTipoVehiculo(@PathVariable Integer id) {
        try {
            tipoVehiculoService.delete(id);
            return ResponseEntity.ok("Tipo Vehiculo eliminado con éxito.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tipo Vehiculo no encontrado");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }
}