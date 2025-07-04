package com.SAFE_Rescue.API_Recursos.controller;

import com.SAFE_Rescue.API_Recursos.modelo.TipoVehiculo;
import com.SAFE_Rescue.API_Recursos.modelo.Vehiculo;
import com.SAFE_Rescue.API_Recursos.service.VehiculoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Clase de prueba para el controlador VehiculoController.
 * Proporciona pruebas unitarias para verificar el correcto funcionamiento
 * de los endpoints relacionados con los vehículos.
 */
@WebMvcTest(VehiculoController.class)
public class VehiculoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VehiculoService vehiculoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Faker faker;
    private Vehiculo vehiculo;
    private Integer id;

    /**
     * Configura el entorno de pruebas antes de cada prueba.
     * Inicializa Faker y crea un objeto Vehiculo para las pruebas.
     */
    @BeforeEach
    public void setUp() {
        faker = new Faker();
        vehiculo = new Vehiculo();
        vehiculo.setId(1);
        vehiculo.setMarca(faker.company().name());
        vehiculo.setModelo(faker.commerce().productName());
        vehiculo.setEstado("Activo");
        vehiculo.setConductor(faker.name().fullName());
        vehiculo.setPatente("ABC123");
        vehiculo.setTipoVehiculo(new TipoVehiculo(1, faker.commerce().department()));
        id = vehiculo.getId();
    }

    /**
     * Prueba que verifica la obtención de todos los vehículos existentes.
     * Asegura que se devuelve un estado 200 OK y la lista de vehículos.
     */
    @Test
    public void listarVehiculosTest() throws Exception {
        // Arrange
        List<Vehiculo> vehiculos = new ArrayList<>();
        vehiculos.add(vehiculo);
        when(vehiculoService.findAll()).thenReturn(vehiculos);

        // Act & Assert
        mockMvc.perform(get("/api-recursos/v1/vehiculos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(vehiculo.getId()))
                .andExpect(jsonPath("$[0].marca").value(vehiculo.getMarca()))
                .andExpect(jsonPath("$[0].modelo").value(vehiculo.getModelo()))
                .andExpect(jsonPath("$[0].patente").value(vehiculo.getPatente()))
                .andExpect(jsonPath("$[0].conductor").value(vehiculo.getConductor()))
                .andExpect(jsonPath("$[0].estado").value(vehiculo.getEstado()))
                .andExpect(jsonPath("$[0].tipoVehiculo.id").value(vehiculo.getTipoVehiculo().getId()))
                .andExpect(jsonPath("$[0].tipoVehiculo.nombre").value(vehiculo.getTipoVehiculo().getNombre()));
    }

    /**
     * Prueba que verifica la búsqueda de un vehículo existente por su ID.
     * Asegura que se devuelve un estado 200 OK y el vehículo encontrado.
     */
    @Test
    public void buscarVehiculoTest() throws Exception {
        // Arrange
        when(vehiculoService.findById(id)).thenReturn(vehiculo);

        // Act & Assert
        mockMvc.perform(get("/api-recursos/v1/vehiculos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(vehiculo.getId()))
                .andExpect(jsonPath("$.marca").value(vehiculo.getMarca()))
                .andExpect(jsonPath("$.modelo").value(vehiculo.getModelo()))
                .andExpect(jsonPath("$.patente").value(vehiculo.getPatente()))
                .andExpect(jsonPath("$.conductor").value(vehiculo.getConductor()))
                .andExpect(jsonPath("$.estado").value(vehiculo.getEstado()))
                .andExpect(jsonPath("$.tipoVehiculo.id").value(vehiculo.getTipoVehiculo().getId()))
                .andExpect(jsonPath("$.tipoVehiculo.nombre").value(vehiculo.getTipoVehiculo().getNombre()));
    }

    /**
     * Prueba que verifica la creación de un nuevo vehículo.
     * Asegura que se devuelve un estado 201 CREATED al agregar un vehículo exitosamente.
     */
    @Test
    public void agregarVehiculoTest() throws Exception {
        // Arrange
        when(vehiculoService.save(any(Vehiculo.class))).thenReturn(vehiculo);

        // Act & Assert
        mockMvc.perform(post("/api-recursos/v1/vehiculos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehiculo))) // Convertir Vehiculo a JSON
                .andExpect(status().isCreated())
                .andExpect(content().string("Vehiculo creado con éxito."));
    }

    /**
     * Prueba que verifica la actualización de un vehículo existente.
     * Asegura que se devuelve un estado 200 OK al actualizar correctamente.
     */
    @Test
    public void actualizarVehiculoTest() throws Exception {
        // Arrange
        when(vehiculoService.update(any(Vehiculo.class), eq(id))).thenReturn(vehiculo);

        // Act & Assert
        mockMvc.perform(put("/api-recursos/v1/vehiculos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehiculo))) // Convertir Vehiculo a JSON
                .andExpect(status().isOk())
                .andExpect(content().string("Actualizado con éxito"));
    }

    /**
     * Prueba que verifica la eliminación de un vehículo existente.
     * Asegura que se devuelve un estado 200 OK al eliminar correctamente.
     */
    @Test
    public void eliminarVehiculoTest() throws Exception {
        // Arrange
        doNothing().when(vehiculoService).delete(id);

        // Act & Assert
        mockMvc.perform(delete("/api-recursos/v1/vehiculos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("Vehiculo eliminado con éxito."));
    }

    // ERRORES

    /**
     * Prueba que verifica el comportamiento cuando no hay vehículos registrados.
     * Asegura que se devuelve un estado 204 NO CONTENT.
     */
    @Test
    public void listarTest_VehiculosNoExistentes() throws Exception {
        // Arrange
        when(vehiculoService.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api-recursos/v1/vehiculos"))
                .andExpect(status().isNoContent());
    }

    /**
     * Prueba que verifica el comportamiento al buscar un vehículo que no existe.
     * Asegura que se devuelve un estado 404 NOT FOUND.
     */
    @Test
    public void buscarVehiculoTest_VehiculoNoExistente() throws Exception {
        // Arrange
        when(vehiculoService.findById(id)).thenThrow(new NoSuchElementException("Vehiculo no encontrado"));

        // Act & Assert
        mockMvc.perform(get("/api-recursos/v1/vehiculos/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Vehiculo no encontrado"));
    }

    /**
     * Prueba que verifica el manejo de errores al intentar agregar un vehículo.
     * Asegura que se devuelve un estado 400 BAD REQUEST al ocurrir un error.
     */
    @Test
    public void agregarVehiculoTest_Error() throws Exception {
        // Arrange
        when(vehiculoService.save(any(Vehiculo.class))).thenThrow(new RuntimeException("Error al crear el vehículo"));

        // Act & Assert
        mockMvc.perform(post("/api-recursos/v1/vehiculos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehiculo))) // Convertir Vehiculo a JSON
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error al crear el vehículo"));
    }

    /**
     * Prueba que verifica el comportamiento al intentar actualizar un vehículo que no existe.
     * Asegura que se devuelve un estado 404 NOT FOUND.
     */
    @Test
    public void actualizarVehiculoTest_VehiculoNoExistente() throws Exception {
        // Arrange
        when(vehiculoService.update(any(Vehiculo.class), eq(id))).thenThrow(new NoSuchElementException("Vehiculo no encontrado"));

        // Act & Assert
        mockMvc.perform(put("/api-recursos/v1/vehiculos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehiculo))) // Convertir Vehiculo a JSON
                .andExpect(status().isNotFound())
                .andExpect(content().string("Vehiculo no encontrado"));
    }

    /**
     * Prueba que verifica el comportamiento al intentar eliminar un vehículo que no existe.
     * Asegura que se devuelve un estado 404 NOT FOUND.
     */
    @Test
    public void eliminarVehiculoTest_VehiculoNoExistente() throws Exception {
        // Arrange
        doThrow(new NoSuchElementException("Vehiculo no encontrado")).when(vehiculoService).delete(id);

        // Act & Assert
        mockMvc.perform(delete("/api-recursos/v1/vehiculos/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Vehiculo no encontrado"));
    }

    /**
     * Prueba que verifica el comportamiento al intentar asignar un tipo de vehículo a un vehículo que no existe.
     * Asegura que se devuelve un estado 404 NOT FOUND.
     */
    @Test
    public void asignarTipoVehiculoTest_VehiculoNoEncontrado() throws Exception {
        Integer vehiculoId = 1;
        Integer tipoVehiculoId = 1;
        doThrow(new RuntimeException("Vehiculo no encontrado")).when(vehiculoService).asignarTipoVehiculo(vehiculoId, tipoVehiculoId);

        // Act & Assert
        mockMvc.perform(post("/api-recursos/v1/vehiculos/{vehiculoId}/asignar-tipo-vehiculo/{tipoVehiculoId}", vehiculoId, tipoVehiculoId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Vehiculo no encontrado"));
    }
}