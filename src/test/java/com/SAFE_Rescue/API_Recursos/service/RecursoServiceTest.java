package com.SAFE_Rescue.API_Recursos.service;

import com.SAFE_Rescue.API_Recursos.modelo.Recurso;
import com.SAFE_Rescue.API_Recursos.modelo.TipoRecurso;
import com.SAFE_Rescue.API_Recursos.repository.RecursoRepository;
import com.SAFE_Rescue.API_Recursos.repository.TipoRecursoRepository;
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
 * Clase de pruebas unitarias para el servicio RecursoService.
 * Verifica la funcionalidad de los métodos dentro de RecursoService,
 * incluyendo operaciones CRUD, validaciones y asignaciones.
 */
@SpringBootTest
public class RecursoServiceTest {

    @Autowired
    private RecursoService recursoService;

    @MockitoBean
    private RecursoRepository recursoRepository;

    @MockitoBean
    private TipoRecursoRepository tipoRecursoRepository;

    private Faker faker;
    private Recurso recurso;
    private Integer id;

    /**
     * Configura el entorno de pruebas antes de cada prueba.
     * Inicializa Faker y crea una instancia de Recurso para las pruebas.
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
     * Prueba que verifica la obtención de todos los recursos.
     * Asegura que el servicio devuelve la lista correcta de recursos.
     */
    @Test
    public void findAllTest() {
        // Arrange
        when(recursoRepository.findAll()).thenReturn(Collections.singletonList(recurso));

        // Act
        List<Recurso> recursos = recursoService.findAll();

        // Assert
        assertNotNull(recursos);
        assertEquals(1, recursos.size());
        assertEquals(recurso.getId(), recursos.get(0).getId());
        assertEquals(recurso.getNombre(), recursos.get(0).getNombre());
        assertEquals(recurso.getCantidad(), recursos.get(0).getCantidad());
        assertEquals(recurso.getEstado(), recursos.get(0).getEstado());
        assertEquals(recurso.getTipoRecurso(), recursos.get(0).getTipoRecurso());
    }

    /**
     * Prueba que verifica la búsqueda de un recurso por su ID.
     * Asegura que se encuentra el recurso correcto.
     */
    @Test
    public void findByIdTest() {
        // Arrange
        when(recursoRepository.findById(id)).thenReturn(Optional.of(recurso));

        // Act
        Recurso encontrado = recursoService.findById(id);

        // Assert
        assertNotNull(encontrado);
        assertEquals(recurso.getId(), encontrado.getId());
        assertEquals(recurso.getNombre(), encontrado.getNombre());
        assertEquals(recurso.getCantidad(), encontrado.getCantidad());
        assertEquals(recurso.getEstado(), encontrado.getEstado());
        assertEquals(recurso.getTipoRecurso(), encontrado.getTipoRecurso());
    }

    /**
     * Prueba que verifica la creación de un nuevo recurso.
     * Asegura que el recurso se guarda correctamente en el repositorio.
     */
    @Test
    public void saveTest() {
        // Arrange
        when(tipoRecursoRepository.save(any(TipoRecurso.class))).thenReturn(recurso.getTipoRecurso());
        when(recursoRepository.save(recurso)).thenReturn(recurso);

        // Act
        Recurso guardado = recursoService.save(recurso);

        // Assert
        assertNotNull(guardado);
        assertEquals(recurso.getNombre(), guardado.getNombre());
        assertEquals(recurso.getCantidad(), guardado.getCantidad());
        assertEquals(recurso.getEstado(), guardado.getEstado());
        assertEquals(recurso.getTipoRecurso(), guardado.getTipoRecurso());
    }

    /**
     * Prueba que verifica la actualización de un recurso existente.
     * Asegura que el recurso se actualiza correctamente.
     */
    @Test
    public void updateTest() {
        // Arrange
        when(recursoRepository.findById(id)).thenReturn(Optional.of(recurso));
        when(recursoRepository.save(recurso)).thenReturn(recurso);

        // Act
        Recurso actualizado = recursoService.update(recurso, id);

        // Assert
        assertNotNull(actualizado);
        assertEquals(recurso.getNombre(), actualizado.getNombre());
        assertEquals(recurso.getCantidad(), actualizado.getCantidad());
        assertEquals(recurso.getEstado(), actualizado.getEstado());
        assertEquals(recurso.getTipoRecurso(), actualizado.getTipoRecurso());
    }

    /**
     * Prueba que verifica la eliminación de un recurso.
     * Asegura que el recurso se elimina correctamente del repositorio.
     */
    @Test
    public void deleteTest() {
        // Arrange
        when(recursoRepository.existsById(id)).thenReturn(true);
        doNothing().when(recursoRepository).deleteById(id);

        // Act
        recursoService.delete(id);

        // Assert
        verify(recursoRepository, times(1)).deleteById(id);
    }

    /**
     * Prueba que verifica la búsqueda por ID cuando el recurso no existe.
     * Asegura que se lanza la excepción correspondiente.
     */
    @Test
    public void findByIdTest_RecursoNoExiste() {
        // Arrange
        when(recursoRepository.findById(id)).thenReturn(Optional.empty());

        // Assert
        assertThrows(NoSuchElementException.class, () -> recursoService.findById(id));
    }

    /**
     * Prueba que verifica el intento de actualización de un recurso que no existe.
     * Asegura que se lanza la excepción correspondiente.
     */
    @Test
    public void updateTest_RecursoNoExistente() {
        // Arrange
        when(recursoRepository.findById(id)).thenReturn(Optional.empty());

        // Assert
        assertThrows(NoSuchElementException.class, () -> recursoService.update(recurso, id));
    }

    /**
     * Prueba que verifica la eliminación de un recurso que no existe.
     * Asegura que se lanza la excepción correspondiente.
     */
    @Test
    public void deleteTest_RecursoNoExistente() {
        // Arrange
        when(recursoRepository.existsById(id)).thenReturn(false);

        // Assert
        assertThrows(NoSuchElementException.class, () -> recursoService.delete(id));
    }

    /**
     * Prueba que verifica la validación de un recurso.
     * Asegura que un recurso válido no lanza excepciones.
     */
    @Test
    public void validarRecursoTest() {
        // Act & Assert
        assertDoesNotThrow(() -> recursoService.validarRecurso(recurso));
    }

    /**
     * Prueba que verifica la validación de un recurso con nombre vacío.
     * Asegura que se lanza IllegalArgumentException.
     */
    @Test
    public void validarRecurso_NombreVacio() {
        // Arrange
        recurso.setNombre(null);

        // Assert
        assertThrows(IllegalArgumentException.class, () -> recursoService.validarRecurso(recurso));
    }

    /**
     * Prueba que verifica la validación de un recurso con cantidad negativa.
     * Asegura que se lanza IllegalArgumentException.
     */
    @Test
    public void validarRecurso_CantidadNegativa() {
        // Arrange
        recurso.setCantidad(-1);

        // Assert
        assertThrows(IllegalArgumentException.class, () -> recursoService.validarRecurso(recurso));
    }

    /**
     * Prueba que verifica la validación de un recurso sin tipo de recurso.
     * Asegura que se lanza IllegalArgumentException.
     */
    @Test
    public void validarRecurso_SinTipoRecurso() {
        // Arrange
        recurso.setTipoRecurso(null);

        // Assert
        assertThrows(IllegalArgumentException.class, () -> recursoService.validarRecurso(recurso));
    }
}