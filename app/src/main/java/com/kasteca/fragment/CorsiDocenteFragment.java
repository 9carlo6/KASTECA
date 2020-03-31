package com.kasteca.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kasteca.R;
import com.kasteca.object.Corso;
import com.kasteca.object.Docente;

public class CorsiDocenteFragment extends Fragment {

    private String LOG = "DEBUG CORSIDOCENTEFRAGMENT";
    private Docente docente;

    public CorsiDocenteFragment(){}

    //Costruttore che permette di inviare al Fragment il Docente
    public CorsiDocenteFragment(Docente docente){
        this.docente=docente;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Creazione della classe Docente serializzata all'interno del bundle
        //Docente docente=(Docente) savedInstanceState.getSerializable("docente");
        Log.e(LOG,"Cambiamento fragment, veridica docente:");
        Log.e(LOG,docente.toString());

        for(Corso corso: docente.getLista_corsi() ){
            Log.e(LOG,corso.toString());
        }

        return inflater.inflate(R.layout.fragment_corsi_docente, container, false);
    }





}
