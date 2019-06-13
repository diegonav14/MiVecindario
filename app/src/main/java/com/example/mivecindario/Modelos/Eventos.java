package com.example.mivecindario.Modelos;

public class Eventos {

    private String tipo;
    private String comentario;

    public Eventos (){

    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    @Override
    public String toString() {
        return comentario;
    }
}
