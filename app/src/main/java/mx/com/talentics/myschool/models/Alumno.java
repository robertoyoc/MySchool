package mx.com.talentics.myschool.models;

import java.util.ArrayList;

/**
 * Created by Roberto on 23/10/2017.
 */

public class Alumno extends Usuario {

    private String matricula;
    private ArrayList<Materia> materias;
    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }



}
