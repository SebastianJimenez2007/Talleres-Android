package com.example.taller2sebastianjimenez.entidades;

public class Universidad {
    private int id;
    private String nombre;
    private String www;

    // Constructores
    public Universidad() {
    }

    public Universidad(int id, String nombre, String www) {
        this.id = id;
        this.nombre = nombre;
        this.www = www;
    }

    public Universidad(String nombre, String www) {
        this.nombre = nombre;
        this.www = www;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getWww() {
        return www;
    }

    public void setWww(String www) {
        this.www = www;
    }

    @Override
    public String toString() {
        return id + " - " + nombre + " (" + www + ")";
    }
}
