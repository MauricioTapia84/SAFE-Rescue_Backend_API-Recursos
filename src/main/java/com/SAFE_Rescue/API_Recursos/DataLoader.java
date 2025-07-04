package com.SAFE_Rescue.API_Recursos;

import com.SAFE_Rescue.API_Recursos.modelo.*;
import com.SAFE_Rescue.API_Recursos.repository.*;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Clase encargada de cargar datos iniciales en la base de datos.
 * <p>
 * Esta clase se ejecuta solo en el perfil 'dev' y utiliza Faker para generar datos ficticios para las entidades
 * de la aplicación.
 * </p>
 */
@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired private BomberoRepository bomberoRepository;
    @Autowired private RecursoRepository recursoRepository;
    @Autowired private SolicitudRecursoRepository solicitudRecursoRepository;
    @Autowired private TipoRecursoRepository tipoRecursoRepository;
    @Autowired private TipoVehiculoRepository tipoVehiculoRepository;
    @Autowired private VehiculoRepository vehiculoRepository;

    /**
     * Método que se ejecuta al iniciar la aplicación.
     * <p>
     * Genera datos ficticios para las entidades TipoRecurso, TipoVehículo, Recurso, SolicitudRecurso y Vehículo.
     * </p>
     *
     * @param args Argumentos de línea de comandos
     * @throws Exception sí ocurre un error durante la ejecución
     */
    @Override
    public void run(String... args) throws Exception {
        System.out.println("DataLoader is running...");

        Faker faker = new Faker();
        Random random = new Random();
        Set<String> uniquePatentes = new HashSet<>();
        List<String> estados = Arrays.asList("Pendiente", "En Proceso", "Completada", "Rechazada");

        // Generar TipoRecurso
        for (int i = 0; i < 5; i++) {
            TipoRecurso tipoRecurso = new TipoRecurso();
            tipoRecurso.setNombre(faker.commerce().productName());
            try {
                tipoRecursoRepository.save(tipoRecurso);
            } catch (Exception e) {
                System.out.println("Error al guardar Tipo Recurso: " + e.getMessage());
            }
        }

        List<TipoRecurso> tiposRecursos = tipoRecursoRepository.findAll();
        if (tiposRecursos.isEmpty()) {
            System.out.println("No se encontraron tiposRecursos, agregue ubicaciones primero");
            return;
        }

        // Generar TipoVehícluo
        for (int i = 0; i < 3; i++) {
            TipoVehiculo tipoVehiculo = new TipoVehiculo();
            tipoVehiculo.setNombre(faker.vehicle().carType());
            try {
                tipoVehiculoRepository.save(tipoVehiculo);
            } catch (Exception e) {
                System.out.println("Error al guardar tipo Vehiculo: " + e.getMessage());
            }
        }

        List<TipoVehiculo> tiposVehiculos = tipoVehiculoRepository.findAll();
        if (tiposVehiculos.isEmpty()) {
            System.out.println("No se encontraron tipo vehículos, agregue tipos vehículos primero");
            return;
        }


        // Generar Recursos
        for (int i = 0; i < 5; i++) {
            Recurso recurso = new Recurso();
            recurso.setNombre(faker.commerce().productName());
            String estado = estados.get(random.nextInt(estados.size()));
            recurso.setEstado(estado);
            recurso.setCantidad(faker.number().numberBetween(0, 9999));

            TipoRecurso tipoRecurso = tiposRecursos.get(random.nextInt(tiposRecursos.size()));
            recurso.setTipoRecurso(tipoRecurso);
            try {
                recursoRepository.save(recurso);
            } catch (Exception e) {
                System.out.println("Error al guardar recurso: " + e.getMessage());
            }
        }

        List<Recurso> recursos = recursoRepository.findAll();
        if (recursos.isEmpty()) {
            System.out.println("No se encontraron recursos, agregue recursos primero");
            return;
        }

        // Generar Vehículos
        for (int i = 0; i < 5; i++) {
            Vehiculo vehiculo = new Vehiculo();
            String patente;
            do {
                patente = String.valueOf(faker.number().numberBetween(0, 999999));
            } while (uniquePatentes.contains(patente));
            uniquePatentes.add(patente);
            vehiculo.setPatente(patente);
            vehiculo.setMarca(faker.vehicle().make());
            vehiculo.setModelo(faker.vehicle().model());
            vehiculo.setConductor(faker.name().firstName());
            String estado = estados.get(random.nextInt(estados.size()));
            vehiculo.setEstado(estado);

            TipoVehiculo tipoVehiculo = tiposVehiculos.get(random.nextInt(tiposVehiculos.size()));
            vehiculo.setTipoVehiculo(tipoVehiculo);
            try {
                vehiculoRepository.save(vehiculo);
            } catch (Exception e) {
                System.out.println("Error al guardar vehiculo: " + e.getMessage());
            }
        }

        List<Vehiculo> vehiculos = vehiculoRepository.findAll();
        if (vehiculos.isEmpty()) {
            System.out.println("No se encontraron vehiculos, agregue vehiculos primero");
            return;
        }

        List<Bombero> bomberos = bomberoRepository.findAll();
        if (bomberos.isEmpty()) {
            System.out.println("No se encontraron bomberos, agregue bomberos primero");
            return;
        }

        // Generar SolicitudRecurso
        for (int i = 0; i < 3; i++) {
            SolicitudRecurso solicitudRecurso = new SolicitudRecurso();

            solicitudRecurso.setTitulo(faker.book().title());
            String detalleSolicitud = faker.lorem().paragraph();
            if (detalleSolicitud.length() > 400) {
                detalleSolicitud = detalleSolicitud.substring(0, 400);
            }
            solicitudRecurso.setDetalle(detalleSolicitud);

            Bombero bombero = bomberos.get(random.nextInt(bomberos.size()));
            solicitudRecurso.setBombero(bombero);
            String estado = estados.get(random.nextInt(estados.size()));
            solicitudRecurso.setEstado(estado);
            Recurso recurso = recursos.get(random.nextInt(recursos.size()));
            solicitudRecurso.setRecurso(recurso);

            try {
                solicitudRecursoRepository.save(solicitudRecurso);
            } catch (Exception e) {
                System.out.println("Error al guardar solicitud recurso: " + e.getMessage());
            }
        }

    }
}