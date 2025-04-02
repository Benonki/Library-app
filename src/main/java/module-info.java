module com.example.library {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.zaxxer.hikari;
    requires java.sql;



    opens Views to javafx.fxml;
    exports Views;
    exports Views.Login;
    opens Views.Login to javafx.fxml;
    exports Views.Main;
    opens Views.Main to javafx.fxml;
}