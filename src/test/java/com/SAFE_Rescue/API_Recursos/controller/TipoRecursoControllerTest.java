package com.SAFE_Rescue.API_Recursos.controller;

import com.SAFE_Rescue.API_Recursos.modelo.TipoRecurso;
import com.SAFE_Rescue.API_Recursos.service.TipoRecursoService;
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
 * Clase de prueba para el controlador TipoRecursoController.
 * Proporciona pruebas unitarias para verificar el correcto funcionamiento
 * de los endpoints relacionados con los tipos de equipo.
 */
@WebMvcTest(TipoRecursoController.class)
public class TipoRecursoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TipoRecursoService tipoRecursoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Faker faker;
    private TipoRecurso tipoRecurso;
    private Integer id;

    /**
     * Configura el entorno de pruebas antes de cada prueba.
     * Inicializa Faker y crea un objeto TipoRecurso para las pruebas.
     */
    @BeforeEach
    public void setUp() {
        faker = new Faker();
        id = 1;
        tipoRecurso = new TipoRecurso(1, faker.job().position());
    }

    /**
     * Prueba que verifica la obtención de todos los tipos de recursos existentes.
     * Asegura que se devuelve un estado 200 OK y la lista de tipos de recursos.
     */
    @Test
    public void listarTiposRecursoTest() throws Exception {
        // Arrange
        when(tipoRecursoService.findAll()).thenReturn(List.of(tipoRecurso));

        // Act & Assert
        mockMvc.perform(get("/api-recursos/v1/tipos-recursos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(tipoRecurso.getId()))
                .andExpect(jsonPath("$[0].nombre").value(tipoRecurso.getNombre()));
    }

    /**
     * Prueba que verifica la búsqueda de un tipo de recurso existente por su ID.
     * Asegura que se devuelve un estado 200 OK y el tipo de recurso encontrado.
     */
    @Test
    public void buscarTipoRecursoTest() throws Exception {
        // Arrange
        when(tipoRecursoService.findById(id)).thenReturn(tipoRecurso);

        // Act & Assert
        mockMvc.perform(get("/api-recursos/v1/tipos-recursos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(tipoRecurso.getId()))
                .andExpect(jsonPath("$.nombre").value(tipoRecurso.getNombre()));
    }

    /**
     * Prueba que verifica la creación de un nuevo tipo de recurso.
     * Asegura que se devuelve un estado 201 CREATED al agregar un tipo de recurso exitosamente.
     */
    @Test
    public void agregarTipoRecursoTest() throws Exception {
        // Arrange
        when(tipoRecursoService.save(any(TipoRecurso.class))).thenReturn(tipoRecurso);

        // Act & Assert
        mockMvc.perform(post("/api-recursos/v1/tipos-recursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tipoRecurso)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Tipo Recurso creado con éxito."));
    }

    /**
     * Prueba que verifica la actualización de un tipo de recurso existente.
     * Asegura que se devuelve un estado 200 OK al actualizar correctamente.
     */
    @Test
    public void actualizarTipoRecursoTest() throws Exception {
        // Arrange
        when(tipoRecursoService.update(any(TipoRecurso.class), eq(id))).thenReturn(tipoRecurso);

        // Act & Assert
        mockMvc.perform(put("/api-recursos/v1/tipos-recursos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tipoRecurso)))
                .andExpect(status().isOk())
                .andExpect(content().string("Actualizado con éxito"));
    }

    /**
     * Prueba que verifica la eliminación de un tipo de recurso existente.
     * Asegura que se devuelve un estado 200 OK al eliminar correctamente.
     */
    @Test
    public void eliminarTipoRecursoTest() throws Exception {
        // Arrange
        doNothing().when(tipoRecursoService).delete(id);

        // Act & Assert
        mockMvc.perform(delete("/api-recursos/v1/tipos-recursos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("Tipo Recurso eliminado con éxito."));
    }

    // ERRORES

    /**
     * Prueba que verifica el comportamiento cuando no hay tipos de recurso registrados.
     * Asegura que se devuelve un estado 204 NO CONTENT.
     */
    @Test
    public void listarTest_TiposRecursoNoExistentes() throws Exception {
        // Arrange
        when(tipoRecursoService.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api-recursos/v1/tipos-recursos"))
                .andExpect(status().isNoContent());
    }

    /**
     * Prueba que verifica el comportamiento al buscar un tipo de recurso que no existe.
     * Asegura que se devuelve un estado 404 NOT FOUND.
     */
    @Test
    public void buscarTipoRecursoTest_TipoNoExistente() throws Exception {
        // Arrange
        when(tipoRecursoService.findById(id)).thenThrow(new NoSuchElementException("Tipo Recurso no encontrado"));

        // Act & Assert
        mockMvc.perform(get("/api-recursos/v1/tipos-recursos/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Tipo Recurso no encontrado"));
    }

    /**
     * Prueba que verifica el manejo de errores al intentar agregar un tipo de recurso.
     * Asegura que se devuelve un estado 400 BAD REQUEST al ocurrir un error.
     */
    @Test
    public void agregarTipoRecursoTest_Error() throws Exception {
        // Arrange
        when(tipoRecursoService.save(any(TipoRecurso.class))).thenThrow(new RuntimeException("Error al crear el tipo de recurso"));

        // Act & Assert
        mockMvc.perform(post("/api-recursos/v1/tipos-recursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tipoRecurso)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error al crear el tipo de recurso"));
    }

    /**
     * Prueba que verifica el comportamiento al intentar actualizar un tipo de recurso que no existe.
     * Asegura que se devuelve un estado 404 NOT FOUND.
     */
    @Test
    public void actualizarTipoRecursoTest_TipoNoExistente() throws Exception {
        // Arrange
        when(tipoRecursoService.update(any(TipoRecurso.class), eq(id))).thenThrow(new NoSuchElementException("Tipo Recurso no encontrado"));

        // Act & Assert
        mockMvc.perform(put("/api-recursos/v1/tipos-recursos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tipoRecurso)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Tipo Recurso no encontrado"));
    }

    /**
     * Prueba que verifica el comportamiento al intentar eliminar un tipo de recurso que no existe.
     * Asegura que se devuelve un estado 404 NOT FOUND.
     */
    @Test
    public void eliminarTipoRecursoTest_TipoNoExistente() throws Exception {
        // Arrange
        doThrow(new NoSuchElementException("Tipo Recurso no encontrado")).when(tipoRecursoService).delete(id);

        // Act & Assert
        mockMvc.perform(delete("/api-recursos/v1/tipos-recursos/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Tipo Recurso no encontrado"));
    }
}