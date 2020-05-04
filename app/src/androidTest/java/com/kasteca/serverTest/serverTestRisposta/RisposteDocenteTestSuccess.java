package com.kasteca.serverTest.serverTestRisposta;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;


@RunWith(JUnit4.class)
public class RisposteDocenteTestSuccess {

    private final static String TAG="Testing-Risposte-Backend";


    private String mailDoc = "docenteProva@unisannio.it";
    private String pwdDoc = "passwordProva";

    private String idDocente = "xXqhMcCwc3R5RibdcLtTOuoMVgm1";

    private String idCorso = "wgB2wdRBM5mBOmZuWG85";
    private String idPost = "Jw37QTjNbSf4MTbEvHzi";
    private String idCommento = "FWoOCqujjKsffuFHJ9ql";
    private String idRisposta = null;
    private String idRispostaDaLeggere = "zd5JNL8hZvmW1CK0O5ex";

    private String preparazione= null;
    private String result= null;
    private int counter=0;

    @Before
    public void signInDocente()  throws InterruptedException {
        login();
    }

    @After
    public void signOut() throws InterruptedException{
    }


    @Test
    public void creazioneEliminazioneDocenteSuccesso() {
        //Testo la creazione di una risposta con dati al suo interno corretti
        result= null;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference risposteReference = db.collection("Risposte_Commenti");

        Map<String, Object> newRisposta = new HashMap<>();
        newRisposta.put("testo", "test");
        newRisposta.put("data", new Date());
        newRisposta.put("commento", idCommento);
        newRisposta.put("proprietario", idDocente);

        risposteReference.add(newRisposta)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        CollectionReference risposteReference = db.collection("Risposte_Commenti");
                        risposteReference.document(documentReference.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                //cancello la risposta creata
                                result="success";
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        result="fail";
                    }
        });

        //Attendiamo la risposta
        while(result == null);
        assertEquals("success",result);
        result=null;
    }




    @Test
    public void letturaDocenteSuccesso() {
        //Setto il risultato a null
        result= null;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference risposteReference = db.collection("Risposte_Commenti");

        risposteReference.document(idRispostaDaLeggere).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        result="success";
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        result="fail";
                    }
                });

        while(result == null);
        assertEquals("success",result);
        result=null;
    }

    @Test
    public void modificaDocenteSuccesso(){
        //Setto il risultato a null
        result= null;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference risposteReference = db.collection("Risposte_Commenti");
        risposteReference.document(idRispostaDaLeggere).update("testo","testModifica")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        result="success";
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
                    public void onFailure(@NonNull Exception e) {
                        result="fail";

                    }
                });
        while(result == null);
        assertEquals("success",result);
        result=null;
    }



    private void login() throws RuntimeException,InterruptedException{
        FirebaseAuth mAuth = FirebaseAuth.getInstance(); // crea un istanza di FirebaseAuth (serve per l'autenticazione)
        mAuth.signOut();

        mAuth.signInWithEmailAndPassword(mailDoc, pwdDoc).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                preparazione="ok";
                Log.d(TAG,"Login Effettuato");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                preparazione="notOk";
                Log.d(TAG,"Login Fallito");
            }
        });

        //attesa login
        while((preparazione==null) && (counter<10)){
            Thread.sleep(1000);
            counter=counter+1;
        };
        counter=0;

        if(!preparazione.equalsIgnoreCase("ok")){
               throw new RuntimeException();
        }else
            preparazione=null;      //Riporto preparazione nello stato iniziale potrebbe essere riutilizzata.

    }


    private void logout(){
        Log.d(TAG,"Logout eseguito");
        FirebaseAuth mauth = FirebaseAuth.getInstance();
        mauth.signOut();
        Log.d(TAG,"Logout eseguito");

    }

    private void cancellazioneSettingTest() throws  InterruptedException{
        Log.d(TAG,"Cancellazione Setting");

        if(idRisposta != null){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference corsiReference = db.collection("Risposte");
            corsiReference.document(idRisposta).delete();
            Log.d(TAG,"Cancellazione Risposta");

        }

        if(idCommento != null){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference corsiReference = db.collection("Commenti");
            corsiReference.document(idCommento).delete();
            Log.d(TAG,"Cancellazione Commento");

        }

        if(idPost != null){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference corsiReference = db.collection("Post");
            corsiReference.document(idPost).delete();
            Log.d(TAG,"Cancellazione Post");

        }

        if(idCorso != null){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference corsiReference = db.collection("Corsi");
            corsiReference.document(idCorso).delete();
            Log.d(TAG,"Cancellazione del corso eseguita");

            //Eliminiamo il corso dalla lista dei corsi
            CollectionReference docentiReference = db.collection("Docenti");
            docentiReference.document(idDocente).update(
                    "lista_corsi",
                    FieldValue.
                            arrayRemove(idCorso)
            );
        }

        Thread.sleep(5000);
        logout();
    }

    private void creazioneCorso() throws RuntimeException{
        preparazione=null;

        Map<String,Object> documentSend= new HashMap<>();
        documentSend.put("nome_corso","corsoTest");
        documentSend.put("codice","test");
        documentSend.put("descrizione","test");
        documentSend.put("docente",idDocente);
        documentSend.put("lista_post", Arrays.asList());
        documentSend.put("lista_studenti",Arrays.asList());
        Date date= new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        documentSend.put("anno_accademico",calendar.get(Calendar.YEAR)+"/"+(calendar.get(Calendar.YEAR)+1));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference corsiReference = db.collection("Corsi");

        corsiReference.add(documentSend).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                idCorso=documentReference.getId();
                Log.d(TAG,"idCorso: "+idCorso);
                preparazione="ok";
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                preparazione="notok";
            }
        });

        while(preparazione == null);
        if(preparazione.equalsIgnoreCase("ok"))
            creazionePost();
        else
            throw new RuntimeException();
    }

    private void creazionePost() throws RuntimeException{

        preparazione=null;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference postsRef = db.collection("Post");

        Map<String, Object> newPost = new HashMap<>();

        newPost.put("corso", idCorso);
        Date data= new Date();
        newPost.put("data", new Timestamp(data));
        newPost.put("link", "testLink");
        newPost.put("pdf", "");
        newPost.put("tag", "approfondimento");
        newPost.put("testo", "test");
        newPost.put("lista_commenti", new ArrayList<String>());

        postsRef.add(newPost)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        idPost= documentReference.getId();
                        Log.d(TAG,"IDPost: "+idPost);
                        preparazione="ok";
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        preparazione="notok";
                    }
                });

        while(preparazione == null);
        if(preparazione.equalsIgnoreCase("ok"))
            creazioneCommento();
        else
            throw new RuntimeException();
    }

    private void creazioneCommento() throws RuntimeException{

        preparazione=null;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference commentiRef = db.collection("Commenti");

        Map<String, Object> newCommento = new HashMap<>();
        newCommento.put("testo", "test");
        newCommento.put("data", new Date());
        newCommento.put("lista_risposte", new ArrayList<String>());
        newCommento.put("post", idPost);
        newCommento.put("proprietarioCommento", idDocente);

        commentiRef.add(newCommento)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        idCommento= documentReference.getId();
                        //Rendiamo la variabile per la sincronizzazione true
                        preparazione= "ok";
                        Log.d(TAG,"idCommento: "+idCommento);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        preparazione="notok";
                    }
                  });

        while(preparazione == null);
        if(!preparazione.equalsIgnoreCase("ok"))
            throw new RuntimeException();

    }



}
