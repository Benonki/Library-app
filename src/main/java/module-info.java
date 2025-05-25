module com.example.library {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.zaxxer.hikari;
    requires org.slf4j;
    requires ch.qos.logback.classic;

    // EXPORTY
    exports Views;
    exports Views.Login;
    exports Views.Coordinator;
    exports Views.Reader;
    exports Views.Manager;
    exports Views.Employee;

    // OPENY dla JavaFX
    opens Views to javafx.fxml;
    opens Views.Login to javafx.fxml;
    opens Views.Coordinator to javafx.fxml;
    opens Views.Reader to javafx.fxml;
    opens Views.Manager to javafx.fxml;
    opens Views.Employee to javafx.fxml;

    // OPENY dla bindingów JavaFX (np. tabel)
    opens Classes.Manager to javafx.base;
    opens Classes.Manager.Util to javafx.base;
    opens Classes.Employee to javafx.base;
    opens Classes.Coordinator to javafx.base;
    opens Classes.Reader to javafx.base;

    // USUNIĘTO: opens Classes to javafx.base; // <- jeśli nie istnieje pakiet "Classes"
}
