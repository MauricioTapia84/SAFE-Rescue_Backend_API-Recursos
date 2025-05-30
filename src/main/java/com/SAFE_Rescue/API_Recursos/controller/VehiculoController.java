package com.SAFE_Rescue.API_Recursos.controller;

import com.SAFE_Rescue.API_Recursos.modelo.Vehiculo;
import com.SAFE_Rescue.API_Recursos.service.VehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Controlador REST para la gestión de Vehiculos
 * Proporciona endpoints para operaciones CRUD y gestión de relaciones de Vehiculos
 */
@RestController
@RequestMapping("/api-recursos/v1/vehiculos")
public class VehiculoController {

    @Autowired
    private VehiculoService vehiculoService;

    // OPERACIONES CRUD BÁSICAS

    /**
     * Obtiene todos los Vehiculo registrados en el sistema.
     * @return ResponseEntity con lista de Vehiculos o estado NO_CONTENT si no hay registros
     */
    @GetMapping
    public ResponseEntity<List<Vehiculo>> listar(){

        List<Vehiculo> vehiculos = vehiculoService.findAll();
        if(vehiculos.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(vehiculos);
    }

    /**
     * Busca un Vehiculo por su ID.
     * @param id ID del Vehiculos a buscar
     * @return ResponseEntity con el Vehiculos encontrado o mensaje de error
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarVehiculo(@PathVariable long id) {
        Vehiculo vehiculo;

        try {
            vehiculo = vehiculoService.findByID(id);
        }catch(NoSuchElementException e){
            return new ResponseEntity<String>("Vehiculo no encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(vehiculo);

    }

    /**
     * Crea un nuevo Vehiculo
     * @param vehiculo Datos del Vehiculo a crear
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PostMapping
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
     * Actualiza un Vehiculo existente.
     * @param id ID del Vehiculo a actualizar
     * @param vehiculo Datos actualizados del Vehiculo
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarVehiculo(@PathVariable long id, @RequestBody Vehiculo vehiculo) {
        try {
            Vehiculo nuevoVehiculo = vehiculoService.update(vehiculo, id);
            return ResponseEntity.ok("Actualizado con éxito");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Vehiculo no encontrado");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor.");
        }
    }

    /**
     * Elimina un Vehiculo del sistema.
     * @param id ID del Vehiculo a eliminar
     * @return ResponseEntity con mensaje de confirmación
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarVehiculo(@PathVariable long id) {
        try {
            vehiculoService.delete(id);
            return ResponseEntity.ok("Vehiculoeliminado con éxito.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Vehiculo no encontrado");
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
     * Asigna un tipo de Vehiculo a un Vehiculo
     * @param vehiculoId ID del Vehiculo
     * @param tipoVehiculoId ID del tipo de Vehiculo a asignar
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @PostMapping("/{vehiculoId}/asignar-tipo-vehiculo/{tipoVehiculoId}")
    public ResponseEntity<String> asignarTipoVehiculo(@PathVariable int vehiculoId, @PathVariable int tipoVehiculoId) {
        try {
            vehiculoService.asignarTipoVehiculo(vehiculoId, tipoVehiculoId);
            return ResponseEntity.ok("Tipo Vehiculo asignado al Vehiculo exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
