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
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

public class RisposteDocenteTestFallimento {
    private final static String TAG="Testing-Risposte-Fail";

    private String mailDoc = "docenteProva@unisannio.it";
    private String pwdDoc = "passwordProva";

    private String idDocente = "xXqhMcCwc3R5RibdcLtTOuoMVgm1";

    private String idCommento = "FWoOCqujjKsffuFHJ9ql";
    private String idRispostaDaLeggere = "zd5JNL8hZvmW1CK0O5ex";
    private final String idRispostaNonDocente= "gpeq156bQS2lLwmSvfUt";


    private String preparazione= "notLogged";
    private String result= null;
    private int counter=0;


    @Before
    public void signInDocente() throws InterruptedException {
        login();
    }

/*
    Test sul Fallimento della creazione di una Risposta
 */
    @Test
    public void creazioneTestoNull(){
        result= null;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference risposteReference = db.collection("Risposte_Commenti");

        Map<String, Object> newRisposta = new HashMap<>();
        newRisposta.put("testo", null);
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
        newRisposta.put("proprietario", idDocente);

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
        result = null;

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

    /*
    Test fallimento modifica di una risposta
     */
    @Test
    public void modificaTestoNull(){
        result= null;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference risposteReference = db.collection("Risposte_Commenti");
        risposteReference.document(idRispostaDaLeggere).update("testo",null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        result="fail";
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                result="success";
            }
        });
        while(result == null);
        assertEquals("success",result);
        result=null;
    }

    @Test
    public void modificaTestoOverSize(){
        result= null;

        String testoOversize="test";
        while(testoOversize.length()<=120)
            testoOversize=testoOversize+testoOversize;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference risposteReference = db.collection("Risposte_Commenti");
        risposteReference.document(idRispostaDaLeggere).update("testo",testoOversize)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        result="fail";
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                result="success";
            }
        });
        while(result == null);
        assertEquals("success",result);
        result=null;
    }

    @Test
    public void modificaNonDalProprietario(){
        //Effettuiamo il login con un account diverso da quello che ha creato la risposta
        result=null;

        //Proviamo a fare una rischiesta di modifica
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference risposteReference = db.collection("Risposte_Commenti");
        risposteReference.document(idRispostaNonDocente).update("testo","modificaNonDalProprietario")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        result="fail";
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        result="success";
                    }
        });

        while(result == null);
        assertEquals("success",result);
        result=null;

    }


    /*
    Test fallimento eliminazione di una risposta
     */
    @Test
    public void eliminazioneProprietazioDiverso(){

        //Effettuiamo richiesta di eliminazione di una risposta non di questo Docente
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference risposteReference = db.collection("Risposte_Commenti");
        risposteReference.document(idRispostaNonDocente).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        result="fail";
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                result="success";
            }
        });

        while(result == null);
        assertEquals("success",result);
        result=null;
    }

    /*
    Test fallimento lettura NON SONO ESEGUIBILI: perchè loggiamo in firebase con gli unici account disponibili che sono
    o studente o docente. Si potrebbe creare un account di test non studente o docente.
     */



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
