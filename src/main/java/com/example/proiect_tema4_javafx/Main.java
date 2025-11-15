package com.example.proiect_tema4_javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Punctul de intrare principal pentru aplicatia JavaFX
 * Clasa initializeaza ManagerMultimedia
 * Incarca interfata FXML si seteaza logica de salvare la inchidere
 * */
public class Main extends Application {

    private ManagerMultimedia manager;

    @Override
    public void init() throws Exception {
        this.manager = new ManagerMultimedia();
    }

    /**
     * Este metoda principala  JavaFX unde se construieste si se afiseaza interfata grafica
     * @param stage Fereastra aplicatiei
     * @throws Exception Daca fisierul FXML nu poate fi incarcat
     * */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("FereastraPrincipala.fxml"));
        Parent root = loader.load();

        FereastraPrincipalaController controller = loader.getController();
        controller.setManager(manager);

        stage.setTitle("Manager Multimedia");
        stage.setScene(new Scene(root, 650, 400));
        stage.show();

        stage.setOnCloseRequest(event -> {
            System.out.println("Se inchide aplicatia");
            manager.salveazaSistem();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
