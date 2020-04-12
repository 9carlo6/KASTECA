package com.kasteca.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

    private OnClickListener mClickListener;
    private String nome_cognome_proprietario;

    public CommentiAdapterFirestore(@NonNull FirestoreRecyclerOptions<Commento> options){
        super(options);
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
        /*FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference studRef = db.collection("Studenti").document(model.getProprietario_commento());

        Source source = Source.SERVER;

        studRef.get(source).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    nome_cognome_proprietario = (String) document.getData().get("nome")
                                                + " "
                                                + (String) document.getData().get("cognome");
                } else {
                    Log.d(TAG, "Failed: " + task.getException());
                }
            }
        });*/
        holder.nome_proprietario.setText(model.getProprietario_commento());
        holder.text_commento.setText(model.getTesto());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        holder.data.setText(sdf.format(model.getData()));
        Log.e(TAG, "data: " + model.getData());

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
            cv = v.findViewById(R.id.card_view_commento);
            nome_proprietario = v.findViewById(R.id.nome_cognome_commento_view);
            text_commento = v.findViewById(R.id.testo_commento_view);
            data = v.findViewById(R.id.data_commento_view);
            risposta = v.findViewById(R.id.rispondi_view);
            tutte_risposte = v.findViewById(R.id.visualizza_risposte_view);

            risposta.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && mClickListener != null){
                        mClickListener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface OnClickListener {
        public void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnClickListener(CommentiAdapterFirestore.OnClickListener clickListener){
        mClickListener = clickListener;
    }
}
