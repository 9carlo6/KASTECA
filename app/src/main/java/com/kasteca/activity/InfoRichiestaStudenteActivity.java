package com.kasteca.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kasteca.R;

public class InfoRichiestaStudenteActivity extends AppCompatActivity {

    private TextView nome_studente_text;
    private TextView email_studente_text;
    private TextView matricola_studente_text;
    private TextView data_richiesta_text;
    private Bundle bundleStudente;

    private String codice_corso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_richiesta_studente);

        nome_studente_text = findViewById(R.id.textViewRichiestaNomeStudente);
        email_studente_text = findViewById(R.id.textViewRichiestaEmailStudente);
        matricola_studente_text = findViewById(R.id.textViewRichiestaMatricolaStudente);
        data_richiesta_text = findViewById(R.id.textViewDataRichiesta);

        bundleStudente = getIntent().getExtras();
        nome_studente_text.setText(bundleStudente.getString("nome_cognome"));
        email_studente_text.setText(bundleStudente.getString("email"));
        matricola_studente_text.setText(bundleStudente.getString("matricola"));
        data_richiesta_text.setText(bundleStudente.getString("data_richiesta"));

    }

    public void accettaRichiesta(View view){
        
    }
}
