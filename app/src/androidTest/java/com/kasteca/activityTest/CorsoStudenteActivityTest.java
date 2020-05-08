package com.kasteca.activityTest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

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
import com.kasteca.activity.CorsoStudenteActivity;
import com.kasteca.util.EspressoIdlingResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;

@RunWith(AndroidJUnit4.class)
public class CorsoStudenteActivityTest {
    private String mailStud = "studenteprova@studenti.unisannio.it";
    private String mailDoc = "docenteprova@unisannio.it";
    private String pwdDoc = "passwordProva";


    private ArrayList<String> lista_post = new ArrayList<>();
    private ArrayList<String>  lista_studenti = new ArrayList<>();
    private String nome_corso = "nome_corso_prova";
    private String id_corso = "id_corso_prova";
    private String anno_accademico =  "2019/2020";
    private String codice ="ABC";
    private String descrizione = "descrizione_prova";
    private String docente = "xXqhMcCwc3R5RibdcLtTOuoMVgm1";

    private String testo = "testo di prova";
    private String link = null;
    private String pdf = null;
    private String tag = "esercizio";
    private ArrayList<String>  lista_commenti = new ArrayList<>();
    private Date data = new Date(0);
    private String id_post = "id_post_prova";

    private String id_studente = "SotSWWJIZHNALPZ32EAARRed9RG2";
    private String nome_studente = "NomeProva";
    private String cognome_studente = "CognomeProva";
    private String matricola =  "Matricola";
    private ArrayList<String>  lista_corsi = new ArrayList<>();



    @Rule
    public ActivityTestRule<CorsoStudenteActivity> activityTestRule = new ActivityTestRule<>(CorsoStudenteActivity.class, false, false);

    @Before
    public void setUp() throws Exception {
        String pwdStud = "passwordProva";
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getIdlingResource());
        FirebaseAuth mAuth = FirebaseAuth.getInstance(); // crea un istanza di FirebaseAuth (serve per l'autenticazione)

        // thread non va bene!!! Occorre utilizzare l'interfaccia IdlingResource
        Thread.sleep(1000);

        mAuth.signInWithEmailAndPassword(mailDoc, pwdDoc).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Log.d(TAG, "login docente ok");

                // bisogna creare il corso
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference corsi = db.collection("Corsi");

                Map<String, Object> corso = new HashMap<>();
                corso.put("anno_accademico", anno_accademico);
                corso.put("codice", codice);
                corso.put("descrizione", descrizione);
                corso.put("docente", docente);
                corso.put("lista_post", lista_post);
                corso.put("lista_studenti", lista_studenti);
                corso.put("nome_corso", nome_corso);

