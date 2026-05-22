package org.example.paskolos.model;


public class LinijinePaskola extends Paskola{

    public LinijinePaskola(double suma, double metinisProcentas, int terminasMet, int terminasMen){
        super(suma, metinisProcentas, terminasMet, terminasMen);
    }
    protected double menesineImoka;
    @Override
    public double skaiciuotiMenesineImoka(int menesisNr) {
        double p = getMenesinisProcentas();
        int n = getTerminasMenesis();
        double s = getSuma() ;

        final double pagrindas = s / n;
        double likutis = s - (pagrindas * (menesisNr - 1));
        double palukanos = likutis * p;
        this.menesineImoka = pagrindas + palukanos;

        return this.menesineImoka;
    }

    @Override
    public double skaiciuotiAtidejimoMokamaPagrinda() {
        double pagrindas = getSuma() / getTerminasMenesis();
        return pagrindas * (atidejimas.getProcentas() / 100);
    }
}
