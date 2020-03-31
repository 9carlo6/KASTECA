package com.kasteca.object;

import java.util.ArrayList;
import java.util.Date;

public class Post {

    private String id;
    private String tag;
    private String testo;
    private String corso;
    private Date data;
    private ArrayList<String> lista_commenti;
    private String link;
    private String pdf;

    //Costruttori
    public Post(String id, String tag, String testo, String corso, Date data, String link, String pdf) {
        this.id = id;
        this.tag = tag;
        this.testo = testo;
        this.corso = corso;
        this.data = data;
        this.lista_commenti = new ArrayList<String>();
        this.link = link;
        this.pdf = pdf;
    }

    public Post(String id, String tag, String testo, String corso, Date data, String link, String pdf, ArrayList<String> commenti) {
        this.id = id;
        this.tag = tag;
        this.testo = testo;
        this.corso = corso;
        this.data = data;
        this.lista_commenti = commenti;
        this.link = link;
        this.pdf = pdf;
    }

    public Post() {
        this.lista_commenti = new ArrayList<String>();
    }

    //Metodo per l'aggiunta di un commento
    public void addComment(String commento){
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

    public String getCorso() {
        return corso;
    }

    public Date getData() {
        return data;
    }

    public ArrayList<String> getLista_commenti() {
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

    public void setCorso(String corso) {
        this.corso = corso;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public void setLista_commenti(ArrayList<String> lista_commenti) {
        this.lista_commenti = lista_commenti;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }
}
