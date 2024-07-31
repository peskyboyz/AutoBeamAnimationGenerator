module com.example.automationdescriptiongenerator {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.automationdescriptiongenerator to javafx.fxml;
    exports com.example.automationdescriptiongenerator;
}