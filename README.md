# Proyecto JavaFX: Gestión de Personas

## 📖 Descripción
Este proyecto es un ejercicio para crear una aplicación JavaFX que permite gestionar una tabla de personas.
Se pueden añadir, eliminar y restaurar filas en la tabla que muestra personas con sus datos (ID, nombre, apellidos y cumpleaños).
Además, se aplica una hoja de estilos CSS y se registran eventos y errores con SLF4J.

## 📂 Archivos más importantes del proyecto
- **Java:**
  - `App.java`: clase principal que lanza la aplicación y carga la interfaz.
  - `Lanzador.java`: clase para iniciar la aplicación.
  - `Persona.java`: modelo de datos para representar a una persona.
  - `VisualizarCliente.java`: controlador para la vista que gestiona la tabla personas.
  - `PersonaDAO.java`: clase de acceso a datos para interactuar con la base de datos.
  - `Conexion.java`: gestión de conexión a base de datos.
- **CSS:**
  - `estilo.css`: hoja de estilos para la interfaz gráfica.
- **FXML:**
  - `visualizarCliente.fxml`: definición de la interfaz gráfica declarativa.
- **XML:**
  - `logback.xml`: configuración para el sistema de logging SLF4J-Logback.
  - `pom.xml`: archivo de configuración y dependencias de Maven.
- **Manifest:**
  - `MANIFEST.MF`: archivo de manifiesto para empaquetado y ejecución.

## 📈 Funcionalidades principales
- Visualizar tabla de personas con atributos.
- Añadir nuevas personas con ID automático.
- Eliminar la persona seleccionada.
- Restaurar la tabla con datos de ejemplo y reiniciar contador de IDs.
- Manejo de errores y notificaciones para el usuario vía alertas.
- Registro detallado de eventos y errores en archivo log.

## 🚀 Instalación y ejecución
1. Clonar el repositorio.
2. Abrir en un IDE compatible con JavaFX y SLF4J (Java +17 recomendado).
3. Configurar dependencias con Maven (`pom.xml`).
4. Ejecutar la clase lanzadora principal `es.erlantzg.Lanzador`.

## ✨ Autor
- 👤 Erlantz García
