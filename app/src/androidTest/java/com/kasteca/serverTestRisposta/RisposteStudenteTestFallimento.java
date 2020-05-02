package com.kasteca.serverTestRisposta;

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
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

public class RisposteStudenteTestFallimento {

    private final String TAG="Testing-Risposte-Backend";

    private final String mailStu = "studenteProva@studenti.unisannio.it";
    private final String pwdStu = "passwordProva";
    private final String idStudente="SotSWWJIZHNALPZ32EAARRed9RG2";

    private String idCorso = null;

    private final String idCommento = "FWoOCqujjKsffuFHJ9ql";
    private final String idRispostaDaLeggere = "XMH9QKnUw2B5RyK0nJ17";
    private final String idRispostaStudente = "gpeq156bQS2lLwmSvfUt";


    private String preparazione= null;
    private String result= null;

    @Before
    public void signInDocente()  throws InterruptedException {
        login();
    }


    @Test
    public void creazioneTestoNull(){
        result= null;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference risposteReference = db.collection("Risposte_Commenti");

        Map<String, Object> newRisposta = new HashMap<>();
        newRisposta.put("testo", null);
        newRisposta.put("data", new Date());
        newRisposta.put("commento", idCommento);
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
                                result="fail";
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                result="success";
            }
        });

        //Attendiamo la risposta
        while(result == null);
        assertEquals("success",result);
        result=null;
    }

    @Test
    public void creazioneTestoOverSize(){
        result= null;

        String testoOversize="test";
        while(testoOversize.length()<=120)
            testoOversize=testoOversize+testoOversize;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference risposteReference = db.collection("Risposte_Commenti");

        Map<String, Object> newRisposta = new HashMap<>();
        newRisposta.put("testo", testoOversize);
        newRisposta.put("data", new Date());
        newRisposta.put("commento", idCommento);
        newRisposta.put("proprietario", idStudente);

        risposteReference.add(newRisposta).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference risposteReference = db.collection("Risposte_Commenti");
                risposteReference.document(documentReference.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //cancello la risposta creata
                        result="fail";
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                result="success";
            }
        });

        //Attendiamo la risposta
        while(result == null);
        assertEquals("success",result);
        result=null;
    }

    @Test
    public void creazioneDataNull(){
        result= null;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference risposteReference = db.collection("Risposte_Commenti");

        Map<String, Object> newRisposta = new HashMap<>();
        newRisposta.put("testo", "test");
        newRisposta.put("data", null);
        newRisposta.put("commento", idCommento);
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
                                result="fail";
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                result="success";
            }
        });

        //Attendiamo la risposta
        while(result == null);
        assertEquals("success",result);
        result=null;
    }

    @Test
    public void creazioneProprietarioNull(){
        result= null;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference risposteReference = db.collection("Risposte_Commenti");

        Map<String, Object> newRisposta = new HashMap<>();
        newRisposta.put("testo", "test");
        newRisposta.put("data", new Date());
        newRisposta.put("commento", idCommento);
        newRisposta.put("proprietario", null);

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
                                result="fail";
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                result="success";
            }
        });

        //Attendiamo la risposta
        while(result == null);
        assertEquals("success",result);
        result=null;
    }


    @Test
    public void creazioneCommentoNull(){
        result= null;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference risposteReference = db.collection("Risposte_Commenti");

        Map<String, Object> newRisposta = new HashMap<>();
        newRisposta.put("testo", "test");
        newRisposta.put("data", new Date());
        newRisposta.put("commento", null);
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
                                result="fail";
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                result="success";
            }
        });

        //Attendiamo la risposta
        while(result == null);
        assertEquals("success",result);
        result=null;
    }

    @Test
    public void creazioneCommentoNotExist(){
        result= null;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference risposteReference = db.collection("Risposte_Commenti");

        Map<String, Object> newRisposta = new HashMap<>();
        newRisposta.put("testo", "test");
        newRisposta.put("data", new Date());
        newRisposta.put("commento","commentoNonEsistente");
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
                                result="fail";
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                result="success";
            }
        });

        //Attendiamo la risposta
        while(result == null);
        assertEquals("success",result);
        result=null;
    }




    private void login() throws RuntimeException{
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
        while(preparazione==null);
        if(!preparazione.equalsIgnoreCase("ok")){
            throw new RuntimeException();
        }else
            preparazione=null;      //Riporto preparazione nello stato iniziale potrebbe essere riutilizzata.

    }

}
