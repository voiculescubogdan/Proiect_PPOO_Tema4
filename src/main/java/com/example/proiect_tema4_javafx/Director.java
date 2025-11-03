package com.example.proiect_tema4_javafx;

import java.util.ArrayList;

public class Director extends ElementSistem{

    private ArrayList<ElementSistem> copii;

    public Director(String nume) {
        super(nume);
        this.copii = new ArrayList<>();
    }

    public ArrayList<ElementSistem> getCopii() {
        return copii;
    }

    @Override
    public double getDimensiune() {
        double dimensiuneTotala = 0;

        for(ElementSistem copil : copii) {
            dimensiuneTotala += copil.getDimensiune();
        }
        return dimensiuneTotala;
    }

    @Override
    public String getCale() {
        if(parinte == null) {
            return "/";
        }

        String caleParinte = parinte.getCale();

        if(caleParinte.equals("/")) {
            return caleParinte + nume;
        } else {
            return caleParinte + "/" + nume;
        }
    }

    public void adaugaElement(ElementSistem element) {
        element.setParinte(this);
        this.copii.add(element);
    }

    public void stergeElemenet(ElementSistem element) {
        this.copii.remove(element);
        element.setParinte(null);
    }

}
