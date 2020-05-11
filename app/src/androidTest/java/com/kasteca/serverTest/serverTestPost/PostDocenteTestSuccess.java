package com.kasteca.serverTest.serverTestPost;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

import static junit.framework.TestCase.assertEquals;

@RunWith(JUnit4.class)
public class PostDocenteTestSuccess {
    private final static String TAG="Testing-Post-Success";

    private String mailDoc = "docenteProva@unisannio.it";
    private String pwdDoc = "passwordProva";

    private String idCorso = "G193crk8g3jzzAm9Nfiq";
    private String idPostDaLeggere = "Jw37QTjNbSf4MTbEvHzi";



    private String preparazione= "notLogged";
    private String result= null;
    private int counter=0;


    @Before
    public void signInDocente()throws InterruptedException {
        login();
    }

    @Test
    @Ignore("This test will be ignored")
    public void creazioneEliminazioneSuccess() throws InterruptedException{
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference postReference = db.collection("Post");

        Map<String,Object> documentSend= new HashMap<>();
        documentSend.put("testo","test");
        documentSend.put("tag","esercizio");
        documentSend.put("link",null);
        documentSend.put("pdf",null);
        documentSend.put("corso",idCorso);
        documentSend.put("lista_commenti", Arrays.asList());
        documentSend.put("data", new Date());

        postReference.add(documentSend).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "DocumentSnapshot successfully written!");

                //cancelliamo il corso
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference postReference = db.collection("Post");
                postReference.document(documentReference.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        result="success";
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error writing document", e);
                result="fail";
            }
        });

        while(result == null & counter<10){
            Log.d(TAG,"Attesa risposta...");
            Thread.sleep(1000);
            counter=counter + 1;
        };
        counter=0;
        assertEquals("success",result);
        result=null;

    }

    @Test
    @Ignore("This test will be ignored")
    public void readSuccess() throws InterruptedException{
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference postReference = db.collection("Post");
        postReference.document(idPostDaLeggere).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                result="success";
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error writing document", e);
                result="fail";
            }
        });

        while(result == null & counter<10){
            Log.d(TAG,"Attesa risposta...");
            Thread.sleep(1000);
            counter=counter + 1;
        };
        counter=0;
        assertEquals("success",result);
        result=null;

    }



    @Test
    @Ignore("This test will be ignored")
    public void updateSuccess() throws InterruptedException{
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference postReference = db.collection("Post");
        postReference.document(idPostDaLeggere).update("testo","testModificato").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                result="success";

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error writing document", e);
                result="fail";
            }
        });

        while(result == null & counter<10){
            Log.d(TAG,"Attesa risposta...");
            Thread.sleep(1000);
            counter=counter + 1;
        };
        counter=0;
        assertEquals("success",result);
        result=null;
    }


    private void login() throws RuntimeException,InterruptedException{
        if (preparazione.equalsIgnoreCase("notLogged")) {
            //preparazione = null;
            FirebaseAuth mAuth = FirebaseAuth.getInstance(); // crea un istanza di FirebaseAuth (serve per l'autenticazione)
            mAuth.signOut();

            mAuth.signInWithEmailAndPassword(mailDoc, pwdDoc).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
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
