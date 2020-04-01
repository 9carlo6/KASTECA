package com.kasteca.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;

import com.kasteca.R;
import com.kasteca.object.Corso;
import com.kasteca.object.Docente;

public class CorsiDocenteFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Context context;

    private String LOG = "DEBUG_CORSI_DOCENTE_FRAGMENT";
    private Docente docente;





    public CorsiDocenteFragment(){}

    //Costruttore che permette di inviare al Fragment il Docente
    public CorsiDocenteFragment(Docente docente,Context context){
        this.docente=docente;
        this.context=context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //LOG DEBUG////////////
        Log.e(LOG,"Cambiamento fragment, verifica docente:");
        Log.e(LOG,docente.toString());
        for(Corso corso: docente.getLista_corsi() ){
            Log.e(LOG,corso.toString());
        }
        /////////////////////
        View v= inflater.inflate(R.layout.fragment_corsi_docente, container, false);

        recyclerView= (RecyclerView) v.findViewById(R.id.reclycleview_docente_corsi);
        if(recyclerView==null){
            Log.e(LOG,"ERRORE CARICAMENTO RECYCLEVIEW: NULL OBJECT.");
        }
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new RecyclerViewAdapterCorsi(this.context,docente.getLista_corsi()));



        Log.e(LOG,"RecycleView impostata.");


        return v;
    }





}
