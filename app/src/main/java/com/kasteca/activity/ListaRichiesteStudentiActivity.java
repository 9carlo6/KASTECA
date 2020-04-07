package com.kasteca.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    private RecyclerView rv;
    private List<Richiesta> lista_richieste;
    private List<Studente> lista_studenti;
    private String codice_corso;
    private ArrayList<String> lista_codici_studenti;
    private String id_studente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_richieste_studenti);

        // la striga codice_corso serve per scaricare gli studenti iscritti al corso specifico
        codice_corso = getIntent().getStringExtra("codice_corso");

        // creo un arraylist che conterra le richieste
        lista_richieste = new ArrayList<>();

        // creo un arraylist che conterra le richieste
        lista_studenti = new ArrayList<>();

        // questo è il recyclerView per la lista delle richieste
        rv = (RecyclerView) findViewById(R.id.rv_lista_richieste_studenti);

        // se si è certi che le dimensioni non cambieranno allora si scrive questo
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);


        // questa parte serve in caso di refresh della pagina per questo è ripetitiva
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
                        lista_richieste = new ArrayList<>();
                        rv = (RecyclerView) findViewById(R.id.rv_lista_richieste_studenti);

                        // se si è certi che le dimensioni non cambieranno
                        rv.setHasFixedSize(true);

                        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                        rv.setLayoutManager(llm);

                        ListaRichiesteStudentiAdapter adapter = new ListaRichiesteStudentiAdapter(lista_richieste, lista_studenti);
                        rv.setAdapter(adapter);

                        scaricaRichieste();
                    }
                }, 3000);
            }
        });

        scaricaRichieste();

        ListaRichiesteStudentiAdapter adapter = new ListaRichiesteStudentiAdapter(lista_richieste, lista_studenti);
        rv.setAdapter(adapter);
    }

    public void scaricaRichieste(){
        lista_codici_studenti = new ArrayList<>();

        // scarico prima gli id degli studenti relativi al corso specifico da firebase e poi scarico con essi i dati relativi a tutti gli studenti
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference richieste_iscrizione= db.collection("Richieste_Iscrizione");
        richieste_iscrizione.whereEqualTo("codice_corso", codice_corso).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot documenti_richieste :task.getResult()){
                        if(documenti_richieste.get("stato_richiesta").toString().equals("in attesa")) {
                            // prendo il documento del corso specifico e scarico gli id degli studenti caricandoli nell'arraylist lista_codici_studenti
                            lista_richieste.add(new Richiesta(documenti_richieste.getId().toString(),
                                    codice_corso,
                                    (Date) documenti_richieste.getDate("data"),
                                    documenti_richieste.get("stato_richiesta").toString()));
                            lista_codici_studenti.add(documenti_richieste.get("studente").toString());
                        }
                    }
                    // scarico i dati relativi a tutti gli studenti e li carico nella lista studenti
                    FirebaseFirestore dbs = FirebaseFirestore.getInstance();
                    CollectionReference studenti_firebase= dbs.collection("Studenti");
                    studenti_firebase.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                // qui si scorre tutti i documenti
                                for(DocumentSnapshot document :task.getResult()){
                                    // qui si scorre tutti i codici presenti nell'arraylist lista_codici_studenti
                                    for(String id_studente : lista_codici_studenti){
                                        // se trova un uguaglianza allora lo studente appartiene al corso e quindi viene aggiunto alla lista studenti da passare all'adapter
                                        if(id_studente.equals(document.getId())){
                                            Studente studente = new Studente(document.getId(), document.get("nome").toString(), document.get("cognome").toString(),document.get("email").toString(), document.get("matricola").toString());
                                            //Toast.makeText(getApplicationContext(), document.get("nome").toString(), Toast.LENGTH_LONG).show();
                                            lista_studenti.add(studente);
                                        }
                                    }
                                }
                                ListaRichiesteStudentiAdapter adapter = new ListaRichiesteStudentiAdapter(lista_richieste, lista_studenti);
                                rv.setAdapter(adapter);
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "fallimento", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(), "fallimento", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        lista_richieste = null;
        lista_studenti = null;
        lista_richieste = new ArrayList<>();
        lista_studenti = new ArrayList<>();
        //ListaRichiesteStudentiAdapter adapter = new ListaRichiesteStudentiAdapter(lista_richieste);
        //rv.setAdapter(adapter);
        scaricaRichieste();

        ListaRichiesteStudentiAdapter adapter = new ListaRichiesteStudentiAdapter(lista_richieste, lista_studenti);
        rv.setAdapter(adapter);
    }
}

