package com.kasteca.activity;

import androidx.appcompat.app.AppCompatActivity;
import com.kasteca.R;
import com.kasteca.object.Studente;

import android.os.Bundle;
import android.widget.TextView;

public class InfoStudenteActivity extends AppCompatActivity {

    private TextView nome_studente_text;
    private TextView email_studente_text;
    private TextView matricola_studente_text;
    private Bundle bundleStudente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_studente);

        nome_studente_text = findViewById(R.id.textViewNomeStudente);
        email_studente_text = findViewById(R.id.textViewEmailStudente);
        matricola_studente_text = findViewById(R.id.textViewMatricolaStudente);

        bundleStudente = getIntent().getExtras();
        nome_studente_text.setText(bundleStudente.getString("nome_cognome"));
        email_studente_text.setText(bundleStudente.getString("email"));
        matricola_studente_text.setText(bundleStudente.getString("matricola"));

    }
}
