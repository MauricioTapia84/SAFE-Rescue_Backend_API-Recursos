package com.SAFE_Rescue.API_Recursos.controller;

import com.SAFE_Rescue.API_Recursos.modelo.TipoVehiculo;
import com.SAFE_Rescue.API_Recursos.service.TipoVehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Controlador REST para la gestión de tipos de Vehiculos
 * Proporciona endpoints para operaciones CRUD de tipos de Vehiculos
 */
@RestController
@RequestMapping("/api-recursos/v1/tipos-vehiculos")
public class TipoVehiculoController {

    // SERVICIOS INYECTADOS

    @Autowired
    private TipoVehiculoService tipoVehiculoService;

    // OPERACIONES CRUD BÁSICAS

    /**
     * Obtiene todos los tipos de Vehiculos registrados en el sistema.
     * @return ResponseEntity con lista de tipos de Vehiculos o estado NO_CONTENT si no hay registros
     */
    @GetMapping
    public ResponseEntity<List<TipoVehiculo>> listarTiposVehiculos() {
        List<TipoVehiculo> tipoVehiculo = tipoVehiculoService.findAll();
        if(tipoVehiculo.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(tipoVehiculo);
    }

    /**
     * Busca un tipo de Vehiculo por su ID.
     * @param id ID del tipo de Vehiculo a buscar
     * @return ResponseEntity con el tipo de Vehiculo encontrado o mensaje de error
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarTipoVehiculo(@PathVariable int id) {
        TipoVehiculo tipoVehiculo;
        try {
            tipoVehiculo = tipoVehiculoService.findByID(id);
        } catch(NoSuchElementException e) {
            return new ResponseEntity<String>("Tipo Vehiculo no encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(tipoVehiculo);
    }

    /**
     * Crea un nuevo tipo de Vehiculo
     * @param tipoVehiculo Datos del tipo de Vehiculo a crear
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PostMapping
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
     * Actualiza un tipo de Vehiculo existente.
     * @param id ID del tipo de Vehiculo a actualizar
     * @param tipoVehiculo Datos actualizados del tipo de Vehiculo
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarTipoVehiculo(@PathVariable long id, @RequestBody TipoVehiculo tipoVehiculo) {
        try {
            TipoVehiculo nuevoTipoVehiculo = tipoVehiculoService.update(tipoVehiculo, id);
            return ResponseEntity.ok("Actualizado con éxito");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Tipo Vehiculo no encontrado");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor.");
        }
    }

    /**
     * Elimina un tipo de Vehiculo del sistema.
     * @param id ID del tipo de Vehiculo a eliminar
     * @return ResponseEntity con mensaje de confirmación
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarTipoVehiculo(@PathVariable long id) {
        try {
            tipoVehiculoService.delete(id);
            return ResponseEntity.ok("Tipo Vehiculo eliminado con éxito.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Tipo Vehiculo no encontrado");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor.");
        }
    }
}
