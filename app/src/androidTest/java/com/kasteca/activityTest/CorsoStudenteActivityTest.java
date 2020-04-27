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
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class CorsoStudenteActivityTest {
    @Rule
    public ActivityTestRule<CorsoStudenteActivity> activityTestRule = new ActivityTestRule<>(CorsoStudenteActivity.class, false, false);

    @Before
    public void setUp() throws Exception {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getIdlingResource());
        FirebaseAuth mAuth = FirebaseAuth.getInstance(); // crea un istanza di FirebaseAuth (serve per l'autenticazione)
        String mail = "studenteprova@studenti.unisannio.it";
        String pwd = "passwordProva";

        // thread non va bene!!! Occorre utilizzare l'interfaccia IdlingResource
        Thread.sleep(1000);

        mAuth.signInWithEmailAndPassword(mail, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "login ok");
            }
        });

        // bisogna creare il corso
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference corsi = db.collection("Corsi");

        Map<String, Object> corso = new HashMap<>();
        corso.put("anno_accademico", "anno_accademico_prova" );
        corso.put("codice", "codice_corso_prova");
        corso.put("descrizione", "descrizione_prova");
        corso.put("docente", "xXqhMcCwc3R5RibdcLtTOuoMVgm1");
        corso.put("lista_post", new ArrayList<String>());
        ArrayList<String> studentiCorso = new ArrayList<>();
        studentiCorso.add("SotSWWJIZHNALPZ32EAARRed9RG2");
        corso.put("lista_studenti", studentiCorso);
        corso.put("nome_corso", "nome_corso_prova");

        corsi.document("id_corso_prova").set(corso).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "Corsi: creazione corso ok");
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference studenti = db.collection("Studenti");
                    studenti.document("SotSWWJIZHNALPZ32EAARRed9RG2")
                            .update("lista_corsi", FieldValue.arrayUnion("id_corso_prova"))
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
                    Log.d(TAG, "Corsi: FALLIMENTO creazione corso");
                }
            }
        });

        // bisogna creare il post
        CollectionReference posts = db.collection("Post");
        Map<String, Object> post = new HashMap<>();
        post.put("testo", "testo di prova" );
        post.put("link", "");
        post.put("pdf", "");
        post.put("tag", "esercizio");
        post.put("lista_commenti", new ArrayList<String>());
        post.put("data", new Date(0));
        post.put("corso", "id_corso_prova");
        posts.document("id_post_prova").set(post).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "Post: creazione post ok");
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference corsi = db.collection("Corsi");
                    corsi.document("id_corso_prova")
                            .update("lista_post", FieldValue.arrayUnion("id_post_prova"))
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

        // thread non va bene!!! Occorre utilizzare l'interfaccia IdlingResource
        Thread.sleep(4000);

        Intent i = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("id_corso", "id_corso_prova");
        bundle.putString("codice_corso", "codice_corso_prova");
        bundle.putString("nome_corso", "nome_corso_prova");
        bundle.putString("anno_accademico", "anno_accademico_prova");

        bundle.putString("docente", "xXqhMcCwc3R5RibdcLtTOuoMVgm1");

        bundle.putString("id", "SotSWWJIZHNALPZ32EAARRed9RG2");
        bundle.putString("nome", "NomeProva");
        bundle.putString("cognome", "CognomeProva");
        bundle.putString("email", "studenteprova@studenti.unisannio.it");
        bundle.putString("matricola", "MatricolaProva");
        ArrayList<String> corsiStudente= new ArrayList<>();
        corsiStudente.add("id_corso_prova");
        bundle.putStringArrayList("id_corsi", corsiStudente);
        i.putExtras(bundle);
        activityTestRule.launchActivity(i);

        // thread non va bene!!! Occorre utilizzare l'interfaccia IdlingResource
        Thread.sleep(2000);
    }

    @After
    public void tearDown() throws Exception {
        // cancellare il corso
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference corsi = db.collection("Corsi");

        corsi.document("id_corso_prova")
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Corsi: cancellazione corso ok");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Corsi: ERRORE cancellazione corso");
                    }
                });

        // cancellare il post
        CollectionReference posts = db.collection("Post");
        posts.document("id_post_prova")
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
        studenti.document("SotSWWJIZHNALPZ32EAARRed9RG2")
                .update("lista_corsi", FieldValue.arrayRemove("id_corso_prova"))
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

        // thread non va bene!!! Occorre utilizzare l'interfaccia IdlingResource
        Thread.sleep(4000);
    }


    // Verifica che Activity visibile, con tutti gli elementi grafici previsti
    @Test
    public void test_isActivityOnView(){
        onView(withId(R.id.corso_studente_drawer_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.corso_studente_drawer_layout)).check(matches(isClosed(Gravity.LEFT)));

        onView(withId(R.id.toolbar_layout_studente)).check(matches(isDisplayed()));
        onView(withId(R.id.toolbar_studente)).check(matches(isDisplayed()));
        onView(withId(R.id.fab_add_post)).check(doesNotExist());
        onView(withId(R.id.fragment_container_corso_studente)).check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view_post_studente)).check(matches(isDisplayed()));
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.toolbar_studente))))
                .check(matches(withText("nome_corso_prova")));
    }

    // Verifica che la selezione di un elemento porta alla PostActivity, che mostra i dettagli corretti
    @Test
    public void test_SelectItem_isPostActivityVisible(){
        onView(withId(R.id.recycler_view_post_studente)).perform(actionOnItemAtPosition(0, click()));
        onView(withId(R.id.post_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.nome_cognome_textView)).check(matches(withText("NomeDocenteProva CognomeDocenteProva")));
        onView(withId(R.id.tagTextView)).check(matches(withText("esercizio")));
        onView(withId(R.id.testoPostTextView)).check(matches(withText("testo di prova")));
        onView(withId(R.id.data_textView)).check(matches(withText(new SimpleDateFormat(activityTestRule.getActivity().getResources().getString(R.string.formato_data)).format(new Date(0)))));
    }

    // Verifica della presenza della Navigation Bar con gli elementi corretti
    @Test
    public void test_isNavigationBarOpenableAndCorrect(){
        onView(withId(R.id.corso_studente_drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.studente_nav_header)).check(matches(isDisplayed()));
        onView(withId(R.id.nome_cognome_nav_header)).check(matches(withText("NomeProva CognomeProva")));
        onView(withId(R.id.email_nav_header)).check(matches(withText("studenteprova@studenti.unisannio.it")));
        onView(withId(R.id.matricola_nav_header)).check(matches(withText("MatricolaProva")));
        onView(withId(R.id.nav_view_corso_studente)).check(matches(isDisplayed()));
    }

    // Verifica che il click su ogni elemento del menu porti alla Activity giusta
    @Test
    public void test_SelectItem_isTheCorrectActivityVisible(){
        onView(withId(R.id.corso_studente_drawer_layout)).perform(DrawerActions.open());

        onView(withId(R.id.nav_view_corso_studente)).perform(NavigationViewActions.navigateTo(R.id.nav_post_corso));
        onView(withId(R.id.fragment_container_corso_studente)).check(matches(isDisplayed()));

        onView(withId(R.id.nav_view_corso_studente)).perform(NavigationViewActions.navigateTo(R.id.nav_cancellazione_dal_corso));
        onView(withText(activityTestRule.getActivity().getResources().getString(R.string.Dialog_titolo_conferma_cancellazione))).check(matches(isDisplayed()));
        pressBack();

        onView(withId(R.id.corso_studente_drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view_corso_studente)).perform(NavigationViewActions.navigateTo(R.id.nav_logout));
        onView(withId(R.id.main)).check(matches(isDisplayed()));
    }
}