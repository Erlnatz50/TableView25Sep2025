package es.erlantzg.controladores;

import es.erlantzg.modelos.Persona;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.awt.*;
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

    /**
     * Columna ID en la tabla de personas
     */
    @FXML
    private TableColumn<Persona, Integer> colId;

    /**
     * Columna Nombre en la tabla de personas
     */
    @FXML
    private TableColumn<Persona, String> colNombre;

    /**
     * Columna Apellidos en la tabla de personas
     */
    @FXML
    private TableColumn<Persona, String> colApellidos;

    /**
     * Columna Cumpleaños en la tabla de personas
     */
    @FXML
    private TableColumn<Persona, LocalDate> colCumple;

    /**
     * Selector de fecha para el cumpleaños
     */
    @FXML
    private DatePicker seleCumpleanios;

    /**
     * Tabla que muesra la lista de personas
     */
    @FXML
    private TableView<Persona> tablaPersonas;

    /**
     * Campo de texto para los apellidos
     */
    @FXML
    private TextField txtApellidos;

    /**
     * Campo de texto para el nombre
     */
    @FXML
    private TextField txtNombre;

    /**
     * Inicializa el controlador.
     * Configura las columnas de la tabla para que muestren correctamente las propiedades de {@link Persona}.
     * Limpia la tabla al inicio.
     */
    @FXML
    public void initialize(){
        // Vincular las columnas de la tabla con propiedades de Persona
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colCumple.setCellValueFactory(new PropertyValueFactory<>("cumpleanos"));

        Persona p1 = new Persona(1, "Erlantz", "García", LocalDate.of(1990, 5, 15));
        Persona p2 = new Persona(2, "Ana", "Pérez", LocalDate.of(1985, 3, 20));
        Persona p3 = new Persona(3, "Luis", "Martínez", LocalDate.of(2000, 12, 1));
        tablaPersonas.getItems().addAll(p1, p2, p3);

        //tablaPersonas.getItems().clear();
    }

    /**
     * Metodo que se ejecuta al pulsat el botón "Añadir".
     * Valida que los campos de nombre, apellidos y cumpleaños estén rellenados.
     * Crea un objeto {@link Persona} y lo añade a la tabla.
     * Limpia los campos del formulario tras añadirlos.
     */
    @FXML
    void btnAniadir() {
        String nombre = txtNombre.getText().trim();
        String apellidos = txtApellidos.getText().trim();
        LocalDate cumple = seleCumpleanios.getValue();

        // Comprobar que todos los campos estan reyenados o mostrara una alerta
        if(nombre.isEmpty() || apellidos.isEmpty() || cumple == null){
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Campos incompletos");
            alerta.setHeaderText("Falta información");
            alerta.setContentText("Debes rellenar el nombre, los apellidos y el cumpleaños.");
            alerta.showAndWait();
            return;
        }

        // Crear un objeto Persona y añadirlo a la tabla
        Persona p = new Persona(0, nombre, apellidos, cumple);
        tablaPersonas.getItems().add(p);

        // Vaciar los campos
        txtNombre.clear();
        txtApellidos.clear();
        seleCumpleanios.setValue(null);
    }

    /**
     * Metodo que se ejecuta al pulsar el botón "Eliminar fila seleccionada".
     * Elimina de la tabla la fila seleccionada.
     * Si no hay ninguna fila seleccionada, muestra una alerta.
     */
    @FXML
    void btnEliminarFilaSelec() {
        // Obtener la persona seleccionada
        Persona pSelec = tablaPersonas.getSelectionModel().getSelectedItem();

        // Borrar a la persona si hay alguna seleccion sino saltar una alerta
        if (pSelec != null) {
            tablaPersonas.getItems().remove(pSelec);
        } else {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Seleccionar fila");
            alerta.setHeaderText("No hay ninguna fila seleccionada");
            alerta.setContentText("Debes seleccionar una fila para poder eliminarla.");
            alerta.showAndWait();
        }
    }

    /**
     * Metodo que se ejecuta al pulsar el botón "Restaurar filas".
     * Permite recargar los datos de la tabla desde la BD.
     *
     */
    @FXML
    void btnRestaurarFilas() {
        try {
            // Vaciar toda la tabla
            tablaPersonas.getItems().clear();

            // Obtener todas las personas desde la BD
            //PersonaDAO dao = new PersonaDAO();
            //List<Persona> personas = dao.obtenerTodasPersonas();

            // Añadir cada persona a la tabla
            //tablaPersonas.getItems().addAll(personas);

        } catch (Exception e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error al cargar los datos");
            alerta.setHeaderText("No se ha podido cargar las filas");
            alerta.setContentText(e.getMessage());
            alerta.showAndWait();
        }
    }
}
