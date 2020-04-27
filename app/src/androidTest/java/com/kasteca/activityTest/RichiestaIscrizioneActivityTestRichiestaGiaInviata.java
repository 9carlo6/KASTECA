package com.kasteca.activityTest;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kasteca.R;
import com.kasteca.activity.RichiestaIscrizioneActivity;
import com.kasteca.util.EspressoIdlingResource;
import com.google.firebase.Timestamp;

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
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static org.hamcrest.Matchers.is;

@LargeTest
public class RichiestaIscrizioneActivityTestRichiestaGiaInviata {

    private static final String TAG = "DEBUG_RICHIESTA_ISCRIZIONE_GIA_INVIATA";
    private String mailDoc = "docenteProva@unisannio.it";
    private String pwdDoc = "passwordProva";
    private String mailStu = "studenteProva@studenti.unisannio.it";
    private String pwdStu = "passwordProva";
    private String codice_corso = "codice";
    private String id_studente = "SotSWWJIZHNALPZ32EAARRed9RG2";
    private String id_docente = "xXqhMcCwc3R5RibdcLtTOuoMVgm1";


    @Rule
    public ActivityTestRule<RichiestaIscrizioneActivity> richiestaIscrizioneActivityActivityTestRule = new ActivityTestRule<>(RichiestaIscrizioneActivity.class, false, false);

    @Before()
    public void singIn() throws InterruptedException {
        FirebaseAuth mAuth = FirebaseAuth.getInstance(); // crea un istanza di FirebaseAuth (serve per l'autenticazione)

        mAuth.signInWithEmailAndPassword(mailDoc, pwdDoc).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "Login Docente eseguito con successo");

                    // creazione del corso
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
                }else{
                    Log.d(TAG, "Fallimento Login Docente");
                }
            }
        });

        Thread.sleep(5000);

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
                                Log.w(TAG, "Corsi: errore nella cancellazione del corso", e);
                            }
                        });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Fallimento Login Docente");
            }
        });

        Thread.sleep(3000);

        mAuth.signOut();

        Thread.sleep(1000);
    }

    // Test iscrizione corso con inserimento codice di un corso al quale ci si è gia iscritti
    @Test()
    public void RichiestaIscrizioneActivityTestRichiestaGiaInviata() throws InterruptedException {

        Intent i = new Intent();
        i.putExtra("id_studente", id_studente);
        richiestaIscrizioneActivityActivityTestRule.launchActivity(i);

        Thread.sleep(2000);


        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.Codice_Corso_Edit_Text),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText(codice_corso), closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.Codice_Corso_Edit_Text), withText(codice_corso),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText6.perform(pressImeActionButton());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.button2), withText("RICHIEDI ISCRIZIONE"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatButton2.perform(click());

        Thread.sleep(2000);

        ViewInteraction textView = onView(withText("Hai già inviato una richiesta per questo corso"));
        textView.check(matches(isDisplayed()));


        ViewInteraction appCompatButton3 = onView(
                allOf(withId(android.R.id.button3), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                0)));
        appCompatButton3.perform(scrollTo(), click());
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