package com.SAFE_Rescue.API_Recursos.service;

import com.SAFE_Rescue.API_Recursos.modelo.TipoRecurso;
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
 * Clase de pruebas unitarias para el servicio TipoRecursoService.
 * Verifica la funcionalidad de los métodos dentro de TipoRecursoService,
 * incluyendo operaciones CRUD y validaciones.
 */
@SpringBootTest
public class TipoRecursoServiceTest {

    @Autowired
    private TipoRecursoService tipoRecursoService;

    @MockitoBean
    private TipoRecursoRepository tipoRecursoRepository;

    private Faker faker;
    private TipoRecurso tipoRecurso;
    private Integer id;

    /**
     * Configura el entorno de pruebas antes de cada prueba.
     * Inicializa Faker y crea una instancia de TipoRecurso para las pruebas.
     */
    @BeforeEach
    public void setUp() {
        faker = new Faker();
        tipoRecurso = new TipoRecurso();
        tipoRecurso.setId(1);
        tipoRecurso.setNombre(faker.commerce().department());
        id = tipoRecurso.getId();
    }

    /**
     * Prueba que verifica la obtención de todos los tipos de recursos.
     * Asegura que el servicio devuelve la lista correcta de tipos de recursos.
     */
    @Test
    public void findAllTest() {
        // Arrange
        when(tipoRecursoRepository.findAll()).thenReturn(Collections.singletonList(tipoRecurso));

        // Act
        List<TipoRecurso> tiposRecursos = tipoRecursoService.findAll();

        // Assert
        assertNotNull(tiposRecursos);
        assertEquals(1, tiposRecursos.size());
        assertEquals(tipoRecurso.getId(), tiposRecursos.get(0).getId());
        assertEquals(tipoRecurso.getNombre(), tiposRecursos.get(0).getNombre());
    }

    /**
     * Prueba que verifica la búsqueda de un tipo de recurso por su ID.
     * Asegura que se encuentra el tipo de recurso correcto.
     */
    @Test
    public void findByIdTest() {
        // Arrange
        when(tipoRecursoRepository.findById(id)).thenReturn(Optional.of(tipoRecurso));

        // Act
        TipoRecurso encontrado = tipoRecursoService.findById(id);

        // Assert
        assertNotNull(encontrado);
        assertEquals(tipoRecurso.getId(), encontrado.getId());
        assertEquals(tipoRecurso.getNombre(),encontrado.getNombre());
    }

    /**
     * Prueba que verifica la creación de un nuevo tipo de recurso.
     * Asegura que el tipo de recurso se guarda correctamente en el repositorio.
     */
    @Test
    public void saveTest() {
        // Arrange
        when(tipoRecursoRepository.save(tipoRecurso)).thenReturn(tipoRecurso);

        // Act
        TipoRecurso guardado = tipoRecursoService.save(tipoRecurso);

        // Assert
        assertNotNull(guardado);
        assertEquals(tipoRecurso.getNombre(), guardado.getNombre());
    }

    /**
     * Prueba que verifica la actualización de un tipo de recurso existente.
     * Asegura que el tipo de recurso se actualiza correctamente.
     */
    @Test
    public void updateTest() {
        // Arrange
        when(tipoRecursoRepository.findById(id)).thenReturn(Optional.of(tipoRecurso));
        when(tipoRecursoRepository.save(tipoRecurso)).thenReturn(tipoRecurso);

        // Act
        TipoRecurso actualizado = tipoRecursoService.update(tipoRecurso, id);

        // Assert
        assertNotNull(actualizado);
        assertEquals(tipoRecurso.getNombre(), actualizado.getNombre());
    }

    /**
     * Prueba que verifica la eliminación de un tipo de recurso.
     * Asegura que el tipo de recurso se elimina correctamente del repositorio.
     */
    @Test
    public void deleteTest() {
        // Arrange
        when(tipoRecursoRepository.existsById(id)).thenReturn(true);
        doNothing().when(tipoRecursoRepository).deleteById(id);

        // Act
        tipoRecursoService.delete(id);

        // Assert
        verify(tipoRecursoRepository, times(1)).deleteById(id);
    }

    /**
     * Prueba que verifica la búsqueda por ID cuando el tipo de recurso no existe.
     * Asegura que se lanza la excepción correspondiente.
     */
    @Test
    public void findByIdTest_TipoRecursoNoExiste() {
        // Arrange
        when(tipoRecursoRepository.findById(id)).thenReturn(Optional.empty());

        // Assert
        assertThrows(NoSuchElementException.class, () -> tipoRecursoService.findById(id));
    }

    /**
     * Prueba que verifica el intento de actualización de un tipo de recurso que no existe.
     * Asegura que se lanza la excepción correspondiente.
     */
    @Test
    public void updateTest_TipoRecursoNoExistente() {
        // Arrange
        when(tipoRecursoRepository.findById(id)).thenReturn(Optional.empty());

        // Assert
        assertThrows(NoSuchElementException.class, () -> tipoRecursoService.update(tipoRecurso, id));
    }

    /**
     * Prueba que verifica la eliminación de un tipo de recurso que no existe.
     * Asegura que se lanza la excepción correspondiente.
     */
    @Test
    public void deleteTest_TipoRecursoNoExistente() {
        // Arrange
        when(tipoRecursoRepository.existsById(id)).thenReturn(false);

        // Assert
        assertThrows(NoSuchElementException.class, () -> tipoRecursoService.delete(id));
    }

    /**
     * Prueba que verifica la validación de un tipo de recurso.
     * Asegura que un tipo de recurso válido no lanza excepciones.
     */
    @Test
    public void validarTipoRecursoTest() {
        // Act & Assert
        assertDoesNotThrow(() -> tipoRecursoService.validarTipoRecurso(tipoRecurso));
    }

    /**
     * Prueba que verifica la validación de un tipo de recurso con nombre vacío.
     * Asegura que se lanza IllegalArgumentException.
     */
    @Test
    public void validarTipoRecurso_NombreVacio() {
        // Arrange
        tipoRecurso.setNombre("");

        // Assert
        assertThrows(IllegalArgumentException.class, () -> tipoRecursoService.validarTipoRecurso(tipoRecurso));
    }

    /**
     * Prueba que verifica la validación de un tipo de recurso con nombre demasiado largo.
     * Asegura que se lanza IllegalArgumentException.
     */
    @Test
    public void validarTipoRecurso_NombreDemasiadoLargo() {
        // Arrange
        tipoRecurso.setNombre("A".repeat(51)); // 51 caracteres

        // Assert
        assertThrows(IllegalArgumentException.class, () -> tipoRecursoService.validarTipoRecurso(tipoRecurso));
    }
}