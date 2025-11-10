package com.example.proiect_tema4_javafx;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class FereastraPrincipalaController {

    @FXML
    private TreeView<ElementSistem> treeView;

    @FXML
    private TextField textFieldNume;

    @FXML
    private TextField textFieldDimensiune;

    @FXML
    private Button butonAdaugaFisier;

    @FXML
    private Button butonAdaugaDirector;

    @FXML
    private Button butonSterge;

    @FXML
    private Label labelCale;

    @FXML
    private Label labelDimensiune;

    private ManagerMultimedia manager;
    private ElementSistem elementSelectat;

    @FXML
    public void initialize() {
        treeView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> gestioneazaSelectie(newValue)
        );
    }

    public void setManager(ManagerMultimedia manager) {
        this.manager = manager;
        refreshTreeView();
    }

    public void refreshTreeView() {
        Director radacinaDate = manager.getRadacina();

        TreeItem<ElementSistem> radacinaVizuala = new TreeItem<>(radacinaDate);

        radacinaVizuala.setExpanded(true);

        buildTreeRecursiv(radacinaDate, radacinaVizuala);

        treeView.setRoot(radacinaVizuala);
    }

    private void buildTreeRecursiv(Director parinteDate, TreeItem<ElementSistem> parinteVizual) {

        for(ElementSistem copilDate : parinteDate.getCopii()) {
            TreeItem<ElementSistem> copilVizual = new TreeItem<>(copilDate);

            parinteVizual.getChildren().add(copilVizual);

            if(copilDate instanceof Director) {
                copilVizual.setExpanded(true);

                buildTreeRecursiv((Director) copilDate, copilVizual);
            }
        }
    }

    @FXML
    private void handleAdaugaFisier() {
        try {
            if(elementSelectat == null) {
                throw new Exception("Nu este selectat niciun element parinte!");
            }

            if(!(elementSelectat instanceof Director)) {
                throw new Exception("Va rugam selectati un Director pentru a adauga in el!");
            }

            Director parinteSelectat = (Director) elementSelectat;

            String numeNou = textFieldNume.getText();
            if(numeNou == null || numeNou.trim().isEmpty()) {
                throw new Exception("Numele noului fisier nu poate fi gol!");
            }

            String dimensiuneText = textFieldDimensiune.getText();
            if(dimensiuneText == null || dimensiuneText.trim().isEmpty()) {
                throw new Exception("Dimensiunea fisierului nu poate fi goala!");
            }

            double dimensiune;
            try {
                dimensiune = Double.parseDouble(dimensiuneText);
            } catch (NumberFormatException e) {
                throw new NumberFormatException("Dimensiunea trebuie sa fie un numar valid!");
            }

            if(dimensiune <= 0) {
                throw new Exception("Dimensiunea trebuie sa fie un numar pozitiv!");
            }

            Fisier fisierNou = new Fisier(numeNou.trim(), dimensiune);
            parinteSelectat.adaugaElement(fisierNou);

            refreshTreeView();

            textFieldNume.clear();
            textFieldDimensiune.clear();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Eroare la adaugare");
            alert.setHeaderText("Nu s-a putut adauga fisierul");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleAdaugaDirector() {
        try {
            if(elementSelectat == null) {
                throw new Exception("Nu este selectat niciun element parinte!");
            }

            if(!(elementSelectat instanceof Director)) {
                throw new Exception("Va rugam selectati un Director pentru a adauga in el!");
            }

            Director parinteSelectat = (Director) elementSelectat;

            String numeNou = textFieldNume.getText();
            if(numeNou == null || numeNou.trim().isEmpty()) {
                throw new Exception("Numele noului director nu poate fi gol!");
            }

            Director directorNou = new Director(numeNou.trim());
            parinteSelectat.adaugaElement(directorNou);

            refreshTreeView();

            textFieldNume.clear();
            textFieldDimensiune.clear();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Eroare la adaugare");
            alert.setHeaderText("Nu s-a putut adauga directorul");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleSterge() {
        try {
            if(elementSelectat == null) {
                throw new Exception("Nu este selectat niciun element parinte!");
            }

            if(elementSelectat.getParinte() == null) {
                throw new Exception("Nu poti sterge directorul radacina al sistemului!");
            }

            Director parinte = elementSelectat.getParinte();
            parinte.stergeElement(elementSelectat);

            refreshTreeView();

            treeView.getSelectionModel().clearSelection();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Eroare la adaugare");
            alert.setHeaderText("Nu s-a putut sterge elementul");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void gestioneazaSelectie(TreeItem<ElementSistem> selectedItem) {
        if(selectedItem != null) {
            this.elementSelectat = selectedItem.getValue();

            labelCale.setText("Cale: " + elementSelectat.getCale());
            labelDimensiune.setText("Dimensiune: " + elementSelectat.getDimensiune());
        } else {
            this.elementSelectat = null;
            labelCale.setText("Cale: -");
            labelDimensiune.setText("Dimensiune: -");
        }
    }
}
