package com.kasteca.serverTest.serverTestCorso;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class CorsoStudenteTestSuccess {

    private final String TAG="Testing-Risposte-Backend";

    private final String mailStu = "studenteProva@studenti.unisannio.it";
    private final String pwdStu = "passwordProva";
    private final String idStudente="SotSWWJIZHNALPZ32EAARRed9RG2";

    private String idCorsoDaLeggere = "G193crk8g3jzzAm9Nfiq";

    private String preparazione= null;
    private String result= null;
    private int counter=0;

    @Before
    public void signInStudente()  throws InterruptedException {
        login();
    }

    @Test
    public void readSuccess() throws InterruptedException{
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference corsiReference = db.collection("Corsi");
        corsiReference.document(idCorsoDaLeggere).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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


    private void login() throws RuntimeException,InterruptedException{
        FirebaseAuth mAuth = FirebaseAuth.getInstance(); // crea un istanza di FirebaseAuth (serve per l'autenticazione)
        mAuth.signOut();

        mAuth.signInWithEmailAndPassword(mailStu, pwdStu).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
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

}
