package com.SAFE_Rescue.API_Recursos.controller;

import com.SAFE_Rescue.API_Recursos.modelo.Vehiculo;
import com.SAFE_Rescue.API_Recursos.service.VehiculoService;
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
 * Controlador REST para la gestión de vehículos.
 * Proporciona endpoints para operaciones CRUD y gestión de relaciones de vehículos.
 */
@RestController
@RequestMapping("/api-recursos/v1/vehiculos")
public class VehiculoController {

    // SERVICIOS INYECTADOS
    @Autowired
    private VehiculoService vehiculoService;

    // OPERACIONES CRUD BÁSICAS

    /**
     * Obtiene todos los vehículos registrados en el sistema.
     * @return ResponseEntity con lista de vehículos o estado NO_CONTENT si no hay registros
     */
    @GetMapping
    @Operation(summary = "Obtener todos los vehículos", description = "Devuelve una lista de todos los vehículos registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de vehículos encontrada"),
            @ApiResponse(responseCode = "204", description = "No hay vehículos registrados")
    })
    public ResponseEntity<List<Vehiculo>> listar() {
        List<Vehiculo> vehiculos = vehiculoService.findAll();
        if (vehiculos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(vehiculos);
    }

    /**
     * Busca un vehículo por su ID.
     * @param id ID del vehículo a buscar
     * @return ResponseEntity con el vehículo encontrado o mensaje de error
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar vehículo por ID", description = "Devuelve un vehículo específico dado su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehículo encontrado"),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
    })
    public ResponseEntity<?> buscarVehiculo(@PathVariable Integer id) {
        Vehiculo vehiculo;
        try {
            vehiculo = vehiculoService.findById(id);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<String>("Vehiculo no encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(vehiculo);
    }

    /**
     * Crea un nuevo vehículo.
     * @param vehiculo Datos del vehículo a crear
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PostMapping
    @Operation(summary = "Crear nuevo vehículo", description = "Crea un nuevo vehículo en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vehículo creado con éxito"),
            @ApiResponse(responseCode = "400", description = "Error de validación"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<String> agregarVehiculo(@RequestBody Vehiculo vehiculo) {
        try {
            vehiculoService.save(vehiculo);
            return ResponseEntity.status(HttpStatus.CREATED).body("Vehiculo creado con éxito.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }

    /**
     * Actualiza un vehículo existente.
     * @param id ID del vehículo a actualizar
     * @param vehiculo Datos actualizados del vehículo
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar vehículo", description = "Actualiza la información de un vehículo existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehículo actualizado con éxito"),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado"),
            @ApiResponse(responseCode = "400", description = "Error de validación"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<String> actualizarVehiculo(@PathVariable Integer id, @RequestBody Vehiculo vehiculo) {
        try {
            Vehiculo nuevoVehiculo = vehiculoService.update(vehiculo, id);
            return ResponseEntity.ok("Actualizado con éxito");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehiculo no encontrado");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }

    /**
     * Elimina un vehículo del sistema.
     * @param id ID del vehículo a eliminar
     * @return ResponseEntity con mensaje de confirmación
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar vehículo", description = "Elimina un vehículo del sistema dado su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehículo eliminado con éxito"),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado"),
            @ApiResponse(responseCode = "400", description = "Error de validación"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<String> eliminarVehiculo(@PathVariable Integer id) {
        try {
            vehiculoService.delete(id);
            return ResponseEntity.ok("Vehiculo eliminado con éxito.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehiculo no encontrado");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }

    // GESTIÓN DE RELACIONES

    /**
     * Asigna un tipo de vehículo a un vehículo.
     * @param vehiculoId ID del vehículo
     * @param tipoVehiculoId ID del tipo de vehículo a asignar
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PostMapping("/{vehiculoId}/asignar-tipo-vehiculo/{tipoVehiculoId}")
    @Operation(summary = "Asignar tipo de vehículo a vehículo", description = "Asigna un tipo de vehículo a un vehículo específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de vehículo asignado al vehículo exitosamente"),
            @ApiResponse(responseCode = "404", description = "Vehículo o tipo de vehículo no encontrado"),
            @ApiResponse(responseCode = "400", description = "Error de validación")
    })
    public ResponseEntity<String> asignarTipoVehiculo(@PathVariable Integer vehiculoId, @PathVariable Integer tipoVehiculoId) {
        try {
            vehiculoService.asignarTipoVehiculo(vehiculoId, tipoVehiculoId);
            return ResponseEntity.ok("Tipo Vehiculo asignado al Vehiculo exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}