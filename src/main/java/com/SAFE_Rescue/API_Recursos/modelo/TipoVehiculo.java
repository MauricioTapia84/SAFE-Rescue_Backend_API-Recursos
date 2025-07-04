package com.SAFE_Rescue.API_Recursos.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Entidad que representa los diferentes tipos de vehículos en el sistema.
 * <p>
 * Cada tipo de vehículo define una categorización particular para los vehículos operativos,
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
     * Identificador único autoincremental del tipo de vehículo.
     * <p>
     * Se genera automáticamente mediante la estrategia IDENTITY de la base de datos.
     * </p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del tipo de vehículo")
    private Integer id;

    /**
     * Nombre descriptivo del tipo de vehículo.
     * <p>
     * Restricciones:
     * </p>
     * <ul>
     *   <li>Máximo 50 caracteres</li>
     *   <li>No puede ser nulo</li>
     *   <li>Se almacena en la columna 'nombre_tipo'</li>
     * </ul>
     */
    @Column(name = "nombre_tipo", length = 50, nullable = false)
    @Schema(description = "Nombre del tipo de vehículo", example = "Vehículo de Rescate", required = true, maxLength = 50)
    private String nombre;
}