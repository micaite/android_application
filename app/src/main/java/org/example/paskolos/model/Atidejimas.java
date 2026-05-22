package org.example.paskolos.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Atidejimas {
    private LocalDate pradzia;
    private int trukme;
    private double procentas;

    public Atidejimas(LocalDate pradzia, int trukme, double procentas) {
        this.pradzia = pradzia;
        this.trukme = trukme;
        this.procentas = procentas;
    }

    public boolean arAtideta(int menesisNr, LocalDate pradziosData) {
        int atidejimoMen = (int) ChronoUnit.MONTHS.between(pradziosData, pradzia) + 1;
        return menesisNr >= atidejimoMen
                && menesisNr < atidejimoMen + trukme;
    }

    public double getProcentas() { return procentas; }
}