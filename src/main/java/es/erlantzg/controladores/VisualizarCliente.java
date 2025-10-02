package es.erlantzg.controladores;

import es.erlantzg.dao.PersonaDAO;
import es.erlantzg.modelos.Persona;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Controlador de la vista "visualizarCliente.fxml".
 * Gestiona la interacción entre la interfaz gráfica (FXML) y los datos de tipo {@link Persona}
 * Permite añadir, eliminar y restaurar filas en una tabla de personas.
 *
 * @author Erlantz García
 * @version 1.0
 */
public class VisualizarCliente {

    /** Columnas que muetra el ID de la persona en la tabla */
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

    /** DAO para acceder y manipular la entidad Persona en la base de datos */
    private PersonaDAO pDAO;

    /**
     * Inicializa el controlador.
     * Configura las columnas de la tabla para enlazar con las propiedades de {@link Persona} y carga las personas desde la base de datos
     */
    @FXML
    public void initialize(){
        pDAO = new PersonaDAO();
        logger.info("Inicializando controlador VisualizarCliente");
        vincularColumnas();
        aniadirPersonasTabla();
    }

    /**
     * Metodo que se ejecuta al pulsar el botón "Añadir".
     * Válida que todos los campos estén completos.
     * Crea un objeto {@link Persona}, lo añade a la tabla y a la base de datos.
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

        try {
            pDAO.insertarPersona(p);

            tablaPersonas.getItems().add(p);

            logger.info("Persona añadida: id={}, nombre={}, apellidos={}, cumpleaños={}", p.getId(), p.getNombre(), p.getApellidos(), p.getCumpleanos());

            limpiarCampos();

        } catch (RuntimeException e) {
            logger.error("Error al insertar persona: {}", e.getMessage());
            mandarAlertas(Alert.AlertType.ERROR, "Error al isertar", "No se ha podido insertar la persona", "Comprueba la conexión con la base de datos y vuelve a intentarlo");
        }
    }

    /**
     * Metodo que se ejecuta al pulsar el botón "Eliminar fila seleccionada".
     * Solicita confirmación antes de eliminar.
     * Elimina la persona seleccionada de la base de datos y actualiza la tabla.
     * Muestra alertas cuando ocurra algún error.
     */
    @FXML
    void btnEliminarFilaSelec() {
        Persona pSelec = tablaPersonas.getSelectionModel().getSelectedItem();

        if (pSelec != null) {
            boolean confirmado = mandarConfirmacion("Confirmar eliminación", "Estas seguro que deseas eliminar la persona seleccionada?", "Nombre: " + pSelec.getNombre() + " " + pSelec.getApellidos());

            if (confirmado) {
                try {
                    pDAO.eliminarPersona(pSelec.getId());

                    tablaPersonas.getItems().remove(pSelec);

                    logger.info("Persona eliminada: id={}, nombre={}, apellidos={}, cumpleaños={}", pSelec.getId(), pSelec.getNombre(), pSelec.getApellidos(), pSelec.getCumpleanos());

                } catch (RuntimeException e) {
                    logger.error("Error al eliminar la persona id = {}: {}", pSelec.getId(), e.getMessage());
                    mandarAlertas(Alert.AlertType.ERROR, "Error al eliminar", "No se pudo eliminar la persona seleccionada", "Comprueba la conexión con la base de datos y vuelve a intentarlo");
                }
            } else {
                logger.info("Eliminación cancelada por el usuario");
            }
        } else {
            logger.warn("Intento eliminar una fila sin selección");
            mandarAlertas(Alert.AlertType.WARNING, "Seleccionar fila", "No hay ninguna fila seleccionada", "Debes seleccionar una fila para poder eliminarla.");
        }
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
            aniadirPersonasTabla();
            logger.info("Tabla restaurada con personas");
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
     * Carga las personas desde la base de datos y las añade a la tabla.
     */
    private void aniadirPersonasTabla() {
        List<Persona> personas = pDAO.obtenerTodasPersonas();
        for (Persona p : personas) {
            tablaPersonas.getItems().add(p);
        }
        logger.debug("Personas de ejemplo añadidas a la tabla");
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