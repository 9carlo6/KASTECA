package com.kasteca.object;

import java.util.ArrayList;
import java.util.Date;

public class Commento {

    private String id;
    private String testo;
    private Date data;
    private String post;
    private String proprietarioCommento;



    //Costruttori
    public Commento(String id, String testo, Date data, String post, String proprietarioCommento) {
        this.id = id;
        this.testo = testo;
        this.data = data;
        this.post = post;
        this.proprietarioCommento = proprietarioCommento;
    }

    public Commento(){ }

    //Metodi get

    public String getId() {
        return id;
    }

    public String getTesto() {
        return testo;
    }

    public Date getData() {
        return data;
    }

    public String getPost() { return post; }

    public String getProprietarioCommento() { return proprietarioCommento; }

    //Metodi set

    public void setId(String id) {
        this.id = id;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public void setPost(String post) { this.post = post; }

}
