package com.SAFE_Rescue.API_Recursos.controller;

import com.SAFE_Rescue.API_Recursos.modelo.Recurso;
import com.SAFE_Rescue.API_Recursos.modelo.TipoRecurso;
import com.SAFE_Rescue.API_Recursos.service.RecursoService;
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
 * Clase de prueba para el controlador RecursoController.
 * Proporciona pruebas unitarias para verificar el correcto funcionamiento
 * de los endpoints relacionados con los recursos.
 */
@WebMvcTest(RecursoController.class)
public class RecursoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RecursoService recursoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Faker faker;
    private Recurso recurso;
    private Integer id;

    /**
     * Configura el entorno de pruebas antes de cada prueba.
     * Inicializa Faker y crea un objeto Recurso para las pruebas.
     */
    @BeforeEach
    public void setUp() {
        faker = new Faker();
        recurso = new Recurso();
        recurso.setId(1);
        recurso.setNombre(faker.commerce().productName());
        recurso.setCantidad(faker.number().numberBetween(1, 100));
        recurso.setEstado("Activo");
        recurso.setTipoRecurso(new TipoRecurso(1, faker.commerce().department()));
        id = recurso.getId();
    }

    /**
     * Prueba que verifica la obtención de todos los recursos existentes.
     * Asegura que se devuelve un estado 200 OK y la lista de recursos.
     */
    @Test
    public void listarRecursosTest() throws Exception {
        // Arrange
        when(recursoService.findAll()).thenReturn(List.of(recurso));

        // Act & Assert
        mockMvc.perform(get("/api-recursos/v1/recursos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(recurso.getId()))
                .andExpect(jsonPath("$[0].nombre").value(recurso.getNombre()))
                .andExpect(jsonPath("$[0].cantidad").value(recurso.getCantidad()))
                .andExpect(jsonPath("$[0].estado").value(recurso.getEstado()))
                .andExpect(jsonPath("$[0].tipoRecurso.id").value(recurso.getTipoRecurso().getId()))
                .andExpect(jsonPath("$[0].tipoRecurso.nombre").value(recurso.getTipoRecurso().getNombre()));
    }

    /**
     * Prueba que verifica la búsqueda de un recurso existente por su ID.
     * Asegura que se devuelve un estado 200 OK y el recurso encontrado.
     */
    @Test
    public void buscarRecursoTest() throws Exception {
        // Arrange
        when(recursoService.findById(id)).thenReturn(recurso);

        // Act & Assert
        mockMvc.perform(get("/api-recursos/v1/recursos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(recurso.getId()))
                .andExpect(jsonPath("$.nombre").value(recurso.getNombre()))
                .andExpect(jsonPath("$.cantidad").value(recurso.getCantidad()))
                .andExpect(jsonPath("$.estado").value(recurso.getEstado()))
                .andExpect(jsonPath("$.tipoRecurso.id").value(recurso.getTipoRecurso().getId()))
                .andExpect(jsonPath("$.tipoRecurso.nombre").value(recurso.getTipoRecurso().getNombre()));
    }

    /**
     * Prueba que verifica la creación de un nuevo recurso.
     * Asegura que se devuelve un estado 201 CREATED al agregar un recurso exitosamente.
     */
    @Test
    public void agregarRecursoTest() throws Exception {
        // Arrange
        when(recursoService.save(any(Recurso.class))).thenReturn(recurso);

        // Act & Assert
        mockMvc.perform(post("/api-recursos/v1/recursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recurso)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Recurso creado con éxito."));
    }

    /**
     * Prueba que verifica la actualización de un recurso existente.
     * Asegura que se devuelve un estado 200 OK al actualizar correctamente.
     */
    @Test
    public void actualizarRecursoTest() throws Exception {
        // Arrange
        when(recursoService.update(any(Recurso.class), eq(id))).thenReturn(recurso);

        // Act & Assert
        mockMvc.perform(put("/api-recursos/v1/recursos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recurso)))
                .andExpect(status().isOk())
                .andExpect(content().string("Actualizado con éxito"));
    }

    /**
     * Prueba que verifica la eliminación de un recurso existente.
     * Asegura que se devuelve un estado 200 OK al eliminar correctamente.
     */
    @Test
    public void eliminarRecursoTest() throws Exception {
        // Arrange
        doNothing().when(recursoService).delete(id);

        // Act & Assert
        mockMvc.perform(delete("/api-recursos/v1/recursos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("Recurso eliminado con éxito."));
    }

    // ERRORES

    /**
     * Prueba que verifica el comportamiento cuando no hay recursos registrados.
     * Asegura que se devuelve un estado 204 NO CONTENT.
     */
    @Test
    public void listarTest_RecursosNoExistentes() throws Exception {
        // Arrange
        when(recursoService.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api-recursos/v1/recursos"))
                .andExpect(status().isNoContent());
    }

    /**
     * Prueba que verifica el comportamiento al buscar un recurso que no existe.
     * Asegura que se devuelve un estado 404 NOT FOUND.
     */
    @Test
    public void buscarRecursoTest_RecursoNoExistente() throws Exception {
        // Arrange
        when(recursoService.findById(id)).thenThrow(new NoSuchElementException("Recurso no encontrado"));

        // Act & Assert
        mockMvc.perform(get("/api-recursos/v1/recursos/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Recurso no encontrado"));
    }

    /**
     * Prueba que verifica el manejo de errores al intentar agregar un recurso.
     * Asegura que se devuelve un estado 400 BAD REQUEST al ocurrir un error.
     */
    @Test
    public void agregarRecursoTest_Error() throws Exception {
        // Arrange
        when(recursoService.save(any(Recurso.class))).thenThrow(new RuntimeException("Error al crear el recurso"));

        // Act & Assert
        mockMvc.perform(post("/api-recursos/v1/recursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recurso))) // Convertir Recurso a JSON
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error al crear el recurso"));
    }

    /**
     * Prueba que verifica el comportamiento al intentar actualizar un recurso que no existe.
     * Asegura que se devuelve un estado 404 NOT FOUND.
     */
    @Test
    public void actualizarRecursoTest_RecursoNoExistente() throws Exception {
        // Arrange
        when(recursoService.update(any(Recurso.class), eq(id))).thenThrow(new NoSuchElementException("Recurso no encontrado"));

        // Act & Assert
        mockMvc.perform(put("/api-recursos/v1/recursos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recurso))) // Convertir Recurso a JSON
                .andExpect(status().isNotFound())
                .andExpect(content().string("Recurso no encontrado"));
    }

    /**
     * Prueba que verifica el comportamiento al intentar eliminar un recurso que no existe.
     * Asegura que se devuelve un estado 404 NOT FOUND.
     */
    @Test
    public void eliminarRecursoTest_RecursoNoExistente() throws Exception {
        // Arrange
        doThrow(new NoSuchElementException("Recurso no encontrado")).when(recursoService).delete(id);

        // Act & Assert
        mockMvc.perform(delete("/api-recursos/v1/recursos/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Recurso no encontrado"));
    }

    /**
     * Prueba que verifica el comportamiento al intentar asignar un tipo de recurso a un recurso que no existe.
     * Asegura que se devuelve un estado 404 NOT FOUND.
     */
    @Test
    public void asignarTipoRecursoTest_RecursoNoEncontrado() throws Exception {
        // Arrange
        Integer recursoId = 1;
        Integer tipoRecursoId = 1;
        doThrow(new RuntimeException("Recurso no encontrado")).when(recursoService).asignarTipoRecurso(recursoId, tipoRecursoId);

        // Act & Assert
        mockMvc.perform(post("/api-recursos/v1/recursos/{recursoId}/asignar-tipo-recurso/{tipoRecursoId}", recursoId, tipoRecursoId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Recurso no encontrado"));
    }
}