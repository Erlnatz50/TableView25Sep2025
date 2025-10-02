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

    /** Identificador único de la persona */
    private int id;

    /** Nombre de la persona */
    private final String nombre;

    /** Apellidos de la persona */
    private final String apellidos;

    /** Fecha de cumpleaños de la persona */
    private final LocalDate cumpleanos;

    /**
     * Constructor principal de la clase Persona
     *
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
     * Inicializa los campos con valores por defecto (ID automatico, cadenas vacías y null).
     */
    public Persona() {
        this(0, "", "", null);
    }

    public Persona(String nombre, String apellidos, LocalDate cumpleanos) {
        this(0, nombre, apellidos, cumpleanos);
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
     * Asigna un id único a la persona
     *
     * @param id de la persona
     */
    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "id= " + id + ", nombre= " + nombre + ", apellidos= " + apellidos + ", cumpleaños= " + cumpleanos;
    }

}