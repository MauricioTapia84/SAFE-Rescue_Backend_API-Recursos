package com.SAFE_Rescue.API_Recursos.service;

import com.SAFE_Rescue.API_Recursos.modelo.TipoVehiculo;
import com.SAFE_Rescue.API_Recursos.repository.TipoVehiculoRepository;
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
 * Clase de pruebas unitarias para el servicio TipoVehiculoService.
 * Verifica la funcionalidad de los métodos dentro de TipoVehiculoService,
 * incluyendo operaciones CRUD y validaciones.
 */
@SpringBootTest
public class TipoVehiculoServiceTest {

    @Autowired
    private TipoVehiculoService tipoVehiculoService;

    @MockitoBean
    private TipoVehiculoRepository tipoVehiculoRepository;

    private Faker faker;
    private TipoVehiculo tipoVehiculo;
    private Integer id;

    /**
     * Configura el entorno de pruebas antes de cada prueba.
     * Inicializa Faker y crea una instancia de TipoVehiculo para las pruebas.
     */
    @BeforeEach
    public void setUp() {
        faker = new Faker();
        tipoVehiculo = new TipoVehiculo();
        tipoVehiculo.setId(1);
        tipoVehiculo.setNombre(faker.commerce().department());
        id = tipoVehiculo.getId();
    }

    /**
     * Prueba que verifica la obtención de todos los tipos de Vehiculo.
     * Asegura que el servicio devuelve la lista correcta de tipos de Vehiculo.
     */
    @Test
    public void findAllTest() {
        // Arrange
        when(tipoVehiculoRepository.findAll()).thenReturn(Collections.singletonList(tipoVehiculo));

        // Act
        List<TipoVehiculo> tiposVehiculos = tipoVehiculoService.findAll();

        // Assert
        assertNotNull(tiposVehiculos);
        assertEquals(1, tiposVehiculos.size());
        assertEquals(tipoVehiculo.getId(), tiposVehiculos.get(0).getId());
        assertEquals(tipoVehiculo.getNombre(), tiposVehiculos.get(0).getNombre());
    }

    /**
     * Prueba que verifica la búsqueda de un tipo de Vehiculo por su ID.
     * Asegura que se encuentra el tipo de Vehiculo correcto.
     */
    @Test
    public void findByIdTest() {
        // Arrange
        when(tipoVehiculoRepository.findById(id)).thenReturn(Optional.of(tipoVehiculo));

        // Act
        TipoVehiculo encontrado = tipoVehiculoService.findById(id);

        // Assert
        assertNotNull(encontrado);
        assertEquals(tipoVehiculo.getId(), encontrado.getId());
        assertEquals(tipoVehiculo.getNombre(), encontrado.getNombre());
    }

    /**
     * Prueba que verifica la creación de un nuevo tipo de Vehiculo.
     * Asegura que el tipo de Vehiculo se guarda correctamente en el repositorio.
     */
    @Test
    public void saveTest() {
        // Arrange
        when(tipoVehiculoRepository.save(tipoVehiculo)).thenReturn(tipoVehiculo);

        // Act
        TipoVehiculo guardado = tipoVehiculoService.save(tipoVehiculo);

        // Assert
        assertNotNull(guardado);
        assertEquals(tipoVehiculo.getNombre(), guardado.getNombre());
    }

    /**
     * Prueba que verifica la actualización de un tipo de Vehiculo existente.
     * Asegura que el tipo de Vehiculo se actualiza correctamente.
     */
    @Test
    public void updateTest() {
        // Arrange
        when(tipoVehiculoRepository.findById(id)).thenReturn(Optional.of(tipoVehiculo));
        when(tipoVehiculoRepository.save(tipoVehiculo)).thenReturn(tipoVehiculo);

        // Act
        TipoVehiculo actualizado = tipoVehiculoService.update(tipoVehiculo, id);

        // Assert
        assertNotNull(actualizado);
        assertEquals(tipoVehiculo.getNombre(), actualizado.getNombre());
    }

    /**
     * Prueba que verifica la eliminación de un tipo de Vehiculo.
     * Asegura que el tipo de Vehiculo se elimina correctamente del repositorio.
     */
    @Test
    public void deleteTest() {
        // Arrange
        when(tipoVehiculoRepository.existsById(id)).thenReturn(true);
        doNothing().when(tipoVehiculoRepository).deleteById(id);

        // Act
        tipoVehiculoService.delete(id);

        // Assert
        verify(tipoVehiculoRepository, times(1)).deleteById(id);
    }

    /**
     * Prueba que verifica la búsqueda por ID cuando el tipo de Vehiculo no existe.
     * Asegura que se lanza la excepción correspondiente.
     */
    @Test
    public void findByIdTest_TipoVehiculoNoExiste() {
        // Arrange
        when(tipoVehiculoRepository.findById(id)).thenReturn(Optional.empty());

        // Assert
        assertThrows(NoSuchElementException.class, () -> tipoVehiculoService.findById(id));
    }

    /**
     * Prueba que verifica el intento de actualización de un tipo de Vehiculo que no existe.
     * Asegura que se lanza la excepción correspondiente.
     */
    @Test
    public void updateTest_TipoVehiculoNoExistente() {
        // Arrange
        when(tipoVehiculoRepository.findById(id)).thenReturn(Optional.empty());

        // Assert
        assertThrows(NoSuchElementException.class, () -> tipoVehiculoService.update(tipoVehiculo, id));
    }

    /**
     * Prueba que verifica la eliminación de un tipo de Vehiculo que no existe.
     * Asegura que se lanza la excepción correspondiente.
     */
    @Test
    public void deleteTest_TipoVehiculoNoExistente() {
        // Arrange
        when(tipoVehiculoRepository.existsById(id)).thenReturn(false);

        // Assert
        assertThrows(NoSuchElementException.class, () -> tipoVehiculoService.delete(id));
    }

    /**
     * Prueba que verifica la validación de un tipo de Vehiculo.
     * Asegura que un tipo de Vehiculo válido no lanza excepciones.
     */
    @Test
    public void validarTipoVehiculoTest() {
        // Act & Assert
        assertDoesNotThrow(() -> tipoVehiculoService.validarTipoVehiculo(tipoVehiculo));
    }

    /**
     * Prueba que verifica la validación de un tipo de Vehiculo con nombre vacío.
     * Asegura que se lanza IllegalArgumentException.
     */
    @Test
    public void validarTipoVehiculo_NombreVacio() {
        // Arrange
        tipoVehiculo.setNombre("");

        // Assert
        assertThrows(IllegalArgumentException.class, () -> tipoVehiculoService.validarTipoVehiculo(tipoVehiculo));
    }

    /**
     * Prueba que verifica la validación de un tipo de Vehiculo con nombre demasiado largo.
     * Asegura que se lanza IllegalArgumentException.
     */
    @Test
    public void validarTipoVehiculo_NombreDemasiadoLargo() {
        // Arrange
        tipoVehiculo.setNombre("A".repeat(51)); // 51 caracteres

        // Assert
        assertThrows(IllegalArgumentException.class, () -> tipoVehiculoService.validarTipoVehiculo(tipoVehiculo));
    }
}