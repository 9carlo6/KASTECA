package com.kasteca.object;

import java.util.ArrayList;
import java.util.Random;

public class Corso {

    private String nome;
    private String descrizione;
    private String anno_accademico;
    private String codice;
    private String id;
    private String docente;

    //COSTRUTTORI
    private Corso(){
    }

    public Corso(String nome,String anno_accademico, String descrizione, String codice, String id, String docente) {
        this.nome = nome;
        this.anno_accademico=anno_accademico;
        this.descrizione = descrizione;
        this.codice = codice;
        this.id = id;
        this.docente = docente;
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

    public String getAnno_accademico() {
        return anno_accademico;
    }

    public void setAnno_accademico(String anno_accademico) {
        this.anno_accademico = anno_accademico;
    }

    public String getDocente() {
        return docente;
    }

    public void setDocente(String docente) {
        this.docente = docente;
    }
}
