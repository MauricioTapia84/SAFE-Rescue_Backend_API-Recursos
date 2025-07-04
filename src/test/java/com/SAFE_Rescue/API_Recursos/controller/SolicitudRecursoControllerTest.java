package com.SAFE_Rescue.API_Recursos.controller;

import com.SAFE_Rescue.API_Recursos.modelo.Bombero;
import com.SAFE_Rescue.API_Recursos.modelo.Recurso;
import com.SAFE_Rescue.API_Recursos.modelo.SolicitudRecurso;
import com.SAFE_Rescue.API_Recursos.modelo.TipoRecurso;
import com.SAFE_Rescue.API_Recursos.service.SolicitudRecursoService;
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
import java.util.Random;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Clase de prueba para el controlador SolicitudRecursoController.
 * Proporciona pruebas unitarias para verificar el correcto funcionamiento
 * de los endpoints relacionados con las solicitudes de recursos.
 */
@WebMvcTest(SolicitudRecursoController.class)
public class SolicitudRecursoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SolicitudRecursoService solicitudRecursoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Faker faker;
    private SolicitudRecurso solicitudRecurso;
    private Integer id;

    /**
     * Configura el entorno de pruebas antes de cada prueba.
     * Inicializa Faker y crea un objeto SolicitudRecurso para las pruebas.
     */
    @BeforeEach
    public void setUp() {
        faker = new Faker();
        solicitudRecurso = new SolicitudRecurso();
        solicitudRecurso.setId(1);
        solicitudRecurso.setTitulo(faker.company().name());
        solicitudRecurso.setEstado("Pendiente");
        solicitudRecurso.setDetalle(faker.lorem().sentence());
        solicitudRecurso.setBombero(new Bombero(1,faker.name().firstName(), faker.name().lastName(), faker.name().lastName(), faker.number().numberBetween(100000000, 999999999)));
        solicitudRecurso.setRecurso(new Recurso(1,faker.commerce().productName(),faker.number().numberBetween(1, 100),"Activo",new TipoRecurso(1, faker.commerce().department())));
        id = solicitudRecurso.getId();
    }

    /**
     * Prueba que verifica la obtención de todas las solicitudes de recursos existentes.
     * Asegura que se devuelve un estado 200 OK y la lista de solicitudes.
     */
    @Test
    public void listarSolicitudesTest() throws Exception {
        // Arrange
        List<SolicitudRecurso> solicitudes = new ArrayList<>();
        solicitudes.add(solicitudRecurso);
        when(solicitudRecursoService.findAll()).thenReturn(solicitudes);

        // Act & Assert
        mockMvc.perform(get("/api-recursos/v1/solicitudes-recursos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(solicitudRecurso.getId()))
                .andExpect(jsonPath("$[0].titulo").value(solicitudRecurso.getTitulo()))
                .andExpect(jsonPath("$[0].estado").value(solicitudRecurso.getEstado()))
                .andExpect(jsonPath("$[0].detalle").value(solicitudRecurso.getDetalle()))
                .andExpect(jsonPath("$[0].bombero.nombre").value(solicitudRecurso.getBombero().getNombre()))
                .andExpect(jsonPath("$[0].recurso.nombre").value(solicitudRecurso.getRecurso().getNombre()));
    }

    /**
     * Prueba que verifica la búsqueda de una solicitud de recurso existente por su ID.
     * Asegura que se devuelve un estado 200 OK y la solicitud encontrada.
     */
    @Test
    public void buscarSolicitudTest() throws Exception {
        // Arrange
        when(solicitudRecursoService.findById(id)).thenReturn(solicitudRecurso);

        // Act & Assert
        mockMvc.perform(get("/api-recursos/v1/solicitudes-recursos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(solicitudRecurso.getId()))
                .andExpect(jsonPath("$.titulo").value(solicitudRecurso.getTitulo()))
                .andExpect(jsonPath("$.estado").value(solicitudRecurso.getEstado()))
                .andExpect(jsonPath("$.detalle").value(solicitudRecurso.getDetalle()))
                .andExpect(jsonPath("$.bombero.nombre").value(solicitudRecurso.getBombero().getNombre()))
                .andExpect(jsonPath("$.recurso.nombre").value(solicitudRecurso.getRecurso().getNombre()));
    }

    /**
     * Prueba que verifica la creación de una nueva solicitud de recurso.
     * Asegura que se devuelve un estado 201 CREATED al agregar una solicitud exitosamente.
     */
    @Test
    public void agregarSolicitudTest() throws Exception {
        // Arrange
        when(solicitudRecursoService.save(any(SolicitudRecurso.class))).thenReturn(solicitudRecurso);

        // Act & Assert
        mockMvc.perform(post("/api-recursos/v1/solicitudes-recursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(solicitudRecurso))) // Convertir SolicitudRecurso a JSON
                .andExpect(status().isCreated())
                .andExpect(content().string("Solicitud Recurso creada con éxito."));
    }

    /**
     * Prueba que verifica la actualización de una solicitud existente.
     * Asegura que se devuelve un estado 200 OK al actualizar correctamente.
     */
    @Test
    public void actualizarSolicitudTest() throws Exception {
        // Arrange
        when(solicitudRecursoService.update(any(SolicitudRecurso.class), eq(id))).thenReturn(solicitudRecurso);

        // Act & Assert
        mockMvc.perform(put("/api-recursos/v1/solicitudes-recursos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(solicitudRecurso))) // Convertir SolicitudRecurso a JSON
                .andExpect(status().isOk())
                .andExpect(content().string("Actualizado con éxito"));
    }

    /**
     * Prueba que verifica la eliminación de una solicitud existente.
     * Asegura que se devuelve un estado 200 OK al eliminar correctamente.
     */
    @Test
    public void eliminarSolicitudTest() throws Exception {
        // Arrange
        doNothing().when(solicitudRecursoService).delete(id);

        // Act & Assert
        mockMvc.perform(delete("/api-recursos/v1/solicitudes-recursos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("Solicitud Recurso eliminada con éxito."));
    }

    // ERRORES

    /**
     * Prueba que verifica el comportamiento cuando no hay solicitudes registradas.
     * Asegura que se devuelve un estado 204 NO CONTENT.
     */
    @Test
    public void listarTest_SolicitudesNoExistentes() throws Exception {
        // Arrange
        when(solicitudRecursoService.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api-recursos/v1/solicitudes-recursos"))
                .andExpect(status().isNoContent());
    }

    /**
     * Prueba que verifica el comportamiento al buscar una solicitud que no existe.
     * Asegura que se devuelve un estado 404 NOT FOUND.
     */
    @Test
    public void buscarSolicitudTest_SolicitudNoExistente() throws Exception {
        // Arrange
        when(solicitudRecursoService.findById(id)).thenThrow(new NoSuchElementException("Solicitud Recurso no encontrada"));

        // Act & Assert
        mockMvc.perform(get("/api-recursos/v1/solicitudes-recursos/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Solicitud Recurso no encontrada"));
    }

    /**
     * Prueba que verifica el manejo de errores al intentar agregar una solicitud.
     * Asegura que se devuelve un estado 400 BAD REQUEST al ocurrir un error.
     */
    @Test
    public void agregarSolicitudTest_Error() throws Exception {
        // Arrange
        when(solicitudRecursoService.save(any(SolicitudRecurso.class))).thenThrow(new RuntimeException("Error al crear la solicitud"));

        // Act & Assert
        mockMvc.perform(post("/api-recursos/v1/solicitudes-recursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(solicitudRecurso))) // Convertir SolicitudRecurso a JSON
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error al crear la solicitud"));
    }

    /**
     * Prueba que verifica el comportamiento al intentar actualizar una solicitud que no existe.
     * Asegura que se devuelve un estado 404 NOT FOUND.
     */
    @Test
    public void actualizarSolicitudTest_SolicitudNoExistente() throws Exception {
        // Arrange
        when(solicitudRecursoService.update(any(SolicitudRecurso.class), eq(id))).thenThrow(new NoSuchElementException("Solicitud Recurso no encontrada"));

        // Act & Assert
        mockMvc.perform(put("/api-recursos/v1/solicitudes-recursos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(solicitudRecurso))) // Convertir SolicitudRecurso a JSON
                .andExpect(status().isNotFound())
                .andExpect(content().string("Solicitud Recurso no encontrada"));
    }

    /**
     * Prueba que verifica el comportamiento al intentar eliminar una solicitud que no existe.
     * Asegura que se devuelve un estado 404 NOT FOUND.
     */
    @Test
    public void eliminarSolicitudTest_SolicitudNoExistente() throws Exception {
        // Arrange
        doThrow(new NoSuchElementException("Solicitud Recurso no encontrada")).when(solicitudRecursoService).delete(id);

        // Act & Assert
        mockMvc.perform(delete("/api-recursos/v1/solicitudes-recursos/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Solicitud Recurso no encontrada"));
    }

    /**
     * Prueba que verifica el comportamiento al intentar asignar un recurso a una solicitud que no existe.
     * Asegura que se devuelve un estado 404 NOT FOUND.
     */
    @Test
    public void asignarRecursoTest_SolicitudNoEncontrada() throws Exception {
        Integer solicitudRecursoId = 1;
        Integer recursoId = 1;
        doThrow(new RuntimeException("Solicitud Recurso no encontrada")).when(solicitudRecursoService).asignarRecurso(solicitudRecursoId, recursoId);

        // Act & Assert
        mockMvc.perform(post("/api-recursos/v1/solicitudes-recursos/{solicitudRecursoId}/asignar-recurso/{recursoId}", solicitudRecursoId, recursoId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Solicitud Recurso no encontrada"));
    }
}