                corsi.document(id_corso).set(corso).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "Corsi: creazione corso ok");

                            // bisogna creare il post
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            CollectionReference posts = db.collection("Post");
                            Map<String, Object> post = new HashMap<>();
                            post.put("testo", testo);
                            post.put("link", link);
                            post.put("pdf", pdf);
                            post.put("tag", tag);
                            post.put("lista_commenti", lista_commenti);
                            post.put("data", data);
                            post.put("corso", id_corso);
                            posts.document(id_post).set(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Log.d(TAG, "Post: creazione post ok");
                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        CollectionReference corsi = db.collection("Corsi");
                                        corsi.document(id_corso)
                                                .update("lista_post", FieldValue.arrayUnion(id_post))
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            Log.d(TAG, "caricamento post nella lista_post corso ok");
                                                        }else{
                                                            Log.d(TAG, "ERRORE caricamento post nella lista_post corso");
                                                        }
                                                    }
                                                });
                                    }else{
                                        Log.d(TAG, "Post: FALLIMENTO creazione post");
                                    }
                                }
                            });

                            CollectionReference corsi = db.collection("Corsi");
                            corsi.document(id_studente)
                                    .update("lista_studenti", FieldValue.arrayUnion(id_studente))
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Log.d(TAG, "caricamento studente nella lista_studenti del corso ok");

                                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                CollectionReference studenti = db.collection("Studenti");
                                                studenti.document(id_studente)
                                                        .update("lista_corsi", FieldValue.arrayUnion(id_corso))
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    Log.d(TAG, "caricamento corso nella lista_corsi dello studente ok");
                                                                }else{
                                                                    Log.d(TAG, "ERRORE caricamento corso nella lista_corsi dello studente");
                                                                }
                                                            }
                                                        });
                                            }else{
                                                Log.d(TAG, "ERRORE caricamento studente nella lista_studenti del corso");
                                            }
                                        }
                                    });
                        }else{
                            Log.d(TAG, "Corsi: FALLIMENTO creazione corso");
                        }
                    }
                });
            }
        });

        // thread non va bene!!! Occorre utilizzare l'interfaccia IdlingResource
        Thread.sleep(6000);

        mAuth.signOut();
        mAuth.signInWithEmailAndPassword(mailStud, pwdStud).addOnSuccessListener(new OnSuccessListener<AuthResult>(){
            @Override
            public void onSuccess(AuthResult authResult) {
                Log.d(TAG, "login studente ok");
            }
        });
        // thread non va bene!!! Occorre utilizzare l'interfaccia IdlingResource
        Thread.sleep(2000);


        Intent i = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("id_corso", id_corso);
        bundle.putString("codice_corso", codice);
        bundle.putString("nome_corso", nome_corso);
        bundle.putString("anno_accademico", anno_accademico);

        bundle.putString("docente", docente);

        bundle.putString("id", id_studente);
        bundle.putString("nome", nome_studente);
        bundle.putString("cognome", cognome_studente);
        bundle.putString("email", mailStud);
        bundle.putString("matricola", matricola);
        lista_corsi.add(id_corso);
        bundle.putStringArrayList("id_corsi", lista_corsi);
        i.putExtras(bundle);
        activityTestRule.launchActivity(i);

        // thread non va bene!!! Occorre utilizzare l'interfaccia IdlingResource
        Thread.sleep(2000);
    }

    @After
    public void tearDown() throws Exception {
        FirebaseAuth mAuth = FirebaseAuth.getInstance(); // crea un istanza di FirebaseAuth (serve per l'autenticazione)
        mAuth.signInWithEmailAndPassword(mailDoc, pwdDoc).addOnSuccessListener(new OnSuccessListener<AuthResult>(){
            @Override
            public void onSuccess(AuthResult authResult) {

                // cancellare il corso
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference corsi = db.collection("Corsi");

                corsi.document(id_corso)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "Corsi: cancellazione corso ok");

                                // cancellare il post
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                CollectionReference posts = db.collection("Post");
                                posts.document(id_post)
                                        .delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d(TAG, "Post: cancellazione post ok");
                                                } else {
                                                    Log.d(TAG, "Post: ERRORE cancellazione post");
                                                }
                                            }
                                        });

                                // cancellare il corso dalla lista_corsi a cui lo studente è iscritto
                                CollectionReference studenti = db.collection("Studenti");
                                studenti.document(id_studente)
                                        .update("lista_corsi", FieldValue.arrayRemove(id_corso))
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d(TAG, "Studente: cancellazione corso dalla lista ok");
                                                } else {
                                                    Log.d(TAG, "Studente: ERRORE cancellazione corso dalla lista");
                                                }
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
        });


        // thread non va bene!!! Occorre utilizzare l'interfaccia IdlingResource
        Thread.sleep(4000);
    }


    // Verifica che Activity visibile, con tutti gli elementi grafici previsti
    @Test
    public void test_isCorsoStudenteActivityOnView(){
        onView(withId(R.id.corso_studente_drawer_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.corso_studente_drawer_layout)).check(matches(isClosed(Gravity.LEFT)));

        onView(withId(R.id.toolbar_layout_studente)).check(matches(isDisplayed()));
        onView(withId(R.id.toolbar_studente)).check(matches(isDisplayed()));
        onView(withId(R.id.fab_add_post)).check(doesNotExist());
        onView(withId(R.id.fragment_container_corso_studente)).check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view_post_studente)).check(matches(isDisplayed()));
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.toolbar_studente))))
                .check(matches(withText(nome_corso)));

        FirebaseAuth mAuth = FirebaseAuth.getInstance(); // crea un istanza di FirebaseAuth (serve per l'autenticazione)
        mAuth.signOut();
    }

    // Verifica che la selezione di un elemento porta alla PostActivity, che mostra i dettagli corretti
    @Test
    public void test_SelectItem_isPostActivityVisible(){
        String nome_docente = "NomeDocenteProva";
        String cognome_docente = "CognomeDocenteProva";
        onView(withId(R.id.recycler_view_post_studente)).perform(actionOnItemAtPosition(0, click()));
        onView(withId(R.id.post_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.nome_cognome_textView)).check(matches(withText(nome_docente + " " + cognome_docente)));
        onView(withId(R.id.tagTextView)).check(matches(withText(tag)));
        onView(withId(R.id.testoPostTextView)).check(matches(withText(testo)));
        onView(withId(R.id.data_textView)).check(matches(withText(new SimpleDateFormat(activityTestRule.getActivity().getResources().getString(R.string.formato_data)).format(data))));
        FirebaseAuth mAuth = FirebaseAuth.getInstance(); // crea un istanza di FirebaseAuth (serve per l'autenticazione)
        mAuth.signOut();
    }

    // Verifica della presenza della Navigation Bar con gli elementi corretti
    @Test
    public void test_isNavigationBarStudenteOpenableAndCorrect(){
        onView(withId(R.id.corso_studente_drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.studente_nav_header)).check(matches(isDisplayed()));
        onView(withId(R.id.nome_cognome_nav_header)).check(matches(withText(nome_studente + " " + cognome_studente)));
        onView(withId(R.id.email_nav_header)).check(matches(withText(mailStud)));
        onView(withId(R.id.matricola_nav_header)).check(matches(withText(matricola)));
        onView(withId(R.id.nav_view_corso_studente)).check(matches(isDisplayed()));
        FirebaseAuth mAuth = FirebaseAuth.getInstance(); // crea un istanza di FirebaseAuth (serve per l'autenticazione)
        mAuth.signOut();
    }

    // Verifica che il click su ogni elemento del menu porti alla Activity giusta
    @Test
    public void test_SelectItem_isTheCorrectStudenteActivityVisible() throws InterruptedException {
        onView(withId(R.id.corso_studente_drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view_corso_studente)).perform(NavigationViewActions.navigateTo(R.id.nav_post_corso));
        Thread.sleep(3000);
        onView(withId(R.id.fragment_container_corso_studente)).check(matches(isDisplayed()));
        Thread.sleep(1000);

        onView(withId(R.id.nav_view_corso_studente)).perform(NavigationViewActions.navigateTo(R.id.nav_cancellazione_dal_corso));
        Thread.sleep(3000);
        onView(withText(activityTestRule.getActivity().getResources().getString(R.string.Dialog_titolo_conferma_cancellazione))).check(matches(isDisplayed()));
        pressBack();
        Thread.sleep(1000);

        onView(withId(R.id.corso_studente_drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view_corso_studente)).perform(NavigationViewActions.navigateTo(R.id.nav_logout));
        Thread.sleep(3000);
        onView(withId(R.id.main)).check(matches(isDisplayed()));

    }
}