package com.kasteca.serverTest.serverTestCorso;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.widget.TextViewCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

public class CorsoDocenteTestFail {

    private final static String TAG="Testing-Corso-Fail";

    private String mailDoc = "docenteProva@unisannio.it";
    private String pwdDoc = "passwordProva";

    private String idDocente = "xXqhMcCwc3R5RibdcLtTOuoMVgm1";

    private String idCorsoDaLeggere = "G193crk8g3jzzAm9Nfiq";
    private String idCorsoNonProprietazio= "pKoyf8pkZLqdwBwAdeoL";




    private String preparazione= null;
    private String result= null;
    private int counter=0;



/*match /Corsi/{document=**} {
      // Applies to writes to nonexistent documents
      allow create: if isDocente(request.auth.uid) &&
      request.resource.data.anno_accademico.size() <= 9 &&
      request.resource.data.descrizione.size() <= 40 &&
      request.resource.data.codice.size() > 0 && request.resource.data.codice.size() <=10 &&
      request.resource.data.nome_corso.size() <= 30 ;

      //leggere se si è il docente del corso o uno studente iscritto
      allow read: if isDocente(request.auth.uid) || isStudente(request.auth.uid);

      //Può modificare solo il docente del corso
      allow update: if request.auth.uid == resource.data.docente;

      //Può cancellare solo il docente del corso
      allow delete: if request.auth.uid == resource.data.docente;
    }

     */

    @Before
    public void signInDocente()throws InterruptedException {
        login();
    }

    /*
    Test scritto nella classe dedicata al fallimento dello studente
    @Test
    public void creazioneNotDocente(){}
     */

    @Test
    public void creazioneYearOverSize() throws InterruptedException{
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference corsiReference = db.collection("Corsi");

        Map<String,Object> documentSend= new HashMap<>();
        documentSend.put("nome_corso","test");
        documentSend.put("codice","test");
        documentSend.put("descrizione","test");
        documentSend.put("docente",idDocente);
        documentSend.put("lista_post", Arrays.asList());
        documentSend.put("lista_studenti",Arrays.asList());
        documentSend.put("anno_accademico","TESTESTESTESTESTESTESTESTESTESTEST");

        corsiReference.add(documentSend).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "DocumentSnapshot successfully written!");

