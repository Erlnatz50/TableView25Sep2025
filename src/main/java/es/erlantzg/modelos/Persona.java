package es.erlantzg.modelos;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Representa una persona con un identificador, nombre, apellido y fecha de cumpleaños.
 * Incluye getters y setters para cada propiedad y un metodo {@link #toString()}.
 *
 * @author Erlantz García
 * @version 1.0
 */
public class Persona {

    /** Identificador único de la persona */
    private final int id;

    /** Nombre de la persona */
    private final String nombre;

    /** Apellidos de la persona */
    private final String apellidos;

    /** Fecha de cumpleaños de la persona */
    private final LocalDate cumpleanos;

    /** Contador único para IDs. */
    private static final AtomicInteger contador = new AtomicInteger(0);

    /**
     * Constructor principal de la clase Persona
     *
     * @param nombre Nombre de la persona
     * @param apellidos Apellidos de la persona
     * @param cumpleanos Fecha de cumpleaños de la persona
     */
    public Persona(String nombre, String apellidos, LocalDate cumpleanos) {
        this.id = contador.incrementAndGet();
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.cumpleanos = cumpleanos;
    }

    /**
     * Constructor por defecto de la clase Persona.
     * Inicializa los campos con valores por defecto (ID automatico, cadenas vacías y null).
     */
    public Persona() {
        this("", "", null);
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

    @Override
    public String toString() {
        return "id= " + id + ", nombre= " + nombre + ", apellidos= " + apellidos + ", cumpleaños= " + cumpleanos;
    }

    public static void resetearContador(int valorInicial) {
        contador.set(valorInicial);
    }

}
