package com.kasteca.activityTest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Toast;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kasteca.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.contrib.DrawerMatchers.isOpen;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;




import androidx.annotation.NonNull;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.kasteca.R;
import com.kasteca.activity.LogDocenteActivity;
import com.kasteca.fragment.CorsiDocenteFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;


@LargeTest
public class LogDocenteActivityTest {
    private final String TAG="Testing-LogDocenteActivity";

    private String mailDoc = "docenteProva@unisannio.it";
    private String pwdDoc = "passwordProva";
    private String nameDocente = "NomeDocenteProva";
    private String surnameDocente = "CognomeDocenteProva";
    private String idDocente = "xXqhMcCwc3R5RibdcLtTOuoMVgm1";

    private String idCorso;


    @Rule
    public ActivityTestRule<LogDocenteActivity> mActivityTestRule = new ActivityTestRule<>(LogDocenteActivity.class, true, false);


    @Before
    public void signInDocente()  throws InterruptedException {
        //Effettuiamo il logic del docente
        login();
        //Attesa Login
        Thread.sleep(5000);
    }

    @After
    public void signOut() throws InterruptedException{
        cancellazioneCorso();
        logout();
    }

    @Test
    public void controlloLayoutTest() throws InterruptedException{
        //Creo l'intent e lancio l'activity con i dati prelevati dal login
        Intent i = new Intent();
        Bundle docente = new Bundle();
        docente.putString("id", idDocente.toString());
        docente.putString("nome", nameDocente.toString());
        docente.putString("cognome", surnameDocente.toString());
        docente.putString("email", mailDoc.toString());

        i.putExtras(docente);
        mActivityTestRule.launchActivity(i);
        Thread.sleep(2000);

        //verifico che l'interfaccia sia stata caricata correttamente
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withId(R.id.fragment_container_docente)).check(matches(isDisplayed()));
        //onView(withId(R.id.nav_view_docente)).check(matches(isDisplayed()));

        //verifico la navigationview
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.drawer_layout)).check(matches(isOpen()));
        //Here's the difference
        //onView(withId(R.id.nav_view_docente)).perform(NavigationViewActions.navigateTo(R.id.nav_scroller));


        //verifico la presenza delle opzioni della navigation view
        onView(withId(R.id.nav_view_docente)).check(matches(isDisplayed()));

        onView(withText("Corsi"))
                .check(matches(isDisplayed()));
        onView(withText("Logout"))
                .check(matches(isDisplayed()));
        onView(withText("Aggiungi Corso"))
                .check(matches(isDisplayed()));
        onView(withText(mailDoc))
                .check(matches(isDisplayed()));

        /* onView(withId(R.id.nav_view_docente))
                .perform(NavigationViewActions.navigateTo(R.id.nav_corsi_docente));//.check(matches(isDisplayed()))
        onView(withId(R.id.nav_view_docente))
                .perform(NavigationViewActions.navigateTo(R.id.nav_aggiungi_corso));
        onView(withId(R.id.nav_view_docente))
                .perform(NavigationViewActions.navigateTo(R.id.nav_logout));*/

    }

    @Test
    public void aggiornamentoRecycleViewTest() throws InterruptedException{
        //Aggiungo un corso dopo aver caricato l'activity
        Intent i = new Intent();
        Bundle docente = new Bundle();
        docente.putString("id", idDocente.toString());
        docente.putString("nome", nameDocente.toString());
        docente.putString("cognome", surnameDocente.toString());
        docente.putString("email", mailDoc.toString());

        i.putExtras(docente);
        mActivityTestRule.launchActivity(i);
        Log.d(TAG, "Lancio activity e attendo.");
        Thread.sleep(1000);

        //Aggiungo il corso al database
        aggiuntaCorso();
        Log.d(TAG, "Aggiungo corso e attendo.");
        Thread.sleep(5000);

        //Eseguiamo lo swipeDown per aggiornare la recycleView
        onView(withId(R.id.swipeLayout_lista_corsi_docente)).perform(swipeDown());

        Log.d(TAG, "Eseguo SwipeUp e attendo.");
        Thread.sleep(5000);

        //Verifichiamo che il corso sia stato caricato nella recycleView
        onView(withText("corsoTest"))
                .check(matches(isDisplayed()));

    }



    private void aggiuntaCorso() throws InterruptedException{
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference corsiReference = db.collection("Corsi");

        //Se i dati possono essere inviati
        Map<String,Object> documentSend= new HashMap<>();
        documentSend.put("nome_corso","corsoTest");
        documentSend.put("codice","test");
        documentSend.put("descrizione","test");
        documentSend.put("docente",idDocente);
        documentSend.put("lista_post", Arrays.asList());
        documentSend.put("lista_studenti",Arrays.asList());

        Date date= new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        documentSend.put("anno_accademico",calendar.get(Calendar.YEAR)+"/"+(calendar.get(Calendar.YEAR)+1));

        Log.d(TAG,"anno_accademico: "+calendar.get(Calendar.YEAR)+"/"+(calendar.get(Calendar.YEAR)+1) );
        Log.d(TAG,documentSend.toString());

        //Invio il documento
        //Utilizzo add in questo modo si autogenererà l'id del documento
        corsiReference.add(documentSend).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "DocumentSnapshot successfully written!");
                //Chiamiamo il metodo per aggiungere l'id del corso nel documento del docente
                idCorso= documentReference.getId();
                Log.d(TAG, "Aggiungo del corso nel docente");
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference docentiReference = db.collection("Docenti");
                docentiReference.document(idDocente).update("lista_corsi", FieldValue.arrayUnion(idCorso)).addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void avoid) {
                            Log.d(TAG,"Id del corso aggiunto con successo nel documento del docente");
                            }
                        }
                );
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error writing document", e);
            }
        });

        Thread.sleep(5000);
    }

    private void cancellazioneCorso(){
        Log.d(TAG,"Cancellazione del corso");
        if(idCorso != null){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference corsiReference = db.collection("Corsi");
            corsiReference.document(idCorso).delete();
            Log.d(TAG,"Cancellazione del corso eseguita");

            //Eliminiamo il corso dalla lista dei corsi
            CollectionReference docentiReference = db.collection("Docenti");
            docentiReference.document(idDocente).update(
                    "lista_corsi",
                    FieldValue.
                            arrayRemove(idCorso)
            );

        }

    }

    private void login(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance(); // crea un istanza di FirebaseAuth (serve per l'autenticazione)
        mAuth.signOut();

        mAuth.signInWithEmailAndPassword(mailDoc, pwdDoc).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Log.d(TAG,"Login Effettuato");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"Login Fallito");
            }
        });
    }



    private void logout(){
        Log.d(TAG,"Logout eseguito");
        FirebaseAuth mauth = FirebaseAuth.getInstance();
        mauth.signOut();
        Log.d(TAG,"Logout eseguito");

    }

}
