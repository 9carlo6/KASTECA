package com.kasteca.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kasteca.R;
import com.kasteca.fragment.PostDocenteFragment;
import com.kasteca.fragment.PostStudenteFragment;
import com.kasteca.object.Studente;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class CorsoStudenteActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Studente studente;
    private String corso_id;
    private String codice_corso;
    private String nome_corso;
    private String anno_accademico;
    private Bundle bundleStudente;


    private TextView nome_cognome_TextView;
    private TextView email_TextView;
    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corso_studente);

        Toolbar toolbar = findViewById(R.id.toolbar_studente);
        toolbar.setTitle(nome_corso);
        setSupportActionBar(toolbar);

        bundleStudente= getIntent().getExtras();

        //Recupero dati dal bundle
        studente = new Studente();
        studente.setId(bundleStudente.getString("id"));
        studente.setNome( bundleStudente.getString("nome"));
        studente.setCognome(bundleStudente.getString("cognome"));
        studente.setEmail(bundleStudente.getString("email"));
        studente.setMatricola(bundleStudente.getString("matricola"));

        // recupero anche il codice del corso
        corso_id = bundleStudente.getString("id_corso");
        codice_corso = bundleStudente.getString("codice_corso");
        nome_corso = bundleStudente.getString("nome_corso");
        anno_accademico = bundleStudente.getString("anno_accademico");

        drawer = findViewById(R.id.corso_studente_drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view_corso_studente);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null){
            PostStudenteFragment pf = new PostStudenteFragment();
            pf.setArguments(bundleStudente);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_corso_studente  , pf).commit();
            navigationView.setCheckedItem(R.id.nav_post_corso);
        }


        View header=navigationView.getHeaderView(0);
        nome_cognome_TextView = header.findViewById(R.id.nome_cognome_nav_header);
        email_TextView = header.findViewById(R.id.email_nav_header);
        //setto le info del menu a tendina con i dati relativi al docente
        nome_cognome_TextView.setText(studente.getNome() + " " + studente.getCognome());
        email_TextView.setText(studente.getEmail());

        navigationView.setCheckedItem(R.id.nav_post_corso);

    }

    public void cancellazione_dal_corso(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference corsi = db.collection("Corsi");

        // cancellazione del dello studente dal corso
        corsi.document(corso_id)
                .update("lista_studenti", FieldValue.arrayRemove(studente.getId()))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        CollectionReference studenti = db.collection("Studenti");

                        // cancellazione del corso dalla lista corsi accessibili dallo studente
                        studenti.document(studente.getId())
                                .update("lista_corsi", FieldValue.arrayRemove(corso_id))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void avoid) {
                                        showAlertInfoCancellazione(getResources().getString(R.string.Dialog_titolo_corretta_cancellazione));
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        showAlertInfoCancellazione(getResources().getString(R.string.Dialog_titolo_incorretta_cancellazione));
                                    }
                                });
                    }
                });
    }

    // dialog per chiedere conferma della cancellazione dal corso
    public void showAlertConfermaCancellazione(String titolo, String descrizione){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(titolo);
        alertDialog.setMessage(descrizione);
        alertDialog.setPositiveButton(getResources().getString(R.string.Dialog_button_si_conferma_cancellazione), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                cancellazione_dal_corso();
            }
        });
        alertDialog.setNegativeButton(getResources().getString(R.string.Dialog_button_no_conferma_cancellazione), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
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
    }

    // dialog per informare del corretto/scorretto abbandono del corso
    public void showAlertInfoCancellazione(final String titolo){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(titolo);
        alertDialog.setNeutralButton(getResources().getString(R.string.Dialog_button_ok_cancellazione), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                if(titolo.equalsIgnoreCase(getResources().getString(R.string.Dialog_titolo_corretta_cancellazione))){
                    finish();
                }
            }
        });
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if(titolo.equalsIgnoreCase(getResources().getString(R.string.Dialog_titolo_corretta_cancellazione))){
                    finish();
                }
            }
        });
        alertDialog.show();
    }

    //Da completare con il collegamento alle activity/fragment di Katia
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_post_corso:
                PostStudenteFragment pf = new PostStudenteFragment();
                pf.setArguments(bundleStudente);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_corso_studente , pf).commit();
                break;
            case R.id.nav_cancellazione_dal_corso:
                showAlertConfermaCancellazione(getResources().getString(R.string.Dialog_titolo_conferma_cancellazione), getResources().getString(R.string.Dialog_descrizione_conferma_cancellazione));
                break;
            case R.id.nav_logout:
                Logout();
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void Logout(){
        FirebaseAuth mauth = FirebaseAuth.getInstance();
        mauth.signOut();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        finish();
        startActivity(intent);
    }

}
