package com.kasteca.serverTest.serverTestCommento;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

import static junit.framework.TestCase.assertEquals;

@RunWith(JUnit4.class)
public class CommentiStudenteTestSuccess {
    private final static String TAG="Testing-Commenti-Backend";


    private String mailStud = "studenteprova@studenti.unisannio.it";
    private String pwdStud= "passwordProva";

    private String idStudente = "SotSWWJIZHNALPZ32EAARRed9RG2";

    private String idPost = "Jw37QTjNbSf4MTbEvHzi";
    private String idCommentoDaLeggere = "FWoOCqujjKsffuFHJ9ql";

    private String preparazione= "notLogged";
    private String result= null;
    private int counter=0;

    @Before
    public void signInDocente()  throws InterruptedException {
        login();
    }

    @After
    public void signOut() throws InterruptedException{
        FirebaseAuth mauth = FirebaseAuth.getInstance();
        mauth.signOut();
        Thread.sleep(2000);
        Log.d(TAG,"Logout eseguito");
    }


    @Test
    @Ignore("This test will be ignored")
    public void creazioneEliminazioneDocenteSuccesso() {
        //Testo la creazione di una risposta con dati al suo interno corretti
        result= null;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference risposteReference = db.collection("Commenti");

        Map<String, Object> newCommento = new HashMap<>();
        newCommento.put("testo", "test");
        newCommento.put("data", new Date());
        newCommento.put("post", idPost);
        newCommento.put("proprietarioCommento", idStudente);
        newCommento.put("lista_risposte", new ArrayList<String>());

        risposteReference.add(newCommento)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        CollectionReference commentiReference = db.collection("Commenti");
                        commentiReference.document(documentReference.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
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
    //@Ignore("This test will be ignored")
    public void letturaDocenteSuccesso() {
        //Setto il risultato a null
        result= null;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference commentiReference = db.collection("Commenti");

        commentiReference.document(idCommentoDaLeggere).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                            result = "success";
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
    @Ignore("This test will be ignored")
    public void modificaDocenteSuccesso(){
        //Setto il risultato a null
        result= null;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference commentiReference = db.collection("Commenti");
        commentiReference.document(idCommentoDaLeggere).update("testo","testModifica")
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
        if (preparazione.equalsIgnoreCase("notLogged")) {
            //preparazione = null;
            FirebaseAuth mAuth = FirebaseAuth.getInstance(); // crea un istanza di FirebaseAuth (serve per l'autenticazione)
            mAuth.signOut();

            mAuth.signInWithEmailAndPassword(mailStud, pwdStud).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    preparazione = "ok";
                    Log.d(TAG, "Login Effettuato");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    preparazione = "notLogged";
                    Log.d(TAG, "Login Fallito");
                }
            });

            //attesa login
            //attesa login
            while ((preparazione.equalsIgnoreCase("notLogged")) && (counter < 10)) {
                Thread.sleep(1000);
                counter = counter + 1;
            }
            ;
            counter = 0;

            if (!preparazione.equalsIgnoreCase("ok")) {
                throw new RuntimeException();
            } else
                preparazione = "notLogged";      //Riporto preparazione nello stato iniziale potrebbe essere riutilizzata.

        }

    }

}
