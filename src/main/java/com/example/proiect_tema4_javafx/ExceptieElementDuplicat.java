package com.example.proiect_tema4_javafx;

/**
 * Exceptie custom aruncata atunci cand utilizatorul incearca sa adauge un element
 * cu un nume care exista deja in directorul parinte
 * */
public class ExceptieElementDuplicat extends Exception {

    public ExceptieElementDuplicat(String message) {
        super(message);
    }
}
