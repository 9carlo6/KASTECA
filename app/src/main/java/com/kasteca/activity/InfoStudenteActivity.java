package com.kasteca.activity;

import androidx.appcompat.app.AppCompatActivity;
import com.kasteca.R;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class InfoStudenteActivity extends AppCompatActivity {

    private Bundle bundleStudente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_studente);

        TextView nome_studente_text = findViewById(R.id.textViewNomeStudente);
        TextView email_studente_text = findViewById(R.id.textViewEmailStudente);
        TextView matricola_studente_text = findViewById(R.id.textViewMatricolaStudente);

        bundleStudente = getIntent().getExtras();
        if(bundleStudente != null) {
            nome_studente_text.setText(bundleStudente.getString("nome_cognome"));
            email_studente_text.setText(bundleStudente.getString("email"));
            matricola_studente_text.setText(bundleStudente.getString("matricola"));
        }

    }

    public void inviaEmail(View v){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse("mailto:" + bundleStudente.getString("email"));
        //Uri data = Uri.parse("mailto:" + bundleStudente.getString("email") + "&subject=" + "subject" + "&body=" + "body");
        intent.setData(data);
        startActivity(intent);
    }
}
