package org.example.paskolos.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Paskola {
    final private double suma;
    final private double metinisProcentas;
    final private int terminasMen;

    protected Atidejimas atidejimas = null;
    private LocalDate pradziosData;

    protected double menesineImoka = 0.0; // bazinė numatytoji įmoka LAUKU SLEPIMUI
    double bendra = 0;

    public Paskola(double suma, double metinisProcentas, int terminasMet, int terminasMen){
        this.suma = suma;
        this.metinisProcentas = metinisProcentas;
        this.terminasMen = terminasMet * 12 + terminasMen;
    }

    public double getSuma() { return suma; }
    public int getTerminasMenesis() { return terminasMen; }
    public double getMenesinisProcentas() {
        return (metinisProcentas / 100.0) / 12;
    }

    public abstract double skaiciuotiMenesineImoka(int menesisNr);
    public abstract double skaiciuotiAtidejimoMokamaPagrinda();

    public List<VienoMenDuomenys> generuotiGrafika(LocalDate pradziosData) {
        this.pradziosData = pradziosData;
        // sukuria sąrašą kiekvienam mėnesiui
        List<VienoMenDuomenys> sarasas = new ArrayList<>();
        double likutis = getSuma();

        double atidejimoImoka = 0;
        double atidejimoPalukanos = 0;
        double atidejimoMokamasPagrindas = 0;

        if (atidejimas != null) {
            atidejimoPalukanos = getSuma() * getMenesinisProcentas();
            atidejimoMokamasPagrindas = skaiciuotiAtidejimoMokamaPagrinda();
            atidejimoImoka = atidejimoPalukanos + atidejimoMokamasPagrindas;
        }

        int i = 1;
        int realusMenesis = 1;

        while (likutis > 0.01) {
            double imoka;
            double palukanos;
            double pagrindas;

            if (atidejimas != null && atidejimas.arAtideta(i, pradziosData)) {
                imoka = atidejimoImoka;
                palukanos = atidejimoPalukanos;
                pagrindas = atidejimoMokamasPagrindas;
                likutis = likutis - pagrindas;
            } else {
                imoka = skaiciuotiMenesineImoka(realusMenesis);
                palukanos = likutis * getMenesinisProcentas();
                pagrindas = imoka - palukanos;
                // jei pagrindas didesnis už likutį — paskutinis mėnuo
                if (pagrindas > likutis) {
                    pagrindas = likutis;
                    imoka = pagrindas + palukanos;
                }
                likutis = likutis - pagrindas;
                realusMenesis++;
            }

            bendra = bendra + imoka;

            sarasas.add(new VienoMenDuomenys(i, pradziosData, imoka, pagrindas, palukanos, likutis, bendra));
            i++;
        }

        return sarasas;
    }

    public void setAtidejimas(Atidejimas atidejimas) {
        this.atidejimas = atidejimas;
    }
    public LocalDate getPradziosData() { return pradziosData; }
}
