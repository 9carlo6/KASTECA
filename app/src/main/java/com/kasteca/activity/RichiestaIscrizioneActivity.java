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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.kasteca.R;

import java.util.HashMap;
import java.util.Map;

public class RichiestaIscrizioneActivity extends AppCompatActivity {

    private EditText codice_corso_edit_text;
    private String id_studente;
    private int controllo_esistenza_codice; // serve per controllare se il codice che si sta cercando esiste su firebase (0 non esiste, 1 esiste)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_richiesta_iscrizione);
        id_studente = getIntent().getStringExtra("id_studente");
        codice_corso_edit_text = findViewById(R.id.Codice_Corso_Edit_Text);
    }

    public void press(View v){
        controllo_esistenza_codice = 0; // setto a 0 controllo_esistenza_codice

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference corsi = db.collection("Corsi");

        Source source = Source.SERVER;

        corsi.get(source).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot document : task.getResult()) {
                    // per ogni documento presente nella collezione 'Corsi' controllo
                    // se il codice del corso inserito dallo studente appartiene a un corso esistente
                    if (codice_corso_edit_text.getText().toString().equals(document.get("codice").toString())) {
                        // aggiorno controllo_esistenza_codice a 1 perchè il codice è stato trovato
                        controllo_esistenza_codice = 1;
                        //allora inviare la richiesta e caricarla nel db

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        CollectionReference richieste_iscrizione = db.collection("Richieste_Iscrizione");

                        Map<String, Object> obj = new HashMap<>();
                        obj.put("corso", document.get("codice").toString());
                        obj.put("data", null);
                        obj.put("stato_richiesta", "in attesa");
                        obj.put("studente", id_studente);

                        richieste_iscrizione.add(obj).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if(task.isSuccessful()){
                                    Intent returnIntent = new Intent();
                                    setResult(Activity.RESULT_OK, returnIntent);
                                    finish();
                                }else{
                                    Toast.makeText(RichiestaIscrizioneActivity.this, ("qualcosa è andato storto nel db"), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }
                if(controllo_esistenza_codice==0){//se non esiste allora comunico allo studente che ha inserito un codice sbagliato
                    showAlert(getResources().getString(R.string.Codice_Corso_non_corretto));
                }
            }
        });
    }

    public void showAlert(String s){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getResources().getString(R.string.Not_Correct_Fields));
        alertDialog.setMessage(s);
        alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
}
