package es.erlantzg.controladores;

import es.erlantzg.dao.PersonaDAO;
import es.erlantzg.modelos.Persona;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Controlador de la vista "visualizarCliente.fxml".
 * Gestiona la interacción entre la interfaz gráfica (FXML) y los datos de tipo {@link Persona}
 * Permite añadir, eliminar y restaurar filas en una tabla de personas.
 * Las operaciones con la base de datos se realizan de forma asincrona en hilos en segundo plano,
 * para mantener la interfaz gráfica receptiva y evitar bloqueos.
 *
 * @author Erlantz
 * @version 1.0
 */
public class VisualizarCliente {

    /** Columnas que muestra el ID de la persona en la tabla */
    @FXML
    private TableColumn<Persona, Integer> colId;

    /** Columna que muestra el nombre de la persona en la tabla */
    @FXML
    private TableColumn<Persona, String> colNombre;

    /** Columna que muestra los apellidos de la persona en la tabla */
    @FXML
    private TableColumn<Persona, String> colApellidos;

    /** Columna que muestra la fecha de cumpleaños de la persona en la tabla */
    @FXML
    private TableColumn<Persona, LocalDate> colCumple;

    /** Selector de fecha para introducir el cumpleaños de la persona */
    @FXML
    private DatePicker seleCumpleanios;

    /** Tabla que muestra la lista de personas */
    @FXML
    private TableView<Persona> tablaPersonas;

    /** Campo de texto para introducir los apellidos */
    @FXML
    private TextField txtApellidos;

    /** Campo de texto para introducir el nombre */
    @FXML
    private TextField txtNombre;

    /** Logger para esta clase */
    private static final Logger logger = LoggerFactory.getLogger(VisualizarCliente.class);

    /** DAO para acceder y manipular la entidad Persona */
    private PersonaDAO pDAO;

    /**
     * Inicializa el controlador.
     * Configura las columnas de la tabla para enlazar con las propiedades de {@link Persona} y carga las personas desde la base de datos.
     */
    @FXML
    public void initialize(){
        pDAO = new PersonaDAO();
        logger.info("Inicializando controlador VisualizarCliente");
        vincularColumnas();
        cargarPersonas();
    }

    /**
     * Metodo que se ejecuta al pulsar el botón "Añadir".
     * Válida que todos los campos estén completos.
     * Inserta una nueva persona en la base de datos de forma asíncrona
     * Limpia los campos del formulario tras añadirlos.
     */
    @FXML
    void btnAniadir() {
        String nombre = txtNombre.getText().trim();
        String apellidos = txtApellidos.getText().trim();
        LocalDate cumple = seleCumpleanios.getValue();

        if(nombre.isEmpty() || apellidos.isEmpty() || cumple == null){
            logger.warn("Intento añadir una persona con campos incompletos.");
            mandarAlertas(Alert.AlertType.WARNING, "Campos incompletos", "Falta información", "Debes rellenar el nombre, los apellidos y el cumpleaños.");
            return;
        }

        Persona p = new Persona(nombre, apellidos, cumple);

        pDAO.insertarPersona(p)
            .thenAccept(personaInsertada -> Platform.runLater(() -> {
                tablaPersonas.getItems().add(personaInsertada);
                limpiarCampos();
                logger.info("Personas añadida asincrónicamente: {}", personaInsertada);
            }))
            .exceptionally(ex -> {
                Platform.runLater(() -> {
                    logger.error("Error insertando persona", ex);
                    mandarAlertas(Alert.AlertType.ERROR, "Error", "No se ha podido insertar la persona", ex.getMessage());
                });
                return null;
            });
    }

    /**
     * Metodo que se ejecuta al pulsar el botón "Eliminar fila seleccionada".
     * Solicita confirmación antes de eliminar.
     * Elimina la persona seleccionada de la base de datos y actualiza la tabla.
     * Muestra alertas en caso de error.
     */
    @FXML
    void btnEliminarFilaSelec() {
        Persona pSelec = tablaPersonas.getSelectionModel().getSelectedItem();

        if (pSelec == null){
            mandarAlertas(Alert.AlertType.WARNING, "Seleccionar fila", null, "Debes seleccionar una fila para eliminar");
            return;
        }

        if (!mandarConfirmacion("Confirmar eliminación", "Eliminar persona?", " " + pSelec.getNombre() + " " + pSelec.getApellidos())) {
            return;
        }

        pDAO.eliminarPersona(pSelec.getId())
            .thenRun(() -> Platform.runLater(() -> {
                tablaPersonas.getItems().remove(pSelec);
                logger.info("Personas eliminada asincrónicamente: {}", pSelec);
            }))
            .exceptionally(ex -> {
                Platform.runLater(() -> {
                    logger.error("Error eliminando persona", ex);
                    mandarAlertas(Alert.AlertType.ERROR, "Error", "No se ha podido eliminar la persona", ex.getMessage());
                });
                return null;
            });
    }

