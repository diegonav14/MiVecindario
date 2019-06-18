package com.example.mivecindario.Modelos;


import java.util.List;

public class Evento {

    private String uid;
    private String tipo;
    private String comentario;
    private String fecha;
    private Usuario usuario;
    private List<Usuario> listaAsistentes;

    public Evento(){

    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public List<Usuario> getListaAsistentes() {
        return listaAsistentes;
    }

    public void setListaAsistentes(List<Usuario> listaAsistentes) {
        this.listaAsistentes = listaAsistentes;
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
