package com.kasteca.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kasteca.R;
import com.kasteca.activity.InfoStudenteActivity;
import com.kasteca.object.Studente;


public class ListaStudentiIscrittiAdapter extends RecyclerView.Adapter<ListaStudentiIscrittiAdapter.StudenteViewHolder>{

    private HashMap<String,Studente> studente_selezionato;
    private Bundle studente;

    public static class StudenteViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView nome_studente;
        TextView matricola_studente;

        StudenteViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv_lista_studenti_iscritti);
            nome_studente = (TextView)itemView.findViewById(R.id.nome_studente);
            matricola_studente = (TextView)itemView.findViewById(R.id.matricola_studente);
        }
    }

    List<Studente> studenti;

    public ListaStudentiIscrittiAdapter(List<Studente> studenti){
        this.studenti = studenti;
    }

    @Override
    public int getItemCount() {
        return studenti.size();
    }

    @Override
    public StudenteViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.linear_layout_lista_studenti_iscritti, viewGroup, false);
        StudenteViewHolder pvh = new StudenteViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final StudenteViewHolder studenteViewHolder, final int i) {

        studenteViewHolder.nome_studente.setText(studenti.get(i).getNome() + " " + studenti.get(i).getCognome());
        studenteViewHolder.matricola_studente.setText(studenti.get(i).getMatricola());

        studenteViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                studente_selezionato= new HashMap<>();

                Intent nuovointent= new Intent(studenteViewHolder.itemView.getContext(), InfoStudenteActivity.class);

                studente = new Bundle();
                studente.putString("nome_cognome", studenti.get(i).getNome() + " " + studenti.get(i).getCognome());
                studente.putString("email",studenti.get(i).getEmail());
                studente.putString("matricola", studenti.get(i).getMatricola());

                nuovointent.putExtras(studente);
                studenteViewHolder.itemView.getContext().startActivity(nuovointent);
            }
        });

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }




}