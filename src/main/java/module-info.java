module com.example.proiect_tema4_javafx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.proiect_tema4_javafx to javafx.fxml;
    exports com.example.proiect_tema4_javafx;
}