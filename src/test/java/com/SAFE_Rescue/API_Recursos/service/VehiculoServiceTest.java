package com.SAFE_Rescue.API_Recursos.service;

import com.SAFE_Rescue.API_Recursos.modelo.TipoVehiculo;
import com.SAFE_Rescue.API_Recursos.modelo.Vehiculo;
import com.SAFE_Rescue.API_Recursos.repository.VehiculoRepository;
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
 * Clase de pruebas unitarias para el servicio VehiculoService.
 * Verifica la funcionalidad de los métodos dentro de VehiculoService,
 * incluyendo operaciones CRUD y validaciones.
 */
@SpringBootTest
public class VehiculoServiceTest {

    @Autowired
    private VehiculoService vehiculoService;

    @MockitoBean
    private VehiculoRepository vehiculoRepository;

    @MockitoBean
    private TipoVehiculoService tipoVehiculoService;

    private Faker faker;
    private Vehiculo vehiculo;
    private Integer id;

    /**
     * Configura el entorno de pruebas antes de cada prueba.
     * Inicializa Faker y crea una instancia de Vehículo para las pruebas.
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
     * Prueba que verifica la obtención de todos los vehiculos.
     * Asegura que el servicio devuelve la lista correcta de vehiculos.
     */
    @Test
    public void findAllTest() {
        // Arrange
        when(vehiculoRepository.findAll()).thenReturn(Collections.singletonList(vehiculo));

        // Act
        List<Vehiculo> vehiculos = vehiculoService.findAll();

        // Assert
        assertNotNull(vehiculos);
        assertEquals(1, vehiculos.size());
        assertEquals(vehiculo.getId(), vehiculos.get(0).getId());
        assertEquals(vehiculo.getMarca(),vehiculos.get(0).getMarca());
        assertEquals(vehiculo.getModelo(), vehiculos.get(0).getModelo());
        assertEquals(vehiculo.getPatente(), vehiculos.get(0).getPatente());
        assertEquals(vehiculo.getConductor(), vehiculos.get(0).getConductor());
        assertEquals(vehiculo.getEstado(), vehiculos.get(0).getEstado());
        assertEquals(vehiculo.getTipoVehiculo(), vehiculos.get(0).getTipoVehiculo());

    }

    /**
     * Prueba que verifica la búsqueda de un vehiculo por su ID.
     * Asegura que se encuentra el vehiculo correcto.
     */
    @Test
    public void findByIdTest() {
        // Arrange
        when(vehiculoRepository.findById(id)).thenReturn(Optional.of(vehiculo));

        // Act
        Vehiculo encontrado = vehiculoService.findById(id);

        // Assert
        assertNotNull(encontrado);
        assertEquals(vehiculo.getId(), encontrado.getId());
        assertEquals(vehiculo.getMarca(),encontrado.getMarca());
        assertEquals(vehiculo.getModelo(),encontrado.getModelo());
        assertEquals(vehiculo.getPatente(),encontrado.getPatente());
        assertEquals(vehiculo.getConductor(),encontrado.getConductor());
        assertEquals(vehiculo.getEstado(),encontrado.getEstado());
        assertEquals(vehiculo.getTipoVehiculo(),encontrado.getTipoVehiculo());
    }

    /**
     * Prueba que verifica la creación de un nuevo vehiculo.
     * Asegura que el vehiculo se guarda correctamente en el repositorio.
     */
    @Test
    public void saveTest() {
        // Arrange
        when(tipoVehiculoService.save(any(TipoVehiculo.class))).thenReturn(vehiculo.getTipoVehiculo());
        when(vehiculoRepository.save(vehiculo)).thenReturn(vehiculo);

        // Act
        Vehiculo guardado = vehiculoService.save(vehiculo);

        // Assert
        assertNotNull(guardado);
        assertEquals(vehiculo.getMarca(),guardado.getMarca());
        assertEquals(vehiculo.getModelo(),guardado.getModelo());
        assertEquals(vehiculo.getPatente(),guardado.getPatente());
        assertEquals(vehiculo.getConductor(),guardado.getConductor());
        assertEquals(vehiculo.getEstado(),guardado.getEstado());
        assertEquals(vehiculo.getTipoVehiculo(),guardado.getTipoVehiculo());
        verify(vehiculoRepository, times(1)).save(guardado);
    }

    /**
     * Prueba que verifica la actualización de un vehiculo existente.
     * Asegura que el vehiculo se actualiza correctamente.
     */
    @Test
    public void updateTest() {
        // Arrange
        when(vehiculoRepository.findById(id)).thenReturn(Optional.of(vehiculo));
        when(vehiculoRepository.save(vehiculo)).thenReturn(vehiculo);

        // Act
        Vehiculo actualizado = vehiculoService.update(vehiculo, id);

        // Assert
        assertNotNull(actualizado);
        assertEquals(vehiculo.getMarca(),actualizado.getMarca());
        assertEquals(vehiculo.getModelo(),actualizado.getModelo());
        assertEquals(vehiculo.getPatente(),actualizado.getPatente());
        assertEquals(vehiculo.getConductor(),actualizado.getConductor());
        assertEquals(vehiculo.getEstado(),actualizado.getEstado());
        assertEquals(vehiculo.getTipoVehiculo(),actualizado.getTipoVehiculo());
        verify(vehiculoRepository, times(1)).save(actualizado);
    }

