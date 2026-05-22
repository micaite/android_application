package org.example.paskolos.model;

import static java.lang.Math.pow;

public class AnuitetinePaskola extends Paskola{

    public AnuitetinePaskola(double suma, double metinisProcentas, int terminasMet, int terminasMen){
        super(suma, metinisProcentas, terminasMet, terminasMen);
    }

    protected double menesineImoka;
    @Override
    public double skaiciuotiMenesineImoka(int menesisNr) {
        double r = getMenesinisProcentas();
        int n = getTerminasMenesis();
        double s = getSuma() ;

        this.menesineImoka = ( s * r * pow((1 + r), n) ) / (pow((1 + r), n) - 1);
        return this.menesineImoka;
    }

    @Override
    public double skaiciuotiAtidejimoMokamaPagrinda() {
        double r = getMenesinisProcentas();
        int n = getTerminasMenesis();
        double s = getSuma();
        double imoka = (s * r * Math.pow(1+r, n)) / (Math.pow(1+r, n) - 1);
        double palukanos = s * r;
        return (imoka - palukanos) * (atidejimas.getProcentas() / 100);
    }
}
