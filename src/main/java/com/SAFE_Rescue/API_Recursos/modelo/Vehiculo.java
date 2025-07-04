package com.SAFE_Rescue.API_Recursos.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Entidad que representa un vehículo en el sistema.
 * Contiene información sobre la composición y estado del vehículo.
 */
@Entity
@Table(name = "vehiculo")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Vehiculo {

    /**
     * ID único del vehículo en la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del vehículo")
    private Integer id;

    /**
     * Marca o fabricante del vehículo (ej: "Toyota", "Ford").
     * Debe ser un valor no nulo y con una longitud razonable.
     */
    @Column(length = 50, nullable = false)
    @Schema(description = "Marca del vehículo", example = "Toyota", required = true, maxLength = 50)
    private String marca;

    /**
     * Modelo del vehículo.
     * Debe ser un valor no nulo y con una longitud razonable.
     */
    @Column(length = 50, nullable = false)
    @Schema(description = "Modelo del vehículo", example = "Hilux", required = true, maxLength = 50)
    private String modelo;

    /**
     * Patente o matrícula del vehículo (ej: "AB123CD").
     * Identificador único legal del vehículo.
     */
    @Column(unique = true, length = 6, nullable = false)
    @Schema(description = "Patente del vehículo", example = "AB1234", required = true)
    private String patente;

    /**
     * Nombre del conductor asignado al vehículo.
     * Puede ser null si el vehículo no tiene conductor asignado.
     */
    @Column(length = 50, nullable = false)
    @Schema(description = "Nombre del conductor asignado al vehículo", example = "Juan Pérez", required = true, maxLength = 50)
    private String conductor;

    /**
     * Nombre descriptivo del estado del vehículo.
     * Debe ser un valor no nulo y con una longitud máxima recomendada de 50 caracteres.
     */
    @Column(length = 50, nullable = false)
    @Schema(description = "Estado del vehículo", example = "Operativo", required = true, maxLength = 50)
    private String estado;

    /**
     * Tipo de Vehículo (especialización).
     * Relación uno-a-uno.
     */
    @ManyToOne
    @JoinColumn(name = "tipo_vehiculo_id", referencedColumnName = "id")
    @Schema(description = "Tipo de vehículo asociado", required = true)
    private TipoVehiculo tipoVehiculo;
}