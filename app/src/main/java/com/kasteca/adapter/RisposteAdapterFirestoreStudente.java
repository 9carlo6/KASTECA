package com.kasteca.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.kasteca.R;
import com.kasteca.object.Risposta;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class RisposteAdapterFirestoreStudente extends RisposteAdapterFirestoreDocente {

    private String nomeCognomeStudente;
    private String idStudente;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     * @param idDocente
     * @param nomeCognome
     * @param nomeCognomeStudente
     * @param idStudente
     */
    public RisposteAdapterFirestoreStudente(@NonNull FirestoreRecyclerOptions<Risposta> options, String idDocente, String nomeCognome, String nomeCognomeStudente, String idStudente) {
        super(options, idDocente, nomeCognome);
        this.idStudente=idStudente;
        this.nomeCognomeStudente= nomeCognomeStudente;
    }


    public RisposteAdapterFirestoreDocente.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.risposta_item, parent, false);
        RisposteAdapterFirestoreDocente.ViewHolder vh = new RisposteAdapterFirestoreDocente.ViewHolder(v);
        return vh;
    }


    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Risposta model) {
        // AGGIUNGERE QUESTA PARTE MANCA IL PROPRIETARIO DELLA RISPOSTA
        holder.nomeProprietario.setText(model.getProprietario());
        if(model.getProprietario()!=null){

            if(super.getIdDocente().equals(model.getProprietario())){
                holder.nomeProprietario.setText(super.getNomeCognomeDocente());
                if(idStudente==null) {
                    holder.elimina.setVisibility(View.VISIBLE);
                    holder.modifica.setVisibility(View.VISIBLE);
                }
            }else
            if(idStudente!=null && nomeCognomeStudente!=null && idStudente.equals(model.getProprietario())){
                holder.nomeProprietario.setText(nomeCognomeStudente);
                holder.elimina.setVisibility(View.VISIBLE);
                holder.modifica.setVisibility(View.VISIBLE);
            }else
                holder.nomeProprietario.setText(model.getProprietario().substring(0, 6));

        }
        holder.textCommento.setText(model.getTesto());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ITALY);
        holder.data.setText(sdf.format(model.getData()));

    }

}
