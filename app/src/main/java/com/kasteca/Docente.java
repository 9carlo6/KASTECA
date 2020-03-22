package com.kasteca;

import java.util.ArrayList;

public class Docente {
    private String id;
    private String nome;
    private String cognome;
    private String email;
    private ArrayList<String> lista_corsi;

    public Docente(String id, String nome, String cognome, String email, ArrayList<String> lista_corsi) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.lista_corsi = lista_corsi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<String> getLista_corsi() {
        return lista_corsi;
    }

    public void setLista_corsi(ArrayList<String> lista_corsi) {
        this.lista_corsi = lista_corsi;
    }
}
