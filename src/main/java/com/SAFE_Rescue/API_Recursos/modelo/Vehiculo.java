package com.SAFE_Rescue.API_Recursos.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa un vehiculo en el sistema.
 * Contiene información sobre la composición y estado del vehiculo.
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
    private int id;

    /**
     * Marca o fabricante del vehículo (ej: "Toyota", "Ford").
     * Debe ser un valor no nulo y con una longitud razonable.
     */
    @Column(length = 50, nullable = false)
    private String marca;

    /**
     * Modelo del vehículo
     * Debe ser un valor no nulo y con una longitud razonable.
     */
    @Column(length = 50, nullable = false)
    private String modelo;


    /**
     * Patente o matrícula del vehículo (ej: "AB123CD").
     * Identificador único legal del vehículo.
     */
    @Column(unique = true,length = 6, nullable = false)
    private String patente;

    /**
     * Nombre del conductor asignado al vehículo.
     * Puede ser null si el vehículo no tiene conductor asignado.
     */
    @Column(length = 50, nullable = false)
    private String conductor;

    /**
     * Nombre descriptivo del estado del vehiculo
     * Debe ser un valor no nulo y con una longitud máxima recomendada de 50 caracteres
     */
    @Column(length = 50, nullable = false)
    private String estado;

    /**
     * Tipo de Vehiculo (especialización)
     * Relación uno-a-muchos
     */
    @ManyToOne
    @JoinColumn(name = "tipo_vehiculo_id", referencedColumnName = "id")
    private TipoVehiculo tipoVehiculo;
}