package es.erlantzg.conexion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

/**
 * Clase encargada de gestionar la conexión a la base de datos de forma asíncrona.
 * Carga los parámetros desde el archivo config.properties.
 *
 * @author Erlantz
 * @version 1.0
 */
public class Conexion {

    /** Logger para esta clase */
    private static final Logger logger = LoggerFactory.getLogger(Conexion.class);

    /** Conexión única */
    private static volatile Connection conn = null;

    /**
     * Devuelve una conexión activa a la base de datos de forma asíncrona.
     *
     * @return CompletableFuture con la conexión activa
     */
    public static CompletableFuture<Connection> crearConexion() {
        return CompletableFuture.supplyAsync(() -> {
            if (conn == null) {
                synchronized (Conexion.class) {
                    if (conn == null) {
                        try (InputStream is = Conexion.class.getClassLoader().getResourceAsStream("config.properties")) {
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
                }
            }
            return conn;
        });
    }
}