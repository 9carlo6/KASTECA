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
import com.kasteca.activity.CorsoDocenteActivity;
import com.kasteca.adapter.RecyclerViewAdapterCorsi;
import com.kasteca.object.Corso;
import com.kasteca.object.Docente;

import java.util.ArrayList;
import java.util.Map;

public class CorsiDocenteFragment extends Fragment implements  RecyclerViewAdapterCorsi.OnNoteListener{

    private RecyclerView recyclerView=null;
    private String LOG = "DEBUG_CORSI_DOCENTE_FRAGMENT";
    private Docente docente;
    private ArrayList<Corso> corsiArrayList;
    private ArrayList<Corso> corsi; // serve per l'adapter, viene riempito quando si fa il get dei corsi su firebase
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle= getArguments();
        //Recupero dati dal bundle
        docente = new Docente();
        docente.setNome(bundle.getString("nome"));
        docente.setCognome(bundle.getString("cognome"));
        docente.setEmail(bundle.getString("email"));
        docente.setId(bundle.getString("id"));

        view = inflater.inflate(R.layout.fragment_corsi_docente, container, false);
        //Carichiamo la recycleview.
        recyclerView= (RecyclerView) view.findViewById(R.id.reclycleview_docente_corsi);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Avviamo metodo per il recupero dei corsi da firebase
        recuperoCorsi(docente.getId());

        Log.d(LOG,"RecycleView impostata.");

        return view;
    }


    void recuperoCorsi(String id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference corsiReference = db.collection("Corsi");
        Source source = Source.SERVER;
        //Toast.makeText(getActivity(),"jrlpppppsdfsdfsd",Toast.LENGTH_SHORT).show();
        corsi = new ArrayList<Corso>();

        //Per ogni id corso che abbiamo, facciamo una query, lo cerchiamo e lo aggiungiamo alla classe studente.
        corsiReference.whereEqualTo("docente",id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documenti_corsi : task.getResult()) {
                        Log.d(LOG,"Documento.");
                        Map<String, Object> c = documenti_corsi.getData();
                        Corso corso = new Corso(
                                    c.get("nome_corso").toString(),
                                    c.get("anno_accademico").toString(),
                                    c.get("descrizione").toString(),
                                    c.get("codice").toString(),
                                    documenti_corsi.getId(),
                                    c.get("docente").toString());
                        corsi.add(corso);

                    }
                    creazioneRecycleView(corsi);
                }else{
                    // c'è stato un problema nel get
                }
            }
        });

    }

    //Metodo per la creazione della recycleView del fragment
    private void creazioneRecycleView(ArrayList<Corso> corsi){
        this.corsiArrayList= corsi;
        recyclerView.setAdapter(new RecyclerViewAdapterCorsi(corsi,this));

    }

    //Metodo dell'interface
    //Metodo che verrà usato come OnClick dagli elementi gestiti dall'adapter.
    @Override
    public void onNoteClick(int position) {

        //intent per l'Activity del corso
        Intent intent= new Intent(getActivity(), CorsoDocenteActivity.class);
        Bundle bundle= new Bundle();

        //Passiamo all'activity del corso il codice del documento firebase del corso
        //in modo che possa recuperarlo autonomamente.
        bundle.putString("id_corso",this.corsiArrayList.get(position).getId());
        bundle.putString("codice_corso", this.corsiArrayList.get(position).getCodice());
        bundle.putString("nome_corso", this.corsiArrayList.get(position).getNome());
        bundle.putString("anno_accademico", this.corsiArrayList.get(position).getAnno_accademico());

        bundle.putString("id_docente", docente.getId());
        bundle.putString("nome_docente", docente.getNome());
        bundle.putString("cognome_docente", docente.getCognome());
        bundle.putString("email_docente", docente.getEmail());

        intent.putExtras(bundle);
        startActivity(intent);
    }

}
