package com.kasteca.object;

import java.util.ArrayList;
import java.util.Random;

public class Corso {

    private String nome;
    private String descrizione;
    private String codice;
    private String id;

    //COSTRUTTORI
    private Corso(){
    }

    public Corso(String nome, String descrizione, String codice, String id) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.codice = codice;
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
