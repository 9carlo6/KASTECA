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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kasteca.activity.ListaStudentiIscrittiActivity;
import com.kasteca.util.EspressoIdlingResource;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


@LargeTest
public class ListaStudentiIscrittiActivityTestSenzaStudenti {
    private static final String TAG = "DEBUG_RICHIESTA_ISCRIZIONE";

    @Rule
    public ActivityTestRule<ListaStudentiIscrittiActivity> listaStudentiIscrittiActivityActivityTestRule = new ActivityTestRule<>(ListaStudentiIscrittiActivity.class, false, false);

    @Before()
    public void singIn() throws InterruptedException {
        FirebaseAuth mAuth = FirebaseAuth.getInstance(); // crea un istanza di FirebaseAuth (serve per l'autenticazione)
        String mail = "docenteProva@unisannio.it";
        String pwd = "passwordProva";

        // thread non va bene!!! Occorre utilizzare l'interfaccia IdlingResource
        Thread.sleep(1000);

        mAuth.signInWithEmailAndPassword(mail, pwd).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Log.d(TAG, "login ok");

                // bisogna creare il corso
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference corsi = db.collection("Corsi");

                Date date= new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

                Map<String,Object> documentSend= new HashMap<>();
                documentSend.put("anno_accademico", calendar.get(Calendar.YEAR)+"/"+(calendar.get(Calendar.YEAR)+1));
                documentSend.put("codice", "codice");
                documentSend.put("descrizione", "descrizione_prova");
                documentSend.put("docente", "xXqhMcCwc3R5RibdcLtTOuoMVgm1");
                documentSend.put("lista_post", new ArrayList<String>());
                documentSend.put("lista_studenti", new ArrayList<String>());
                documentSend.put("nome_corso", "nome_corso_prova");


                corsi.document("id_corso_prova").set(documentSend).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "Corsi: creazione corso ok");
                        }else{
                            Log.d(TAG, "Corsi: FALLIMENTO creazione corso");
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "login negato");
            }
        });


        // thread non va bene!!! Occorre utilizzare l'interfaccia IdlingResource
        Thread.sleep(3000);
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
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Corsi: ERRORE cancellazione corso");
                    }
                });

        // thread non va bene!!! Occorre utilizzare l'interfaccia IdlingResource
        Thread.sleep(2000);

        FirebaseAuth mauth = FirebaseAuth.getInstance();
        mauth.signOut();

        Thread.sleep(1000);
    }

    @Test()
    public void ListaStudentiIscrittiActivityTestSenzaStudenti() throws InterruptedException {

        Intent i = new Intent();
        i.putExtra("id_corso", "id_corso_prova");
        listaStudentiIscrittiActivityActivityTestRule.launchActivity(i);

        // thread non va bene!!! Occorre utilizzare l'interfaccia IdlingResource
        Thread.sleep(2000);

        ViewInteraction textView = onView(withText("Non ci sono studenti iscritti al corso"));
        textView.check(matches(isDisplayed()));
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