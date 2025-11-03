package com.example.proiect_tema4_javafx;

public class Fisier extends ElementSistem{

    private double dimensiuneKB;

    public Fisier(String nume, double dimensiuneKB) {
        super(nume);
        this.dimensiuneKB = dimensiuneKB;
    }

    public double getDimensiuneKB() {
        return dimensiuneKB;
    }

    @Override
    public double getDimensiune() {
        return this.dimensiuneKB;
    }

    @Override
    public String getCale() {
        if(parinte == null) {
            return "/" + nume;
        }

        String caleParinte = parinte.getCale();

        if(caleParinte.equals("/")) {
            return caleParinte + nume;
        } else {
            return caleParinte + "/" + nume;
        }
    }
}
