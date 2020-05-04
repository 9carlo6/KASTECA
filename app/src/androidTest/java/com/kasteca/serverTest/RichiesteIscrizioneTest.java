package com.kasteca.serverTest;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kasteca.activity.RichiestaIscrizioneActivity;
import com.kasteca.util.EspressoIdlingResource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;

public class RichiesteIscrizioneTest {
    private static final String TAG = "DEBUG_RICHIESTE_ISCRIZIONE";
    private String task_state;
    private static String mailDoc = "docenteProva@unisannio.it";
    private static String pwdDoc = "passwordProva";
    private String mailStu = "studenteProva@studenti.unisannio.it";
    private String pwdStu = "passwordProva";
    private static String codice_corso = "codice";
    private String id_studente = "SotSWWJIZHNALPZ32EAARRed9RG2";
    private static String id_docente = "xXqhMcCwc3R5RibdcLtTOuoMVgm1";

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        FirebaseAuth mAuth = FirebaseAuth.getInstance(); // crea un istanza di FirebaseAuth (serve per l'autenticazione)

        mAuth.signOut();

        // thread non va bene!!! Occorre utilizzare l'interfaccia IdlingResource
        Thread.sleep(1000);

        mAuth.signInWithEmailAndPassword(mailDoc, pwdDoc).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Log.d(TAG, "Login Docente eseguito con successo");

                // bisogna creare il corso
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference corsi = db.collection("Corsi");

                Date date= new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

                Map<String,Object> obj= new HashMap<>();
                obj.put("anno_accademico", calendar.get(Calendar.YEAR)+"/"+(calendar.get(Calendar.YEAR)+1));
                obj.put("codice", codice_corso);
                obj.put("descrizione", "descrizione_prova");
                obj.put("docente", id_docente);
                obj.put("lista_post", new ArrayList<String>());
                obj.put("lista_studenti", new ArrayList<String>());
                obj.put("nome_corso", "nome_corso_prova");

