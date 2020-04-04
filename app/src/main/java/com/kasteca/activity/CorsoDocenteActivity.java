package com.kasteca.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.cardview.widget.CardView;

import com.kasteca.R;

public class CorsoDocenteActivity extends AppCompatActivity {

    private final String LOG="DEBUG_CORSO_ACTIVITY";


    private CardView option_Post;

    private Bundle bundle_corso;
    private String id_corso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_corso_docente);

        bundle_corso=getIntent().getExtras();
        id_corso= bundle_corso.getString("codice_corso");
        if(id_corso==null)
            Log.e(LOG,"Codice corso non recuperato correttamente dall'intent.");

        //Aggiungo il clickListener al cardView
        //Al click della card si sarà selezionata l'opzione di mostrare il corso
        option_Post=findViewById(R.id.card_option_post);
        option_Post.setOnClickListener(new View.OnClickListener() {
            //Da completare
            @Override
            public void onClick(View v) {
                Intent intent= new Intent();
            }
        });


        recuperoCorso();

    }


    private void recuperoCorso(){

    }



}
