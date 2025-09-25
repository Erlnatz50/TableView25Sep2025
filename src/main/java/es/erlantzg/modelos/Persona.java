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
    private int id;
    /**
     * Nombre de la persona
     */
    private String nombre;
    /**
     * Apellidos de la persona
     */
    private String apellidos;
    /**
     * Fecha de cumpleaños de la persona
     */
    private LocalDate cumpleanos;

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
     * Establece el identificador de la persona.
     *
     * @param id Nuevo identificador
     */
    public void setId(int id){
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
     * Establece el nombre de la persona.
     *
     * @param nombre Nuevo nombre
     */
    public void setNombre(String nombre){
        this.nombre = nombre;
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
     * Establece los apellidos de la persona.
     *
     * @param apellidos Nuevos apellidos
     */
    public void setApellidos(String apellidos){
        this.apellidos = apellidos;
    }

    /**
     * Obtiene la fecha de cumpleaños de la persona.
     *
     * @return cumpleaños de la persona
     */
    public LocalDate getCumpleanos(){
        return cumpleanos;
    }

    /**
     * Establece la fecha de cumpleaños de la persona.
     *
     * @param cumpleanos Nueva fecha de cumpleaños
     */
    public void setCumpleanos(LocalDate cumpleanos){
        this.cumpleanos = cumpleanos;
    }

    /**
     * Devulve todos los valores de una persona en cadena.
     *
     * @return cadena con id, nombre, apellidos y cumpleaños
     */
    @Override
    public String toString(){
        return "Id: " + id + ", nombre: " + nombre + ", apellidos: " + apellidos + ", cumpleaños: " + cumpleanos;
    }
}
