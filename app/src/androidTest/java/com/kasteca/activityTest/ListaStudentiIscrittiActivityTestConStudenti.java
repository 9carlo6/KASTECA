package com.kasteca.activityTest;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kasteca.R;
import com.kasteca.activity.ListaStudentiIscrittiActivity;
import com.kasteca.activity.RichiestaIscrizioneActivity;
import com.kasteca.util.EspressoIdlingResource;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.*;


@LargeTest
public class ListaStudentiIscrittiActivityTestConStudenti {
    private static final String TAG = "DEBUG_LISTA_STUDENTI_ISCRITTI_CON_STUDENTI";

    private String mailDoc = "docenteProva@unisannio.it";
    private String pwdDoc = "passwordProva";
    private String mailStu = "studenteProva@studenti.unisannio.it";
    private String pwdStu = "passwordProva";
    private String codice_corso = "codice";
    private String id_studente = "SotSWWJIZHNALPZ32EAARRed9RG2";
    private String id_docente = "xXqhMcCwc3R5RibdcLtTOuoMVgm1";

    @Rule
    public ActivityTestRule<ListaStudentiIscrittiActivity> listaStudentiIscrittiActivityActivityTestRule = new ActivityTestRule<>(ListaStudentiIscrittiActivity.class, false, false);

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

                            FirebaseAuth mAuth = FirebaseAuth.getInstance(); // crea un istanza di FirebaseAuth (serve per l'autenticazione)
                            mAuth.signOut();

                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            FirebaseAuth mauth = FirebaseAuth.getInstance(); // crea un istanza di FirebaseAuth (serve per l'autenticazione)

                            mauth.signInWithEmailAndPassword(mailStu, pwdStu).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Log.d(TAG, "Login Studente eseguito con successo");

                                    // bisogna iscrivere lo studente al corso
                                    // caricare il corso nella lista dei corsi dello studente in questione
                                    FirebaseFirestore db1 = FirebaseFirestore.getInstance();
                                    CollectionReference studenti = db1.collection("Studenti");
                                    studenti.document(id_studente)
                                            .update("lista_corsi", FieldValue.arrayUnion("id_corso_prova"))
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Log.d(TAG, "caricamento corso nella lista_corsi studente ok");

                                                        FirebaseAuth mAuth = FirebaseAuth.getInstance(); // crea un istanza di FirebaseAuth (serve per l'autenticazione)
                                                        mAuth.signInWithEmailAndPassword(mailDoc, pwdDoc).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                                            @Override
                                                            public void onSuccess(AuthResult authResult) {
                                                                Log.d(TAG, "Login 2 Docente eseguito con successo");

                                                                // caricare lo studente nella lista degli studenti del corso
                                                                FirebaseFirestore db2 = FirebaseFirestore.getInstance();
                                                                CollectionReference corsi = db2.collection("Corsi");
                                                                corsi.document("id_corso_prova")
                                                                        .update("lista_studenti", FieldValue.arrayUnion(id_studente))
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if(task.isSuccessful()){
                                                                                    Log.d(TAG, "caricamento studente nella lista_studenti corso ok");

                                                                                }else{
                                                                                    Log.d(TAG, "ERRORE caricamento studente nella lista_studenti corso");
                                                                                }
                                                                            }
                                                                        });
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.d(TAG, "Fallimento Login 2 Docente");
                                                            }
                                                        });
                                                    }else{
                                                        Log.d(TAG, "ERRORE caricamento corso nella lista_corsi studente");
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
        // cancellare il corso
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference corsi = db.collection("Corsi");

        corsi.document("id_corso_prova")
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Corsi: cancellazione corso ok");

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
                        Log.d(TAG, "Corsi: ERRORE cancellazione corso");
                    }
                });

        // thread non va bene!!! Occorre utilizzare l'interfaccia IdlingResource
        Thread.sleep(4000);
    }


    @Test()
    public void ListaStudentiIscrittiActivityTestConStudenti() throws InterruptedException {

        Intent i = new Intent();
        i.putExtra("id_corso", "id_corso_prova");
        listaStudentiIscrittiActivityActivityTestRule.launchActivity(i);

        Thread.sleep(2000);

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