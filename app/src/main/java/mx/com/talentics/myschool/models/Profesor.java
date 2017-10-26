package mx.com.talentics.myschool.models;

import java.util.ArrayList;

/**
 * Created by Roberto on 23/10/2017.
 */

public class Profesor extends Usuario {
    private String RFC;
    private ArrayList<String> materias;

    public ArrayList<String> getMaterias() {
        return materias;
    }

    public void setMaterias(ArrayList<String> materias) {
        this.materias = materias;
    }

    public String getRFC() {
        return RFC;
    }

    public void setRFC(String RFC) {
        this.RFC = RFC;
    }
}
