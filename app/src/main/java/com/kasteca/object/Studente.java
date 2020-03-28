package com.kasteca.object;

import java.util.ArrayList;

public class Studente {
    private String id;
    private String nome;
    private String cognome;
    private String email;
    private String matricola;
    private ArrayList<String> lista_corsi;

    public Studente(String id, String nome, String cognome, String email, String matricola, ArrayList<String> lista_corsi) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.matricola = matricola;
        this.lista_corsi = lista_corsi;
    }

    public Studente() {
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

    public String getMatricola() {
        return matricola;
    }

    public void setMatricola(String matricola) {
        this.matricola = matricola;
    }

    public ArrayList<String> getLista_corsi() {
        return lista_corsi;
    }

    public void setLista_corsi(ArrayList<String> lista_corsi) {
        this.lista_corsi = lista_corsi;
    }
}
