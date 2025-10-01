package es.erlantzg.dao;

import es.erlantzg.modelos.Persona;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Clase DAO para acceder y manipular datos de la entidad {@link Persona} en la base de datos.
 * Carga configuración de conexión desde el archivo properties externo.
 * Permite operaciones: obtener, insertar y eliminar personas.
 *
 * @author Erlantz García
 * @version 1.0
 */
public class PersonaDAO {

    /** Conexión JDBC con la base de datos */
    private Connection conn;

    /** Logger para esta clase */
    private static final Logger logger = LoggerFactory.getLogger(PersonaDAO.class);

    /**
     * Constructor que carga la configuración desde el archivo `config.properties`
     * y establece la conexión con la base de datos.
     *
     * @throws RuntimeException si config.properties no lo encuentra o si falla la conexión
     */
    public PersonaDAO() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (is == null) {
                logger.error("No se ha podido encontrar config.properties");
                throw new RuntimeException("No se ha podido encontrar config.properties");
            }

            Properties prop = new Properties();
            prop.load(is);

            String URL = prop.getProperty("url");
            String USER = prop.getProperty("user");
            String PASSWORD = prop.getProperty("password");

            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            logger.info("Conexión a la BBDD establecida correctamente");

        } catch (SQLException e) {
            logger.error("Error al conectar a la base de datos", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error("Error al cargar la configuración", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Obtiene todas las personas registradas en la base de datos.
     *
     * @return Lista de objetos {@link Persona} encontrados
     * @throws RuntimeException si ocurre un error al ejecutar la consulta
     */
    public List<Persona> obtenerTodasPersonas() {
        List<Persona> personas = new ArrayList<>();
        String sqlSelec = "SELECT id, nombre, apellidos, cumpleanos FROM personas";

        try {
            PreparedStatement pst = conn.prepareStatement(sqlSelec);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String apellidos = rs.getString("apellidos");
                Date fecha = rs.getDate("cumpleanos");
                LocalDate cumpleanos = fecha != null ? fecha.toLocalDate() : null;

                Persona p = new Persona(nombre, apellidos, cumpleanos);
                personas.add(p);
            }
            logger.info("{} registros encontrados", personas.size());

        } catch (SQLException e) {
            logger.error("Error al obtener las personas", e);
            throw new RuntimeException(e);
        }

        return personas;
    }

    /**
     * Insertar una nueva persona en la base de datos.
     *
     * @param p Objeto {@link Persona} que será insertado
     * @throws RuntimeException si ocurre un error al ejecutar la consulta
     */
    public void insertarPersona(Persona p) {
        String sqlInsert = "INSERT INTO personas (nombre, apellidos, cumpleanos) VALUES (?, ?, ?)";
        try {
            PreparedStatement pst = conn.prepareStatement(sqlInsert);
            pst.setString(1, p.getNombre());
            pst.setString(2, p.getApellidos());
            pst.setDate(3, Date.valueOf(p.getCumpleanos()));
            int filas = pst.executeUpdate();
            logger.info("Insert ejecutado, filas afectadas: {}", filas);

        } catch (SQLException e) {
            logger.error("Error al insertar la persona: {}", p, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Eliminar una persona de la base de datos por su id.
     *
     * @param id Identificador único de la persona a eliminar
     * @throws RuntimeException si ocurre un error al ejecutar la consulta
     */
    public void eliminarPersona(int id) {
        String sqlDel = "DELETE FROM personas WHERE id = ?";
        try {
            PreparedStatement pst = conn.prepareStatement(sqlDel);
            pst.setInt(1, id);
            pst.executeUpdate();
            logger.info("Persona eliminada correctamente con el id {}", id);

        } catch (SQLException e) {
            logger.error("Error al eliminar a la persona con id: {}", id, e);
            throw new RuntimeException(e);
        }
    }

}
