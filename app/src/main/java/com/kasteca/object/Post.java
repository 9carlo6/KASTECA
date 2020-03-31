package com.kasteca.object;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

public class Post implements Parcelable {

    private String id;
    private String tag;
    private String testo;
    private String corso;
    private Date data;
    private ArrayList<String> lista_commenti;
    private String link;
    private Uri pdf;

    //Costruttori
    public Post(String id, String tag, String testo, String corso, Date data, String link, Uri pdf) {
        this.id = id;
        this.tag = tag;
        this.testo = testo;
        this.corso = corso;
        this.data = data;
        this.lista_commenti = new ArrayList<String>();
        this.link = link;
        this.pdf = pdf;
    }

    public Post(String id, String tag, String testo, String corso, Date data, String link, Uri pdf, ArrayList<String> commenti) {
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

    protected Post(Parcel in) {
        id = in.readString();
        tag = in.readString();
        testo = in.readString();
        corso = in.readString();
        lista_commenti = in.createStringArrayList();
        link = in.readString();
        pdf = Uri.parse(in.readString());
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

    public Uri getPdf() {
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

    public void setPdf(Uri pdf) {
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
        dest.writeStringList(lista_commenti);
        dest.writeString(link);
        dest.writeString(pdf.toString());
    }
}
