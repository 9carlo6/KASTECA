package com.kasteca.object;

import java.util.ArrayList;
import java.util.Date;

public class Commento {

    private String id;
    private String testo;
    private Date data;
    private String Post;
    private String proprietario_commento;

    //Costruttori
    public Commento(String id, String testo, Date data, String post, String proprietario_commento) {
        this.id = id;
        this.testo = testo;
        this.data = data;
        this.Post = post;
        this.proprietario_commento = proprietario_commento;
    }

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

    public String getPost() { return Post; }

    public String getProprietario_commento() { return proprietario_commento; }
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

    public void setPost(String post) { Post = post; }

    public void setProprietario_commento(String proprietario_commento) { this.proprietario_commento = proprietario_commento; }
}
