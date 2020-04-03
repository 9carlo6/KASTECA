package com.kasteca.object;

import java.util.ArrayList;
import java.util.Date;

public class Post {

    private String id;
    private String tag;
    private String testo;
    private Date data;
    private String link;
    private String pdf;

    //Costruttori
    public Post(String id, String tag, String testo, Date data, String link, String pdf) {
        this.id = id;
        this.tag = tag;
        this.testo = testo;
        this.data = data;
        this.link = link;
        this.pdf = pdf;
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

    public Date getData() {
        return data;
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

    public void setData(Date data) {
        this.data = data;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }
}
