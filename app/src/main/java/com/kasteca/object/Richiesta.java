package com.kasteca.object;

import java.util.Date;

public class Richiesta {
    private String id;
    private String codiceCorso;
    private Date data;
    private String stato;

    public Richiesta(String id, String codiceCorso, Date data, String stato) {
        this.id = id;
        this.codiceCorso = codiceCorso;
        this.data = data;
        this.stato = stato;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodice_corso() {
        return codiceCorso;
    }

    public void setCodice_corso(String codiceCorso) {
        this.codiceCorso = codiceCorso;
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

}
