package com.SAFE_Rescue.API_Recursos.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa los diferentes tipos de vehiculos en el sistema.
 * <p>
 * Cada tipos de vehiculos define una categorización particular para los vehiculos operativos,
 * permitiendo agruparlos por características o funciones específicas.
 * </p>
 *
 * @see Vehiculo
 */
@Entity
@Table(name = "tipo_vehiculo")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TipoVehiculo {

    /**
     * Identificador único autoincremental del tipo de equipo.
     * <p>
     * Se genera automáticamente mediante la estrategia IDENTITY de la base de datos.
     * </p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Nombre descriptivo del tipos de vehiculo
     * <p>
     * Restricciones:
     * </p>
     * <ul>
     *   <li>Máximo 50 caracteres</li>
     *   <li>No puede ser nulo</li>
     *   <li>Se almacena en la columna 'nombre_tipo'</li>
     * </ul>
     *
     */
    @Column(name = "nombre_tipo", length = 50, nullable = false)
    private String nombre;
}
