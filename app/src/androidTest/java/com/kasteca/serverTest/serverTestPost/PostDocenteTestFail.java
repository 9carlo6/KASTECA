package com.kasteca.serverTest.serverTestPost;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static junit.framework.TestCase.assertEquals;

@RunWith(AndroidJUnit4.class)
public class PostDocenteTestFail {
    private final static String TAG="Testing-Post-Fail";

    private String mailDoc = "docenteProva@unisannio.it";
    private String pwdDoc = "passwordProva";

    private String idDocente = "xXqhMcCwc3R5RibdcLtTOuoMVgm1";

    private String idCorso = "G193crk8g3jzzAm9Nfiq";


    private String preparazione= "notLogged";
    private String result= null;
    private int counter=0;


    @Before
    public void signInDocente()throws InterruptedException {
        login();
    }


    @Test
    @Ignore("This test will be ignored")
    public void creazioneTestoOverSize() throws InterruptedException{

        String testoOversize="test";
        while(testoOversize.length()<=2010)
            testoOversize=testoOversize+testoOversize;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference postReference = db.collection("Post");

        Map<String,Object> documentSend= new HashMap<>();
        documentSend.put("testo",testoOversize);
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

                result="fail";
                //cancelliamo il corso
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference postReference = db.collection("Post");
                postReference.document(documentReference.getId()).delete();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error writing document", e);
                result="success";
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
    public void creazioneTestoVuoto() throws InterruptedException{
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference postReference = db.collection("Post");


        Map<String,Object> documentSend= new HashMap<>();
        documentSend.put("testo","");
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
                result="fail";
                //cancelliamo il corso
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference postReference = db.collection("Post");
                postReference.document(documentReference.getId()).delete();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error writing document", e);
                result="success";

            }
        });

        while(result == null & counter<100){
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
    public void creazioneTagNull() throws InterruptedException{
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference postReference = db.collection("Post");


        Map<String,Object> documentSend= new HashMap<>();
        documentSend.put("testo","testo");
        documentSend.put("tag",null);
        documentSend.put("link",null);
        documentSend.put("pdf",null);
        documentSend.put("corso",idCorso);
        documentSend.put("lista_commenti", Arrays.asList());
        documentSend.put("data", new Date());

        postReference.add(documentSend).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "DocumentSnapshot successfully written!");
                result="fail";
                //cancelliamo il corso
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference postReference = db.collection("Post");
                postReference.document(documentReference.getId()).delete();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error writing document", e);
                result="success";

            }
        });

        while(result == null & counter<100){
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
    public void creazioneWrongTag()throws InterruptedException{
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference postReference = db.collection("Post");


        Map<String,Object> documentSend= new HashMap<>();
        documentSend.put("testo","testo");
        documentSend.put("tag","wrong");
        documentSend.put("link",null);
        documentSend.put("pdf",null);
        documentSend.put("corso",idCorso);
        documentSend.put("lista_commenti", Arrays.asList());
        documentSend.put("data", new Date());

        postReference.add(documentSend).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "DocumentSnapshot successfully written!");
                result="fail";
                //cancelliamo il corso
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference postReference = db.collection("Post");
                postReference.document(documentReference.getId()).delete();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error writing document", e);
                result="success";

            }
        });

        while(result == null & counter<100){
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
    public void creazioneTestoNull()throws InterruptedException{
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference postReference = db.collection("Post");

        Map<String,Object> documentSend= new HashMap<>();
        documentSend.put("testo",null);
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
                result="fail";
                //cancelliamo il corso
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference postReference = db.collection("Post");
                postReference.document(documentReference.getId()).delete();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error writing document", e);
                result="success";

            }
        });

        while(result == null & counter<100){
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
    public void creazioneCorsoNull()throws InterruptedException{
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference postReference = db.collection("Post");

        Map<String,Object> documentSend= new HashMap<>();
        documentSend.put("testo","testp");
        documentSend.put("tag","esercizio");
        documentSend.put("link",null);
        documentSend.put("pdf",null);
        documentSend.put("corso",null);
        documentSend.put("lista_commenti", Arrays.asList());
        documentSend.put("data", new Date());

        postReference.add(documentSend).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "DocumentSnapshot successfully written!");
                result="fail";
                //cancelliamo il corso
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference postReference = db.collection("Post");
                postReference.document(documentReference.getId()).delete();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error writing document", e);
                result="success";

            }
        });

        while(result == null & counter<100){
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
    public void creazioneCorsoWrong()throws InterruptedException{
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference postReference = db.collection("Post");

        Map<String,Object> documentSend= new HashMap<>();
        documentSend.put("testo","testp");
        documentSend.put("tag","esercizio");
        documentSend.put("link",null);
        documentSend.put("pdf",null);
        documentSend.put("corso","wrongIdCorso");
        documentSend.put("lista_commenti", Arrays.asList());
        documentSend.put("data", new Date());

        postReference.add(documentSend).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "DocumentSnapshot successfully written!");
                result="fail";
                //cancelliamo il corso
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference postReference = db.collection("Post");
                postReference.document(documentReference.getId()).delete();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error writing document", e);
                result="success";

            }
        });

        while(result == null & counter<100){
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
    public void creazioneDataNull()throws InterruptedException{
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference postReference = db.collection("Post");

        Map<String,Object> documentSend= new HashMap<>();
        documentSend.put("testo","testp");
        documentSend.put("tag","esercizio");
        documentSend.put("link",null);
        documentSend.put("pdf",null);
        documentSend.put("corso",idCorso);
        documentSend.put("lista_commenti", Arrays.asList());
        documentSend.put("data", null);

        postReference.add(documentSend).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "DocumentSnapshot successfully written!");
                result="fail";
                //cancelliamo il corso
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference postReference = db.collection("Post");
                postReference.document(documentReference.getId()).delete();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error writing document", e);
                result="success";

            }
        });

        while(result == null & counter<100){
            Log.d(TAG,"Attesa risposta...");
            Thread.sleep(1000);
            counter=counter + 1;
        };
        counter=0;
        assertEquals("success",result);
        result=null;
    }

    private void login() throws RuntimeException,InterruptedException {

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
