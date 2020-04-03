package com.kasteca.object;

import java.util.Date;

public class Risposta {

    private String id;
    private String commento;
    private Date data;
    private String testo;

    //Costruttori
    public Risposta(String id, String commento, Date data , String testo) {
        this.id = id;
        this.commento = commento;
        this.data = data;
        this.testo = testo;
    }

    public Risposta(){
    }

    //Metodi get

    public String getId() {
        return id;
    }


    public Date getData() {
        return data;
    }


    public String getTesto() {
        return testo;
    }

    //Metodi set

    public void setId(String id) {
        this.id = id;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }
}
