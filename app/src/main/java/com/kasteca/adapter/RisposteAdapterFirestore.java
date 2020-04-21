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
import com.google.firebase.firestore.DocumentSnapshot;
import com.kasteca.R;
import com.kasteca.object.Risposta;

import java.text.SimpleDateFormat;

public class RisposteAdapterFirestore extends FirestoreRecyclerAdapter<Risposta, RisposteAdapterFirestore.ViewHolder> {
    private final String LOG= "RISPOSTE_ADAPTER";
    private CommentiAdapterFirestore.OnRispondiClickListener mRispondiClickListener;
    private RisposteAdapterFirestore.Delete delete;
    private String idDocente;
    private String nomeCognomeDocente;
    private String nomeCognomeStudente;
    private String idStudente;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public RisposteAdapterFirestore(@NonNull FirestoreRecyclerOptions<Risposta> options, String id_docente, String nomeCognome,  String nomeCognomeStudente, String idStudente) {
        super(options);
        this.idDocente = id_docente;
        this.idStudente= idStudente;
        this.nomeCognomeDocente = nomeCognome;
        this.nomeCognomeStudente= nomeCognomeStudente;
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
        holder.nomeProprietario.setText(model.getProprietario());
        if(model.getProprietario()!=null){

            if(idDocente.equals(model.getProprietario())){
                holder.nomeProprietario.setText(nomeCognomeDocente);
                if(idStudente==null)
                    holder.elimina.setVisibility(View.VISIBLE);
            }else
            if(idStudente!=null && nomeCognomeStudente!=null && idStudente.equals(model.getProprietario())){
                holder.nomeProprietario.setText(nomeCognomeStudente);
                holder.elimina.setVisibility(View.VISIBLE);
            }else
                holder.nomeProprietario.setText(model.getProprietario().substring(0, 6));

        }
        holder.textCommento.setText(model.getTesto());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        holder.data.setText(sdf.format(model.getData()));

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cv;
        public TextView nomeProprietario;
        public TextView textCommento;
        public TextView data;
        public TextView elimina;


        public ViewHolder(View v) {
            super(v);
            cv = v.findViewById(R.id.card_view_risposta);
            nomeProprietario = v.findViewById(R.id.nome_cognome_risposta_view);
            textCommento = v.findViewById(R.id.testo_risposta_view);
            data = v.findViewById(R.id.data_risposta_view);
            elimina = v.findViewById(R.id.elimina_view);


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



        }
    }

    public void setOnRispondiClickListener(CommentiAdapterFirestore.OnRispondiClickListener clickListener){
        mRispondiClickListener = clickListener;
    }

    public interface Delete{
        void deleteOnClick(DocumentSnapshot documentSnapshot);
    }

    public void setDelete(RisposteAdapterFirestore.Delete clickDelete){
        delete=clickDelete;
    }

}
