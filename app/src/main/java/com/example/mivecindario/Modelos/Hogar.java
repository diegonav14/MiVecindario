package com.example.mivecindario.Modelos;

public class Hogar {

    private String direccion;
    private String nombre;
    private String uid;
    private String comentario;
    private Vecindario vecindario;

    public Hogar (){
    }

    public Vecindario getVecindario() {
        return vecindario;
    }

    public void setVecindario(Vecindario vecindario) {
        this.vecindario = vecindario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
