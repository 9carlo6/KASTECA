package com.kasteca.activityTest;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
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
import com.kasteca.activity.PostActivity;
import com.kasteca.object.Post;

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
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
public class PostActivityRispostaCommentoDocenteTest {

    private final String TAG="Test-PARispostaCommentoTest";

    private String mailDoc = "docenteProva@unisannio.it";
    private String pwdDoc = "passwordProva";
    private String nameDocente = "NomeDocenteProva";
    private String surnameDocente = "CognomeDocenteProva";
    private String idDocente = "xXqhMcCwc3R5RibdcLtTOuoMVgm1";

    private String idCorso = null;
    private String idPost = null;
    private String idCommento = null;
    private String idRisposta = null;
    private Post post;
    private Date data;


    private String preparazione= null;
    private String ready = "no";


    @Rule
    public ActivityTestRule<PostActivity> mActivityTestRule = new ActivityTestRule<>(PostActivity.class, true, false);

    @Before
    public void setting() throws InterruptedException{
        //Effettuiamo il logic del docente
        login();
        //Attesa Login e creazione delle cose per il test
        while(ready.equalsIgnoreCase("no"))
            Log.d(TAG,"waiting setting...");


    }

    @After
    public void cancel() throws InterruptedException{
        //per fare la cancellazione solo alla fine del test
        cancellazioneSettingTest();
    }

    @Test
    public void controllInteface() throws InterruptedException{
        //Creo l'intent e lancio l'activity
        Intent intent =new Intent();
        intent.putExtra("docente", nameDocente+" "+surnameDocente);
        intent.putExtra("id_docente", idDocente);
        intent.putExtra("post", post);
        mActivityTestRule.launchActivity(intent);

        Thread.sleep(2000);

        //Controllo il corretto caricamento di tutte le parti dell'activity
        onView(withId(R.id.commenti_numero_textView)).check(matches(isDisplayed()));
        onView(withId(R.id.testoPostTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.link_textView)).check(matches(isDisplayed()));
        onView(withId(R.id.aggiungi_commento_textView)).check(matches(isDisplayed()));
        onView(withId(R.id.nome_cognome_textView)).check(matches(isDisplayed()));
        onView(withId(R.id.data_textView)).check(matches(isDisplayed()));
        onView(withId(R.id.tagTextView)).check(matches(isDisplayed()));

        //Controllo che la grafica del post sia stata creata correttamente
        onView(withText(post.getTesto()))
                .check(matches(isDisplayed()));
        onView(withText(post.getTag()))
                .check(matches(isDisplayed()));
        onView(withText(post.getLink()))
                .check(matches(isDisplayed()));
        onView(withText(nameDocente+" "+surnameDocente))
                .check(matches(isDisplayed()));


        //Apro il popup
        onView(withId(R.id.commenti_numero_textView)).perform(click());


        Thread.sleep(4000);
        //Controlliamo che il commento sia stato inserito commentamento

        onView(withText(nameDocente+" "+surnameDocente))
                .check(matches(isDisplayed()));
        onView(withText("test"))
                .check(matches(isDisplayed()));
        onView(withText("Rispondi"))
                .check(matches(isDisplayed()));
        onView(withText("Elimina"))
                .check(matches(isDisplayed()));
        onView(withText("Modifica"))
                .check(matches(isDisplayed()));
        onView(withText("Visualizza tutte le risposte"))
                .check(matches(isDisplayed()));

    }


