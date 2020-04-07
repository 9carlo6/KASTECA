package com.kasteca.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.kasteca.R;
import com.kasteca.activity.InfoRichiestaStudenteActivity;
import com.kasteca.activity.InfoStudenteActivity;
import com.kasteca.object.Richiesta;
import com.kasteca.object.Studente;

import java.util.HashMap;
import java.util.List;


public class ListaRichiesteStudentiAdapter extends RecyclerView.Adapter<ListaRichiesteStudentiAdapter.RichiestaViewHolder>{

    private HashMap<String,Studente> richiesta_selezionata;
    private Bundle richiesta;
    private int LAUNCH_INFO_RICHIESTA_ISCRIZIONE_ACTIVITY = 1;

    public static class RichiestaViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView nome_studente_richiesta;
        TextView matricola_studente_richiesta;

        RichiestaViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv_lista_richieste_studenti);
            nome_studente_richiesta = (TextView)itemView.findViewById(R.id.nome_studente_richiesta);
            matricola_studente_richiesta = (TextView)itemView.findViewById(R.id.matricola_studente_richiesta);
        }
    }

    List<Richiesta> richieste;
    List<Studente> studenti;

    public ListaRichiesteStudentiAdapter(List<Richiesta> richieste, List<Studente> studenti){
        this.richieste = richieste;
        this.studenti = studenti;
    }

    @Override
    public int getItemCount() {
        return richieste.size();
    }

    @Override
    public RichiestaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.linear_layout_lista_richieste_studenti, viewGroup, false);
        RichiestaViewHolder pvh = new RichiestaViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final RichiestaViewHolder richiestaViewHolder, final int i) {

        //richiestaViewHolder.nome_studente_richiesta.setText(.get(i).getStudente().getNome() + " " + richieste.get(i).getStudente().getCognome());
        richiestaViewHolder.nome_studente_richiesta.setText(studenti.get(i).getNome() + " " + studenti.get(i).getCognome());
        //richiestaViewHolder.matricola_studente_richiesta.setText(richieste.get(i).getStudente().getMatricola());
        richiestaViewHolder.matricola_studente_richiesta.setText(studenti.get(i).getMatricola());

        richiestaViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                richiesta_selezionata= new HashMap<>();

                Intent nuovointent= new Intent(richiestaViewHolder.itemView.getContext(), InfoRichiestaStudenteActivity.class);

                richiesta = new Bundle();
                //richiesta.putString("nome_cognome", richieste.get(i).getStudente().getNome() + " " + richieste.get(i).getStudente().getCognome());
                richiesta.putString("nome_cognome", studenti.get(i).getNome() + " " + studenti.get(i).getCognome());
                //richiesta.putString("email",richieste.get(i).getStudente().getEmail());
                richiesta.putString("email",studenti.get(i).getEmail());
                //richiesta.putString("matricola", richieste.get(i).getStudente().getMatricola());
                richiesta.putString("matricola", studenti.get(i).getMatricola());
                richiesta.putString("data_richiesta", richieste.get(i).getData().toString());

                richiesta.putString("id_studente",studenti.get(i).getId());
                //richiesta.putString("id_studente",richieste.get(i).getStudente().getId());
                richiesta.putString("codice_corso", richieste.get(i).getCodice_corso());
                richiesta.putString("id_richiesta", richieste.get(i).getId());

                //richiesta.putStringArrayList("richieste", richieste);

                nuovointent.putExtras(richiesta);
                ((Activity) richiestaViewHolder.itemView.getContext()).startActivityForResult(nuovointent, LAUNCH_INFO_RICHIESTA_ISCRIZIONE_ACTIVITY);
                //((Activity) richiestaViewHolder.itemView.getContext()).startActivity(nuovointent);


                //richieste.remove(i);
                //notifyDataSetChanged();



            }
        });

    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }




}