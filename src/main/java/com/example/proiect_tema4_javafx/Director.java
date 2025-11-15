package com.example.proiect_tema4_javafx;

import java.util.ArrayList;

/**
 * Reprezinta un director in sistemul simulat
 * Aceasta clasa extinde ElementSistem
 * Contine o lista de copii (elemente din interior)
 * Implementeaza metode abstracte
 * */
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

    /**
     * Adauga un element nou (Fisier sau Director) in acest director
     * Seteaza automat parintele elementului
     * @param element Elementul de adaugat
     * */
    public void adaugaElement(ElementSistem element) {
        element.setParinte(this);
        this.copii.add(element);
    }

    /**
     * Sterge un element nou (Fisier sau Director) din acest director
     * Rupe legatura cu parintele
     * @param element Elementul de sters
     * */
    public void stergeElement(ElementSistem element) {
        this.copii.remove(element);
        element.setParinte(null);
    }

    /**
     * Verifica daca acest director contine deja un copil cu numele specificat
     * @param nume Numele de cautat
     * @return true daca numele exista deja, false altfel
     * */
    public boolean areCopilCuNumele(String nume) {
        for(ElementSistem copil : this.copii) {
            if(copil.getNume().equalsIgnoreCase(nume)) {
                return true;
            }
        }
        return false;
    }

}
