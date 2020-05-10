package com.kasteca.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.kasteca.R;
import com.kasteca.object.Commento;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class CommentiAdapterFirestoreStudente extends CommentiAdapterFirestoreDocente {

    private String nomeCognomeStudente;
    private String idStudente;

    public CommentiAdapterFirestoreStudente(@NonNull FirestoreRecyclerOptions<Commento> options, String idDocente, String nomeCognomeDocente, String nomeCognomeStudente, String idStudente){
        super( options,  idDocente,  nomeCognomeDocente);
        this.idStudente= idStudente;
        this.nomeCognomeStudente= nomeCognomeStudente;
    }


    @NonNull
    @Override
    public CommentiAdapterFirestoreDocente.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.commento_item, parent, false);
        CommentiAdapterFirestoreDocente.ViewHolder vh = new CommentiAdapterFirestoreDocente.ViewHolder(v);
        return vh;
    }



    protected void onBindViewHolder(@NonNull CommentiAdapterFirestoreStudente.ViewHolder holder, int position, @NonNull Commento model) {
        if(model.getProprietarioCommento()!=null){

            if(super.getIdDocente().equals(model.getProprietarioCommento())){
                holder.nome_proprietario.setText(super.getNomeCognomeDocente());
                if(idStudente==null){
                    holder.elimina.setVisibility(View.VISIBLE);
                    holder.modifica.setVisibility(View.VISIBLE);
                }
            }else
            if(idStudente!=null && nomeCognomeStudente!=null && idStudente.equals(model.getProprietarioCommento())){
                holder.nome_proprietario.setText(nomeCognomeStudente);
                holder.elimina.setVisibility(View.VISIBLE);
                holder.modifica.setVisibility(View.VISIBLE);
            }else
                holder.nome_proprietario.setText(model.getProprietarioCommento().substring(0, 6));

        }

        holder.text_commento.setText(model.getTesto());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ITALY);
        holder.data.setText(sdf.format(model.getData()));

    }


}
