module com.example.library {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.zaxxer.hikari;
    requires java.sql;

    opens Views to javafx.fxml;
    opens Views.Login to javafx.fxml;
    opens Views.Coordinator to javafx.fxml;
    opens Views.Reader to javafx.fxml;
    opens Views.Manager to javafx.fxml;
    opens Views.Employee to javafx.fxml;
    opens Classes.Manager.Util to javafx.base;
    opens Classes.Employee.Util to javafx.base;
}