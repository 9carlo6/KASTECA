package com.kasteca.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.kasteca.R;
import com.kasteca.object.Commento;
import java.text.SimpleDateFormat;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class CommentiAdapterFirestoreDocente extends FirestoreRecyclerAdapter<Commento, CommentiAdapterFirestoreDocente.ViewHolder> {

    private OnRispondiClickListener mRispondiClickListener;
    private OnVisualizzaRisposteClickListener mVisualizzaRisposteClickListener;
    private RisposteAdapterFirestoreDocente.Delete delete;
    private OnModificaClickListener modificaClickListener;
    private String idDocente;
    private String nomeCognomeDocente;


    public CommentiAdapterFirestoreDocente(@NonNull FirestoreRecyclerOptions<Commento> options, String idDocente, String nomeCognomeDocente){
        super(options);
        this.idDocente = idDocente;
        this.nomeCognomeDocente = nomeCognomeDocente;
    }

    @NonNull
    @Override
    public CommentiAdapterFirestoreDocente.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.commento_item, parent, false);
        CommentiAdapterFirestoreDocente.ViewHolder vh = new CommentiAdapterFirestoreDocente.ViewHolder(v);
        return vh;
    }


    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Commento model) {

        if(model.getProprietarioCommento()!=null){
            if( idDocente.equals(model.getProprietarioCommento())){
                holder.nome_proprietario.setText(nomeCognomeDocente);
                holder.elimina.setVisibility(View.VISIBLE);
                holder.modifica.setVisibility(View.VISIBLE);
             }else
                 holder.nome_proprietario.setText(model.getProprietarioCommento().substring(0, 6));
        }

        holder.text_commento.setText(model.getTesto());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ITALY);
        holder.data.setText(sdf.format(model.getData()));

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cv;
        public TextView nome_proprietario;
        public TextView text_commento;
        public TextView data;
        public TextView risposta;
        public TextView tutte_risposte;
        public TextView elimina;
        public TextView modifica;

        public ViewHolder(View v) {
            super(v);
            cv = v.findViewById(R.id.card_view_commento);
            nome_proprietario = v.findViewById(R.id.nome_cognome_commento_view);
            text_commento = v.findViewById(R.id.testo_commento_view);
            data = v.findViewById(R.id.data_commento_view);
            risposta = v.findViewById(R.id.rispondi_view);
            tutte_risposte = v.findViewById(R.id.visualizza_risposte_view);
            elimina= v.findViewById(R.id.elimina_commento_view);
            modifica= v.findViewById(R.id.modifica_commento_view);

            risposta.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && mRispondiClickListener != null){
                        mRispondiClickListener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });

            tutte_risposte.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && mVisualizzaRisposteClickListener != null){
                        mVisualizzaRisposteClickListener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
            elimina.setOnClickListener(new View.OnClickListener() {
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

    public interface OnRispondiClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public interface OnVisualizzaRisposteClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public interface OnModificaClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnRispondiClickListener(CommentiAdapterFirestoreDocente.OnRispondiClickListener clickListener){
        mRispondiClickListener = clickListener;
    }

    public void setOnVisualizzaRisposteClickListener(CommentiAdapterFirestoreDocente.OnVisualizzaRisposteClickListener clickListener){
        mVisualizzaRisposteClickListener = clickListener;
    }

    public void setDelete(RisposteAdapterFirestoreDocente.Delete clickDelete){
        delete=clickDelete;
    }

    public void setModificaClickListener(OnModificaClickListener md){
        this.modificaClickListener=md;
    }


    public String getIdDocente() {
        return idDocente;
    }

    public String getNomeCognomeDocente() {
        return nomeCognomeDocente;
    }

    public OnRispondiClickListener getmRispondiClickListener() {
        return mRispondiClickListener;
    }

    public OnVisualizzaRisposteClickListener getmVisualizzaRisposteClickListener() {
        return mVisualizzaRisposteClickListener;
    }

    public RisposteAdapterFirestoreDocente.Delete getDelete() {
        return delete;
    }

    public OnModificaClickListener getModificaClickListener() {
        return modificaClickListener;
    }
}
