package com.example.proiect_tema4_javafx;

/**
 * Un fisier individual in sistemul simulat
 * Aceasta clasa extinde ElementSistem
 * Implementeaza metode abstracte pentru calcularea dimensiunii si a caii
 * */
public class Fisier extends ElementSistem{

    private double dimensiuneKB;

    public Fisier(String nume, double dimensiuneKB) {
        super(nume);
        this.dimensiuneKB = dimensiuneKB;
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
