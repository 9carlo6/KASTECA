package com.kasteca.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.kasteca.R;
import com.kasteca.activity.CorsoActivity;
import com.kasteca.activity.LogDocenteActivity;
import com.kasteca.object.Corso;
import com.kasteca.object.Docente;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CorsiDocenteFragment extends Fragment implements  RecyclerViewAdapterCorsi.OnNoteListener{

    private RecyclerView recyclerView=null;

    private String LOG = "DEBUG_CORSI_DOCENTE_FRAGMENT";

    private Docente docente;

    private ArrayList<Corso> corsiArrayList;
    private ArrayList<String> id_corsi;
    private View view;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //LOG DEBUG////////////
        Log.d(LOG,"Cambiamento fragment, verifica docente:");
        /////////////////////

        Bundle bundle= getArguments();
        //Recupero dati dal bundle
        docente = new Docente();
        docente.setNome(bundle.getString("nome"));
        docente.setCognome(bundle.getString("cognome"));
        docente.setEmail(bundle.getString("email"));
        id_corsi=bundle.getStringArrayList("lista_corsi");
        docente.setId(bundle.getString("id"));
        Log.d(LOG," Dati recuperati dal bundle.");

        view = inflater.inflate(R.layout.fragment_corsi_docente, container, false);
        //Carichiamo la recycleview.
        recyclerView= (RecyclerView) view.findViewById(R.id.reclycleview_docente_corsi);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Avviamo metodo per il recupero dei corsi da firebase
        recuperoCorsi(id_corsi);

        Log.d(LOG,"RecycleView impostata.");

        return view;
    }



    void recuperoCorsi(ArrayList<String> corsi) {
        Log.d(LOG,"recupero corsi da firebase ...");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference corsiReference = db.collection("Corsi");
        //QuerySnapshot q =db.collection("Corsi").whereEqualTo(com.google.firebase.firestore.FieldPath.documentId(),  idCorso);
        //where(firebase.firestore.FieldPath.documentId(), '==', "scindv");
        Source source = Source.SERVER;

        Log.e(LOG, "Lista codici corsi: ");

        if (!corsi.isEmpty()) {
            //Per ogni id corso che abbiamo, facciamo una query, lo cerchiamo e lo aggiungiamo alla classe studente.
            for (String idCorso : corsi) {
                Log.e(LOG, "Codice corso: " + idCorso);
                corsiReference.whereEqualTo(com.google.firebase.firestore.FieldPath.documentId(), idCorso).get(source).addOnCompleteListener(
                        new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()) {
                                    //FirebaseAuth mAuth1 = FirebaseAuth.getInstance();
                                    //FirebaseUser user = mAuth1.getCurrentUser();
                                    if (task.getResult() == null) {
                                        Log.e(LOG, "risultato della query null");
                                    } else {

                                        //Controllo dati scaticati da firebase
                                        List<DocumentSnapshot> testDocuments = task.getResult().getDocuments();
                                        for (DocumentSnapshot d : testDocuments) {
                                            Log.e(LOG, d.getData().toString());
                                            Log.e(LOG, d.getData().get("codice").toString());
                                            Log.e(LOG, d.getData().get("descrizione").toString());
                                            Log.e(LOG, d.getData().get("anno_accademico").toString());
                                            Log.e(LOG, d.getData().get("nome_corso").toString());
                                            Log.e(LOG, d.getId());
                                        }

                                        //Per ogni corso controllo se è del docente
                                        for (DocumentSnapshot document : task.getResult().getDocuments()) {
                                            //if (user.getUid().equalsIgnoreCase(document.get("id").toString())) {
                                            //Controllo del risultato della query:
                                            if (document == null) {
                                                Log.e(LOG, "risultato della query null: document == null");

                                            } else {
                                                //Se la richiesta è andata a buon fine, creo un ArrayList
                                                //di corsi, all'interno del quale inserisco i corsi scariscati
                                                //tramite firebase e che serviranno per creare la recycleView
                                                ArrayList<Corso> cors= new ArrayList<Corso>();
                                                List<DocumentSnapshot> corsi = task.getResult().getDocuments();
                                                for (DocumentSnapshot d : corsi) {
                                                    Log.e(LOG, d.getData().toString());
                                                    Map<String, Object> c = d.getData();
                                                    Corso corso = new Corso(
                                                            c.get("nome_corso").toString(),
                                                            c.get("anno_accademico").toString(),
                                                            c.get("descrizione").toString(),
                                                            docente.getId(),
                                                            c.get("codice").toString(),
                                                            d.getId());
                                                    Log.e(LOG, "Corso: " + corso.toString());

                                                    cors.add(corso);

                                                }
                                                //Dopo la creazione dei corsi dobbiamo richiamare il metodo
                                                //per la creazione della recycleview.
                                                creazioneRecycleView(cors);

                                            }

                                        }

                                    }

                                }
                            }
                        });


            }


        } else {
            Log.e(LOG, "Nessun corso presente per questo docente.");
        }
    }




    //Metodo per la creazione della recycleView del fragment
    private void creazioneRecycleView(ArrayList<Corso> corsi){
        Log.e(LOG,"CARICAMENTO ADAPTER RECYCLEVIEW.");
        this.corsiArrayList= corsi;
        recyclerView.setAdapter(new RecyclerViewAdapterCorsi(corsi,this));

    }


    //Metodo dell'interface
    //Metodo che verrà usato come OnClick dagli elementi gestiti dall'adapter.
    @Override
    public void onNoteClick(int position) {
        Log.d(LOG,"Eseguzione onNoteClick sul corso in posizione: "+position);

        //intent per l'Activity del corso
        Intent intent= new Intent(getActivity(),CorsoActivity.class);
        Bundle bundle= new Bundle();

        //Passiamo all'activity del corso il codice del documento firebase del corso
        //in modo che possa recuperarlo autonomamente.
        bundle.putString("codice_corso",this.corsiArrayList.get(position).getId());

        intent.putExtras(bundle);
        startActivity(intent);
    }


}