                result="fail";
                //cancelliamo il corso
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference corsiReference = db.collection("Corsi");
                corsiReference.document(documentReference.getId()).delete();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error writing document", e);
                result="success";
            }
        });

        while(result == null & counter<10){
            Log.d(TAG,"Attesa risposta...");
            Thread.sleep(1000);
            counter=counter + 1;
        };
        counter=0;
        assertEquals("success",result);
        result=null;
    }

    @Test
    public void creazioneDescrizioneOverSize() throws InterruptedException{
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference corsiReference = db.collection("Corsi");

        String descrizione="test";
        while(descrizione.length()<=40)
            descrizione=descrizione+descrizione;

        Map<String,Object> documentSend= new HashMap<>();
        documentSend.put("nome_corso","test");
        documentSend.put("codice","test");
        documentSend.put("descrizione",descrizione);
        documentSend.put("docente",idDocente);
        documentSend.put("lista_post", Arrays.asList());
        documentSend.put("lista_studenti",Arrays.asList());

        Date date= new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        documentSend.put("anno_accademico",calendar.get(Calendar.YEAR)+"/"+(calendar.get(Calendar.YEAR)+1));

        corsiReference.add(documentSend).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "DocumentSnapshot successfully written!");
                result="fail";
                //cancelliamo il corso
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference corsiReference = db.collection("Corsi");
                corsiReference.document(documentReference.getId()).delete();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error writing document", e);
                result="success";

            }
        });

        while(result == null & counter<10){
            Log.d(TAG,"Attesa risposta...");
            Thread.sleep(1000);
            counter=counter + 1;
        };
        counter=0;
        assertEquals("success",result);
        result=null;
    }

    @Test
    public void creazioneCodiceOverSize() throws InterruptedException{
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference corsiReference = db.collection("Corsi");


        Map<String,Object> documentSend= new HashMap<>();
        documentSend.put("nome_corso","test");
        documentSend.put("codice","TESTESTESTESTESTESTESTESTESTESTEST");
        documentSend.put("descrizione","test");
        documentSend.put("docente",idDocente);
        documentSend.put("lista_post", Arrays.asList());
        documentSend.put("lista_studenti",Arrays.asList());

        Date date= new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        documentSend.put("anno_accademico",calendar.get(Calendar.YEAR)+"/"+(calendar.get(Calendar.YEAR)+1));

        corsiReference.add(documentSend).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "DocumentSnapshot successfully written!");
                result="fail";
                //cancelliamo il corso
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference corsiReference = db.collection("Corsi");
                corsiReference.document(documentReference.getId()).delete();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error writing document", e);
                result="success";

            }
        });

        while(result == null & counter<10){
            Log.d(TAG,"Attesa risposta...");
            Thread.sleep(1000);
            counter=counter + 1;
        };
        counter=0;
        assertEquals("success",result);
        result=null;
    }

    @Test
    public void creazioneCodiceLenghtZero()throws InterruptedException{
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference corsiReference = db.collection("Corsi");


        Map<String,Object> documentSend= new HashMap<>();
        documentSend.put("nome_corso","test");
        documentSend.put("codice","");
        documentSend.put("descrizione","test");
        documentSend.put("docente",idDocente);
        documentSend.put("lista_post", Arrays.asList());
        documentSend.put("lista_studenti",Arrays.asList());

        Date date= new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        documentSend.put("anno_accademico",calendar.get(Calendar.YEAR)+"/"+(calendar.get(Calendar.YEAR)+1));

        corsiReference.add(documentSend).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "DocumentSnapshot successfully written!");
                result="fail";
                //cancelliamo il corso
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference corsiReference = db.collection("Corsi");
                corsiReference.document(documentReference.getId()).delete();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error writing document", e);
                result="success";

            }
        });

        while(result == null & counter<10){
            Log.d(TAG,"Attesa risposta...");
            Thread.sleep(1000);
            counter=counter + 1;
        };
        counter=0;
        assertEquals("success",result);
        result=null;
    }

    @Test
    public void creazioneNomeCorsoOverSize()throws InterruptedException{
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference corsiReference = db.collection("Corsi");

        String nome="test";
        while(nome.length()<=30)
            nome=nome+nome;

        Map<String,Object> documentSend= new HashMap<>();
        documentSend.put("nome_corso",nome);
        documentSend.put("codice","test");
        documentSend.put("descrizione","test");
        documentSend.put("docente",idDocente);
        documentSend.put("lista_post", Arrays.asList());
        documentSend.put("lista_studenti",Arrays.asList());

        Date date= new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        documentSend.put("anno_accademico",calendar.get(Calendar.YEAR)+"/"+(calendar.get(Calendar.YEAR)+1));

        corsiReference.add(documentSend).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "DocumentSnapshot successfully written!");
                result="fail";
                //cancelliamo il corso
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference corsiReference = db.collection("Corsi");
                corsiReference.document(documentReference.getId()).delete();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error writing document", e);
                result="success";

            }
        });

        while(result == null & counter<10){
            Log.d(TAG,"Attesa risposta...");
            Thread.sleep(1000);
            counter=counter + 1;
        };
        counter=0;
        assertEquals("success",result);
        result=null;
    }

    /*
    Fallimento modifica
     */
    @Test
    public void updateDocenteNonProprietario() throws InterruptedException{
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference corsiReference = db.collection("Corsi");
        corsiReference.document(idCorsoNonProprietazio).update("descrizione","testModificato").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                result="fail";

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error writing document", e);
                result="success";
            }
        });

        while(result == null & counter<10){
            Log.d(TAG,"Attesa risposta...");
            Thread.sleep(1000);
            counter=counter + 1;
        };
        counter=0;
        assertEquals("success",result);
        result=null;
    }
    /*
    Fallimento lettura: non può mai avvenire perchè abbiamo solo studenti e docenti
     */

    /*
    Fallimento eliminazione
     */
    @Test
    public void deliteDocenteNonProprietario() throws InterruptedException{
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference corsiReference = db.collection("Corsi");
        corsiReference.document(idCorsoNonProprietazio).update("descrizione","testModificato").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                result="fail";

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error writing document", e);
                result="success";
            }
        });

        while(result == null & counter<10){
            Log.d(TAG,"Attesa risposta...");
            Thread.sleep(1000);
            counter=counter + 1;
        };
        counter=0;
        assertEquals("success",result);
        result=null;
    }


    private void login() throws RuntimeException,InterruptedException{
        preparazione=null;
        FirebaseAuth mAuth = FirebaseAuth.getInstance(); // crea un istanza di FirebaseAuth (serve per l'autenticazione)
        mAuth.signOut();

        mAuth.signInWithEmailAndPassword(mailDoc, pwdDoc).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                preparazione="ok";
                Log.d(TAG,"Login Effettuato");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                preparazione="notOk";
                Log.d(TAG,"Login Fallito");
            }
        });

        //attesa login
        //attesa login
        while((preparazione==null) && (counter<10)){
            Thread.sleep(1000);
            counter=counter+1;
        };
        counter=0;

        if(!preparazione.equalsIgnoreCase("ok")){
            throw new RuntimeException();
        }else
            preparazione=null;      //Riporto preparazione nello stato iniziale potrebbe essere riutilizzata.

    }


}
