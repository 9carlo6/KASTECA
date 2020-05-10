package com.kasteca.serverTest.serverTestRisposta;

import android.util.Log;

import androidx.annotation.NonNull;

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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

public class RisposteStudentTestSuccess {

    private final String TAG="Testing-Risposte-Backend";

    private final String mailStu = "studenteProva@studenti.unisannio.it";
    private final String pwdStu = "passwordProva";
    private final String idStudente="SotSWWJIZHNALPZ32EAARRed9RG2";

    private final String idCommentoEsistente = "FWoOCqujjKsffuFHJ9ql";
    private final String idRispostaDaLeggere = "zd5JNL8hZvmW1CK0O5ex";
    private final String idRispostaStudente = "gpeq156bQS2lLwmSvfUt";


    private String preparazione= "notLogged";
    private String result= null;
    private int counter=0;

    @Before
    public void signInStudente()  throws InterruptedException {
        login();
    }

    @After
    public void signOut() throws InterruptedException{
        logout();
    }

    @Test
    @Ignore("This test will be ignored")
    public void creazioneEliminazioneStudenteSuccesso() {
        //Testo la creazione di una risposta con dati al suo interno corretti
        result = null;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference risposteReference = db.collection("Risposte_Commenti");

        Map<String, Object> newRisposta = new HashMap<>();
        newRisposta.put("testo", "test");
        newRisposta.put("data", new Date());
        newRisposta.put("commento", idCommentoEsistente);
        newRisposta.put("proprietario", idStudente);

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
                                result = "success";
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                result = "fail";
            }
        });
    }

    @Test
    @Ignore("This test will be ignored")
    public void letturaStudente(){
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
    @Ignore("This test will be ignored")
    public void modificaStudente(){
        //Setto il risultato a null
        result= null;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference risposteReference = db.collection("Risposte_Commenti");
        risposteReference.document(idRispostaStudente).update("testo","testModifica")
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

            mAuth.signInWithEmailAndPassword(mailStu, pwdStu).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
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




    private void logout(){
        Log.d(TAG,"Logout eseguito");
        FirebaseAuth mauth = FirebaseAuth.getInstance();
        mauth.signOut();
        Log.d(TAG,"Logout eseguito");

    }




}
