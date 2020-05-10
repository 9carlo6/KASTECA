package com.kasteca.activityTest;

import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kasteca.R;
import com.kasteca.activity.NewPostActivity;
import com.kasteca.util.EspressoIdlingResource;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;

import static android.app.Activity.RESULT_OK;
import static androidx.constraintlayout.widget.Constraints.TAG;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasType;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

public class NewPostActivityTestWithUpload {

    private String id_corso = "id_corso_prova";
    private String anno_accademico =  "2019/2020";
    private String codice ="ABC";
    private String descrizione = "descrizione_prova";
    private String docente = "xXqhMcCwc3R5RibdcLtTOuoMVgm1";
    private ArrayList<String> lista_post = new ArrayList<>();
    private ArrayList<String>  lista_studenti = new ArrayList<>();
    private String nome_corso = "nome_corso_prova";
    private String id_post = null;

    @Rule
    public ActivityTestRule<NewPostActivity> activityTestRule = new ActivityTestRule<>(NewPostActivity.class, false, false);

    @Before
    public void setUp() throws Exception {
        String mail = "docenteprova@unisannio.it";
        String pwd = "passwordProva";


        IdlingRegistry.getInstance().register(EspressoIdlingResource.getIdlingResource());
        FirebaseAuth mAuth = FirebaseAuth.getInstance(); // crea un istanza di FirebaseAuth (serve per l'autenticazione)
        mAuth.signInWithEmailAndPassword(mail, pwd).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Log.d(TAG, "login docente ok");

                // bisogna creare il corso
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference corsi = db.collection("Corsi");

                Map<String, Object> corso = new HashMap<>();
                corso.put("anno_accademico", anno_accademico );
                corso.put("codice", codice);
                corso.put("descrizione", descrizione);
                corso.put("docente", docente);
                corso.put("lista_post", lista_post);
                corso.put("lista_studenti", lista_studenti);
                corso.put("nome_corso", nome_corso);

                corsi.document(id_corso).set(corso).addOnCompleteListener(new OnCompleteListener<Void>() {
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
        });


        // thread non va bene!!! Occorre utilizzare l'interfaccia IdlingResource
        Thread.sleep(4000);

        Intent i = new Intent();
        i.putExtra("corso_id", id_corso);
        activityTestRule.launchActivity(i);

        // thread non va bene!!! Occorre utilizzare l'interfaccia IdlingResource
        Thread.sleep(2000);
        Intents.init();
    }

    @After
    public void tearDown() throws Exception {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference corsi = db.collection("Corsi");

        // recuperare l'id del post caricato
        corsi.document(id_corso)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.d(TAG, "Corsi: recuperato l'id del post caricato");

                        //cancellare il post
                        ArrayList<?> ar = (ArrayList<?>) documentSnapshot.getData().get("lista_post");
                        ArrayList<String> listaPost = new ArrayList<>();
                        for(Object x : ar){
                            listaPost.add((String) x);
                        }
                        if((!listaPost.isEmpty())){
                            id_post = listaPost.get(0);
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            CollectionReference posts = db.collection("Post");
                            posts.document(id_post)
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "Post: cancellazione post ok");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG, "Post: ERRORE cancellazione post");
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Corsi: ERRORE recupero dell'id del post caricato");
                    }
                });

        Thread.sleep(4000);

        // cancellare il corso
        corsi.document(id_corso)
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
        Intents.release();
    }

    // Verifica che lo spinner funziona correttamente
    @Test
    public void test_isSpinnerCorrect(){
        String tag = "esercizio";
        closeSoftKeyboard();
        onView(withId(R.id.tags_spinner)).perform(click());
        // thread non va bene!!! Occorre utilizzare l'interfaccia IdlingResource
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onData(allOf(is(instanceOf(String.class)), is(tag))).perform(click());
        onView(withId(R.id.tags_spinner)).check(matches(withSpinnerText(tag)));
    }

    // Verifica che cliccando sul bottone di upload, viene effetivamente mandato al server un post
    @Test
    public void test_upload() throws InterruptedException {
        String testo = "Testo prova";
        String link = "https://www.unisannio.it/";
        onView(withId(R.id.text_post_Edit_Text)).perform(typeText(testo));
        onView(withId(R.id.link_Edit_Text)).perform(typeText(link));
        closeSoftKeyboard();
        onView(withId(R.id.uploadButton)).perform(click());

        onView(withText(R.string.upload_successo)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));

    }

    // Verifica che cliccando sul bottone di upload, viene effetivamente mandato al server un post
    @Test
    public void test_uploadWithPdf() throws InterruptedException {
        String testo = "Testo prova";
        String link = "https://www.unisannio.it/";
        onView(withId(R.id.text_post_Edit_Text)).perform(typeText(testo));
        onView(withId(R.id.link_Edit_Text)).perform(typeText(link));
        closeSoftKeyboard();

        Matcher<Intent> expectedIntent = allOf(
                hasAction(Intent.ACTION_GET_CONTENT),
                hasType("application/pdf")
        );

        Instrumentation.ActivityResult activityResult = createFilePickActivityResultStub();
        intending(expectedIntent).respondWith(activityResult);

        onView(withId(R.id.pdfButton)).perform(click());

        onView(withId(R.id.uploadButton)).perform(click());



        // thread non va bene!!! Occorre utilizzare l'interfaccia IdlingResource
        Thread.sleep(4000);

        onView(withText(R.string.upload_pdf_fallito)).check(matches(isDisplayed()));
        onView(withText(R.string.Dialog_neutral_button_login_failed)).perform(click());
        onView(withText(R.string.upload_pdf_fallito)).check(doesNotExist());

        File fdelete = new File((getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)) + "/mypdf/test.pdf");
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                Log.e(TAG, "file Deleted");
            } else {
                Log.e(TAG,"file not Deleted");
            }
        }

    }

    // Metodo per simulare la selezione del file pdf
    private Instrumentation.ActivityResult createFilePickActivityResultStub(){
        PdfDocument document = new PdfDocument();
        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawText("Testo.", 80, 50, paint);
        //canvas.drawt
        // finish the page
        document.finishPage(page);
        // write the document content
        String directory_path = (getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)) + "/mypdf/";
        File file;
        file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path+"test.pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
        } catch (IOException e) {
            Log.e("main", "error "+e.toString());
        }
        // close the document
        document.close();


        Log.e(TAG, "pdf uri: " + targetPdf);

        Intent resultIntent = new Intent();
        resultIntent.setData(Uri.parse(targetPdf));
        return new Instrumentation.ActivityResult(RESULT_OK, resultIntent);
    }




}