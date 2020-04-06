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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.kasteca.R;
import com.kasteca.activity.CorsoStudenteActivity;
import com.kasteca.adapter.RecyclerViewAdapterCorsi;
import com.kasteca.object.Corso;
import com.kasteca.object.Studente;

import java.util.ArrayList;
import java.util.Map;

public class CorsiStudenteFragment extends Fragment implements  RecyclerViewAdapterCorsi.OnNoteListener {

    private String LOG = "DEBUG_CORSI_STUDENTE_FRAGMENT";
    private RecyclerView recyclerView=null;
    private Studente studente;
    private ArrayList<String> idcorsi; // serve per l'adapter, viene riempito quando si fa il get dei corsi su firebase
    private ArrayList<Corso> corsi= null;
    private View view;
    private RecyclerViewAdapterCorsi adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.e(LOG,"Creazione Fragment");

        Bundle bundleStudente= getArguments();
        if(getArguments()== null){
            Log.e(LOG,"Bundle non ricevuto.");
        }

        Log.e(LOG,"Bundle: "+bundleStudente.toString());
        //Recupero dati dal bundle
        studente = new Studente();
        studente.setId(bundleStudente.getString("id"));
        studente.setNome( bundleStudente.getString("nome"));
        studente.setCognome(bundleStudente.getString("cognome"));
        studente.setEmail(bundleStudente.getString("email"));
        studente.setMatricola(bundleStudente.getString("matricola"));
        idcorsi= bundleStudente.getStringArrayList("id_corsi");

        Log.e(LOG,"id corsi studente: "+idcorsi.toString());

        view = inflater.inflate(R.layout.fragment_corsi_studente, container, false);

        recuperoCorsi(idcorsi);

        //Carichiamo la recycleview.
        recyclerView= (RecyclerView) view.findViewById(R.id.reclycleview_studente_corsi);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    private void recuperoCorsi(ArrayList<String> idcorsi){
        Log.e(LOG,"Recupero corsi");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference corsiReference = db.collection("Corsi");
        Source source = Source.SERVER;

        //Facciamo una query per ogni corso dello studente e lo aggiungiamo alla recycleview tramite il suo adapter
        for(String id: idcorsi) {
            corsiReference.whereEqualTo(com.google.firebase.firestore.FieldPath.documentId(), id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot documenti_corsi : task.getResult()) {
                            Log.d(LOG, "Documento.");
                            Map<String, Object> c = documenti_corsi.getData();
                            Corso corso = new Corso(
                                    c.get("nome_corso").toString(),
                                    c.get("anno_accademico").toString(),
                                    c.get("descrizione").toString(),
                                    c.get("codice").toString(),
                                    documenti_corsi.getId());
                            Log.d(LOG, "Corso: "+corso.toString());
                            aggiornamentoRecycleView(corso);
                        }

                    } else {
                        Log.e(LOG,"QUERY FAIL.");
                    }
                }
            });

        }

    }


    private void aggiornamentoRecycleView(Corso corso){
        Log.e(LOG,"aggiornamentoRecycleView");

        if(corsi == null){
            Log.e(LOG,"Primo elemento");
            //Caso nel quale la recycleview viene creata per la prima volta
            corsi = new ArrayList<Corso>();
            corsi.add(corso);
            adapter= new RecyclerViewAdapterCorsi(corsi,this);
            recyclerView.setAdapter(adapter);
        }else{
            //Caso nel quale la recycleview già esiste
            Log.e(LOG,"Aggiunta elementi diversi dal primo");
            corsi.add(corso);
        }


    }




    @Override
    public void onNoteClick(int position) {
        //intent per l'Activity del corso
        Intent intent= new Intent(getActivity(), CorsoStudenteActivity.class);
        Bundle bundle= new Bundle();

        //Passiamo all'activity del corso il codice del documento firebase del corso
        //in modo che possa recuperarlo autonomamente.
        bundle.putString("codice_corso",this.corsi.get(position).getId());
        bundle.putString("id", studente.getId());
        bundle.putString("nome", studente.getNome());
        bundle.putString("cognome", studente.getCognome());
        bundle.putString("email", studente.getEmail());
        bundle.putString("matricola", studente.getMatricola());

        intent.putExtras(bundle);
        startActivity(intent);
    }



}
