package com.kasteca.object;

import java.util.ArrayList;
import java.util.Date;

public class Commento {

    private String id;
    private String testo;
    private Date data;
    private ArrayList<Risposta> lista_risposte;

    //Costruttori
    public Commento(String id, String testo, Date data) {
        this.id = id;
        this.testo = testo;
        this.data = data;
        this.lista_risposte = new ArrayList<Risposta>();
    }

    public Commento(){
        this.lista_risposte = new ArrayList<Risposta>();
    }

    //Metodo per aggiungere una risposta al commento
    public void addRisposta(Risposta risposta){
        lista_risposte.add(risposta);
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

    public ArrayList<Risposta> getLista_risposte() {
        return lista_risposte;
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
