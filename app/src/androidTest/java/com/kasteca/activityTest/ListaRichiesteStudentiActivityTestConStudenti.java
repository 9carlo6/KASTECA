package com.kasteca.activityTest;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kasteca.R;
import com.kasteca.activity.InfoRichiestaStudenteActivity;
import com.kasteca.activity.ListaRichiesteStudentiActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


@LargeTest
public class ListaRichiesteStudentiActivityTestConStudenti {
    private static final String TAG = "DEBUG_INFO_RICHIESTA_STUDENTE_TEST";

    private String mailDoc = "docenteProva@unisannio.it";
    private String pwdDoc = "passwordProva";
    private String mailStu = "studenteProva@studenti.unisannio.it";
    private String pwdStu = "passwordProva";
    private String codice_corso = "codice";
    private String id_studente = "SotSWWJIZHNALPZ32EAARRed9RG2";
    private String id_docente = "xXqhMcCwc3R5RibdcLtTOuoMVgm1";

    @Rule
    public ActivityTestRule<ListaRichiesteStudentiActivity> listaRichiesteStudentiActivityActivityTestRule = new ActivityTestRule<>(ListaRichiesteStudentiActivity.class, false, false);

    @Before()
    public void singIn() throws InterruptedException {
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

                            FirebaseAuth mauth = FirebaseAuth.getInstance();
                            mauth.signOut();

                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            FirebaseAuth mAuth = FirebaseAuth.getInstance(); // crea un istanza di FirebaseAuth (serve per l'autenticazione)

                            mAuth.signInWithEmailAndPassword(mailStu, pwdStu).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Log.d(TAG, "Login Studente eseguito con successo");

                                    // creazione della richiesta
                                    FirebaseFirestore db1 = FirebaseFirestore.getInstance();
                                    CollectionReference richieste_iscrizione = db1.collection("Richieste_Iscrizione");

                                    // allora inviare la richiesta e caricarla nel db
                                    Map<String, Object> obj2 = new HashMap<>();
                                    obj2.put("codice_corso", codice_corso);
                                    obj2.put("data", new Timestamp(Calendar.getInstance().getTime()));
                                    obj2.put("stato_richiesta", "in attesa");
                                    obj2.put("studente", "SotSWWJIZHNALPZ32EAARRed9RG2");

                                    richieste_iscrizione.document("id_richiesta_prova").set(obj2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Log.d(TAG, "Richieste_Iscrizione: creazione richiesta ok");
                                            }else{
                                                Log.d(TAG, "Richieste_Iscrizione: FALLIMENTO creazione richiesta");
                                            }
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Fallimento Login Studente");
                                }
                            });
                        }else{
                            Log.d(TAG, "Corsi: FALLIMENTO creazione corso");
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Fallimento Login Docente");
            }
        });

        // thread non va bene!!! Occorre utilizzare l'interfaccia IdlingResource
        Thread.sleep(10000);
    }

    @After()
    public void logOut() throws InterruptedException {
        Thread.sleep(1000);

        FirebaseAuth mAuth = FirebaseAuth.getInstance(); // crea un istanza di FirebaseAuth (serve per l'autenticazione)
        mAuth.signOut();

        Thread.sleep(1000);

        mAuth.signInWithEmailAndPassword(mailDoc, pwdDoc).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Log.d(TAG, "Login Docente eseguito con successo");

                // cancellare il corso
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference corsi = db.collection("Corsi");

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

                                                FirebaseAuth mAuth = FirebaseAuth.getInstance(); // crea un istanza di FirebaseAuth (serve per l'autenticazione)
                                                mAuth.signOut();

                                                try {
                                                    Thread.sleep(1000);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }

                                                mAuth.signInWithEmailAndPassword(mailStu, pwdStu).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                                    @Override
                                                    public void onSuccess(AuthResult authResult) {
                                                        Log.d(TAG, "Login Studente 2 eseguito con successo");

                                                        // cancellare il corso nella lista dei corsi dello studente in questione
                                                        FirebaseFirestore db1 = FirebaseFirestore.getInstance();
                                                        CollectionReference studenti = db1.collection("Studenti");
                                                        studenti.document(id_studente)
                                                                .update("lista_corsi", null)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            Log.d(TAG, "Corsi: cancellazione corso dalla lista dei corsi dello studente ok");
                                                                        } else {
                                                                            Log.d(TAG, "Corsi: ERRORE cancellazione corso dalla lista dei corsi dello studente");
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.d(TAG, "Fallimento Login 2 Studente");
                                                    }
                                                });

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Richieste_Iscrizione: errore nella cancellazione della richiesta", e);
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Corsi: ERRORE cancellazione corso");
                            }
                        });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Fallimento Login Docente");
            }
        });

        // thread non va bene!!! Occorre utilizzare l'interfaccia IdlingResource
        Thread.sleep(4000);
    }

    @Test()
    public void ListaRichiesteStudentiActivityTestConStudenti() throws InterruptedException {

        Intent i = new Intent();
        i.putExtra("codice_corso", codice_corso);

        listaRichiesteStudentiActivityActivityTestRule.launchActivity(i);

        Thread.sleep(2000);

        onView(withId(R.id.rv_lista_richieste_studenti)).perform(swipeDown());

        Thread.sleep(6000);

        ViewInteraction textView1 = onView(withText("NomeProva CognomeProva"));
        textView1.check(matches(isDisplayed()));
    }


    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }



}