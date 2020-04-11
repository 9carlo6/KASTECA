package com.kasteca.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.kasteca.R;
import com.kasteca.object.Richiesta;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.kasteca.util.EspressoIdlingResource;

public class RichiestaIscrizioneActivity extends AppCompatActivity {

    private EditText codice_corso_edit_text;
    private String id_studente;
    private int controllo_esistenza_codice; // serve per controllare se il codice che si sta cercando esiste su firebase (0 non esiste, 1 esiste)
    private ArrayList<String> lista_codici_studenti;
    private ArrayList<String> id_richieste_studenti;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_richiesta_iscrizione);
        id_studente = getIntent().getStringExtra("id_studente");
        codice_corso_edit_text = findViewById(R.id.Codice_Corso_Edit_Text);

        lista_codici_studenti = new ArrayList<>();
        id_richieste_studenti = new ArrayList<>();
    }

    public void press(View v){
        controllo_esistenza_codice = 0; // setto a 0 controllo_esistenza_codice

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference corsi = db.collection("Corsi");

        Source source = Source.SERVER;

        EspressoIdlingResource.increment();

        corsi.whereEqualTo("codice", codice_corso_edit_text.getText().toString()).get(source).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    EspressoIdlingResource.decrement();
                    for (DocumentSnapshot document : task.getResult()) {
                        // per ogni documento presente nella collezione 'Corsi' controllo
                        // se il codice del corso inserito dallo studente appartiene a un corso esistente
                        if (codice_corso_edit_text.getText().toString().equals(document.get("codice").toString())) {
                            // aggiorno controllo_esistenza_codice a 1 perchè il codice è stato trovato
                            controllo_esistenza_codice = 1;

                            // bisogna controllare se già è iscritto al corso
                            lista_codici_studenti = (ArrayList<String>) document.get("lista_studenti");

                            if (controllo_esistenza_codice == 1) {
                                // se è gia iscritto allora mostra un dialog attraverso il quale comunica con l'user
                                if (lista_codici_studenti.contains(id_studente)) {
                                    showAlert(getResources().getString(R.string.Studente_gia_iscritto));
                                } else {
                                    //EspressoIdlingResource.decrement();

                                    // se non è gia iscritto allora controlla se esiste una richiesta di questo studente per questo corso
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    CollectionReference richieste_iscrizione = db.collection("Richieste_Iscrizione");

                                    EspressoIdlingResource.increment();

                                    // deve prendere tutte le richieste che rispettano queste condizioni:
                                    //      l'id studente deve corrispondere all'utente autenticato
                                    //      il codice del corso deve corrispondere al codice inserito dallo studente
                                    //      lo stato richiesta deve essere "in attesa"
                                    richieste_iscrizione.whereEqualTo("studente", id_studente)
                                            .whereEqualTo("codice_corso", codice_corso_edit_text.getText().toString())
                                            .whereEqualTo("stato_richiesta", "in attesa").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                EspressoIdlingResource.decrement();
                                                for (DocumentSnapshot document : task.getResult()) {
                                                    id_richieste_studenti.add(document.getId());
                                                }
                                                // se la richiesta non esiste allora mostra un dialog attraverso il quale comunica con l'user
                                                if (!id_richieste_studenti.isEmpty()) {
                                                    showAlert(getResources().getString(R.string.Richiesta_gia_inviata));
                                                } else {

                                                    // altrimenti va avanti e carica la richiesta nel db
                                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                    CollectionReference richieste_iscrizione = db.collection("Richieste_Iscrizione");

                                                    EspressoIdlingResource.increment();

                                                    // allora inviare la richiesta e caricarla nel db
                                                    Map<String, Object> obj = new HashMap<>();
                                                    obj.put("codice_corso", codice_corso_edit_text.getText().toString());
                                                    obj.put("data", new Timestamp(Calendar.getInstance().getTime()));
                                                    obj.put("stato_richiesta", "in attesa");
                                                    obj.put("studente", id_studente);

                                                    richieste_iscrizione.add(obj).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                                            if (task.isSuccessful()) {

                                                                EspressoIdlingResource.decrement();

                                                                Intent returnIntent = new Intent();
                                                                setResult(Activity.RESULT_OK, returnIntent);
                                                                finish();
                                                            } else {
                                                                Toast.makeText(RichiestaIscrizioneActivity.this, ("qualcosa è andato storto nel db"), Toast.LENGTH_LONG).show();

                                                                EspressoIdlingResource.decrement();
                                                            }
                                                        }
                                                    });
                                                }
                                            } else {
                                                Toast.makeText(RichiestaIscrizioneActivity.this, ("qualcosa è andato storto nel db"), Toast.LENGTH_LONG).show();

                                                EspressoIdlingResource.decrement();
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    }
                }else{
                    Toast.makeText(RichiestaIscrizioneActivity.this, ("qualcosa è andato storto nel db"), Toast.LENGTH_LONG).show();

                    EspressoIdlingResource.decrement();
                }
                if(controllo_esistenza_codice == 0){
                    showAlert(getResources().getString(R.string.Codice_Corso_non_corretto));
                }
            }
        });
    }

    public void showAlert(String s){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(s);
        alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
}
