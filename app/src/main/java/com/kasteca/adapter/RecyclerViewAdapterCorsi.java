package com.kasteca.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kasteca.R;
import com.kasteca.object.Corso;

import java.util.ArrayList;


public class RecyclerViewAdapterCorsi extends RecyclerView.Adapter<RecyclerViewAdapterCorsi.ViewHolder> {

    private final String LOG= "AdapterRVCorsi_DEBUG";
    private ArrayList<Corso> corsi;
    private OnNoteListener adapterOnNoteListener;


    //Costruttore
    public RecyclerViewAdapterCorsi(ArrayList<Corso> corsi, OnNoteListener onNoteListener){
        this.adapterOnNoteListener= onNoteListener;
        this.corsi=corsi;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_corso_adapter_recycleview,parent,false);

        ViewHolder holder= new ViewHolder(view,adapterOnNoteListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(LOG,"OnBindViewHolder");
        holder.textView.setText(corsi.get(position).getNome());

    }

    @Override
    public int getItemCount() {
        return corsi.size();
    }


    //DA ELIMINARE
    /*
    //metodo per l'aggiunta di un corso nella recycleview
    public void addCorso(Corso corso){
        corsi.add(corso);
        Log.e(LOG,"Aggiunta corso.");
        Log.e(LOG,"lista corsi : "+corsi.toString());
        //metodo per aggiornare la grafica della recycleview e aggiungere l'elemento
        //this.notifyItemInserted(0);
    }
    */

    //ViewHolder per questo Adapter
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView;
        RelativeLayout relativeLayout;
        OnNoteListener onNoteListener;

        public ViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            this.relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativelayout_item);
            this.textView = (TextView) itemView.findViewById(R.id.nomeCorso);
            this.onNoteListener=onNoteListener;

            itemView.setOnClickListener(this);
        }

        //Il metodo onClick del ViewHolder utilizzarà il metodo onClick definito dall'OnNoteListener
        //Che nel nostro caso è li fragment stesso.
        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }


    public interface OnNoteListener{
        void onNoteClick(int position);
    }


}
