package com.SAFE_Rescue.API_Recursos.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa un recurso en el sistema.
 * Contiene información sobre la composición y estado del recurso.
 */
@Entity
@Table(name = "recurso")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Recurso {

    /**
     * Identificador único del recurso
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremental
    private int id;

    /**
     * Nombre descriptivo del recurso
     * Ejemplos: "Recurso de rescate", "Botiquín primeros auxilios"
     * Debe ser un valor no nulo y con una longitud máxima recomendada de 50 caracteres
     */
    @Column(length = 50, nullable = false)
    private String nombre;

    /**
     * Cantidad disponible de este recurso
     * Valor entero no negativo (>= 0)
     * Representa unidades disponibles en inventario
     */
    @Column(unique = true, length = 9, nullable = false)
    private int cantidad;

    /**
     * Nombre descriptivo del estado del recurso
     * Debe ser un valor no nulo y con una longitud máxima recomendada de 50 caracteres
     */
    @Column(length = 50, nullable = false)
    private String estado;

    /**
     * Tipo de Recurso (especialización)
     * Relación uno-a-muchos
     */
    @ManyToOne
    @JoinColumn(name = "tipo_recurso_id", referencedColumnName = "id")
    private TipoRecurso tipoRecurso;

}
