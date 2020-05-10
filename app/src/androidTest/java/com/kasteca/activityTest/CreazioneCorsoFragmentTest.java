package com.kasteca.activityTest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kasteca.R;
import com.kasteca.activity.LogDocenteActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isOpen;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class CreazioneCorsoFragmentTest {
    private final String TAG="Testing-LogDocenteActivity";

    private String mailDoc = "docenteProva@unisannio.it";
    private String pwdDoc = "passwordProva";
    private String nameDocente = "NomeDocenteProva";
    private String surnameDocente = "CognomeDocenteProva";
    private String idDocente = "xXqhMcCwc3R5RibdcLtTOuoMVgm1";

    private String descrizione="testDescriozione";
    private String nome="testNome";
    private String codice="4321";


    private boolean corso=false;


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
        Thread.sleep(5000);
        logout();
    }

    @Test
    public void creazioneCorsoControlloGrafica() throws InterruptedException{
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

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.drawer_layout)).check(matches(isOpen()));

        //Raggiungo il fragment
        onView(withText("Aggiungi Corso"))
                .perform(click());
        Thread.sleep(2000);

        //Controlliamo la presenza di tutti i campo
        onView(withId(R.id.descrizione)).check(matches(isDisplayed()));
        onView(withId(R.id.codice)).check(matches(isDisplayed()));
        onView(withId(R.id.nome)).check(matches(isDisplayed()));
        onView(withId(R.id.conferma_button)).check(matches(isDisplayed()));
        onView(withId(R.id.textView2)).check(matches(isDisplayed()));


    }

    @Test
    public void creazioneCorsoSuccess()throws InterruptedException{
        Intent i = new Intent();
        Bundle docente = new Bundle();
        docente.putString("id", idDocente.toString());
        docente.putString("nome", nameDocente.toString());
        docente.putString("cognome", surnameDocente.toString());
        docente.putString("email", mailDoc.toString());

        i.putExtras(docente);
        mActivityTestRule.launchActivity(i);
        Thread.sleep(2000);

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.drawer_layout)).check(matches(isOpen()));

        //Raggiungo il fragment
        onView(withText("Aggiungi Corso"))
                .perform(click());
        Thread.sleep(2000);

        //Controlliamo la presenza di tutti i campo
        onView(withId(R.id.descrizione)).perform(replaceText(descrizione), closeSoftKeyboard());
        onView(withId(R.id.codice)).perform(replaceText(codice), closeSoftKeyboard());
        onView(withId(R.id.nome)).perform(replaceText(nome), closeSoftKeyboard());
        onView(withId(R.id.conferma_button)) .perform(click());
        Thread.sleep(2000);

        //Controlliamo che il corso sia presente nella lista
        onView(withText(nome))
                .check(matches(isDisplayed()));
        Thread.sleep(1000);
        corso=true;
    }

    @Test
    public void creazioneCorsoCodiceUtilizzato()throws InterruptedException{
        Intent i = new Intent();
        Bundle docente = new Bundle();
        docente.putString("id", idDocente.toString());
        docente.putString("nome", nameDocente.toString());
        docente.putString("cognome", surnameDocente.toString());
        docente.putString("email", mailDoc.toString());

        i.putExtras(docente);
        mActivityTestRule.launchActivity(i);
        Thread.sleep(2000);

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.drawer_layout)).check(matches(isOpen()));

        //Raggiungo il fragment
        onView(withText("Aggiungi Corso"))
                .perform(click());
        Thread.sleep(2000);

        //Controlliamo la presenza di tutti i campo
        onView(withId(R.id.descrizione)).perform(replaceText(descrizione), closeSoftKeyboard());
        onView(withId(R.id.codice)).perform(replaceText("test"), closeSoftKeyboard());
        onView(withId(R.id.nome)).perform(replaceText(nome), closeSoftKeyboard());
        onView(withId(R.id.conferma_button)) .perform(click());
        Thread.sleep(2000);

        //Controlliamo messaggio di errore
        Thread.sleep(1000);
        onView(withText("Codice già utilizzato.")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }

    @Test
    public void creazioneCorsoNoName()throws InterruptedException{//Creo l'intent e lancio l'activity con i dati prelevati dal login
        Intent i = new Intent();
        Bundle docente = new Bundle();
        docente.putString("id", idDocente.toString());
        docente.putString("nome", nameDocente.toString());
        docente.putString("cognome", surnameDocente.toString());
        docente.putString("email", mailDoc.toString());

        i.putExtras(docente);
        mActivityTestRule.launchActivity(i);
        Thread.sleep(2000);

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.drawer_layout)).check(matches(isOpen()));

        //Raggiungo il fragment
        onView(withText("Aggiungi Corso"))
                .perform(click());
        Thread.sleep(2000);

        //Controlliamo la presenza di tutti i campo
        onView(withId(R.id.descrizione)).perform(replaceText(descrizione), closeSoftKeyboard());
        onView(withId(R.id.codice)).perform(replaceText(codice), closeSoftKeyboard());
        onView(withId(R.id.nome)).perform(replaceText(""), closeSoftKeyboard());
        onView(withId(R.id.conferma_button)) .perform(click());
        Thread.sleep(1000);

        onView(withText("Errore, dati mancanti.")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));

    }

    @Test
    public void creazioneCorsoNoDescrizione()throws InterruptedException{
        Intent i = new Intent();
        Bundle docente = new Bundle();
        docente.putString("id", idDocente.toString());
        docente.putString("nome", nameDocente.toString());
        docente.putString("cognome", surnameDocente.toString());
        docente.putString("email", mailDoc.toString());

        i.putExtras(docente);
        mActivityTestRule.launchActivity(i);
        Thread.sleep(2000);

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.drawer_layout)).check(matches(isOpen()));

        //Raggiungo il fragment
        onView(withText("Aggiungi Corso"))
                .perform(click());
        Thread.sleep(2000);

        //Controlliamo la presenza di tutti i campo
        onView(withId(R.id.descrizione)).perform(replaceText(""), closeSoftKeyboard());
        onView(withId(R.id.codice)).perform(replaceText(codice), closeSoftKeyboard());
        onView(withId(R.id.nome)).perform(replaceText(nome), closeSoftKeyboard());
        onView(withId(R.id.conferma_button)) .perform(click());
        Thread.sleep(1000);

        onView(withText("Errore, dati mancanti.")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }

    @Test
    public void creazioneCorsoNoCodice()throws InterruptedException{
        Intent i = new Intent();
        Bundle docente = new Bundle();
        docente.putString("id", idDocente.toString());
        docente.putString("nome", nameDocente.toString());
        docente.putString("cognome", surnameDocente.toString());
        docente.putString("email", mailDoc.toString());

        i.putExtras(docente);
        mActivityTestRule.launchActivity(i);
        Thread.sleep(2000);

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.drawer_layout)).check(matches(isOpen()));

        //Raggiungo il fragment
        onView(withText("Aggiungi Corso"))
                .perform(click());
        Thread.sleep(2000);

        //Controlliamo la presenza di tutti i campo
        onView(withId(R.id.descrizione)).perform(replaceText(descrizione), closeSoftKeyboard());
        onView(withId(R.id.codice)).perform(replaceText(""), closeSoftKeyboard());
        onView(withId(R.id.nome)).perform(replaceText(nome), closeSoftKeyboard());
        onView(withId(R.id.conferma_button)) .perform(click());
        Thread.sleep(1000);

        onView(withText("Errore, dati mancanti.")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }




    private void cancellazioneCorso(){
        Log.d(TAG,"Cancellazione del corso");

        if(corso){
            corso= !corso;

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference corsiReference = db.collection("Corsi");
            corsiReference.whereEqualTo("nome_corso",nome).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference corsiReference = db.collection("Corsi");
                    for (DocumentSnapshot documentiCorsi : queryDocumentSnapshots.getDocuments()) {
                        corsiReference.document(documentiCorsi.getId()).delete();
                    }

                }
            });

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
