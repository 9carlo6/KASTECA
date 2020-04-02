package com.kasteca.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.kasteca.R;

public class CorsoActivity extends AppCompatActivity {

    private final String LOG="DEBUG_CORSO_ACTIVITY";

    private Bundle bundle_corso;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_corso);

        bundle_corso=getIntent().getExtras();
        String id_corso= bundle_corso.getString("codice_corso");
        if(id_corso==null)
            Log.e(LOG,"Codice corso non recuperato correttamente dall'intent.");

        recuperoCorso();

    }


    private void recuperoCorso(){

    }



}
