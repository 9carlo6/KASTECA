package com.kasteca.object;

import java.util.ArrayList;
import java.util.Date;

public class Post {

    private String id;
    private String tag;
    private String testo;
    private Corso corso;
    private Date data;
    private ArrayList<Commento> lista_commenti;
    private String link;
    private String pdf;

    //Costruttori
    public Post(String id, String tag, String testo, Corso corso, Date data, String link, String pdf) {
        this.id = id;
        this.tag = tag;
        this.testo = testo;
        this.corso = corso;
        this.data = data;
        this.lista_commenti = new ArrayList<Commento>();
        this.link = link;
        this.pdf = pdf;
    }

    public Post() {
        this.lista_commenti = new ArrayList<Commento>();
    }

    //Metodo per l'aggiunta di un commento
    public void addComment(Commento commento){
        lista_commenti.add(commento);
    }

    //Metodi get

    public String getId() {
        return id;
    }

    public String getTag() {
        return tag;
    }

    public String getTesto() {
        return testo;
    }

    public Corso getCorso() {
        return corso;
    }

    public Date getData() {
        return data;
    }

    public ArrayList<Commento> getLista_commenti() {
        return lista_commenti;
    }

    public String getLink() {
        return link;
    }

    public String getPdf() {
        return pdf;
    }

    //Metodi set

    public void setId(String id) {
        this.id = id;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public void setCorso(Corso corso) {
        this.corso = corso;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public void setLista_commenti(ArrayList<Commento> lista_commenti) {
        this.lista_commenti = lista_commenti;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }
}
