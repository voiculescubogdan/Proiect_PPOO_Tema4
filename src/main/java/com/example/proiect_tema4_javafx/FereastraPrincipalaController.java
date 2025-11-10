package com.example.proiect_tema4_javafx;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Optional;

public class FereastraPrincipalaController {

    @FXML
    private TreeView<ElementSistem> treeView;

    @FXML
    private TextField textFieldNume;

    @FXML
    private TextField textFieldDimensiune;

    @FXML
    private ChoiceBox<String> choiceBoxTipFisier;

    @FXML
    private Button butonAdaugaFisier;

    @FXML
    private Button butonAdaugaDirector;

    @FXML
    private Button butonSterge;

    @FXML
    private Button butonMuta;

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

        choiceBoxTipFisier.setItems(FXCollections.observableArrayList(".mp3", ".wav", ".jpg", ".png", ".jpeg", ".txt"));

        choiceBoxTipFisier.setValue(".mp3");
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

            String numeBaza = textFieldNume.getText();
            if(numeBaza == null || numeBaza.trim().isEmpty()) {
                throw new Exception("Numele noului fisier nu poate fi gol!");
            }

            String extensie = choiceBoxTipFisier.getValue();
            if(extensie == null) {
                throw new Exception("Tipul fisierului nu este selectat!");
            }

            String numeComplet = numeBaza.trim() + extensie;

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

            Fisier fisierNou = new Fisier(numeComplet, dimensiune);
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

    @FXML
    private void handleMuta() {
        try {
            if(elementSelectat == null) {
                throw new Exception("Nu ai selectat niciun element de mutat!");
            }

            if(elementSelectat.getParinte() == null) {
                throw new Exception("Nu poti muta directorul radacina!");
            }

            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Mutare Element");
            dialog.setHeaderText("Mutare: " + elementSelectat.getNume());
            dialog.setContentText("Introduceti calea directorului destinatie: ");

            Optional<String> rezultat = dialog.showAndWait();

            if(!rezultat.isPresent() || rezultat.get().trim().isEmpty()) {
                throw new Exception("Operatie de mutare anulata sau calea este goala!");
            }

            String caleDestinatie = rezultat.get().trim();

            ElementSistem destinatie = gasesteElementDupaCale(caleDestinatie);

            if(destinatie == null) {
                throw new Exception("Calea destinatie: " + caleDestinatie + " nu a fost gasita!");
            }

            if(!(destinatie instanceof Director)) {
                throw new Exception("Destinatia trebuie sa fie un Director");
            }

            if(destinatie.getCale().startsWith(elementSelectat.getCale())) {
                throw new Exception("Nu poti muta un director in interiorul lui insusi!");
            }

            Director parinteVechi = elementSelectat.getParinte();
            Director parinteNou = (Director) destinatie;

            parinteVechi.stergeElement(elementSelectat);
            parinteNou.adaugaElement(elementSelectat);

            refreshTreeView();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Eroare la mutare");
            alert.setHeaderText("Nu s-a putut muta elementul");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private ElementSistem gasesteElementDupaCale(String cale) {
        if(cale == null || cale.isEmpty()) {
            return null;
        }

        Director parinteCurent = manager.getRadacina();

        if(cale.equals("/")) {
            return parinteCurent;
        }

        String[] parti = cale.split("/");

        for(int i = 0; i < parti.length; i++) {
            String numeParte = parti[i];
            ElementSistem gasit = null;

            for(ElementSistem copil : parinteCurent.getCopii()) {
                if(copil.getNume().equals(numeParte)) {
                    gasit = copil;
                    break;
                }
            }

            if(gasit == null) {
                return null;
            }

            if(i < parti.length - 1) {
                if(gasit instanceof Director) {
                    parinteCurent = (Director) gasit;
                } else {
                    return null;
                }
            } else {
                return gasit;
            }
        }

        return null;
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
