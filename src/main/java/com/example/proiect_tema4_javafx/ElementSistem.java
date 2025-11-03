package com.example.proiect_tema4_javafx;

public abstract class ElementSistem {
    protected String nume;
    protected Director parinte;

    public ElementSistem(String nume) {
        this.nume = nume;
        this.parinte = null;
    }

    public String getNume() {
        return nume;
    }

    public Director getParinte() {
        return parinte;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setParinte(Director parinte) {
        this.parinte = parinte;
    }

    public abstract double getDimensiune();

    public abstract String getCale();

    @Override
    public String toString() {
        return this.nume;
    }
}
