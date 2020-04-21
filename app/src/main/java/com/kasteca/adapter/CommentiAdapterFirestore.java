package com.kasteca.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.common.BaseObservableSnapshotArray;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.kasteca.R;
import com.kasteca.object.Commento;
import com.kasteca.object.Corso;
import com.kasteca.object.Post;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class CommentiAdapterFirestore extends FirestoreRecyclerAdapter<Commento, CommentiAdapterFirestore.ViewHolder> {

    private OnRispondiClickListener mRispondiClickListener;
    private OnVisualizzaRisposteClickListener mVisualizzaRisposteClickListener;
    private RisposteAdapterFirestore.Delete delete;
    private String idDocente;
    private String nomeCognomeDocente;
    private String nomeCognomeStudente;
    private String idStudente;

    public CommentiAdapterFirestore(@NonNull FirestoreRecyclerOptions<Commento> options, String idDocente, String nomeCognomeDocente, String nomeCognomeStudente, String idStudente){
        super(options);
        this.idDocente = idDocente;
        this.idStudente= idStudente;
        this.nomeCognomeDocente = nomeCognomeDocente;
        this.nomeCognomeStudente= nomeCognomeStudente;
    }

    @NonNull
    @Override
    public CommentiAdapterFirestore.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.commento_item, parent, false);
        CommentiAdapterFirestore.ViewHolder vh = new CommentiAdapterFirestore.ViewHolder(v);
        return vh;
    }


    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Commento model) {
        if(model.getProprietarioCommento()!=null){
            if(!idDocente.equals(model.getProprietarioCommento())) {
                if( idStudente!=null && nomeCognomeStudente!=null && idStudente.equals(model.getProprietarioCommento())){
                    holder.nome_proprietario.setText(nomeCognomeStudente);
                    holder.elimina.setVisibility(View.VISIBLE);
                }else{
                    holder.nome_proprietario.setText(model.getProprietarioCommento().substring(0, 6));
                }
            }
            else{
                holder.nome_proprietario.setText(nomeCognomeDocente);
                holder.elimina.setVisibility(View.VISIBLE);
            }
        }
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
        public TextView elimina;

        public ViewHolder(View v) {
            super(v);
            cv = v.findViewById(R.id.card_view_commento);
            nome_proprietario = v.findViewById(R.id.nome_cognome_commento_view);
            text_commento = v.findViewById(R.id.testo_commento_view);
            data = v.findViewById(R.id.data_commento_view);
            risposta = v.findViewById(R.id.rispondi_view);
            tutte_risposte = v.findViewById(R.id.visualizza_risposte_view);
            elimina= v.findViewById(R.id.elimina_commento_view);

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
        }
    }

    public interface OnRispondiClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public interface OnVisualizzaRisposteClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnRispondiClickListener(CommentiAdapterFirestore.OnRispondiClickListener clickListener){
        mRispondiClickListener = clickListener;
    }

    public void setOnVisualizzaRisposteClickListener(CommentiAdapterFirestore.OnVisualizzaRisposteClickListener clickListener){
        mVisualizzaRisposteClickListener = clickListener;
    }

    public void setDelete(RisposteAdapterFirestore.Delete clickDelete){
        delete=clickDelete;
    }
}
