package com.example.proiect_tema4_javafx;

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
