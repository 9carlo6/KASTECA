package com.kasteca;

import java.util.ArrayList;
import java.util.Date;

public class Commento {

    private String id;
    private String testo;
    private Studente proprietario;
    private Post post;
    private Date data;
    private ArrayList<Risposta> lista_risposte;

    //Costruttori
    public Commento(String id, String testo, Studente proprietario, Post post, Date data) {
        this.id = id;
        this.testo = testo;
        this.proprietario = proprietario;
        this.post = post;
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

    public Studente getProprietario() {
        return proprietario;
    }

    public Post getPost() {
        return post;
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

    public void setProprietario(Studente proprietario) {
        this.proprietario = proprietario;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
