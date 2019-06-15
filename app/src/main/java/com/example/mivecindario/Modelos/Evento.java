package com.example.mivecindario.Modelos;

public class Evento {

    private String uid;
    private String tipo;
    private String comentario;
    private Usuario usuario;

    public Evento(){

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

    public Usuario getUsuario() {
        return usuario;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }


}
