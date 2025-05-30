package com.SAFE_Rescue.API_Recursos.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa los diferentes tipos de recursos en el sistema.
 * <p>
 * Cada tipos de recursos define una categorización particular para los recursos operativos,
 * permitiendo agruparlos por características o funciones específicas.
 * </p>
 *
 * @see Recurso
 */
@Entity
@Table(name = "tipo_recurso")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TipoRecurso {

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
     * Nombre descriptivo del tipos de recursos
     * <p>
     * Restricciones:
     * </p>
     * <ul>
     *   <li>Máximo 50 caracteres</li>
     *   <li>No puede ser nulo</li>
     *   <li>Se almacena en la columna 'nombre_tipo'</li>
     * </ul>
     *
     * <p>Ejemplos: "Recurso de Rescate", "Recurso de Primeros Auxilios", "Recurso de Materiales Peligrosos"</p>
     */
    @Column(name = "nombre_tipo", length = 50, nullable = false)
    private String nombre;

}