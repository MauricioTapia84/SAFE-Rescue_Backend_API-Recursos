package com.SAFE_Rescue.API_Recursos.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

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
     * Identificador único del recurso.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremental
    @Schema(description = "Identificador único del recurso")
    private Integer id;

    /**
     * Nombre descriptivo del recurso.
     * Ejemplos: "Recurso de rescate", "Botiquín primeros auxilios".
     * Debe ser un valor no nulo y con una longitud máxima recomendada de 50 caracteres.
     */
    @Column(length = 50, nullable = false)
    @Schema(description = "Nombre del recurso", example = "Botiquín primeros auxilios", required = true, maxLength = 50)
    private String nombre;

    /**
     * Cantidad disponible de este recurso.
     * Valor entero no negativo (>= 0).
     * Representa unidades disponibles en inventario.
     */
    @Column(unique = true, length = 9, nullable = false)
    @Schema(description = "Cantidad disponible del recurso", example = "10", required = true, minimum = "0")
    private Integer cantidad;

    /**
     * Nombre descriptivo del estado del recurso.
     * Debe ser un valor no nulo y con una longitud máxima recomendada de 50 caracteres.
     */
    @Column(length = 50, nullable = false)
    @Schema(description = "Estado del recurso", example = "Disponible", required = true, maxLength = 50)
    private String estado;

    /**
     * Tipo de Recurso (especialización).
     * Relación uno-a-muchos.
     */
    @ManyToOne
    @JoinColumn(name = "tipo_recurso_id", referencedColumnName = "id")
    @Schema(description = "Tipo de recurso asociado", required = true)
    private TipoRecurso tipoRecurso;
}