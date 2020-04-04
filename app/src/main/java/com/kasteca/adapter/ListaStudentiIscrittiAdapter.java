package com.kasteca.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.kasteca.R;
import com.kasteca.object.Studente;


public class ListaStudentiIscrittiAdapter extends RecyclerView.Adapter<ListaStudentiIscrittiAdapter.StudenteViewHolder>{

    private HashMap<String,Studente> studente_selezionato;

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
                    /*
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference collectionReference= db.collection("Studenti");
                    final DocumentReference documentReference = collectionReference.document(studenti.get(i).getId());
                    CollectionReference collectionReferencetask = documentReference.collection("tasks");

                    collectionReferencetask.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            for(DocumentSnapshot documentSnapshot : task.getResult()){
                                currentTasks.put(documentSnapshot.getId(),documentSnapshot.toObject(CurrentTask.class));
                            }
                            //Intent nuovointent= new Intent(studenteViewHolder.itemView.getContext(),TasksListActivity.class);
                            //nuovointent.putExtra("tasks",currentTasks);
                            // nuovointent.putExtra()
                            //studenteViewHolder.itemView.getContext().startActivity(nuovointent);
                        }
                    });
                    */
            }
        });

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }




}