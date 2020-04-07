package com.kasteca.object;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

public class Post implements Parcelable, Comparable {

    private String id;
    private String tag;
    private String testo;
    private String corso;
    private Date data;
    private String link;
    private String pdf;

    //Costruttori
    public Post(String id, String tag, String testo, String corso, Date data, String link, String pdf) {
        this.id = id;
        this.tag = tag;
        this.testo = testo;
        this.corso = corso;
        this.data = data;
        this.link = link;
        this.pdf = pdf;
    }

    public Post(){}


    protected Post(Parcel in) {
        id = in.readString();
        tag = in.readString();
        testo = in.readString();
        corso = in.readString();
        link = in.readString();
        pdf = in.readString();
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };


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

    public void setLink(String link) {
        this.link = link;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(tag);
        dest.writeString(testo);
        dest.writeString(corso);
        dest.writeString(link);
        dest.writeString(pdf);
    }

    public String toString(){
        return testo + " " + tag + " " + link + " " + pdf + " " + id;
    }

    @Override
    public int compareTo(Object o) {
        Post postConfronto = (Post) o;
        if(this.data.before(((Post) o).getData())) return 1;
        else if(this.data.after(((Post) o).getData())) return -1;
        else return 0;
    }
}
