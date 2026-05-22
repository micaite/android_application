package org.example.paskolos.model;
import java.time.LocalDate;

public class VienoMenDuomenys {
    // TableView reikalauja kad kiekviena eilutė būtų vienas objektas. Negalima atskirai duoti 5 sąrašų

    private int menesisNr;
    private LocalDate data;
    private double imoka;
    private double pagrindas;
    private double palukanos;
    private double likutis;
    private double bendra;


    public VienoMenDuomenys(int menesisNr, LocalDate pradziosData, double imoka, double pagrindas, double palukanos, double likutis, double bendra){
        this.menesisNr = menesisNr;
        this.data = pradziosData.plusMonths(menesisNr - 1);
        this.imoka = imoka;
        this.pagrindas = pagrindas;
        this.palukanos = palukanos;
        this.likutis = likutis;
        this.bendra = bendra;
    }

    // skaiciavimams
    public int getMenesisNr(){ return menesisNr;}
    public LocalDate getData() { return data; }
    public double getImoka(){return imoka;}
    public double getPagrindas(){return pagrindas;}
    public double getPalukanos(){return palukanos;}
    public double getLikutis(){return likutis;}
    public double getBendra(){return bendra;}

    // rodymui ir apvalinimui
    public String getImokaStr() {return String.format("%.2f €", imoka);}
    public String getPagrindasStr() {return String.format("%.2f €", pagrindas);}
    public String getPalukanosStr() {return String.format("%.2f €", palukanos);}
    public String getLikutisStr() {return String.format("%.2f €", likutis);}
    public String getBendraStr() {return String.format("%.2f €", bendra);}
}
