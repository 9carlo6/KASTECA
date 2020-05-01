package com.kasteca.activityTest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kasteca.R;
import com.kasteca.activity.LogDocenteActivity;
import com.kasteca.activity.LogStudenteActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isOpen;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@LargeTest
public class LogStudenteActivityTest {
    private final String TAG="Testing-LogDocenteActivity";

    private String nameStudente = "NomeProva";
    private String surnameStudente = "CognomeProva";
    private String mailStu = "studenteProva@studenti.unisannio.it";
    private String matricola= "Matricola";
    private String pwdStu = "passwordProva";
    private String codice_corso = "codice";
    private String idStudente = "SotSWWJIZHNALPZ32EAARRed9RG2";


    private String mailDoc = "docenteProva@unisannio.it";
    private String pwdDoc = "passwordProva";
    private String nameDocente = "NomeDocenteProva";
    private String surnameDocente = "CognomeDocenteProva";
    private String idDocente = "xXqhMcCwc3R5RibdcLtTOuoMVgm1";

    private String idCorso =null;



    @Rule
    public ActivityTestRule<LogStudenteActivity> mActivityTestRule = new ActivityTestRule<>(LogStudenteActivity.class, true, false);


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
        Bundle studente = new Bundle();
        studente = new Bundle();

        studente.putString("id", idStudente);
        studente.putString("nome", nameStudente);
        studente.putString("cognome", surnameStudente);
        studente.putString("email", mailStu);
        studente.putString("matricola", matricola);

        i.putExtras(studente);
        mActivityTestRule.launchActivity(i);
        Thread.sleep(2000);

        //verifico che l'interfaccia sia stata caricata correttamente
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withId(R.id.fragment_container_studente)).check(matches(isDisplayed()));
        //onView(withId(R.id.nav_view_docente)).check(matches(isDisplayed()));

        //verifico la navigationview
        onView(withId(R.id.drawer_layout_studente)).perform(DrawerActions.open());
        onView(withId(R.id.drawer_layout_studente)).check(matches(isOpen()));
        //Here's the difference
        //onView(withId(R.id.nav_view_docente)).perform(NavigationViewActions.navigateTo(R.id.nav_scroller));


        //verifico la presenza delle opzioni della navigation view
        onView(withId(R.id.nav_view_studente)).check(matches(isDisplayed()));

        onView(withText("Corsi"))
                .check(matches(isDisplayed()));
        onView(withText("Logout"))
                .check(matches(isDisplayed()));

        onView(withText(mailStu))
                .check(matches(isDisplayed()));


    }


    @Test
    public void aggiornamentoRecycleViewTest() throws InterruptedException{
        //Creo l'intent e lancio l'activity con i dati prelevati dal login
        Intent i = new Intent();
        Bundle studente = new Bundle();
        studente.putString("id", idStudente);
        studente.putString("nome", nameStudente);
        studente.putString("cognome", surnameStudente);
        studente.putString("email", mailStu);
        studente.putString("matricola", matricola);

        i.putExtras(studente);
        mActivityTestRule.launchActivity(i);
        Thread.sleep(1000);

        //Aggiungo il corso al database
        aggiuntaCorsoStudente();
        Log.d(TAG, "Aggiungo corso e attendo.");
        Thread.sleep(5000);

        //Eseguiamo lo swipeDown per aggiornare la recycleView
        onView(withId(R.id.swipeLayout_lista_corsi_studente)).perform(swipeDown());

        Log.d(TAG, "Eseguo SwipeUp e attendo.");
        Thread.sleep(10000);

        //Verifichiamo che il corso sia stato caricato nella recycleView
        onView(withText("corsoTest"))
                .check(matches(isDisplayed()));

    }


    private void aggiuntaCorsoStudente() throws InterruptedException{
        //Per aggiungere un corso dobbiamo eseguire il login con il professore
        loginProfessore();
        Thread.sleep(5000);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference corsiReference = db.collection("Corsi");

        List<String> listaStudenti= new ArrayList<String>();
        listaStudenti.add(idStudente);

        //Se i dati possono essere inviati
        Map<String,Object> documentSend= new HashMap<>();
        documentSend.put("nome_corso","corsoTest");
        documentSend.put("codice","test");
        documentSend.put("descrizione","test");
        documentSend.put("docente",idDocente);
        documentSend.put("lista_post", Arrays.asList());
        documentSend.put("lista_studenti",listaStudenti);

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
                //Logout dal docente
                logout();
                //Login Studente
                login();
                try{
                    Thread.sleep(5000);
                }
                catch(InterruptedException e){
                }

                //Chiamiamo il metodo per aggiungere l'id del corso nel documento del docente
                idCorso= documentReference.getId();
                Log.d(TAG, "Aggiungo del corso nello Studente");
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference docentiReference = db.collection("Studenti");
                docentiReference.document(idStudente).update("lista_corsi", FieldValue.arrayUnion(idCorso)).addOnSuccessListener(
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

        //logout();
        //login();

        Thread.sleep(5000);
    }



    private void login(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance(); // crea un istanza di FirebaseAuth (serve per l'autenticazione)
        mAuth.signOut();

        mAuth.signInWithEmailAndPassword(mailStu, pwdStu).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
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
        FirebaseAuth mauth = FirebaseAuth.getInstance();
        mauth.signOut();
    }

    private void loginProfessore(){
        logout();
        //Effettuiamo il login con il professore
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

    private void cancellazioneCorso() throws InterruptedException {
        Log.d(TAG, "Cancellazione del corso");
        if (idCorso != null) {
            loginProfessore();
            Thread.sleep(2000);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference corsiReference = db.collection("Corsi");
            corsiReference.document(idCorso).delete();
            Log.d(TAG, "Cancellazione del corso eseguita");


            //Eliminiamo il corso dalla lista dei corsi
            CollectionReference docentiReference = db.collection("Studenti");
            docentiReference.document(idStudente).update(
                    "lista_corsi",
                    FieldValue.
                            arrayRemove(idCorso)
            );

        }

    }

}
