package com.kasteca.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.kasteca.R;
import com.kasteca.object.Risposta;

import java.text.SimpleDateFormat;

public class RisposteAdapterFirestore extends FirestoreRecyclerAdapter<Risposta, RisposteAdapterFirestore.ViewHolder> {
    private final String LOG= "RISPOSTE_ADAPTER";
    private CommentiAdapterFirestore.OnRispondiClickListener mRispondiClickListener;
    private String id_docente;
    private String nome_cognome_docente;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public RisposteAdapterFirestore(@NonNull FirestoreRecyclerOptions<Risposta> options, String id_docente, String nome_cognome_docente) {
        super(options);
        this.id_docente = id_docente;
        this.nome_cognome_docente = nome_cognome_docente;
    }

    public RisposteAdapterFirestore.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.risposta_item, parent, false);
        RisposteAdapterFirestore.ViewHolder vh = new RisposteAdapterFirestore.ViewHolder(v);
        return vh;
    }


    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Risposta model) {
        // AGGIUNGERE QUESTA PARTE MANCA IL PROPRIETARIO DELLA RISPOSTA
        holder.nome_proprietario.setText(model.getProprietario_risposta());
        holder.text_commento.setText(model.getTesto());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        holder.data.setText(sdf.format(model.getData()));
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cv;
        public TextView nome_proprietario;
        public TextView text_commento;
        public TextView data;
        public TextView risposta;
        public TextView tutte_risposte;

        public ViewHolder(View v) {
            super(v);
            cv = v.findViewById(R.id.card_view_risposta);
            nome_proprietario = v.findViewById(R.id.nome_cognome_risposta_view);
            text_commento = v.findViewById(R.id.testo_risposta_view);
            data = v.findViewById(R.id.data_risposta_view);
            risposta = v.findViewById(R.id.rispondi_view);


            risposta.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Log.d(LOG,"rispondi alla risposta");
                }
            });

        }
    }

    public void setOnRispondiClickListener(CommentiAdapterFirestore.OnRispondiClickListener clickListener){
        mRispondiClickListener = clickListener;
    }



}
