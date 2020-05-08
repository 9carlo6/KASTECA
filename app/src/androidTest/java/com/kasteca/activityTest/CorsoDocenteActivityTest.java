package com.kasteca.activityTest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
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
import com.kasteca.activity.CorsoDocenteActivity;
import com.kasteca.util.EspressoIdlingResource;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
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
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;

@RunWith(AndroidJUnit4.class)
public class CorsoDocenteActivityTest {
    private String mail = "docenteprova@unisannio.it";
    private String pwd = "passwordProva";

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

    private String nome_docente = "NomeDocenteProva";
    private String cognome_docente = "CognomeDocenteProva";

    @Rule
    public ActivityTestRule<CorsoDocenteActivity> activityTestRule = new ActivityTestRule<>(CorsoDocenteActivity.class, false, false);

    @Before
    public void setUp() throws InterruptedException {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getIdlingResource());
        FirebaseAuth mAuth = FirebaseAuth.getInstance(); // crea un istanza di FirebaseAuth (serve per l'autenticazione)


        mAuth.signInWithEmailAndPassword(mail, pwd).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Log.d(TAG, "login docente ok");

                // bisogna creare il corso
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference corsi = db.collection("Corsi");

                Map<String, Object> corso = new HashMap<>();
                corso.put("anno_accademico", anno_accademico );
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

                        }else{
                            Log.d(TAG, "Corsi: FALLIMENTO creazione corso");
                        }
                    }
                });
            }
        });

        Thread.sleep(6000);


        Intent i = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("id_corso", id_corso);
        bundle.putString("codice_corso", codice);
        bundle.putString("nome_corso", nome_corso);
        bundle.putString("anno_accademico", anno_accademico);

        bundle.putString("id_docente", docente);
        bundle.putString("nome_docente", nome_docente);
        bundle.putString("cognome_docente", cognome_docente);
        bundle.putString("email_docente", mail);
        i.putExtras(bundle);
        activityTestRule.launchActivity(i);

    }

    @After
    public void tearDown() throws InterruptedException {

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

                            // cancellare il corso
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            CollectionReference corsi = db.collection("Corsi");

                            corsi.document(id_corso)
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
                        } else {
                            Log.d(TAG, "Post: ERRORE cancellazione post");
                        }
                    }
                });

        Thread.sleep(2000);
    }


    // Verifica che Activity visibile, con tutti gli elementi grafici previsti
    @Test
    public void test_isCorsoDocenteActivityOnView(){
        onView(withId(R.id.corso_drawer_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.corso_drawer_layout)).check(matches(isClosed(Gravity.LEFT)));

        onView(withId(R.id.toolbar_layout_docente)).check(matches(isDisplayed()));
        onView(withId(R.id.toolbar_docente)).check(matches(isDisplayed()));
        onView(withId(R.id.fab_add_post)).check(matches(isDisplayed()));
        onView(withId(R.id.fragment_container_corso_docente)).check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view_post_docente)).check(matches(isDisplayed()));
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.toolbar_docente))))
                .check(matches(withText(nome_corso)));
    }

    // Verifica che la selezione di un elemento porta alla PostActivity, che mostra i dettagli corretti
    @Test
    public void test_SelectItem_isPostActivityVisible(){
        onView(withId(R.id.recycler_view_post_docente)).perform(actionOnItemAtPosition(0, click()));
        onView(withId(R.id.post_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.nome_cognome_textView)).check(matches(withText(nome_docente + " " + cognome_docente)));
        onView(withId(R.id.tagTextView)).check(matches(withText(tag)));
        onView(withId(R.id.testoPostTextView)).check(matches(withText(testo)));
        onView(withId(R.id.data_textView)).check(matches(withText(new SimpleDateFormat(activityTestRule.getActivity().getResources().getString(R.string.formato_data)).format(data))));
    }

    // Verifica della presenza della Navigation Bar con gli elementi corretti
    @Test
    public void test_isNavigationBarDocenteOpenableAndCorrect(){
        onView(withId(R.id.corso_drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.docente_nav_header)).check(matches(isDisplayed()));
        onView(withId(R.id.nome_cognome_nav_header)).check(matches(withText(nome_docente + " " + cognome_docente)));
        onView(withId(R.id.email_nav_header)).check(matches(withText(mail)));
        onView(withId(R.id.nav_view_corso_docente)).check(matches(isDisplayed()));
    }

    // Verifica della presenza della Navigation Bar con gli elementi corretti
    @Test
    public void test_SelectItem_isTheCorrectDocenteActivityVisible() throws InterruptedException {

        // Verifica 1
        onView(allOf(withContentDescription("Open navigation drawer"), isDisplayed())).perform(click());
        onView(allOf(childAtPosition(
                allOf(withId(R.id.design_navigation_view),
                        childAtPosition(withId(R.id.nav_view_corso_docente),
                                0)),
                1),
                isDisplayed())).perform(click());
        onView(allOf(withId(R.id.corso_drawer_layout))).check(matches(isDisplayed()));

        // Verifica 2
        onView(allOf(withContentDescription("Open navigation drawer"), isDisplayed())).perform(click());
        onView(allOf(childAtPosition(
                allOf(withId(R.id.design_navigation_view),
                        childAtPosition(withId(R.id.nav_view_corso_docente),
                                0)),
                2),
                isDisplayed())).perform(click());

        Thread.sleep(3000);

        onView(allOf(IsInstanceOf.<View>instanceOf(android.widget.TextView.class), withText(activityTestRule.getActivity().getResources().getString(R.string.nessuno_studente_iscritto)),
                        isDisplayed())).check(matches(withText("Non ci sono studenti iscritti al corso")));

        pressBack();
        Thread.sleep(1000);

        //Verifica 3
        onView(allOf(withContentDescription("Open navigation drawer"), isDisplayed())).perform(click());
        onView(allOf(childAtPosition(
                allOf(withId(R.id.design_navigation_view),
                        childAtPosition(withId(R.id.nav_view_corso_docente),
                                0)),
                3),
                isDisplayed())).perform(click());

        Thread.sleep(1000);

        onView(allOf(IsInstanceOf.<View>instanceOf(android.widget.TextView.class), withText("Non ci sono nuove richieste"),
                        isDisplayed())).check(matches(withText("Non ci sono nuove richieste")));

        pressBack();
        Thread.sleep(1000);

        // Verifica 4
        onView(allOf(withContentDescription("Open navigation drawer"), isDisplayed())).perform(click());
        onView(allOf(childAtPosition(
                allOf(withId(R.id.design_navigation_view),
                        childAtPosition(withId(R.id.nav_view_corso_docente),
                                0)),
                4),
                isDisplayed())).perform(click());

        Thread.sleep(1000);

        onView(allOf(withId(R.id.main), isDisplayed())).check(matches(isDisplayed()));

        FirebaseAuth mAuth = FirebaseAuth.getInstance(); // crea un istanza di FirebaseAuth (serve per l'autenticazione)

        mAuth.signInWithEmailAndPassword(mail, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "login ok");
            }
        });

        // thread non va bene!!! Occorre utilizzare l'interfaccia IdlingResource
        Thread.sleep(3000);
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


    // Verifica che cliccando sul Floating Button si apra la NewPostActivity
    @Test
    public void test_ClickFab_isNewPostActivityVisible(){
        onView(withId(R.id.fab_add_post)).perform(click());
        onView(withId(R.id.new_post_layout)).check(matches(isDisplayed()));
    }
}