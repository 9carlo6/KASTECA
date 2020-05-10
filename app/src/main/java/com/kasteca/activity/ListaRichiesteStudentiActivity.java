package com.kasteca.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

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
import com.kasteca.object.Richiesta;
import com.kasteca.object.Studente;

import java.util.ArrayList;
import java.util.List;

public class ListaRichiesteStudentiActivity extends AppCompatActivity {

    private static final String TAG = "DEBUG_LISTA_RICHIESTE";
    private RecyclerView rv;
    private List<Richiesta> lista_richieste;
    private List<Studente> lista_studenti;
    private String codice_corso;
    private ArrayList<String> lista_codici_studenti;
    //private String id_studente;
    private int contatore_studenti_caricati_nella_lista = 0;

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
        rv = findViewById(R.id.rv_lista_richieste_studenti);

        // se si è certi che le dimensioni non cambieranno allora si scrive questo
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);


        // questa parte serve in caso di refresh della pagina per questo è ripetitiva
        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeLayout_lista_richieste_studenti);
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
                        rv = findViewById(R.id.rv_lista_richieste_studenti);

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
        lista_studenti = new ArrayList<>();

        // scarico prima gli id degli studenti relativi al corso specifico da firebase e poi scarico con essi i dati relativi a tutti gli studenti
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference richiesteIscrizione= db.collection("Richieste_Iscrizione");

        richiesteIscrizione.whereEqualTo("codice_corso", codice_corso).whereEqualTo("stato_richiesta", "in attesa").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "Download delle richieste ok");
                    for(DocumentSnapshot documentiRichieste :task.getResult()){
                        lista_richieste.add(new Richiesta(documentiRichieste.getId().toString(),
                                codice_corso,
                                documentiRichieste.getDate("data"),
                                documentiRichieste.get("stato_richiesta").toString()));
                        lista_codici_studenti.add(documentiRichieste.get("studente").toString());
                    }
                    if(lista_richieste.isEmpty()){
                        Log.d(TAG, "Non ci sono richieste in attesa per questo corso");
                        if(!isFinishing())
                            showAlert();
                    }else{
                        Log.d(TAG, "Ci sono richieste in attesa per questo corso");
                        FirebaseFirestore dbs = FirebaseFirestore.getInstance();
                        CollectionReference studentiFirebase= dbs.collection("Studenti");
                        contatore_studenti_caricati_nella_lista = 0;

                        // per ogni id studente relativo alle richieste del corso specifico scarico lo studente corrispondente
                        for(String idStudente : lista_codici_studenti){
                            studentiFirebase.document(idStudente).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()){
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            Log.d(TAG, "Download del documento relativo allo studente " + document.get("nome").toString() + " " + document.get("cognome").toString() + " andato a buon fine");
                                            Studente studente = new Studente(document.getId(), document.get("nome").toString(), document.get("cognome").toString(),document.get("email").toString(), document.get("matricola").toString());
                                            contatore_studenti_caricati_nella_lista++;
                                            lista_studenti.add(studente);

                                            // quando siamo arrivati alla fine della lista vado a settare l'adapter
                                            // c'è bisogno di mettere la condizione qui perche mettendo il set dell'adapter al di fuori del for che cicla i codici degli studenti
                                            // non riesce a scaricare in tempo gli studenti per passarli all'adapter
                                            if(contatore_studenti_caricati_nella_lista==lista_codici_studenti.size()){
                                                Log.d(TAG, "Sono stati scaricati tutti gli studenti");
                                                ListaRichiesteStudentiAdapter adapter = new ListaRichiesteStudentiAdapter(lista_richieste, lista_studenti);
                                                rv.setAdapter(adapter);
                                            }
                                        } else {
                                            Log.d(TAG, "ERRORE: Non esiste un documento relativo a questo studente");
                                        }
                                    }else{
                                        Log.d(TAG, "ERRORE: Problema nella richiesta del documento ", task.getException());
                                    }
                                }
                            });
                        }
                    }
                }else{
                    Log.d(TAG, "Qualcosa è andato storto nel download delle richieste");
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

    // questa funzione serve per mostrare un dialog nel caso di assenza di nuove richieste
    public void showAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getResources().getString(R.string.nessuna_richiesta));
        alertDialog.setNeutralButton(getResources().getString(R.string.Dialog_neutral_button_nessuna_richiesta), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                //finish();
            }
        });
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        alertDialog.show();
    }
}

