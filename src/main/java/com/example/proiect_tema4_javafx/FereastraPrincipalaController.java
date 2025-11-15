package com.example.proiect_tema4_javafx;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;

/**
 * Controller-ul JavaFX pentru fereastra principala
 * Gestioneaza interactiunile utilizatorului si comunica cu ManagerMultimedia
 * */
public class FereastraPrincipalaController {

    @FXML
    private TreeView<ElementSistem> treeView;

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

    /**
     * Metoda speciala apelata de JavaFX dupa ce componentele FXML au fost injectate
     * Aici setam un listener pentru TreeView
     * */
    @FXML
    public void initialize() {
        treeView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> gestioneazaSelectie(newValue)
        );
    }

    /**
     * Metoda apelata din Main pentru a injecta logica in controller
     * Populeaza arborele vizual
     * @param manager Instanta ManagerMultimedia
     * */
    public void setManager(ManagerMultimedia manager) {
        this.manager = manager;
        refreshTreeView();
    }

    /**
     * Goleste si repopuleaza intregul TreeView pe baza datelor curente din manager
     * Aceasta metoda este apelata dupa fiecare operatie
     * */
    public void refreshTreeView() {
        Director radacinaDate = manager.getRadacina();

        TreeItem<ElementSistem> radacinaVizuala = new TreeItem<>(radacinaDate);

        radacinaVizuala.setExpanded(true);

        buildTreeRecursiv(radacinaDate, radacinaVizuala);

        treeView.setRoot(radacinaVizuala);
    }

    /**
     * Metoda ajutatoare recursiva pentru a construi arborele de TreeItem-uri vizuale pe baza arborelui de date
     * @param parinteDate Nodul de date (Director), procesam copiii
     * @param parinteVizual Nodul vizual (TreeItem) la care adaugam noii copii
     * */
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

    /**
     * Apelat cand utilizatorul apasa butonul "Adauga Fisier"
     * Afiseaza un dialog customizat pentru a cere nume, tip si dimensiune
     * Valideaza datele si adauga un nou Fisier
     * */
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

            Dialog<RezultatDialogFisier> dialog = new Dialog<>();
            dialog.setTitle("Adauga Fisier Nou: ");
            dialog.setHeaderText("Adaugare in: " + parinteSelectat.getCale());

            ButtonType butonAdauga = new ButtonType("Adauga", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(butonAdauga, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);

            TextField fieldNume = new TextField();
            fieldNume.setPromptText("Nume");
            TextField fieldDimensiune = new TextField();
            fieldDimensiune.setPromptText("Dimensiune (KB)");
            ChoiceBox<String> comboTip = new ChoiceBox<>(
                    FXCollections.observableArrayList(".mp3", ".wav", ".jpg", ".png")
            );
            comboTip.setValue(".mp3");

            grid.add(new Label("Nume: "), 0, 0);
            grid.add(fieldNume, 1, 0);
            grid.add(comboTip, 2, 0);
            grid.add(new Label("Dimensiune: "), 0, 1);
            grid.add(fieldDimensiune, 1, 1);

            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(tipButon -> {
                if(tipButon == butonAdauga) {
                    try {
                        String nume = fieldNume.getText().trim() + comboTip.getValue();
                        double dim = Double.parseDouble(fieldDimensiune.getText());

                        if(nume.trim().isEmpty() || comboTip.getValue() == null) {
                            return null;
                        }

                        return new RezultatDialogFisier(nume, dim);
                    } catch (NumberFormatException e) {
                        return null;
                    }
                }
                return null;
            });

            Optional<RezultatDialogFisier> rezultat = dialog.showAndWait();

            if(rezultat.isPresent() && rezultat.get() != null) {
                RezultatDialogFisier dateFisier = rezultat.get();

                if(parinteSelectat.areCopilCuNumele(dateFisier.getNume())) {
                    throw new ExceptieElementDuplicat("Un element cu numele: " + dateFisier.getNume() + " exista deja!");
                }

                if(dateFisier.getDimensiune() <= 0) {
                    throw new Exception("Dimensiunea trebuie sa fie pozitiva!");
                }

                Fisier fisierNou = new Fisier(dateFisier.getNume(), dateFisier.getDimensiune());
                parinteSelectat.adaugaElement(fisierNou);

                refreshTreeView();
            } else if (rezultat.isPresent()) {
                throw new Exception("Date invalide! Numele nu poate fi gol si dimensiunea trebuie sa fie un numar!");
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Eroare la adaugare");
            alert.setHeaderText("Nu s-a putut adauga fisierul");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Apelat cand utilizatorul apasa butonul "Adauga Director"
     * Valideaza selectia, cere un nume nou printr-un dialog si adauga un nou Director
     * */
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

            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Adauga director nou");
            dialog.setHeaderText("Adaugare in: " + parinteSelectat.getCale());
            dialog.setContentText("Numele directorului: ");

            Optional<String> rezultat = dialog.showAndWait();

            if(rezultat.isPresent() && !rezultat.get().trim().isEmpty()) {
                String numeNou = rezultat.get().trim();

                if(parinteSelectat.areCopilCuNumele(numeNou)) {
                    throw new ExceptieElementDuplicat("Un element cu numele: " + numeNou + " exista deja!");
                }

                Director directorNou = new Director(numeNou);
                parinteSelectat.adaugaElement(directorNou);

                refreshTreeView();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Eroare la adaugare");
            alert.setHeaderText("Nu s-a putut adauga directorul");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Apelat cand utilizatorul apasa butonul "Sterge"
     * Valideaza selectia si sterge elementul selectat
     * */
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

    /**
     * Apelat cand utilizatorul apasa butonul "Muta element"
     * Cere o cale de destinatie, valideaza si muta elementul in noul parinte
     * */
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

    /**
     * Metoda ajutatoare care cauta un ElementSistem in arbore pe baza caii sale complete
     * @param cale Calea de cautat
     * @return ElementSistem gasit, sau null daca nu exista
     * */
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

    /**
     * Apelat de fiecare data cand utilizatorul selecteaza un alt element in TreeView
     * Actualizeaza label-urile de informatii (cale si dimensiune)
     * @param selectedItem Noul TreeItem selectat
     * */
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
