package com.kasteca.object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Corso implements Serializable {

    private String nome;
    private String descrizione;
    private String anno_accademico;
    private String idDocente;

    private String codice;      //codice deciso dal docente
    private String id;          //id firebase




    //COSTRUTTORI
    private Corso(){

    }

    public Corso(String nome, String anno_accademico, String descrizione) {
        this.nome = nome;
        this.anno_accademico= anno_accademico;
        this.descrizione = descrizione;
        this.idDocente = idDocente;
        this.codice = codice;
        this.id = Corso.idGenerator();

    }


    public Corso(String nome, String anno_accademico, String descrizione, String idDocente, String codice, String id) {
        this.nome = nome;
        this.anno_accademico= anno_accademico;
        this.descrizione = descrizione;
        this.idDocente = idDocente;
        this.codice = codice;
        this.id = id;

    }


    //metodo statico per la creazione di id
    private static String idGenerator(){
        Random r = new Random();
        String alphabet = "abcdefghijklmnopqrstuvzxy";
        return alphabet.charAt(r.nextInt(25))+alphabet.charAt(r.nextInt(25))+String.valueOf(r.nextInt(1000));
    }










    //METODI DI GET
    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getIdDocente() {
        return idDocente;
    }

    public String getCodice() {
        return codice;
    }

    public String getId() {
        return id;
    }


    public String getAnno_accademico() {
        return anno_accademico;
    }


    @Override
    public String toString() {
        return "Corso{" +
                "nome='" + nome + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", anno_accademico='" + anno_accademico + '\'' +
                ", id-docente=" + idDocente +
                ", codice='" + codice + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
