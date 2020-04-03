package com.kasteca.object;

import java.util.ArrayList;
import java.util.Date;

public class Commento {

    private String id;
    private String testo;
    private Date data;

    //Costruttori
    public Commento(String id, String testo, Date data) {
        this.id = id;
        this.testo = testo;
        this.data = data;
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
}
