module es.erlantzg {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires jdk.compiler;

    opens es.erlantzg to javafx.fxml;
    exports es.erlantzg;
}
