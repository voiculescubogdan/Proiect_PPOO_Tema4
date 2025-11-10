package com.example.proiect_tema4_javafx;

import java.io.*;
import java.util.Stack;

public class ManagerMultimedia {

    private Director radacina;
    private String fisiereSalvare = "sistemFisiere.txt";

    public ManagerMultimedia() {
        this.radacina = new Director("/");
        this.radacina.setParinte(null);

        incarcaSistem();
    }

    public Director getRadacina() {
        return radacina;
    }

    public void incarcaSistem() {
        System.out.println("Se incarca sistemul de fisiere..." + fisiereSalvare);

        Stack<Director> stivaParinti = new Stack<>();
        stivaParinti.push(radacina);

        try(BufferedReader br = new BufferedReader(new FileReader(fisiereSalvare))) {

            String linie;
            while((linie = br.readLine()) != null) {
                int nivelIndentare = calculeazaNivelIndentare(linie);
                String continut = linie.trim();

                while(stivaParinti.size() > nivelIndentare + 1) {
                    stivaParinti.pop();
                }

                Director parinteCurent = stivaParinti.peek();

                ElementSistem elementNou = null;

                if(continut.startsWith("DIR:")) {
                    String nume = continut.substring(4);
                    elementNou = new Director(nume);
                } else if(continut.startsWith("FIS:")) {
                    String[] parti = continut.substring(4).split(",");
                    String nume = parti[0];
                    double dimensiune = Double.parseDouble(parti[1]);

                    elementNou = new Fisier(nume, dimensiune);
                }

                if(elementNou != null) {
                    parinteCurent.adaugaElement(elementNou);

                    if(elementNou instanceof Director) {
                        stivaParinti.push((Director) elementNou);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Fisierul de salvare" + fisiereSalvare + "nu a fost gasit");
        } catch (Exception e) {
            System.out.println("Eroare la parsarea fisierului de salvare!");
            e.printStackTrace();
            this.radacina = new Director("/");
        }
    }

    private int calculeazaNivelIndentare(String linie) {
        int numarSpatii = 0;

        while(numarSpatii < linie.length() && linie.charAt(numarSpatii) == ' ') {
            numarSpatii++;
        }

        return numarSpatii / 2;
    }

    public void salveazaSistem() {
        System.out.println("Se salveaza sistemul de fisiere..." + fisiereSalvare);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fisiereSalvare))) {

            for(ElementSistem element : radacina.getCopii()) {
                salveazaElementRecursiv(element, bw, "");
            }

        } catch (IOException e) {
            System.out.println("Eroare la salvarea sistemului: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void salveazaElementRecursiv(ElementSistem element, BufferedWriter bw, String indentare) throws IOException {
        bw.write(indentare);

        if(element instanceof Fisier) {
            Fisier fisier = (Fisier) element;

            bw.write("FIS:" + fisier.getNume() + "," + fisier.getDimensiuneKB());
            bw.newLine();
        } else if(element instanceof Director) {
            Director director = (Director) element;

            bw.write("DIR:" + director.getNume());
            bw.newLine();

            String indentareUrmatoare = indentare + "  ";
            for(ElementSistem copil : director.getCopii()) {
                salveazaElementRecursiv(copil, bw, indentareUrmatoare);
            }
        }
    }
}
