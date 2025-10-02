package es.erlantzg.controladores;

import es.erlantzg.modelos.Persona;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

/**
 * Controlador de la vista "visualizarCliente.fxml".
 * Gestiona la interacción entre la interfaz gráfica (FXML) y los datos de tipo {@link Persona}
 * Permite añadir, eliminar y restaurar filas en una tabla de personas.
 *
 * @author Erlantz García
 * @version 1.0
 */
public class VisualizarCliente {

    /** Columna ID en la tabla de personas */
    @FXML
    private TableColumn<Persona, Integer> colId;

    /** Columna Nombre en la tabla de personas */
    @FXML
    private TableColumn<Persona, String> colNombre;

    /** Columna Apellidos en la tabla de personas */
    @FXML
    private TableColumn<Persona, String> colApellidos;

    /** Columna Cumpleaños en la tabla de personas */
    @FXML
    private TableColumn<Persona, LocalDate> colCumple;

    /** Selector de fecha para el cumpleaños */
    @FXML
    private DatePicker seleCumpleanios;

    /** Tabla que muestra la lista de personas */
    @FXML
    private TableView<Persona> tablaPersonas;

    /** Campo de texto para los apellidos */
    @FXML
    private TextField txtApellidos;

    /** Campo de texto para el nombre */
    @FXML
    private TextField txtNombre;

    /** Logger para esta clase */
    private static final Logger logger = LoggerFactory.getLogger(VisualizarCliente.class);

    /**
     * Inicializa el controlador.
     * - Configura las columnas de la tabla para que muestren correctamente las propiedades de {@link Persona}.
     * - Carga personas de ejemplo en la tabla.
     */
    @FXML
    public void initialize(){
        logger.info("Inicializando controlador VisualizarCliente");
        vincularColumnas();
        aniadirPersonasTabla();
    }

    /**
     * Metodo que se ejecuta al pulsar el botón "Añadir".
     * - Válida que los campos de nombre, apellidos y cumpleaños estén rellenados.
     * - Crea un objeto {@link Persona} y lo añade a la tabla.
     * - Asigna automáticamente un ID único a la persona añadida.
     * - Limpia los campos del formulario tras añadirlos.
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
        tablaPersonas.getItems().add(p);
        logger.info("Persona añadida: id={}, nombre={}, apellidos={}, cumpleaños={}", p.getId(), p.getNombre(), p.getApellidos(), p.getCumpleanos());

        limpiarCampos();
    }

    /**
     * Metodo que se ejecuta al pulsar el botón "Eliminar fila seleccionada".
     * Elimina de la tabla la fila seleccionada.
     * Si no hay ninguna fila seleccionada, muestra una alerta.
     */
    @FXML
    void btnEliminarFilaSelec() {
        Persona pSelec = tablaPersonas.getSelectionModel().getSelectedItem();

        if (pSelec != null) {
            tablaPersonas.getItems().remove(pSelec);
            logger.info("Persona eliminada: id={}, nombre={}, apellidos={}, cumpleaños={}", pSelec.getId(), pSelec.getNombre(), pSelec.getApellidos(), pSelec.getCumpleanos());
        } else {
            logger.warn("Intento eliminar una fila sin selección");
            mandarAlertas(Alert.AlertType.WARNING, "Seleccionar fila", "No hay ninguna fila seleccionada", "Debes seleccionar una fila para poder eliminarla.");
        }
    }

    /**
     * Metodo que se ejecuta al pulsar el botón "Restaurar filas".
     * - Limpia todas las finas de la tabla.
     * - Vuelve a cargar las personas de ejemplo.
     */
    @FXML
    void btnRestaurarFilas() {
        try {
            tablaPersonas.getItems().clear();
            Persona.resetearContador(0);
            aniadirPersonasTabla();
            logger.info("Tabla restaurada con personas de ejemplo");
        } catch (Exception e) {
            logger.error("Error al restaurar filas: {}", e.getMessage());
            mandarAlertas(Alert.AlertType.ERROR,"Error al cargar los datos", "No se ha podido cargas las filas", e.getMessage());
        }
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
     * Añade personas de ejemplo a la tabla.
     */
    private void aniadirPersonasTabla() {
        Persona p1 = new Persona("Miguel", "De La Fuente", LocalDate.of(2000, 4, 14));
        Persona p2 = new Persona("Ana", "Pérez", LocalDate.of(1999, 3, 25));
        Persona p3 = new Persona("Angela", "Lopez", LocalDate.of(2003, 9, 1));
        tablaPersonas.getItems().addAll(p1, p2, p3);
        logger.debug("Personas de ejemplo añadidas a la tabla");
    }

    /**
     * Limpia los campos del formulario
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
}
