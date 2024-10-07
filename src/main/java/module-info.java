module com.example.automationdescgen {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.json;

    opens com.example.automationdescgen to javafx.fxml;
    exports com.example.automationdescgen;
}