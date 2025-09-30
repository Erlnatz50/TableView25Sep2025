package es.erlantzg;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase principal de la aplicación JavaFX.
 * Esta clase extiende {@link Application} y se encarga de:
 * - Cargar la interfaz desde un archivo FXML.
 * - Aplicar la hoja de estilos CSS.
 * - Configurar el stage principal y mostrar la ventana.
 * - Registrar mensajes de log con SLF4J.
 * Contiene también el metodo {@link #main(String[])} para lanzar la aplicación.
 *
 * @author Erlantz García
 * @version 1.0
 */
public class App extends Application {

    /** Logger para esta clase */
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    /**
     * Metodo principal que se ejecuta al iniciar la aplicación JavaFX.
     * Carga el FXML, aplica el CSS, configura el stage y muetra la ventana.
     * Si ocurre algún error, muestra una alerta y registra el error en el Log.
     *
     * @param stage Stage principal proporcionado por JavaFX.
     */
    @Override
    public void start(Stage stage) {
        try{
            logger.debug("Intentando cargar el FXML: /fxml/visualizarCliente.fxml");
            FXMLLoader loaded = new FXMLLoader(getClass().getResource("/es/erlantzg/fxml/visualizarCliente.fxml"));
            Scene scene = new Scene(loaded.load());
            logger.info("FXML cargado correctamente");

            // Comprobar que el archivo de CSS existe y si no mostrar una alerta
            logger.debug("Buscando el archivo CSS");
            var archivoCSS = getClass().getResource("/es/erlantzg/css/estilo.css");
            if(archivoCSS != null){
                logger.info("CSS cargado correctamente");
                scene.getStylesheets().add(archivoCSS.toExternalForm());
            } else{
                logger.error("No se ha podido cargar el CSS");
                new Alert(Alert.AlertType.INFORMATION, "No se ha podido cargar el CSS").showAndWait();
            }

            logger.info("Cargar la aplicación");
            stage.setTitle("Adding/Deleting Rows in a TableViews");
            stage.setScene(scene);
            stage.setResizable(true);
            stage.setMinWidth(400);
            stage.setMinHeight(350);
            stage.setMaxWidth(500);
            stage.setMaxHeight(500);
            stage.show();

        } catch (Exception e) {
            logger.error("Error al intentar cargar la aplicación", e);
            new Alert(Alert.AlertType.ERROR, "Error al intentar cargar la aplicación").showAndWait();
        }
    }

    /**
     * Metodo que se ejecuta cuando cierra la aplicación.
     * Registra un mensaje de cierre en el archivo de Log.
     */
    @Override
    public void stop(){
        logger.info("Aplicación finalizada correctamente");
    }

    /**
     * Metodo principal que lanza la aplicación JavaFX
     *
     * @param args Argumentos de línea de comandos (no usados).
     */
    public static void main(String[] args) {
        logger.info("Iniciando aplicación JavaFX...");
        launch();
    }

}