package com.kasteca.object;

import java.util.Date;

public class Risposta {

    private String id;
    private Date data;
    private String testo;
    private String proprietario;

    //Costruttori
    public Risposta(String id, Date data, String testo, String proprietario) {
        this.id = id;
        this.data = data;
        this.testo = testo;
        this.proprietario= proprietario;
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

    public String getProprietario() {
        return proprietario;
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

    public void setProprietario(String proprietario) {
        this.proprietario = proprietario;
    }
}
