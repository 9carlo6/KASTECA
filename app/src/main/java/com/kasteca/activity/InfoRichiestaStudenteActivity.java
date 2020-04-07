package com.kasteca.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kasteca.R;
import com.kasteca.adapter.ListaRichiesteStudentiAdapter;
import com.kasteca.object.Richiesta;
import com.kasteca.object.Studente;

import java.util.Date;

public class InfoRichiestaStudenteActivity extends AppCompatActivity {

    private TextView nome_studente_text;
    private TextView email_studente_text;
    private TextView matricola_studente_text;
    private TextView data_richiesta_text;
    private Bundle bundleStudente;

    private String codice_corso;
    private String id_studente;
    private String id_richiesta;
    private String id_corso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_richiesta_studente);

        nome_studente_text = findViewById(R.id.textViewRichiestaNomeStudente);
        email_studente_text = findViewById(R.id.textViewRichiestaEmailStudente);
        matricola_studente_text = findViewById(R.id.textViewRichiestaMatricolaStudente);
        data_richiesta_text = findViewById(R.id.textViewDataRichiesta);

        bundleStudente = getIntent().getExtras();
        nome_studente_text.setText(bundleStudente.getString("nome_cognome"));
        email_studente_text.setText(bundleStudente.getString("email"));
        matricola_studente_text.setText(bundleStudente.getString("matricola"));
        data_richiesta_text.setText(bundleStudente.getString("data_richiesta"));

        id_studente = bundleStudente.getString("id_studente");
        codice_corso = bundleStudente.getString("codice_corso");
        id_richiesta = bundleStudente.getString("id_richiesta");


        // vado a prendere l'id del corso dal database
        FirebaseFirestore db2 = FirebaseFirestore.getInstance();
        CollectionReference corsi = db2.collection("Corsi");
        corsi.whereEqualTo("codice", codice_corso).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot document :task.getResult()){
                        id_corso = document.getId();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "fallimento", Toast.LENGTH_LONG).show();
                }
            }
        });



    }

    public void accettaRichiesta(View v){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference richieste = db.collection("Richieste_Iscrizione");
        richieste.document(id_richiesta)
                .update("stato_richiesta", "accettata")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // caricare il corso nella lista dei corsi dello studente in questione
                            FirebaseFirestore db1 = FirebaseFirestore.getInstance();
                            CollectionReference studenti = db1.collection("Studenti");
                            studenti.document(id_studente)
                                    .update("lista_corsi", FieldValue.arrayUnion(id_corso))
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                // caricare lo studente nella lista degli studenti del corso
                                                FirebaseFirestore db2 = FirebaseFirestore.getInstance();
                                                CollectionReference corsi = db2.collection("Corsi");
                                                corsi.document(id_corso)
                                                        .update("lista_studenti", FieldValue.arrayUnion(id_studente))
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    Intent returnIntent = new Intent();
                                                                    setResult(Activity.RESULT_OK, returnIntent);
                                                                    finish();
                                                                }
                                                            }
                                                        });
                                            }else{
                                                Toast.makeText(getApplicationContext(), "fallimento", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(getApplicationContext(), "Fallimento nella modifica dello stato della richiesta", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    public void rifiutaRichiesta(View v) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference richieste = db.collection("Richieste_Iscrizione");
        richieste.document(id_richiesta)
                .update("stato_richiesta", "rifiutata")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Intent intent = new Intent(getApplicationContext(), ListaRichiesteStudentiActivity.class);
                            Intent returnIntent = new Intent();
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                            //intent.putExtra("codice_corso", codice_corso);
                            //startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Fallimento nella modifica dello stato della richiesta", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}
