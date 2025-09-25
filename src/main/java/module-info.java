module es.erlantzg {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires jdk.compiler;
    requires java.desktop;

    opens es.erlantzg to javafx.fxml;
    exports es.erlantzg;
}
