package es.erlantzg.modelos;

import java.time.LocalDate;

/**
 * Representa una persona con un identificador, nombre, apellido y fecha de cumpleaños.
 * Incluye getters y setters para cada propiedad y un metodo {@link #toString()}.
 *
 * @author Erlantz García
 * @version 1.0
 */
public class Persona {

    /**
     * Identificador único de la persona
     */
    private final int id;
    /**
     * Nombre de la persona
     */
    private final String nombre;
    /**
     * Apellidos de la persona
     */
    private final String apellidos;
    /**
     * Fecha de cumpleaños de la persona
     */
    private final LocalDate cumpleanos;

    /**
     * Constructor principal de la clase Persona
     *
     * @param id Identificador de la persona
     * @param nombre Nombre de la persona
     * @param apellidos Apellidos de la persona
     * @param cumpleanos Fecha de cumpleaños de la persona
     */
    public Persona(int id, String nombre, String apellidos, LocalDate cumpleanos) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.cumpleanos = cumpleanos;
    }

    /**
     * Constructor por defecto de la clase Persona.
     * Inicializa los campos con valores por defecto (0 o null).
     */
    public Persona() {
        this(0, "", "", null);
    }

    /**
     * Obtiene el identificador de la persona.
     *
     * @return id de la persona
     */
    public int getId(){
        return id;
    }


    /**
     * Obtiene el nombre de la persona.
     *
     * @return nombre de la persona
     */
    public String getNombre(){
        return nombre;
    }

    /**
     * Obtiene los apellidos de la persona.
     *
     * @return apellidos de la persona
     */
    public String getApellidos(){
        return apellidos;
    }

    /**
     * Obtiene la fecha de cumpleaños de la persona.
     *
     * @return cumpleaños de la persona
     */
    public LocalDate getCumpleanos(){
        return cumpleanos;
    }


}
