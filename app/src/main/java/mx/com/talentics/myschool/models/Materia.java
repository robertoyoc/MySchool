package mx.com.talentics.myschool.models;

import java.util.ArrayList;

/**
 * Created by Roberto on 23/10/2017.
 */

public class Materia {
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    private String nombre;
    private String periodo;
    private Profesor profesor;
    private ArrayList<Alumno> alumnos;
    private int gpo;

    public int getGpo() {
        return gpo;
    }

    public void setGpo(int gpo) {
        this.gpo = gpo;
    }



    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }


    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public ArrayList<Alumno> getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(ArrayList<Alumno> alumnos) {
        this.alumnos = alumnos;
    }
}