                corsi.document("id_corso_prova").set(obj).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "Corsi: creazione corso ok");
                            //EspressoIdlingResource.decrement();
                        }else{
                            Log.d(TAG, "Corsi: FALLIMENTO creazione corso");
                            //EspressoIdlingResource.decrement();
                        }
                    }
                });
            }
        });

        Thread.sleep(3000);

    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        Thread.sleep(1000);

        FirebaseAuth mauth = FirebaseAuth.getInstance();
        mauth.signOut();

        FirebaseAuth mAuth = FirebaseAuth.getInstance(); // crea un istanza di FirebaseAuth (serve per l'autenticazione)
        String mail = "docenteProva@unisannio.it";
        String pwd = "passwordProva";

        mAuth.signInWithEmailAndPassword(mail, pwd).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference corsi = db.collection("Corsi");

                // cancellazione del corso
                corsi.document("id_corso_prova")
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "Corsi: cancellazione corso ok");

                                // cancellazionde della richiesta
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                CollectionReference richieste_iscrizione = db.collection("Richieste_Iscrizione");

                                richieste_iscrizione.document("id_richiesta_prova")
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "Richieste_Iscrizione: cancellazione richiesta ok");
                                                //cancellazioneCorso();
                                                //EspressoIdlingResource.decrement();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Richieste_Iscrizione: errore nella cancellazione della richiesta", e);
                                                //EspressoIdlingResource.decrement();
                                            }
                                        });

                                //EspressoIdlingResource.decrement();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Corsi: errore nella cancellazione del corso", e);
                                //EspressoIdlingResource.decrement();
                            }
                        });
            }
        });

        // thread non va bene!!! Occorre utilizzare l'interfaccia IdlingResource
        Thread.sleep(3000);

        mauth.signOut();

        Thread.sleep(1000);

    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void RichiesteIscrizioneTestOk() throws ExecutionException, InterruptedException {

        FirebaseAuth mauth = FirebaseAuth.getInstance();
        mauth.signOut();

        String mail = "studenteProva@studenti.unisannio.it";
        String pwd = "passwordProva";

        mauth.signInWithEmailAndPassword(mail, pwd).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // altrimenti va avanti e carica la richiesta nel db
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference richiesteIscrizione = db.collection("Richieste_Iscrizione");

                EspressoIdlingResource.increment();

                // allora inviare la richiesta e caricarla nel db
                Map<String, Object> obj = new HashMap<>();
                obj.put("codice_corso", codice_corso);
                obj.put("data", new Timestamp(Calendar.getInstance().getTime()));
                obj.put("stato_richiesta", "in attesa");
                obj.put("studente", id_studente);


                richiesteIscrizione.document("id_richiesta_prova").set(obj).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "creazione richiesta ok");
                            task_state = "OK";
                            //EspressoIdlingResource.decrement();
                        }else{
                            Log.d(TAG, "FALLIMENTO creazione richiesta");
                            task_state = "ERROR";
                            //EspressoIdlingResource.decrement();
                        }
                    }
                });

            }

        });

        Thread.sleep(3000);

        assertEquals("OK", task_state);
    }

    @Test
    public void RichiesteIscrizioneTestErroreCodiceNull() throws ExecutionException, InterruptedException {

        FirebaseAuth mauth = FirebaseAuth.getInstance();
        mauth.signOut();

        String mail = "studenteProva@studenti.unisannio.it";
        String pwd = "passwordProva";

        mauth.signInWithEmailAndPassword(mail, pwd).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // altrimenti va avanti e carica la richiesta nel db
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference richiesteIscrizione = db.collection("Richieste_Iscrizione");

                EspressoIdlingResource.increment();

                // allora inviare la richiesta e caricarla nel db
                Map<String, Object> obj = new HashMap<>();
                //obj.put("codice_corso", ""); NON INSERIAMO IL CODICE CORSO
                obj.put("data", new Timestamp(Calendar.getInstance().getTime()));
                obj.put("stato_richiesta", "in attesa");
                obj.put("studente", id_studente);


                richiesteIscrizione.document("id_richiesta_prova").set(obj).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "creazione richiesta ok");
                            task_state = "OK";
                            //EspressoIdlingResource.decrement();
                        }else{
                            Log.d(TAG, "FALLIMENTO creazione richiesta per via del codice null");
                            task_state = "ERROR";
                            //EspressoIdlingResource.decrement();
                        }
                    }
                });

            }

        });

        Thread.sleep(3000);

        assertEquals("ERROR", task_state);
    }

    @Test
    public void RichiesteIscrizioneTestErroreDataNull() throws ExecutionException, InterruptedException {

        FirebaseAuth mauth = FirebaseAuth.getInstance();
        mauth.signOut();

        String mail = "studenteProva@studenti.unisannio.it";
        String pwd = "passwordProva";

        mauth.signInWithEmailAndPassword(mail, pwd).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // altrimenti va avanti e carica la richiesta nel db
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference richiesteIscrizione = db.collection("Richieste_Iscrizione");

                EspressoIdlingResource.increment();

                // allora inviare la richiesta e caricarla nel db
                Map<String, Object> obj = new HashMap<>();
                obj.put("codice_corso", "");
                //obj.put("data", new Timestamp(Calendar.getInstance().getTime())); NON INSERIAMO LA DATA
                obj.put("stato_richiesta", "in attesa");
                obj.put("studente", id_studente);


                richiesteIscrizione.document("id_richiesta_prova").set(obj).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "creazione richiesta ok");
                            task_state = "OK";
                            //EspressoIdlingResource.decrement();
                        }else{
                            Log.d(TAG, "FALLIMENTO creazione richiesta per via della data null");
                            task_state = "ERROR";
                            //EspressoIdlingResource.decrement();
                        }
                    }
                });

            }

        });

        Thread.sleep(3000);

        assertEquals("ERROR", task_state);
    }

    @Test
    public void RichiesteIscrizioneTestErroreStatoSbagliato() throws ExecutionException, InterruptedException {

        FirebaseAuth mauth = FirebaseAuth.getInstance();
        mauth.signOut();

        String mail = "studenteProva@studenti.unisannio.it";
        String pwd = "passwordProva";

        mauth.signInWithEmailAndPassword(mail, pwd).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // altrimenti va avanti e carica la richiesta nel db
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference richiesteIscrizione = db.collection("Richieste_Iscrizione");

                EspressoIdlingResource.increment();

                // allora inviare la richiesta e caricarla nel db
                Map<String, Object> obj = new HashMap<>();
                obj.put("codice_corso", "");
                obj.put("data", new Timestamp(Calendar.getInstance().getTime()));
                obj.put("stato_richiesta", "stato diverso da in attesa"); //STATO DIVERSO DA IN ATTESA
                obj.put("studente", id_studente);


                richiesteIscrizione.document("id_richiesta_prova").set(obj).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "creazione richiesta ok");
                            task_state = "OK";
                            //EspressoIdlingResource.decrement();
                        }else{
                            Log.d(TAG, "FALLIMENTO creazione richiesta per via dello stato richiesta sbagliato");
                            task_state = "ERROR";
                            //EspressoIdlingResource.decrement();
                        }
                    }
                });

            }

        });

        Thread.sleep(3000);

        assertEquals("ERROR", task_state);
    }

    @Test
    public void RichiesteIscrizioneTestErroreIdStudenteSbagliato() throws ExecutionException, InterruptedException {

        FirebaseAuth mauth = FirebaseAuth.getInstance();
        mauth.signOut();

        String mail = "studenteProva@studenti.unisannio.it";
        String pwd = "passwordProva";

        mauth.signInWithEmailAndPassword(mail, pwd).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // altrimenti va avanti e carica la richiesta nel db
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference richiesteIscrizione = db.collection("Richieste_Iscrizione");

                EspressoIdlingResource.increment();

                // allora inviare la richiesta e caricarla nel db
                Map<String, Object> obj = new HashMap<>();
                obj.put("codice_corso", "");
                obj.put("data", new Timestamp(Calendar.getInstance().getTime()));
                obj.put("stato_richiesta", "in attesa");
                obj.put("studente", "id studente sbagliato"); //INSERIAMO UN ID UTENTE SBAGLIATO


                richiesteIscrizione.document("id_richiesta_prova").set(obj).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "creazione richiesta ok");
                            task_state = "OK";
                            //EspressoIdlingResource.decrement();
                        }else{
                            Log.d(TAG, "FALLIMENTO creazione richiesta per via dell'id studente sbagliato");
                            task_state = "ERROR";
                            //EspressoIdlingResource.decrement();
                        }
                    }
                });

            }

        });

        Thread.sleep(3000);

        assertEquals("ERROR", task_state);
    }
}
