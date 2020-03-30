package com.kasteca.object;

import java.util.ArrayList;
import java.util.Random;

public class Corso {

    private String nome;
    private String descrizione;
    private String anno_accademico;
    private Docente docente;
    //id e codice potrebbero essere la stessa cosa?
    private String codice;      //codice deciso dal docente
    private String id;          //id firebase
    private ArrayList<Post> posts;
    private ArrayList<Studente> studenti;




    //COSTRUTTORI
    private Corso(){

    }

    public Corso(String nome, String anno_accademico, String descrizione, Docente docente, String codice) {
        this.nome = nome;
        this.anno_accademico= anno_accademico;
        this.descrizione = descrizione;
        this.docente = docente;
        this.codice = codice;
        this.id = Corso.idGenerator();
        this.posts= new ArrayList<Post>();
        this.studenti= new ArrayList<Studente>();
    }

    public Corso(String nome, String anno_accademico, String descrizione, Docente docente, String codice, String id) {
        this.nome = nome;
        this.anno_accademico= anno_accademico;
        this.descrizione = descrizione;
        this.docente = docente;
        this.codice = codice;
        this.id = id;
        this.posts= new ArrayList<Post>();
        this.studenti= new ArrayList<Studente>();
    }

    public Corso(String nome, String anno_accademico, String descrizione, Docente docente, String codice, String id, ArrayList<Post> posts, ArrayList<Studente> studenti) {
        this.nome = nome;
        this.anno_accademico= anno_accademico;
        this.descrizione = descrizione;
        this.docente = docente;
        this.codice = codice;
        this.id = id;
        this.posts = posts;
        this.studenti = studenti;
    }


    //metodo statico per la creazione di id
    private static String idGenerator(){
        Random r = new Random();
        String alphabet = "abcdefghijklmnopqrstuvzxy";
        return alphabet.charAt(r.nextInt(25))+alphabet.charAt(r.nextInt(25))+String.valueOf(r.nextInt(1000));
    }


    //Metodi per aggiunta studenti e posts
    public void addStudente(Studente s){
        try{
            studenti.add(s);
        }catch(NullPointerException e){
            System.err.print(e.toString());
            System.err.print("ArrayList degli studenti ancora non inizializzata o null.");
        }
    }

    public void addPost(Post p){
        try{
            posts.add(p);
        }catch(NullPointerException e){
            System.err.print(e.toString());
            System.err.print("ArrayList dei post ancora non inizializzata o null.");
        }
    }






    //METODI DI GET
    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public Docente getDocente() {
        return docente;
    }

    public String getCodice() {
        return codice;
    }

    public String getId() {
        return id;
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public ArrayList<Studente> getStudenti() {
        return studenti;
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
                ", docente=" + docente +
                ", codice='" + codice + '\'' +
                ", id='" + id + '\'' +
                ", posts=" + posts +
                ", studenti=" + studenti +
                '}';
    }
}
