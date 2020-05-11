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
import com.google.firebase.firestore.FirebaseFirestore;

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
public class CommentiDocenteTestFallimento {
    private final static String TAG="Testing-Risposte-Fail";

    private String mailDoc = "docenteProva@unisannio.it";
    private String pwdDoc = "passwordProva";

    private String idDocente = "xXqhMcCwc3R5RibdcLtTOuoMVgm1";

    private String idPost = "Jw37QTjNbSf4MTbEvHzi";
    private String idCommentoDaLeggere = "FWoOCqujjKsffuFHJ9ql";
    private final String idCommentoNonDocente= "OjApX0Okx3Zv0LfRwqmy";


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
    @Ignore("This test will be ignored")
    public void creazioneTestoNull(){
        result= null;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference commentiReference = db.collection("Commenti");

        Map<String, Object> newCommento = new HashMap<>();
        newCommento.put("testo", null);
        newCommento.put("data", new Date());
        newCommento.put("post", idPost);
        newCommento.put("proprietarioCommento", idDocente);
        newCommento.put("lista_risposte", new ArrayList<String>());

        commentiReference.add(newCommento)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        CollectionReference commentiReference = db.collection("Commenti");
                        commentiReference.document(documentReference.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
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
    @Ignore("This test will be ignored")
    public void creazioneTestoOverSize(){
        result= null;

        String testoOversize="test";
        while(testoOversize.length()<=510)
            testoOversize=testoOversize+testoOversize;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference commentiReference = db.collection("Commenti");

        Map<String, Object> newCommento = new HashMap<>();
        newCommento.put("testo", testoOversize);
        newCommento.put("data", new Date());
        newCommento.put("post", idPost);
        newCommento.put("proprietarioCommento", idDocente);
        newCommento.put("lista_risposte", new ArrayList<String>());

        commentiReference.add(newCommento).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference commentiReference = db.collection("Commenti");
                commentiReference.document(documentReference.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
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
    @Ignore("This test will be ignored")
    public void creazioneTestoVuoto(){
        result= null;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference commentiReference = db.collection("Commenti");

        Map<String, Object> newCommento = new HashMap<>();
        newCommento.put("testo", "");
        newCommento.put("data", new Date());
        newCommento.put("post", idPost);
        newCommento.put("proprietarioCommento", idDocente);
        newCommento.put("lista_risposte", new ArrayList<String>());

        commentiReference.add(newCommento).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference commentiReference = db.collection("Commenti");
                commentiReference.document(documentReference.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
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
    @Ignore("This test will be ignored")
    public void creazioneDataNull(){
        result= null;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference commentiReference = db.collection("Commenti");

        Map<String, Object> newCommento = new HashMap<>();
        newCommento.put("testo", "testo");
        newCommento.put("data", null);
        newCommento.put("post", idPost);
        newCommento.put("proprietarioCommento", idDocente);
        newCommento.put("lista_risposte", new ArrayList<String>());

        commentiReference.add(newCommento)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        CollectionReference commentiReference = db.collection("Commenti");
                        commentiReference.document(documentReference.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
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
    @Ignore("This test will be ignored")
    public void creazioneProprietarioNotDocenteorStudente(){
        result = null;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference commentiReference = db.collection("Commenti");

        Map<String, Object> newCommento = new HashMap<>();
        newCommento.put("testo", "testo");
        newCommento.put("data", newCommento);
        newCommento.put("post", idPost);
        newCommento.put("proprietarioCommento", "wrong_id");
        newCommento.put("lista_risposte", new ArrayList<String>());

        commentiReference.add(newCommento)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        CollectionReference commentiReference = db.collection("Commenti");
                        commentiReference.document(documentReference.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
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
    @Ignore("This test will be ignored")
    public void creazionePostNull(){
        result= null;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference commentiReference = db.collection("Commenti");

        Map<String, Object> newCommento = new HashMap<>();
        newCommento.put("testo", "testo");
        newCommento.put("data", new Date());
        newCommento.put("post", null);
        newCommento.put("proprietarioCommento", idDocente);
        newCommento.put("lista_risposte", new ArrayList<String>());

        commentiReference.add(newCommento)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        CollectionReference commentiReference = db.collection("Commenti");
                        commentiReference.document(documentReference.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
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
    @Ignore("This test will be ignored")
    public void creazionePostNotExist(){
        result= null;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference commentiReference = db.collection("Commenti");

        Map<String, Object> newCommento = new HashMap<>();
        newCommento.put("testo", "testo");
        newCommento.put("data", new Date());
        newCommento.put("post", "notExistentIdPost");
        newCommento.put("proprietarioCommento", idDocente);
        newCommento.put("lista_risposte", new ArrayList<String>());

        commentiReference.add(newCommento)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        CollectionReference commentiReference = db.collection("Commenti");
                        commentiReference.document(documentReference.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
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
    @Ignore("This test will be ignored")
    public void modificaTestoNull(){
        result= null;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference commentiReference = db.collection("Commenti");
        commentiReference.document(idCommentoDaLeggere).update("testo",null)
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
    @Ignore("This test will be ignored")
    public void modificaTestoOverSize(){
        result= null;

        String testoOversize="test";
        while(testoOversize.length()<=510)
            testoOversize=testoOversize+testoOversize;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference commentiReference = db.collection("Commenti");
        commentiReference.document(idCommentoDaLeggere).update("testo",testoOversize)
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
    @Ignore("This test will be ignored")
    public void modificaNonDalProprietario(){
        //Effettuiamo il login con un account diverso da quello che ha creato la risposta
        result=null;

        //Proviamo a fare una rischiesta di modifica
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference commentiReference = db.collection("Commenti");
        commentiReference.document(idCommentoNonDocente).update("testo","modificaNonDalProprietario")
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
    @Ignore("This test will be ignored")
    public void eliminazioneProprietazioDiverso(){

        //Effettuiamo richiesta di eliminazione di una risposta non di questo Docente
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference commentiReference = db.collection("Commenti");
        commentiReference.document(idCommentoNonDocente).delete()
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
