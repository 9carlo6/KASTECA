package com.kasteca.activityTest;

import static org.junit.Assert.*;


import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.kasteca.R;
import com.kasteca.activity.MainActivity;
import com.kasteca.activity.RichiestaIscrizioneActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;



@LargeTest
@RunWith(AndroidJUnit4.class)
public class RichiestaIscrizioneActivityTest {

    private static final String TAG = "DEBUG_RICHIESTA_ISCRIZIONE";
    private String codice_corso_edit_text;
    private String id_studente;
    private int controllo_esistenza_codice; // serve per controllare se il codice che si sta cercando esiste su firebase (0 non esiste, 1 esiste)
    private ArrayList<String> lista_codici_studenti;
    private ArrayList<String> id_richieste_studenti;

    @Rule
    public ActivityTestRule<RichiestaIscrizioneActivity> richiestaIscrizioneActivityActivityTestRule = new ActivityTestRule<>(RichiestaIscrizioneActivity.class);

    @BeforeClass()
    public static void singIn() throws InterruptedException {
        FirebaseAuth mAuth = FirebaseAuth.getInstance(); // crea un istanza di FirebaseAuth (serve per l'autenticazione)
        String mail = "studenteProva@studenti.unisannio.it";
        String pwd = "passwordProva";
        mAuth.signInWithEmailAndPassword(mail, pwd);
        //Log.d(LOG, "OK SING IN");

        //id_studente = "SotSWWJIZHNALPZ32EAARRed9RG2";
        //codice_corso_edit_text = "codiceProvaTest";

        //lista_codici_studenti = new ArrayList<>();
        //id_richieste_studenti = new ArrayList<>();

        // bisogna creare il corso
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference corsi = db.collection("Corsi");

        Map<String, Object> obj = new HashMap<>();
        obj.put("anno_accademico", "anno_accademico_prova" );
        obj.put("codice", "codice_corso_prova");
        obj.put("descrizione", "descrizione_prova");
        obj.put("docente", "docente_prova");
        obj.put("lista_post", new ArrayList<String>());
        obj.put("lista_studenti", new ArrayList<String>());
        obj.put("nome_corso", "nome_corso_prova");

        corsi.document("id_corso_prova").set(obj).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //
                }else{
                    //
                }
            }
        });

        // thread non va bene!!! Occorre utilizzare l'interfaccia IdlingResource
        Thread.sleep(3000);


    }

    // Test iscrizione corso con inserimento codice di un corso non esistente
    @Test()
    public void A_RichiestaIscrizioneActivityTestCodiceCorsoNonEsistente() throws InterruptedException {

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.Codice_Corso_Edit_Text),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("codiceSbagliatoTest"), closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.Codice_Corso_Edit_Text), withText("codiceSbagliatoTest"),
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

        // thread non va bene!!! Occorre utilizzare l'interfaccia IdlingResource
        Thread.sleep(5000);

        ViewInteraction textView = onView(withText("Il codice inserito non appartiene a nessun corso esistente"));
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


    // Test iscrizione corso con inserimento codice esatto
    @Test()
    public void B_RichiestaIscrizioneActivityTestCodiceCorsoEsatto() throws InterruptedException {

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.Codice_Corso_Edit_Text),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("codice_corso_prova"), closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.Codice_Corso_Edit_Text), withText("codice_corso_prova"),
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

        // thread non va bene!!! Occorre utilizzare l'interfaccia IdlingResource
        Thread.sleep(5000);
    }

    // Test iscrizione corso con inserimento codice di un corso al quale ci si è gia iscritti
    @Test()
    public void C_RichiestaIscrizioneActivityTestRichiestaGiaInviata() throws InterruptedException {

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.Codice_Corso_Edit_Text),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("codice_corso_prova"), closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.Codice_Corso_Edit_Text), withText("codice_corso_prova"),
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

        // thread non va bene!!! Occorre utilizzare l'interfaccia IdlingResource
        Thread.sleep(5000);

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


    @AfterClass()
    public static void logOut() throws InterruptedException {
        // bisogna cancellare il corso
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference corsi = db.collection("Corsi");
        corsi.document("id_corso_prova")
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Corsi: DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Corsi: Error deleting document", e);
                    }
                });

        // bisogna cancellare la richiesta
        CollectionReference richieste_iscrizione = db.collection("Richieste_Iscrizione");
        richieste_iscrizione.whereEqualTo("codice_corso", "codice_corso_prova").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                String id_documento_richiesta_iscrizone = new String();
                if(task.isSuccessful()){
                    Log.d(TAG, "Richieste_Iscrizione: get document ok");
                    for(DocumentSnapshot document :  task.getResult()){
                        id_documento_richiesta_iscrizone = document.getId();
                    }
                    FirebaseFirestore db1 = FirebaseFirestore.getInstance();
                    CollectionReference richieste = db1.collection("Richieste_Iscrizione");
                    richieste.document(id_documento_richiesta_iscrizone)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "Richieste Iscrizione: DocumentSnapshot successfully deleted!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Richieste Iscrizione: Error deleting document", e);
                                }
                            });
                }else{
                    Log.w(TAG, "Richieste_Iscrizione: error get document");
                }
            }
        });

        // LOGOUT
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();

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




}