package com.SAFE_Rescue.API_Recursos.service;

import com.SAFE_Rescue.API_Recursos.modelo.Bombero;
import com.SAFE_Rescue.API_Recursos.modelo.Recurso;
import com.SAFE_Rescue.API_Recursos.modelo.SolicitudRecurso;
import com.SAFE_Rescue.API_Recursos.modelo.TipoRecurso;
import com.SAFE_Rescue.API_Recursos.repository.BomberoRepository;
import com.SAFE_Rescue.API_Recursos.repository.RecursoRepository;
import com.SAFE_Rescue.API_Recursos.repository.SolicitudRecursoRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Clase de pruebas unitarias para el servicio SolicitudRecursoService.
 * Verifica la funcionalidad de los métodos dentro de SolicitudRecursoService,
 * incluyendo operaciones CRUD, validaciones y asignaciones.
 */
@SpringBootTest
public class SolicitudRecursoServiceTest {

    @Autowired
    private SolicitudRecursoService solicitudRecursoService;

    @MockitoBean
    private SolicitudRecursoRepository solicitudRecursoRepository;

    @MockitoBean
    private BomberoRepository bomberoRepository;

    @MockitoBean
    private RecursoRepository recursoRepository;

    private Faker faker;
    private SolicitudRecurso solicitudRecurso;
    private Integer id;

    /**
     * Configura el entorno de pruebas antes de cada prueba.
     * Inicializa Faker y crea una instancia de SolicitudRecurso para las pruebas.
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
     * Prueba que verifica la obtención de todas las solicitudes.
     * Asegura que el servicio devuelve la lista correcta de solicitudes.
     */
    @Test
    public void findAllTest() {
        // Arrange
        when(solicitudRecursoRepository.findAll()).thenReturn(Collections.singletonList(solicitudRecurso));

        // Act
        List<SolicitudRecurso> solicitudes = solicitudRecursoService.findAll();

        // Assert
        assertNotNull(solicitudes);
        assertEquals(1, solicitudes.size());
        assertEquals(solicitudRecurso.getId(), solicitudes.get(0).getId());
        assertEquals(solicitudRecurso.getId(), solicitudes.get(0).getId());
        assertEquals(solicitudRecurso.getTitulo(), solicitudes.get(0).getTitulo());
        assertEquals(solicitudRecurso.getEstado(), solicitudes.get(0).getEstado());
        assertEquals(solicitudRecurso.getDetalle(), solicitudes.get(0).getDetalle());
        assertEquals(solicitudRecurso.getBombero(), solicitudes.get(0).getBombero());
        assertEquals(solicitudRecurso.getRecurso(), solicitudes.get(0).getRecurso());
    }

    /**
     * Prueba que verifica la búsqueda de una solicitud por su ID.
     * Asegura que se encuentra la solicitud correcta.
     */
    @Test
    public void findByIdTest() {
        // Arrange
        when(solicitudRecursoRepository.findById(id)).thenReturn(Optional.of(solicitudRecurso));

        // Act
        SolicitudRecurso encontrada = solicitudRecursoService.findById(id);

        // Assert
        assertNotNull(encontrada);
        assertEquals(solicitudRecurso.getId(), encontrada.getId());
        assertEquals(solicitudRecurso.getTitulo(), encontrada.getTitulo());
        assertEquals(solicitudRecurso.getEstado(), encontrada.getEstado());
        assertEquals(solicitudRecurso.getDetalle(), encontrada.getDetalle());
        assertEquals(solicitudRecurso.getBombero(), encontrada.getBombero());
        assertEquals(solicitudRecurso.getRecurso(), encontrada.getRecurso());
    }

    /**
     * Prueba que verifica la creación de una nueva solicitud.
     * Asegura que la solicitud se guarda correctamente en el repositorio.
     */
    @Test
    public void saveTest() {
        // Arrange
        when(solicitudRecursoRepository.save(solicitudRecurso)).thenReturn(solicitudRecurso);

        // Act
        SolicitudRecurso guardada = solicitudRecursoService.save(solicitudRecurso);

        // Assert
        assertNotNull(guardada);
        assertEquals(solicitudRecurso.getTitulo(), guardada.getTitulo());
        assertEquals(solicitudRecurso.getEstado(), guardada.getEstado());
        assertEquals(solicitudRecurso.getDetalle(), guardada.getDetalle());
        assertEquals(solicitudRecurso.getBombero(), guardada.getBombero());
        assertEquals(solicitudRecurso.getRecurso(), guardada.getRecurso());
    }