    /**
     * Metodo que se ejecuta al pulsar el botón "Restaurar filas".
     * Limpia la tabla y recarga todas las personas almacenadas en la base de datos
     * Refresca la vista para mostrar las personas actuales.
     */
    @FXML
    void btnRestaurarFilas() {
        try {
            tablaPersonas.getItems().clear();
            cargarPersonas();
            logger.info("Tabla restaurada con personas");
        } catch (Exception e) {
            logger.error("Error al restaurar filas: {}", e.getMessage());
            mandarAlertas(Alert.AlertType.ERROR,"Error al cargar los datos", "No se ha podido cargas las filas", e.getMessage());
        }
    }

    /**
     * Muestra una ventana modal con información sobre el autor.
     * La ventana bloque la interacción con la ventana principal hasta que la cierre.
     */
    @FXML
    void menuAcercaDe() {
        Stage ventanaAcercaDe = new Stage();
        ventanaAcercaDe.setTitle("Acerca de");

        Label etiqueta = new Label("Está aplicación la a creado Erlantz García.");
        etiqueta.setPadding(new Insets(10));

        Scene escena = new Scene(new StackPane(etiqueta), 300, 150);
        ventanaAcercaDe.setScene(escena);

        ventanaAcercaDe.initModality(Modality.APPLICATION_MODAL);

        ventanaAcercaDe.showAndWait();
    }

    /**
     * Cierra la aplicación JavaFX terminando la ejecución de forma ordenada.
     * Solicita confirmación al usuario antes de cerrar.
     */
    @FXML
    void menuCerrar() {
        boolean confirmar = mandarConfirmacion("Cerrar aplicación", "Estas seguro que deseas cerrar la aplicación?", "");

        if (confirmar) {
            Platform.exit();
        }
    }

    /**
     * Ejecuta la acción de eliminar la fila seleccionada en la tabla de personas.
     */
    @FXML
    void menuEliminarFila() {
        btnEliminarFilaSelec();
    }

    /**
     * Vincula las columnas de la tabla con las propiedades de {@link Persona}.
     */
    private void vincularColumnas() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colCumple.setCellValueFactory(new PropertyValueFactory<>("cumpleanos"));
        logger.debug("Columnas vinculadas con propiedades de Persona");
    }

    /**
     * Carga todas las personas desde la base de datos de forma asíncrona y actualiza la tabla en e hilo de la interfaz gráfica.
     */
    private void cargarPersonas() {
        pDAO.obtenerTodasPersonas()
            .thenAccept(personas -> Platform.runLater(() -> {
                tablaPersonas.getItems().setAll(personas);
                logger.info("Personas cargadas asincrónicamente: {}", personas.size());
            }))
            .exceptionally(ex -> {
               Platform.runLater(() -> {
                   logger.error("Error al cargar las personas", ex);
                   mandarAlertas(Alert.AlertType.ERROR, "Error", "Fallo al cargar los datos", ex.getMessage());
               });
               return null;
            });
    }

    /**
     * Limpia los campos del formulario para añadir una nueva persona.
     */
    private void limpiarCampos() {
        txtNombre.clear();
        txtApellidos.clear();
        seleCumpleanios.setValue(null);
    }

    /**
     * Muestra una alerta JavaFX con los datos proporcionados.
     *
     * @param tipo Tipo de alerta (INFO, WARNING, ERROR...)
     * @param titulo Título de la ventana de alerta
     * @param mensajeTitulo Texto del encabezado de la alerta
     * @param mensaje Texto del contenido de la alerta
     */
    private void mandarAlertas(Alert.AlertType tipo, String titulo, String mensajeTitulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(mensajeTitulo);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
        logger.debug("Alerta mostrada: tipo={}, titulo={}", tipo, titulo);
    }

    /**
     * Muestra una alerta de confirmación y espera la respuesta del usuario.
     *
     * @param titulo Título de la ventana de alerta
     * @param mensajeTitulo Texto del encabezado de la alerta
     * @param mensaje Texto del contenido de la alerta
     * @return {@code true} si el usuario confirma la acción, {@code false} en caso contrario
     */
    private boolean mandarConfirmacion(String titulo, String mensajeTitulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(mensajeTitulo);
        alerta.setContentText(mensaje);
        Optional<ButtonType> resultado = alerta.showAndWait();
        return resultado.isPresent() && resultado.get() == ButtonType.OK;
    }
}