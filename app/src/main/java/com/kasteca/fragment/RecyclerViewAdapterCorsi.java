package com.kasteca.fragment;

import android.content.Context;
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
    private Context context;


    //Costruttore
    public RecyclerViewAdapterCorsi(Context context,ArrayList<Corso> corsi){
        this.context=context;
        this.corsi=corsi;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = null;
        view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_corso_adapter_recycleview,parent,false);

        ViewHolder holder= new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(LOG,"OnBindViewHolder");

        holder.textView.setText(corsi.get(position).getNome());

        //DA SETTARE L'ONCLICK LISTENER

    }

    @Override
    public int getItemCount() {
        return corsi.size();
    }



    //ViewHolder per questo Adapter
    public class ViewHolder extends  RecyclerView.ViewHolder{

        TextView textView;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.relativeLayout=(RelativeLayout) itemView.findViewById(R.id.relativelayout_item);
            this.textView= (TextView) itemView.findViewById(R.id.nomeCorso);
        }
    }





}
