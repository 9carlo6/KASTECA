package com.kasteca.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.kasteca.R;
import com.kasteca.object.Risposta;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class RisposteAdapterFirestoreDocente extends FirestoreRecyclerAdapter<Risposta, RisposteAdapterFirestoreDocente.ViewHolder> {
    private final String LOG= "RISPOSTE_ADAPTER";
    private CommentiAdapterFirestoreDocente.OnRispondiClickListener mRispondiClickListener;
    private RisposteAdapterFirestoreDocente.Delete delete;
    private CommentiAdapterFirestoreDocente.OnModificaClickListener modificaClickListener;
    private String idDocente;
    private String nomeCognomeDocente;


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public RisposteAdapterFirestoreDocente(@NonNull FirestoreRecyclerOptions<Risposta> options, String idDocente, String nomeCognome) {
        super(options);
        this.idDocente = idDocente;
        this.nomeCognomeDocente = nomeCognome;

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

            if(idDocente.equals(model.getProprietario())){
                holder.nomeProprietario.setText(nomeCognomeDocente);
                holder.elimina.setVisibility(View.VISIBLE);
                holder.modifica.setVisibility(View.VISIBLE);

            }else
                holder.nomeProprietario.setText(model.getProprietario().substring(0, 6));

        }
        holder.textCommento.setText(model.getTesto());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ITALY);
        holder.data.setText(sdf.format(model.getData()));

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cv;
        public TextView nomeProprietario;
        public TextView textCommento;
        public TextView data;
        public TextView elimina;
        public TextView modifica;


        public ViewHolder(View v) {
            super(v);
            cv = v.findViewById(R.id.card_view_risposta);
            nomeProprietario = v.findViewById(R.id.nome_cognome_risposta_view);
            textCommento = v.findViewById(R.id.testo_risposta_view);
            data = v.findViewById(R.id.data_risposta_view);
            elimina = v.findViewById(R.id.elimina_view);
            modifica = v.findViewById(R.id.modifica_risposta_view);


            //Abilito l'eliminazione solo se il proprietario è lo stesso
            elimina.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && delete != null){
                        delete.deleteOnClick(getSnapshots().getSnapshot(position));
                    }
                }
            });
            modifica.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && modificaClickListener != null){
                        modificaClickListener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });

        }
    }

    public void setOnRispondiClickListener(CommentiAdapterFirestoreDocente.OnRispondiClickListener clickListener){
        mRispondiClickListener = clickListener;
    }

    public interface Delete{
        void deleteOnClick(DocumentSnapshot documentSnapshot);
    }

    public void setDelete(RisposteAdapterFirestoreDocente.Delete clickDelete){
        delete=clickDelete;
    }

    public void setModificaClickListener(CommentiAdapterFirestoreDocente.OnModificaClickListener md){
        this.modificaClickListener=md;
    }


    public CommentiAdapterFirestoreDocente.OnRispondiClickListener getmRispondiClickListener() {
        return mRispondiClickListener;
    }

    public Delete getDelete() {
        return delete;
    }

    public CommentiAdapterFirestoreDocente.OnModificaClickListener getModificaClickListener() {
        return modificaClickListener;
    }

    public String getIdDocente() {
        return idDocente;
    }

    public String getNomeCognomeDocente() {
        return nomeCognomeDocente;
    }
}