    /**
     * Prueba que verifica la eliminación de un vehiculo.
     * Asegura que el vehiculo se elimina correctamente del repositorio.
     */
    @Test
    public void deleteTest() {
        // Arrange
        when(vehiculoRepository.existsById(id)).thenReturn(true);
        doNothing().when(vehiculoRepository).deleteById(id);

        // Act
        vehiculoService.delete(id);

        // Assert
        verify(vehiculoRepository, times(1)).deleteById(id);
    }

    /**
     * Prueba que verifica la búsqueda por ID cuando el vehiculo no existe.
     * Asegura que se lanza la excepción correspondiente.
     */
    @Test
    public void findByIdTest_VehiculoNoExiste() {
        // Arrange
        when(vehiculoRepository.findById(id)).thenReturn(Optional.empty());

        // Assert
        assertThrows(NoSuchElementException.class, () -> vehiculoService.findById(id));
    }

    /**
     * Prueba que verifica el intento de actualización de un vehiculo que no existe.
     * Asegura que se lanza la excepción correspondiente.
     */
    @Test
    public void updateTest_VehiculoNoExistente() {
        // Arrange
        when(vehiculoRepository.findById(id)).thenReturn(Optional.empty());

        // Assert
        assertThrows(NoSuchElementException.class, () -> vehiculoService.update(vehiculo, id));
    }

    /**
     * Prueba que verifica la eliminación de un vehiculo que no existe.
     * Asegura que se lanza la excepción correspondiente.
     */
    @Test
    public void deleteTest_VehiculoNoExistente() {
        // Arrange
        when(vehiculoRepository.existsById(id)).thenReturn(false);

        // Assert
        assertThrows(NoSuchElementException.class, () -> vehiculoService.delete(id));
    }

    /**
     * Prueba que verifica la validación de un vehiculo.
     * Asegura que un vehiculo válido no lanza excepciones.
     */
    @Test
    public void validarVehiculoTest() {
        // Act & Assert
        assertDoesNotThrow(() -> vehiculoService.validarVehiculo(vehiculo));
    }

    /**
     * Prueba que verifica la validación de un vehiculo con patente existente.
     * Asegura que se lanza RuntimeException.
     */
    @Test
    public void validarVehiculo_PatenteExistente() {
        // Arrange
        when(vehiculoRepository.existsByPatente(vehiculo.getPatente())).thenReturn(true);

        // Assert
        assertThrows(RuntimeException.class, () -> vehiculoService.validarVehiculo(vehiculo));
    }

    /**
     * Prueba que verifica la validación de un vehiculo con marca vacía.
     * Asegura que se lanza IllegalArgumentException.
     */
    @Test
    public void validarVehiculo_MarcaVacia() {
        // Arrange
        vehiculo.setMarca(null);

        // Assert
        assertThrows(IllegalArgumentException.class, () -> vehiculoService.validarVehiculo(vehiculo));
    }

    /**
     * Prueba que verifica la validación de un vehiculo con modelo vacío.
     * Asegura que se lanza IllegalArgumentException.
     */
    @Test
    public void validarVehiculo_ModeloVacio() {
        // Arrange
        vehiculo.setModelo(null);

        // Assert
        assertThrows(IllegalArgumentException.class, () -> vehiculoService.validarVehiculo(vehiculo));
    }

    /**
     * Prueba que verifica la validación de un vehiculo con estado vacío.
     * Asegura que se lanza IllegalArgumentException.
     */
    @Test
    public void validarVehiculo_EstadoVacio() {
        // Arrange
        vehiculo.setEstado(null);

        // Assert
        assertThrows(IllegalArgumentException.class, () -> vehiculoService.validarVehiculo(vehiculo));
    }

    /**
     * Prueba que verifica la validación de un vehiculo con conductor vacío.
     * Asegura que se lanza IllegalArgumentException.
     */
    @Test
    public void validarVehiculo_ConductorVacio() {
        // Arrange
        vehiculo.setConductor(null);

        // Assert
        assertThrows(IllegalArgumentException.class, () -> vehiculoService.validarVehiculo(vehiculo));
    }

    /**
     * Prueba que verifica la validación de un vehiculo con patente demasiado larga.
     * Asegura que se lanza IllegalArgumentException.
     */
    @Test
    public void validarVehiculo_PatenteDemasiadoLarga() {
        // Arrange
        vehiculo.setPatente("ABCDEF11");

        // Assert
        assertThrows(IllegalArgumentException.class, () -> vehiculoService.validarVehiculo(vehiculo));
    }
}