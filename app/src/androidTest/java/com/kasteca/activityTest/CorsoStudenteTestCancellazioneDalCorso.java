package com.kasteca.activityTest;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kasteca.R;
import com.kasteca.activity.CorsoStudenteActivity;
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
import java.util.HashMap;
import java.util.Map;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;


@LargeTest
public class CorsoStudenteTestCancellazioneDalCorso {
    private static final String TAG = "DEBUG_RICHIESTA_ISCRIZIONE";

    @Rule
    public ActivityTestRule<CorsoStudenteActivity> corsoStudenteActivityActivityTestRule = new ActivityTestRule<>(CorsoStudenteActivity.class, false, false);

    @Before()
    public void singIn() throws InterruptedException {
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
                }else{
                    Log.d(TAG, "Corsi: FALLIMENTO creazione corso");
                }
            }
        });

        // bisogna iscrivere lo studente al corso
        // caricare il corso nella lista dei corsi dello studente in questione
        FirebaseFirestore db1 = FirebaseFirestore.getInstance();
        CollectionReference studenti = db1.collection("Studenti");
        studenti.document("SotSWWJIZHNALPZ32EAARRed9RG2")
                .update("lista_corsi", FieldValue.arrayUnion("id_corso_prova"))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "caricamento corso nella lista_corsi studente ok");
                            // caricare lo studente nella lista degli studenti del corso
                            FirebaseFirestore db2 = FirebaseFirestore.getInstance();
                            CollectionReference corsi = db2.collection("Corsi");
                            corsi.document("id_corso_prova")
                                    .update("lista_studenti", FieldValue.arrayUnion("SotSWWJIZHNALPZ32EAARRed9RG2"))
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
                        }else{
                            Log.d(TAG, "ERRORE caricamento corso nella lista_corsi studente");
                        }
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

        // cancellare il corso nella lista dei corsi dello studente in questione
        FirebaseFirestore db1 = FirebaseFirestore.getInstance();
        CollectionReference studenti = db1.collection("Studenti");
        studenti.document("SotSWWJIZHNALPZ32EAARRed9RG2")
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

        // thread non va bene!!! Occorre utilizzare l'interfaccia IdlingResource
        Thread.sleep(3000);
    }

    @Test()
    public void CorsoStudenteTestCancellazioneDalCorsoConfermata() throws InterruptedException {

        //intent per l'Activity del corso
        Intent intent= new Intent();
        Bundle bundle= new Bundle();

        //Passiamo all'activity del corso il codice del documento firebase del corso
        //in modo che possa recuperarlo autonomamente.
        bundle.putString("id_corso","id_corso_prova");
        bundle.putString("codice_corso", "codice_corso_prova");
        bundle.putString("nome_corso", "nome_corso_prova");
        bundle.putString("anno_accademico", "anno_accademico_prova");
        bundle.putString("docente", "xXqhMcCwc3R5RibdcLtTOuoMVgm1");
        bundle.putString("id", "SotSWWJIZHNALPZ32EAARRed9RG2");
        bundle.putString("nome", "NomeProva");
        bundle.putString("cognome", "CognomeProva");
        bundle.putString("email", "studenteProva@studenti.unisannio.it");
        bundle.putString("matricola", "MatricolaProva");

        intent.putExtras(bundle);
        corsoStudenteActivityActivityTestRule.launchActivity(intent);


        // thread non va bene!!! Occorre utilizzare l'interfaccia IdlingResource
        Thread.sleep(3000);

        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar_studente)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction textView = onView(withText("Abbandona il Corso"));
        textView.perform(click());

        ViewInteraction appCompatButton = onView(withText("Si"));
        appCompatButton.perform(click());

        Thread.sleep(1000);

        ViewInteraction appCompatButton2 = onView(withText("Ok"));
        appCompatButton2.perform(click());
    }

    @Test()
    public void CorsoStudenteTestCancellazioneDalCorsoNegata() throws InterruptedException {

        //intent per l'Activity del corso
        Intent intent= new Intent();
        Bundle bundle= new Bundle();

        //Passiamo all'activity del corso il codice del documento firebase del corso
        //in modo che possa recuperarlo autonomamente.
        bundle.putString("id_corso","id_corso_prova");
        bundle.putString("codice_corso", "codice_corso_prova");
        bundle.putString("nome_corso", "nome_corso_prova");
        bundle.putString("anno_accademico", "anno_accademico_prova");
        bundle.putString("docente", "xXqhMcCwc3R5RibdcLtTOuoMVgm1");
        bundle.putString("id", "SotSWWJIZHNALPZ32EAARRed9RG2");
        bundle.putString("nome", "NomeProva");
        bundle.putString("cognome", "CognomeProva");
        bundle.putString("email", "studenteProva@studenti.unisannio.it");
        bundle.putString("matricola", "MatricolaProva");

        intent.putExtras(bundle);
        corsoStudenteActivityActivityTestRule.launchActivity(intent);

        // thread non va bene!!! Occorre utilizzare l'interfaccia IdlingResource
        Thread.sleep(5000);

        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar_studente)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction textView = onView(withText("Abbandona il Corso"));
        textView.perform(click());

        ViewInteraction appCompatButton = onView(withText("No"));
        appCompatButton.perform(click());


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