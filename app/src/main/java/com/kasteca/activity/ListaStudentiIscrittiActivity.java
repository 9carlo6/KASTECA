package com.kasteca.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kasteca.R;
import com.kasteca.adapter.ListaStudentiIscrittiAdapter;
import com.kasteca.object.Studente;

import java.util.ArrayList;
import java.util.List;

public class ListaStudentiIscrittiActivity extends AppCompatActivity {

    private RecyclerView rv;
    private List<Studente> studenti;
    private String id_corso;
    private ArrayList<String> lista_codici_studenti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_studenti_iscritti);

        // la striga codice_corso serve per scaricare gli studenti iscritti al corso specifico
        id_corso = getIntent().getStringExtra("id_corso");

        // creo un arraylist che conterra gli studenti iscritti
        studenti = new ArrayList<>();

        // questo è il recyclerView per la lista degli studenti iscritti
        rv = findViewById(R.id.rv_lista_studenti_iscritti);

        // se si è certi che le dimensioni non cambieranno allora si scrive questo
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        // questa parte serve in caso di refresh della pagina per questo è ripetitiva
        // in futuro la parte di query sul db potrebbe essere fatta anche in una funzione esterna
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout_lista_studenti_iscritti);
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh, R.color.refresh1, R.color.refresh2);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        studenti = new ArrayList<>();
                        rv = findViewById(R.id.rv_lista_studenti_iscritti);

                        // se si è certi che le dimensioni non cambieranno
                        rv.setHasFixedSize(true);

                        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                        rv.setLayoutManager(llm);

                        ListaStudentiIscrittiAdapter adapter = new ListaStudentiIscrittiAdapter(studenti, id_corso);
                        rv.setAdapter(adapter);

                        lista_codici_studenti = new ArrayList<>();

                        // scarico prima gli id degli studenti relativi al corso specifico da firebase e poi scarico con essi i dati relativi a tutti gli studenti
                        // potrebbe essere fatta anche in una funzione esterna al metodo onCreate()
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        CollectionReference corsi= db.collection("Corsi");
                        corsi.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for(DocumentSnapshot documentiCorsi :task.getResult()){
                                        // prendo il documento del corso specifico e scarico gli id degli studenti caricandoli nell'arraylist lista_codici_studenti
                                        if(documentiCorsi.getId().equals(id_corso)){
                                            ArrayList<?> ar = (ArrayList<?>) documentiCorsi.get("lista_studenti");
                                            ArrayList<String> lista_studenti = new ArrayList<>();
                                            for(Object x : ar){
                                                lista_studenti.add((String) x);
                                            }
                                            lista_codici_studenti = lista_studenti;

                                            // scarico i dati relativi a tutti gli studenti e li carico nella lista studenti
                                            FirebaseFirestore dbs = FirebaseFirestore.getInstance();
                                            CollectionReference studentiFirebase= dbs.collection("Studenti");
                                            studentiFirebase.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if(task.isSuccessful()){
                                                        // qui si scorre tutti i documenti
                                                        for(DocumentSnapshot document :task.getResult()){
                                                            // qui si scorre tutti i codici presenti nell'arraylist lista_codici_studenti
                                                            for(String idStudente : lista_codici_studenti){
                                                                // se trova un uguaglianza allora lo studente appartiene al corso e quindi viene aggiunto alla lista studenti da passare all'adapter
                                                                if(idStudente.equals(document.getId())){
                                                                    Studente studente = new Studente(document.getId(),
                                                                            document.get("nome").toString(),
                                                                            document.get("cognome").toString(),
                                                                            document.get("email").toString(),
                                                                            document.get("matricola").toString());
                                                                    //Toast.makeText(getApplicationContext(), document.get("nome").toString(), Toast.LENGTH_LONG).show();
                                                                    studenti.add(studente);
                                                                }
                                                            }
                                                        }
                                                        if(studenti.isEmpty()){
                                                            if(!isFinishing())
                                                                showAlert();
                                                        }
                                                        ListaStudentiIscrittiAdapter adapter = new ListaStudentiIscrittiAdapter(studenti, id_corso);
                                                        rv.setAdapter(adapter);
                                                    }
                                                    else{
                                                        Toast.makeText(getApplicationContext(), "fallimento", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "fallimento", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                },3000);
            }
        });

        lista_codici_studenti = new ArrayList<>();

        // scarico prima gli id degli studenti relativi al corso specifico da firebase e poi scarico con essi i dati relativi a tutti gli studenti
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference corsi= db.collection("Corsi");
        corsi.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot documentiCorsi :task.getResult()){
                        // prendo il documento del corso specifico e scarico gli id degli studenti caricandoli nell'arraylist lista_codici_studenti
                        if(documentiCorsi.getId().equals(id_corso)){
                            ArrayList<?> ar = (ArrayList<?>) documentiCorsi.get("lista_studenti");
                            ArrayList<String> lista_studenti = new ArrayList<>();
                            for(Object x : ar){
                                lista_studenti.add((String) x);
                            }
                            lista_codici_studenti = lista_studenti;

                            // scarico i dati relativi a tutti gli studenti e li carico nella lista studenti
                            FirebaseFirestore dbs = FirebaseFirestore.getInstance();
                            CollectionReference studentiFirebase= dbs.collection("Studenti");
                            studentiFirebase.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        // qui si scorre tutti i documenti
                                        for(DocumentSnapshot document :task.getResult()){
                                            // qui si scorre tutti i codici presenti nell'arraylist lista_codici_studenti
                                            for(String idStudente : lista_codici_studenti){
                                                // se trova un uguaglianza allora lo studente appartiene al corso e quindi viene aggiunto alla lista studenti da passare all'adapter
                                                if(idStudente.equals(document.getId())){
                                                    Studente studente = new Studente(document.getId(), document.get("nome").toString(), document.get("cognome").toString(),document.get("email").toString(), document.get("matricola").toString());
                                                    //Toast.makeText(getApplicationContext(), document.get("nome").toString(), Toast.LENGTH_LONG).show();
                                                    studenti.add(studente);
                                                }
                                            }
                                        }
                                        if(studenti.isEmpty()){
                                            if(!isFinishing())
                                                showAlert();
                                        }
                                        ListaStudentiIscrittiAdapter adapter = new ListaStudentiIscrittiAdapter(studenti, id_corso);
                                        rv.setAdapter(adapter);
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), "fallimento", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "fallimento", Toast.LENGTH_LONG).show();
                }
            }
        });

        ListaStudentiIscrittiAdapter adapter = new ListaStudentiIscrittiAdapter(studenti, id_corso);
        rv.setAdapter(adapter);
    }

    // questa funzione serve per mostrare un dialog nel caso di assenza di studenti iscritti
    public void showAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getResources().getString(R.string.nessuno_studente_iscritto));
        alertDialog.setNeutralButton(getResources().getString(R.string.Dialog_neutral_button_nessuno_studente_iscritto), new DialogInterface.OnClickListener() {
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
