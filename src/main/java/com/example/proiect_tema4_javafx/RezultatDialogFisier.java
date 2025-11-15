package com.example.proiect_tema4_javafx;

/**
 * O clasa simpla pentru a returna multiple valori din dialogul customizat de adaugare a unui fisier
 * */
public class RezultatDialogFisier {
    private String nume;
    private double dimensiune;

    public RezultatDialogFisier(String nume, double dimensiune) {
        this.nume = nume;
        this.dimensiune = dimensiune;
    }

    public String getNume() {
        return nume;
    }

    public double getDimensiune() {
        return dimensiune;
    }
}
