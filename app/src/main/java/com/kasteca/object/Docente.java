package com.kasteca.object;

import java.io.Serializable;
import java.util.ArrayList;

public class Docente implements Serializable {
    private String id;
    private String nome;
    private String cognome;
    private String email;
    private ArrayList<Corso> lista_corsi= null;     //Per evitare accessi sbagliati e per verificare che sia riempita correttamente.

    public Docente(String id, String nome, String cognome, String email, ArrayList<Corso> lista_corsi) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.lista_corsi = lista_corsi;
    }

    public Docente() {
    }

    //metodo per l'aggiunta dei corsi nella classe studente
    public void addCorso(Corso corso){
        if(lista_corsi!=null)
            lista_corsi.add(corso);
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

    public ArrayList<Corso> getLista_corsi() {
        return lista_corsi;
    }

    public void setLista_corsi(ArrayList<Corso> lista_corsi) {
        this.lista_corsi = lista_corsi;
    }

    @Override
    public String toString() {
        if(lista_corsi!=null)
            return "Docente{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", email='" + email + '\'' +
                ", numero corsi=" + lista_corsi.size() +
                '}';
        else
            return "Docente{" +
                    "id='" + id + '\'' +
                    ", nome='" + nome + '\'' +
                    ", cognome='" + cognome + '\'' +
                    ", email='" + email + '\'' +
                    ", numero corsi=" + "LISTA VUOTA" +
                    '}';

    }
}
