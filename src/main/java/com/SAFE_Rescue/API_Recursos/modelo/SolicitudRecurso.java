package com.SAFE_Rescue.API_Recursos.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa una solicitud recurso.
 * Contiene información básica de identificación y ubicación.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "solicitud_recurso")
public class SolicitudRecurso {

    /**
     * Identificador único de la solicitud recurso
     * Se genera automáticamente mediante estrategia de identidad
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Titulo de la solicitud recurso
     * Restricciones:
     * - Máximo 50 caracteres
     * - No puede ser nulo
     */
    @Column(length = 50, nullable = false)
    private String Titulo;

    /**
     * Detalle de la solicitud
     * - Máximo 400 caracteres
     * - No puede ser nulo
     */
    @Column(length = 400, nullable = false)
    private String Detalle;

    /**
     * Bombero que hace la solicitud
     * Relación uno-a-muchos
     */
    @OneToOne
    @JoinColumn(name = "bombero_id", referencedColumnName = "id")
    private Bombero bombero;

    /**
     * Estado de la solicitud
     */
    @Column(length = 50, nullable = false)
    private String estado;

    /**
     * Recurso solicitado
     * Relación uno-a-muchos
     */
    @OneToOne
    @JoinColumn(name = "recurso_id", referencedColumnName = "id")
    private Recurso recurso;



}