    /**
     * Prueba que verifica la actualización de una solicitud existente.
     * Asegura que la solicitud se actualiza correctamente.
     */
    @Test
    public void updateTest() {
        // Arrange
        when(solicitudRecursoRepository.findById(id)).thenReturn(Optional.of(solicitudRecurso));
        when(solicitudRecursoRepository.save(solicitudRecurso)).thenReturn(solicitudRecurso);

        // Act
        SolicitudRecurso actualizada = solicitudRecursoService.update(solicitudRecurso, id);

        // Assert
        assertNotNull(actualizada);
        assertEquals(solicitudRecurso.getTitulo(), actualizada.getTitulo());
        assertEquals(solicitudRecurso.getEstado(), actualizada.getEstado());
        assertEquals(solicitudRecurso.getDetalle(), actualizada.getDetalle());
        assertEquals(solicitudRecurso.getBombero(), actualizada.getBombero());
        assertEquals(solicitudRecurso.getRecurso(), actualizada.getRecurso());
    }

    /**
     * Prueba que verifica la eliminación de una solicitud.
     * Asegura que la solicitud se elimina correctamente del repositorio.
     */
    @Test
    public void deleteTest() {
        // Arrange
        when(solicitudRecursoRepository.existsById(id)).thenReturn(true);
        doNothing().when(solicitudRecursoRepository).deleteById(id);

        // Act
        solicitudRecursoService.delete(id);

        // Assert
        verify(solicitudRecursoRepository, times(1)).deleteById(id);
    }

    /**
     * Prueba que verifica la búsqueda por ID cuando la solicitud no existe.
     * Asegura que se lanza la excepción correspondiente.
     */
    @Test
    public void findByIdTest_SolicitudNoExiste() {
        // Arrange
        when(solicitudRecursoRepository.findById(id)).thenReturn(Optional.empty());

        // Assert
        assertThrows(NoSuchElementException.class, () -> solicitudRecursoService.findById(id));
    }

    /**
     * Prueba que verifica el intento de actualización de una solicitud que no existe.
     * Asegura que se lanza la excepción correspondiente.
     */
    @Test
    public void updateTest_SolicitudNoExistente() {
        // Arrange
        when(solicitudRecursoRepository.findById(id)).thenReturn(Optional.empty());

        // Assert
        assertThrows(NoSuchElementException.class, () -> solicitudRecursoService.update(solicitudRecurso, id));
    }

    /**
     * Prueba que verifica la eliminación de una solicitud que no existe.
     * Asegura que se lanza la excepción correspondiente.
     */
    @Test
    public void deleteTest_SolicitudNoExistente() {
        // Arrange
        when(solicitudRecursoRepository.existsById(id)).thenReturn(false);

        // Assert
        assertThrows(NoSuchElementException.class, () -> solicitudRecursoService.delete(id));
    }

    /**
     * Prueba que verifica la validación de una solicitud.
     * Asegura que una solicitud válida no lanza excepciones.
     */
    @Test
    public void validarSolicitudTest() {
        // Act & Assert
        assertDoesNotThrow(() -> solicitudRecursoService.validarSolicitud(solicitudRecurso));
    }

    /**
     * Prueba que verifica la validación de una solicitud con título vacío.
     * Asegura que se lanza IllegalArgumentException.
     */
    @Test
    public void validarSolicitud_TituloVacio() {
        // Arrange
        solicitudRecurso.setTitulo("");

        // Assert
        assertThrows(IllegalArgumentException.class, () -> solicitudRecursoService.validarSolicitud(solicitudRecurso));
    }

    /**
     * Prueba que verifica la validación de una solicitud sin detalle.
     * Asegura que se lanza IllegalArgumentException.
     */
    @Test
    public void validarSolicitud_SinDetalle() {
        // Arrange
        solicitudRecurso.setDetalle(null);

        // Assert
        assertThrows(IllegalArgumentException.class, () -> solicitudRecursoService.validarSolicitud(solicitudRecurso));
    }

    /**
     * Prueba que verifica la validación de una solicitud sin estado.
     * Asegura que se lanza IllegalArgumentException.
     */
    @Test
    public void validarSolicitud_SinEstado() {
        // Arrange
        solicitudRecurso.setEstado(null);

        // Assert
        assertThrows(IllegalArgumentException.class, () -> solicitudRecursoService.validarSolicitud(solicitudRecurso));
    }
}