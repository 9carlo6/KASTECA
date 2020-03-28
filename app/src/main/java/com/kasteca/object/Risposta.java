package com.kasteca.object;

import java.util.Date;

public class Risposta {

    private String id;
    private Commento commento;
    private Date data;
    private Docente docente;
    private String testo;

    //Costruttori
    public Risposta(String id, Commento commento, Date data, Docente docente, String testo) {
        this.id = id;
        this.commento = commento;
        this.data = data;
        this.docente = docente;
        this.testo = testo;
    }

    public Risposta(){
    }

    //Metodi get

    public String getId() {
        return id;
    }

    public Commento getCommento() {
        return commento;
    }

    public Date getData() {
        return data;
    }

    public Docente getDocente() {
        return docente;
    }

    public String getTesto() {
        return testo;
    }

    //Metodi set

    public void setId(String id) {
        this.id = id;
    }

    public void setCommento(Commento commento) {
        this.commento = commento;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }
}
