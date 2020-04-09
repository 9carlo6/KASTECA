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
import org.junit.Before;
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

    private String LOG = "DEBUG_RICHIESTA_ISCRIZIONE";
    private String codice_corso_edit_text;
    private String id_studente;
    private int controllo_esistenza_codice; // serve per controllare se il codice che si sta cercando esiste su firebase (0 non esiste, 1 esiste)
    private ArrayList<String> lista_codici_studenti;
    private ArrayList<String> id_richieste_studenti;

    @Rule
    public ActivityTestRule<RichiestaIscrizioneActivity> richiestaIscrizioneActivityActivityTestRule = new ActivityTestRule<>(RichiestaIscrizioneActivity.class);

    @Before
    public void singIn() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance(); // crea un istanza di FirebaseAuth (serve per l'autenticazione)
        String mail = "studenteProva@studenti.unisannio.it";
        String pwd = "passwordProva";
        mAuth.signInWithEmailAndPassword(mail, pwd);
        //Log.d(LOG, "OK SING IN");

        //id_studente = "SotSWWJIZHNALPZ32EAARRed9RG2";
        //codice_corso_edit_text = "codiceProvaTest";

        //lista_codici_studenti = new ArrayList<>();
        //id_richieste_studenti = new ArrayList<>();


    }

    // Test iscrizione corso con inserimento codice di un corso non esistente
    @Test()
    public void RichiestaIscrizioneActivityTestCodiceCorsoNonEsistente() throws InterruptedException {

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.Codice_Corso_Edit_Text),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("codiceProvaTest"), closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.Codice_Corso_Edit_Text), withText("codiceProvaTest"),
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
    public void RichiestaIscrizioneActivityTestCodiceCorsoEsatto() throws InterruptedException {

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.Codice_Corso_Edit_Text),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("so_20"), closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.Codice_Corso_Edit_Text), withText("so_20"),
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
    public void RichiestaIscrizioneActivityTestRichiestaGiaInviata() throws InterruptedException {

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.Codice_Corso_Edit_Text),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("so_20"), closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.Codice_Corso_Edit_Text), withText("so_20"),
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