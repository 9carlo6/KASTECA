package com.kasteca.activity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kasteca.R;
import com.kasteca.adapter.ListaRichiesteStudentiAdapter;
import com.kasteca.adapter.ListaStudentiIscrittiAdapter;
import com.kasteca.object.Richiesta;
import com.kasteca.object.Studente;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListaRichiesteStudentiActivity extends AppCompatActivity {

    private String LOG = "DEBUG_LISTA_STUDENTE_ACTIVITY";
    private RecyclerView rv;
    private List<Richiesta> richieste;
    private String codice_corso;
    private ArrayList<String> lista_codici_studenti;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_richieste_studenti);

        // la striga codice_corso serve per scaricare gli studenti iscritti al corso specifico
        codice_corso = getIntent().getStringExtra("codice_corso");

        // creo un arraylist che conterra le richieste
        richieste = new ArrayList<>();

        // questo è il recyclerView per la lista delle richieste
        rv = (RecyclerView) findViewById(R.id.rv_lista_richieste_studenti);

        // se si è certi che le dimensioni non cambieranno allora si scrive questo
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        // questa parte serve in caso di refresh della pagina per questo è ripetitiva
        // in futuro la parte di query sul db potrebbe essere fatta anche in una funzione esterna
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout_lista_richieste_studenti);
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh, R.color.refresh1, R.color.refresh2);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        richieste = new ArrayList<>();
                        rv = (RecyclerView) findViewById(R.id.rv_lista_richieste_studenti);

                        // se si è certi che le dimensioni non cambieranno
                        rv.setHasFixedSize(true);

                        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                        rv.setLayoutManager(llm);

                        ListaRichiesteStudentiAdapter adapter = new ListaRichiesteStudentiAdapter(richieste);
                        rv.setAdapter(adapter);

                        // scarico prima tutte le richieste relative al corso specifico e poi gli studenti relativi a queste richieste
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        CollectionReference richieste_iscrizione = db.collection("Richieste_Iscrizione");
                        richieste_iscrizione.whereEqualTo("codice_corso", codice_corso).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (final DocumentSnapshot document : task.getResult()) {
                                        // scarico i dati relativi allo studente della richiesta e poi carico la richiesta nell'array
                                        FirebaseFirestore dbs = FirebaseFirestore.getInstance();
                                        CollectionReference studenti_firebase= dbs.collection("Studenti");
                                        studenti_firebase.whereEqualTo(com.google.firebase.firestore.FieldPath.documentId(), document.get("studente")).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if(task.isSuccessful()){
                                                    // qui si scorre tutti i documenti
                                                    for(DocumentSnapshot studente_document :task.getResult()){
                                                        Studente studente = new Studente(studente_document.getId(), studente_document.get("nome").toString(), studente_document.get("cognome").toString(),studente_document.get("email").toString(), studente_document.get("matricola").toString());
                                                        //Toast.makeText(getApplicationContext(), document.get("nome").toString(), Toast.LENGTH_LONG).show();
                                                        Richiesta richiesta = new Richiesta(document.getId().toString(), codice_corso, (Date) document.getDate("data"), document.get("stato_richiesta").toString(), studente);
                                                        //studenti.add(studente);
                                                        richieste.add(richiesta);
                                                    }
                                                    ListaRichiesteStudentiAdapter adapter = new ListaRichiesteStudentiAdapter(richieste);
                                                    rv.setAdapter(adapter);
                                                }
                                                else{
                                                    Toast.makeText(getApplicationContext(), "fallimento nello scaricare gli studenti", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "fallimento nello scaricare le richieste", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }, 3000);
            }
        });


        // scarico prima tutte le richieste relative al corso specifico e poi gli studenti relativi a queste richieste
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference richieste_iscrizione = db.collection("Richieste_Iscrizione");
        richieste_iscrizione.whereEqualTo("codice_corso", codice_corso).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (final DocumentSnapshot document : task.getResult()) {
                        // scarico i dati relativi allo studente della richiesta e poi carico la richiesta nell'array
                        FirebaseFirestore dbs = FirebaseFirestore.getInstance();
                        CollectionReference studenti_firebase= dbs.collection("Studenti");
                        studenti_firebase.whereEqualTo(com.google.firebase.firestore.FieldPath.documentId(), document.get("studente")).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    // qui si scorre tutti i documenti
                                    for(DocumentSnapshot studente_document :task.getResult()){
                                        Studente studente = new Studente(studente_document.getId(), studente_document.get("nome").toString(), studente_document.get("cognome").toString(),studente_document.get("email").toString(), studente_document.get("matricola").toString());
                                        //Toast.makeText(getApplicationContext(), document.get("nome").toString(), Toast.LENGTH_LONG).show();
                                        Richiesta richiesta = new Richiesta(document.getId().toString(), codice_corso, (Date) document.getDate("data"), document.get("stato_richiesta").toString(), studente);
                                        //studenti.add(studente);
                                        richieste.add(richiesta);
                                    }
                                    ListaRichiesteStudentiAdapter adapter = new ListaRichiesteStudentiAdapter(richieste);
                                    rv.setAdapter(adapter);
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "fallimento nello scaricare gli studenti", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "fallimento nello scaricare le richieste", Toast.LENGTH_LONG).show();
                }
            }

        });


        ListaRichiesteStudentiAdapter adapter = new ListaRichiesteStudentiAdapter(richieste);
        rv.setAdapter(adapter);
    }
}

