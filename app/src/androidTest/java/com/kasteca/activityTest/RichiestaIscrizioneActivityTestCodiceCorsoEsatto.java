package com.kasteca.activityTest;

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

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
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
public class RichiestaIscrizioneActivityTestCodiceCorsoEsatto {

    private static final String TAG = "DEBUG_RICHIESTA_ISCRIZIONE";

    @Rule
    public ActivityTestRule<RichiestaIscrizioneActivity> richiestaIscrizioneActivityActivityTestRule = new ActivityTestRule<>(RichiestaIscrizioneActivity.class);

    @Before()
    public void singIn() throws InterruptedException {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getIdlingResource());

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        //EspressoIdlingResource.increment();
        mAuth.signInWithEmailAndPassword("studenteprova@studenti.unisannio.it", "passwordProva").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "Login eseguito con successo");

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference corsi = db.collection("Corsi");

                    Log.d(TAG, "entrato");
                    //EspressoIdlingResource.increment();

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
                                Log.d(TAG, "Corsi: creazione corso ok");
                                //EspressoIdlingResource.decrement();
                            }else{
                                Log.d(TAG, "Corsi: FALLIMENTO creazione corso");
                                //EspressoIdlingResource.decrement();
                            }
                        }
                    });

                }else{
                    Log.d(TAG, "Login fallito");
                    //EspressoIdlingResource.decrement();
                }
            }
        });

        // thread non va bene!!! Occorre utilizzare l'interfaccia IdlingResource
        Thread.sleep(3000);

    }

    @After()
    public void logOut() throws InterruptedException {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getIdlingResource());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference corsi = db.collection("Corsi");

        //EspressoIdlingResource.increment();

        // cancellazione del corso
        corsi.document("id_corso_prova")
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Corsi: cancellazione corso ok");
                        //EspressoIdlingResource.decrement();

                        // cancellazionde della richiesta
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        CollectionReference richieste_iscrizione = db.collection("Richieste_Iscrizione");

                        //EspressoIdlingResource.increment();

                        richieste_iscrizione.whereEqualTo("codice_corso", "codice_corso_prova").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                String id_documento_richiesta_iscrizone = new String();
                                if(task.isSuccessful()){
                                    Log.d(TAG, "Richieste_Iscrizione: get document ok");
                                    //EspressoIdlingResource.decrement();
                                    for(DocumentSnapshot document :  task.getResult()){
                                        id_documento_richiesta_iscrizone = document.getId();
                                    }
                                    FirebaseFirestore db1 = FirebaseFirestore.getInstance();
                                    CollectionReference richieste = db1.collection("Richieste_Iscrizione");

                                    //EspressoIdlingResource.increment();

                                    richieste.document(id_documento_richiesta_iscrizone)
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "Richieste Iscrizione: DocumentSnapshot successfully deleted!");
                                                    //EspressoIdlingResource.decrement();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Richieste Iscrizione: Error deleting document", e);
                                                    //EspressoIdlingResource.decrement();
                                                }
                                            });
                                }else{
                                    Log.w(TAG, "Richieste_Iscrizione: error get document");
                                    //EspressoIdlingResource.decrement();
                                }
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

        // thread non va bene!!! Occorre utilizzare l'interfaccia IdlingResource
        Thread.sleep(5000);
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
        //Thread.sleep(5000);
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