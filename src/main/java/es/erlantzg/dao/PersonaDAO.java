package es.erlantzg.dao;

import es.erlantzg.conexion.Conexion;
import es.erlantzg.modelos.Persona;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Clase DAO para acceder y manipular datos de la entidad {@link Persona} en la base de datos.
 * Usa la clase Conexion para obtener la conexión.
 *
 * @author Erlantz
 * @version 1.0
 */
public class PersonaDAO {

    /** Logger para esta clase */
    private static final Logger logger = LoggerFactory.getLogger(PersonaDAO.class);

    /**
     * Obtiene todas las personas registradas en la base de datos de forma asíncrona.
     *
     * @return CompletableFuture de lista de objetos {@link Persona}
     */
    public CompletableFuture<List<Persona>> obtenerTodasPersonas() {
        return Conexion.crearConexion().thenApplyAsync(conn -> {
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

                    Persona p = new Persona(id, nombre, apellidos, cumpleanos);
                    personas.add(p);
                }
                logger.info("{} registros encontrados", personas.size());
                rs.close();
                pst.close();

            } catch (SQLException e) {
                logger.error("Error al obtener las personas", e);
                throw new RuntimeException(e);
            }

            return personas;
        });
    }

    /**
     * Insertar una nueva persona en la base de datos de forma asíncrona.
     *
     * @param p Objeto {@link Persona} que será insertado
     * @return CompletableFuture con la persona insertada con su ID actualizado
     */
    public CompletableFuture<Persona> insertarPersona(Persona p) {
        return Conexion.crearConexion().thenApplyAsync(conn -> {
            String sqlInsert = "INSERT INTO personas (nombre, apellidos, cumpleanos) VALUES (?, ?, ?)";
            try {
                PreparedStatement pst = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
                pst.setString(1, p.getNombre());
                pst.setString(2, p.getApellidos());
                pst.setDate(3, Date.valueOf(p.getCumpleanos()));
                int filas = pst.executeUpdate();

                // Obtener el ID único de la persona añadida
                try {
                    ResultSet generarID = pst.getGeneratedKeys();
                    if (generarID.next()) {
                        int idGenerado = generarID.getInt(1);
                        p.setId(idGenerado);
                    } else {
                        throw new SQLException("No se ha podido obtener el id");
                    }

                } catch (SQLException e) {
                    logger.error("Error al obtener el id generado para la persona: {}", p);
                    throw e;
                }

                logger.info("Insert ejecutado, filas afectadas: {}", filas);
                pst.close();

            } catch (SQLException e) {
                logger.error("Error al insertar la persona: {}", p, e);
                throw new RuntimeException(e);
            }

            return p;
        });
    }

    /**
     * Eliminar una persona de la base de datos por su id de forma asíncrona.
     *
     * @param id Identificador único de la persona a eliminar
     * @return CompletableFuture<Void>
     */
    public CompletableFuture<Void> eliminarPersona(int id) {
        return Conexion.crearConexion().thenAcceptAsync(conn -> {
            String sqlDel = "DELETE FROM personas WHERE id = ?";
            try {
                PreparedStatement pst = conn.prepareStatement(sqlDel);
                pst.setInt(1, id);
                pst.executeUpdate();
                logger.info("Persona eliminada correctamente con el id {}", id);
                pst.close();

            } catch (SQLException e) {
                logger.error("Error al eliminar a la persona con id: {}", id, e);
                throw new RuntimeException(e);
            }
        });
    }

}