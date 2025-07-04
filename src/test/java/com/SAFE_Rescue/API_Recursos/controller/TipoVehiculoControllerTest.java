package com.SAFE_Rescue.API_Recursos.controller;

import com.SAFE_Rescue.API_Recursos.modelo.TipoVehiculo;
import com.SAFE_Rescue.API_Recursos.service.TipoVehiculoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Clase de prueba para el controlador TipoVehiculoController.
 * Proporciona pruebas unitarias para verificar el correcto funcionamiento
 * de los endpoints relacionados con los tipos de equipo.
 */
@WebMvcTest(TipoVehiculoController.class)
public class TipoVehiculoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TipoVehiculoService tipoVehiculoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Faker faker;
    private TipoVehiculo tipoVehiculo;
    private Integer id;

    /**
     * Configura el entorno de pruebas antes de cada prueba.
     * Inicializa Faker y crea un objeto TipoVehiculo para las pruebas.
     */
    @BeforeEach
    public void setUp() {
        faker = new Faker();
        id = 1;
        tipoVehiculo = new TipoVehiculo(1, faker.job().position());
    }

    /**
     * Prueba que verifica la obtención de todos los tipos de vehículos existentes.
     * Asegura que se devuelve un estado 200 OK y la lista de tipos de vehículos.
     */
    @Test
    public void listarTiposVehiculoTest() throws Exception {
        // Arrange
        when(tipoVehiculoService.findAll()).thenReturn(List.of(tipoVehiculo));

        // Act & Assert
        mockMvc.perform(get("/api-recursos/v1/tipos-vehiculos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(tipoVehiculo.getId()))
                .andExpect(jsonPath("$[0].nombre").value(tipoVehiculo.getNombre()));
    }

    /**
     * Prueba que verifica la búsqueda de un tipo de vehículo existente por su ID.
     * Asegura que se devuelve un estado 200 OK y el tipo de vehículo encontrado.
     */
    @Test
    public void buscarTipoVehiculoTest() throws Exception {
        // Arrange
        when(tipoVehiculoService.findById(id)).thenReturn(tipoVehiculo);

        // Act & Assert
        mockMvc.perform(get("/api-recursos/v1/tipos-vehiculos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(tipoVehiculo.getId()))
                .andExpect(jsonPath("$.nombre").value(tipoVehiculo.getNombre()));
    }

    /**
     * Prueba que verifica la creación de un nuevo tipo de vehículo.
     * Asegura que se devuelve un estado 201 CREATED al agregar un tipo de vehiculo exitosamente.
     */
    @Test
    public void agregarTipoVehiculoTest() throws Exception {
        // Arrange
        when(tipoVehiculoService.save(any(TipoVehiculo.class))).thenReturn(tipoVehiculo);

        // Act & Assert
        mockMvc.perform(post("/api-recursos/v1/tipos-vehiculos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tipoVehiculo)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Tipo Vehiculo creado con éxito."));
    }

    /**
     * Prueba que verifica la actualización de un tipo de vehiculo existente.
     * Asegura que se devuelve un estado 200 OK al actualizar correctamente.
     */
    @Test
    public void actualizarTipoVehiculoTest() throws Exception {
        // Arrange
        when(tipoVehiculoService.update(any(TipoVehiculo.class), eq(id))).thenReturn(tipoVehiculo);

        // Act & Assert
        mockMvc.perform(put("/api-recursos/v1/tipos-vehiculos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tipoVehiculo)))
                .andExpect(status().isOk())
                .andExpect(content().string("Actualizado con éxito"));
    }

    /**
     * Prueba que verifica la eliminación de un tipo de vehículo existente.
     * Asegura que se devuelve un estado 200 OK al eliminar correctamente.
     */
    @Test
    public void eliminarTipoVehiculoTest() throws Exception {
        // Arrange
        doNothing().when(tipoVehiculoService).delete(id);

        // Act & Assert
        mockMvc.perform(delete("/api-recursos/v1/tipos-vehiculos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("Tipo Vehiculo eliminado con éxito."));
    }

    // ERRORES

    /**
     * Prueba que verifica el comportamiento cuando no hay tipos de vehículo registrados.
     * Asegura que se devuelve un estado 204 NO CONTENT.
     */
    @Test
    public void listarTest_TiposVehiculoNoExistentes() throws Exception {
        // Arrange
        when(tipoVehiculoService.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api-recursos/v1/tipos-vehiculos"))
                .andExpect(status().isNoContent());
    }

    /**
     * Prueba que verifica el comportamiento al buscar un tipo de vehiculo que no existe.
     * Asegura que se devuelve un estado 404 NOT FOUND.
     */
    @Test
    public void buscarTipoVehiculoTest_TipoNoExistente() throws Exception {
        // Arrange
        when(tipoVehiculoService.findById(id)).thenThrow(new NoSuchElementException("Tipo Vehiculo no encontrado"));

        // Act & Assert
        mockMvc.perform(get("/api-recursos/v1/tipos-vehiculos/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Tipo Vehiculo no encontrado"));
    }

    /**
     * Prueba que verifica el manejo de errores al intentar agregar un tipo de vehículo.
     * Asegura que se devuelve un estado 400 BAD REQUEST al ocurrir un error.
     */
    @Test
    public void agregarTipoVehiculoTest_Error() throws Exception {
        // Arrange
        when(tipoVehiculoService.save(any(TipoVehiculo.class))).thenThrow(new RuntimeException("Error al crear el tipo de Vehiculo"));

        // Act & Assert
        mockMvc.perform(post("/api-recursos/v1/tipos-vehiculos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tipoVehiculo)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error al crear el tipo de Vehiculo"));
    }

    /**
     * Prueba que verifica el comportamiento al intentar actualizar un tipo de Vehiculo que no existe.
     * Asegura que se devuelve un estado 404 NOT FOUND.
     */
    @Test
    public void actualizarTipoVehiculoTest_TipoNoExistente() throws Exception {
        // Arrange
        when(tipoVehiculoService.update(any(TipoVehiculo.class), eq(id))).thenThrow(new NoSuchElementException("Tipo Vehiculo no encontrado"));

        // Act & Assert
        mockMvc.perform(put("/api-recursos/v1/tipos-vehiculos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tipoVehiculo)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Tipo Vehiculo no encontrado"));
    }

    /**
     * Prueba que verifica el comportamiento al intentar eliminar un tipo de Vehiculo que no existe.
     * Asegura que se devuelve un estado 404 NOT FOUND.
     */
    @Test
    public void eliminarTipoVehiculoTest_TipoNoExistente() throws Exception {
        // Arrange
        doThrow(new NoSuchElementException("Tipo Vehiculo no encontrado")).when(tipoVehiculoService).delete(id);

        // Act & Assert
        mockMvc.perform(delete("/api-recursos/v1/tipos-vehiculos/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Tipo Vehiculo no encontrado"));
    }
}