package org.example.paskolos.model;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Ataskaita {

    public void eksportuotiCSV(List<VienoMenDuomenys> duomenys, String failoPavadinimas) throws IOException {
        FileWriter fw = new FileWriter(failoPavadinimas);

        fw.write("Mėnuo,Įmoka,Pagrindas,Palūkanos,Likutis\n");

        for (VienoMenDuomenys e : duomenys) {
            fw.write(e.getData() + "," +
                    e.getImokaStr() + "," +
                    e.getPagrindasStr() + "," +
                    e.getPalukanosStr() + "," +
                    e.getLikutisStr() + "\n");
        }

        fw.close();
    }
}