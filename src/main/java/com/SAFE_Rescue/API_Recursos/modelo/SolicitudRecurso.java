package com.SAFE_Rescue.API_Recursos.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa una solicitud recurso.
 * Contiene información básica de identificación y ubicación.
 */
@NoArgsConstructor // Genera constructor sin argumentos
@AllArgsConstructor // Genera constructor con todos los argumentos
@Data // Genera getters, setters, toString, equals y hashCode
@Entity // Indica que es una entidad persistente
@Table(name = "solicitud_recurso") // Nombre de la tabla en la base de datos
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

    @Column(length = 400, nullable = false)
    private String Detalle;

    @OneToOne
    @JoinColumn(name = "bombero_id", referencedColumnName = "id")
    private Bombero bombero;

    @Column(nullable = false)
    private boolean estado;

    @OneToOne
    @JoinColumn(name = "recurso_id", referencedColumnName = "id")
    private Recurso recurso;



}
