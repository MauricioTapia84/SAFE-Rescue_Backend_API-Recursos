package com.SAFE_Rescue.API_Recursos.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Entidad que representa una solicitud de recurso.
 * Contiene información básica de identificación y ubicación.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "solicitud_recurso")
public class SolicitudRecurso {

    /**
     * Identificador único de la solicitud de recurso.
     * Se genera automáticamente mediante estrategia de identidad.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único de la solicitud de recurso")
    private Integer id;

    /**
     * Título de la solicitud de recurso.
     * Restricciones:
     * - Máximo 50 caracteres.
     * - No puede ser nulo.
     */
    @Column(length = 50, nullable = false)
    @Schema(description = "Título de la solicitud de recurso", example = "Solicitud de Botiquín", required = true, maxLength = 50)
    private String titulo;

    /**
     * Detalle de la solicitud.
     * Restricciones:
     * - Máximo 400 caracteres.
     * - No puede ser nulo.
     */
    @Column(length = 400, nullable = false)
    @Schema(description = "Detalle de la solicitud", example = "Se requiere un botiquín de primeros auxilios urgente", required = true, maxLength = 400)
    private String detalle;

    /**
     * Bombero que hace la solicitud.
     * Relación uno-a-uno.
     */
    @ManyToOne
    @JoinColumn(name = "bombero_id", referencedColumnName = "id")
    @Schema(description = "Bombero que realiza la solicitud", required = true)
    private Bombero bombero;

    /**
     * Estado de la solicitud.
     * Debe ser un valor no nulo y con una longitud máxima recomendada de 50 caracteres.
     */
    @Column(length = 50, nullable = false)
    @Schema(description = "Estado de la solicitud", example = "Pendiente", required = true, maxLength = 50)
    private String estado;

    /**
     * Recurso solicitado.
     * Relación uno-a-uno.
     */
    @ManyToOne
    @JoinColumn(name = "recurso_id", referencedColumnName = "id")
    @Schema(description = "Recurso solicitado en la solicitud", required = true)
    private Recurso recurso;
}