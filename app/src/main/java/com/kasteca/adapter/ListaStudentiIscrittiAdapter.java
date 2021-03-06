package com.kasteca.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.MessageFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kasteca.R;
import com.kasteca.activity.InfoStudenteActivity;
import com.kasteca.object.Studente;


public class ListaStudentiIscrittiAdapter extends RecyclerView.Adapter<ListaStudentiIscrittiAdapter.StudenteViewHolder>{

    private Bundle studente;
    private String idCorso;

    static class StudenteViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView nome_studente;
        TextView matricola_studente;

        StudenteViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv_lista_studenti_iscritti);
            nome_studente = itemView.findViewById(R.id.nome_studente);
            matricola_studente = itemView.findViewById(R.id.matricola_studente);
        }
    }

    private List<Studente> studenti;

    public ListaStudentiIscrittiAdapter(List<Studente> studenti, String idCorso){
        this.studenti = studenti;
        this.idCorso = idCorso;
    }

    @Override
    public int getItemCount() {
        return studenti.size();
    }

    @NonNull
    @Override
    public StudenteViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.linear_layout_lista_studenti_iscritti, viewGroup, false);
        return new StudenteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final StudenteViewHolder studenteViewHolder, final int i) {

        studenteViewHolder.nome_studente.setText(MessageFormat.format("{0} {1}", studenti.get(i).getNome(), studenti.get(i).getCognome()));
        studenteViewHolder.matricola_studente.setText(studenti.get(i).getMatricola());

        studenteViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                Intent nuovointent= new Intent(studenteViewHolder.itemView.getContext(), InfoStudenteActivity.class);

                studente = new Bundle();
                studente.putString("nome_cognome", studenti.get(i).getNome() + " " + studenti.get(i).getCognome());
                studente.putString("email",studenti.get(i).getEmail());
                studente.putString("matricola", studenti.get(i).getMatricola());

                nuovointent.putExtras(studente);
                studenteViewHolder.itemView.getContext().startActivity(nuovointent);
            }
        });


        studenteViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(studenteViewHolder.itemView.getContext());
                alertDialog.setTitle(studenteViewHolder.itemView.getContext().getString(R.string.Dialog_cancellazione_stuednte_question));
                alertDialog.setPositiveButton(studenteViewHolder.itemView.getContext().getResources().getString(R.string.Dialog_neutral_button_cancella_studente), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //fare partire il metodo che cancella lo studente dal corso
                        cancellazioneStudente(studenti.get(i).getId());

                        studenti.remove(i);
                        notifyDataSetChanged();
                        dialog.cancel();
                    }
                });
                alertDialog.setNegativeButton(studenteViewHolder.itemView.getContext().getResources().getString(R.string.Dialog_neutral_button_non_cancellare_lo_studente), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //fare partire il metodo che cancella lo studente dal corso
                        dialog.cancel();
                    }
                });
                alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        //finish();
                    }
                });
                alertDialog.show();
                return false;
            }
        });

    }


    private void cancellazioneStudente(final String idStudente){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference corsi = db.collection("Corsi");

        // cancellazione del dello studente dal corso
        corsi.document(idCorso)
                .update("lista_studenti", FieldValue.arrayRemove(idStudente))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        CollectionReference studenti = db.collection("Studenti");

                        // cancellazione del corso dalla lista corsi accessibili dallo studente
                        studenti.document(idStudente)
                                .update("lista_corsi", FieldValue.arrayRemove(idCorso))
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        //qualcosa
                                    }
                                });
                    }
                });
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }




}