    @Test
    public void creazioneModificaEliminazioneCommentoDocente()throws InterruptedException{
        //Creo l'intent e lancio l'activity
        Intent intent =new Intent();
        intent.putExtra("docente", nameDocente+" "+surnameDocente);
        intent.putExtra("id_docente", idDocente);
        intent.putExtra("post", post);
        mActivityTestRule.launchActivity(intent);

        Thread.sleep(2000);

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.commenti_numero_textView), withText("Commenti"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                0)));
        appCompatTextView.perform(scrollTo(), click());


        Thread.sleep(2000);

        //Eliminiamo il commento che è già presente
        onView(withText("Elimina")).perform(click());
        Thread.sleep(2000);

        //Scrivo il commento
        ViewInteraction appCompatEditText22 = onView(
                allOf(withId(R.id.writeComment),
                        childAtPosition(
                                allOf(withId(R.id.comment_section),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                3)),
                                0),
                        isDisplayed()));
        appCompatEditText22.perform(replaceText("TestCommento"), closeSoftKeyboard());

        Thread.sleep(2000);

        //Invio il commento
        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.send_commento_button),
                        childAtPosition(
                                allOf(withId(R.id.comment_section),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                3)),
                                1),
                        isDisplayed()));
        appCompatImageButton2.perform(forceClick());

        Thread.sleep(2000);

        //Controllo se il commento è stato correttamente inserito
        onView(withText("TestCommento")).perform(click());
        Thread.sleep(2000);

        //Modifico la risposta inserita
        onView(withText("Modifica")).perform(click());
        Thread.sleep(2000);

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.modifica_edittext),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.custom),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText6.perform(replaceText("testmodifica"), closeSoftKeyboard());
        //onView(withText("Modifica")).perform(click());
        ViewInteraction appCompatButton2 = onView(
                allOf(withId(android.R.id.button1),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatButton2.perform(scrollTo(), click());
        Thread.sleep(2000);

        //Controllo che la modifica sia stata effettuata correttamente
        onView(withText("testmodifica")).perform(click());

        //Elimino la risposta inserita
        onView(withText("Elimina")).perform(click());
    }


    @Test
    public void creazioneModificaEliminazioneRispostaDocente() throws InterruptedException{

        //Creo l'intent e lancio l'activity
        Intent intent =new Intent();
        intent.putExtra("docente", nameDocente+" "+surnameDocente);
        intent.putExtra("id_docente", idDocente);
        intent.putExtra("post", post);
        mActivityTestRule.launchActivity(intent);

        Thread.sleep(2000);

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.commenti_numero_textView), withText("Commenti"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                0)));
        appCompatTextView.perform(scrollTo(), click());


        Thread.sleep(2000);

        onView(withText("Rispondi")).perform(click());

        Thread.sleep(2000);

        ViewInteraction appCompatEditText12 = onView(
                allOf(withId(R.id.writeRisposta),
                        childAtPosition(
                                allOf(withId(R.id.comment_section),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                3)),
                                0),
                        isDisplayed()));
        appCompatEditText12.perform(replaceText("TestRisposta"), closeSoftKeyboard());

        Thread.sleep(1000);


        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.send_risposta_button),
                        childAtPosition(
                                allOf(withId(R.id.comment_section),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                3)),
                                1),
                        isDisplayed()));
        //appCompatImageButton2.check(matches(isCompletelyDisplayed()));
        appCompatImageButton2.perform(forceClick());

        Thread.sleep(2000);

        //Controllo se il commento è stato correttamente inserito
        onView(withText("TestRisposta")).perform(click());
        Thread.sleep(2000);

        //Modifico la risposta inserita
        onView(withText("Modifica")).perform(click());
        Thread.sleep(2000);

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.modifica_edittext),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.custom),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText6.perform(replaceText("testmodifica"), closeSoftKeyboard());
        //onView(withText("Modifica")).perform(click());
        ViewInteraction appCompatButton2 = onView(
                allOf(withId(android.R.id.button1),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatButton2.perform(scrollTo(), click());
        Thread.sleep(2000);

        //Controllo che la modifica sia stata effettuata correttamente
        onView(withText("testmodifica")).perform(click());

        //Elimino la risposta inserita
        onView(withText("Elimina")).perform(click());

    }


    public static ViewAction forceClick() {
        return new ViewAction() {
            @Override public Matcher<View> getConstraints() {
                return allOf(isClickable(), isEnabled(), isDisplayed());
            }

            @Override public String getDescription() {
                return "force click";
            }

            @Override public void perform(UiController uiController, View view) {
                view.performClick(); // perform click without checking view coordinates.
                uiController.loopMainThreadUntilIdle();
            }
        };
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




    private void logout(){
        Log.d(TAG,"Logout eseguito");
        FirebaseAuth mauth = FirebaseAuth.getInstance();
        mauth.signOut();
        Log.d(TAG,"Logout eseguito");

    }

    private void login() throws RuntimeException,InterruptedException {

        if (ready.equalsIgnoreCase("no")) {
            preparazione = null;
            FirebaseAuth mAuth = FirebaseAuth.getInstance(); // crea un istanza di FirebaseAuth (serve per l'autenticazione)
            mAuth.signOut();

            mAuth.signInWithEmailAndPassword(mailDoc, pwdDoc).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    preparazione="ok";
                    Log.d(TAG, "Login Effettuato");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    preparazione = "notLogged";
                    Log.d(TAG, "Login Fallito");
                }
            });

            while(preparazione == null);
            if(preparazione.equalsIgnoreCase("ok"))
                creazioneCorso();
            else
                throw new RuntimeException();

        }
    }

    private void creazioneCorso() throws RuntimeException,InterruptedException{
        preparazione=null;

        Map<String,Object> documentSend= new HashMap<>();
        documentSend.put("nome_corso","rispcomment");
        documentSend.put("codice","test");
        documentSend.put("descrizione","test");
        documentSend.put("docente",idDocente);
        documentSend.put("lista_post", Arrays.asList());
        documentSend.put("lista_studenti",Arrays.asList());
        Date date= new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        documentSend.put("anno_accademico",calendar.get(Calendar.YEAR)+"/"+(calendar.get(Calendar.YEAR)+1));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference corsiReference = db.collection("Corsi");

        corsiReference.add(documentSend).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                idCorso=documentReference.getId();
                Log.d(TAG,"idCorso: "+idCorso);
                preparazione="ok";
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                preparazione="notok";
            }
        });


        while(preparazione == null);
        if(preparazione.equalsIgnoreCase("ok"))
            creazionePost();
        else
            throw new RuntimeException();
    }

    private void creazionePost() throws RuntimeException{

        preparazione=null;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference postsRef = db.collection("Post");

        Map<String, Object> newPost = new HashMap<>();

        newPost.put("corso", idCorso);
        data= new Date(0);
        newPost.put("data", data);
        newPost.put("link", "testLink");
        newPost.put("pdf", null);
        newPost.put("tag", "approfondimento");
        newPost.put("testo", "testoTest");
        newPost.put("lista_commenti", new ArrayList<String>());

        post= new Post();

        postsRef.add(newPost)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        idPost= documentReference.getId();
                        Log.d(TAG,"IDPost: "+idPost);
                        post= new Post(idPost,"approfondimento", "test",idCorso,data,"testLink","");
                        preparazione="ok";
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        preparazione="notok";
                    }
                });

        while(preparazione == null);
        if(preparazione.equalsIgnoreCase("ok"))
            creazioneCommento();
        else
            throw new RuntimeException();
    }

    private void creazioneCommento() throws RuntimeException{

        preparazione=null;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference commentiRef = db.collection("Commenti");

        Map<String, Object> newCommento = new HashMap<>();
        newCommento.put("testo", "test");
        newCommento.put("data", new Date());
        newCommento.put("lista_risposte", new ArrayList<String>());
        newCommento.put("post", idPost);
        newCommento.put("proprietarioCommento", idDocente);

        commentiRef.add(newCommento)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        idCommento= documentReference.getId();
                        //Rendiamo la variabile per la sincronizzazione true
                        preparazione= "ok";
                        Log.d(TAG,"idCommento: "+idCommento);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                preparazione="notok";
            }
        });

        while(preparazione == null);
        if(!preparazione.equalsIgnoreCase("ok"))
            throw new RuntimeException();
        else
            ready="ok";

    }

    private void cancellazioneSettingTest() throws  InterruptedException{
        Log.d(TAG,"Cancellazione Setting");

        if(idRisposta != null){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference corsiReference = db.collection("Risposte");
            corsiReference.document(idRisposta).delete();
            Log.d(TAG,"Cancellazione Risposta");

        }

        if(idCommento != null){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference corsiReference = db.collection("Commenti");
            corsiReference.document(idCommento).delete();
            Log.d(TAG,"Cancellazione Commento");

        }

        if(idPost != null){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference corsiReference = db.collection("Post");
            corsiReference.document(idPost).delete();
            Log.d(TAG,"Cancellazione Post");

        }

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

        Thread.sleep(5000);
        logout();
    }

}
