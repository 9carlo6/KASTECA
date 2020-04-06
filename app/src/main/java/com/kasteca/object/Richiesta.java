package com.kasteca.object;

import java.util.Date;

public class Richiesta {
    private String id;
    private String codice_corso;
    private Date data;
    private String stato;
    private Studente studente;

    public Richiesta(String id, String codice_corso, Date data, String stato, Studente studente) {
        this.id = id;
        this.codice_corso = codice_corso;
        this.data = data;
        this.stato = stato;
        this.studente = studente;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodice_corso() {
        return codice_corso;
    }

    public void setCodice_corso(String codice_corso) {
        this.codice_corso = codice_corso;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public Studente getStudente() {
        return studente;
    }

    public void setStudente(Studente studente) {
        this.studente = studente;
    }
}
