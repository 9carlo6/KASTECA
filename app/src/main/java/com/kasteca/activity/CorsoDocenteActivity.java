package com.kasteca.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.kasteca.R;
import com.kasteca.fragment.CorsiDocenteFragment;
import com.kasteca.fragment.CorsiStudenteFragment;
import com.kasteca.fragment.PostDocenteFragment;
import com.kasteca.object.Docente;
import com.kasteca.object.Post;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class CorsoDocenteActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final String LOG="DEBUG_CORSO_ACTIVITY";

    private CardView option_Post;

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private Bundle bundleCorso;
    private Docente docente;
    private String id_corso;
    private String codice_corso;

    private TextView nome_cognome_TextView;
    private TextView email_TextView;

    private CorsiDocenteFragment cf=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corso_docente);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // recupero il docente autenticato dall'intent inviato dalla MainActivity e creo una nuova istanza docente
        bundleCorso = getIntent().getExtras();
        docente = new Docente();
        docente.setId(bundleCorso.getString("id_docente"));
        docente.setNome(bundleCorso.getString("nome_docente"));
        docente.setCognome(bundleCorso.getString("cognome_docente"));
        docente.setEmail(bundleCorso.getString("email_docente"));

        // recupero anche il codice del corso
        id_corso = bundleCorso.getString("id_corso");
        codice_corso = bundleCorso.getString("codice_corso");

        drawer = findViewById(R.id.corso_drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view_corso_docente);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null){
            PostDocenteFragment pf = new PostDocenteFragment();
            pf.setArguments(bundleCorso);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_corso_docente, pf).commit();
            navigationView.setCheckedItem(R.id.nav_post_corso);
        }

        View header=navigationView.getHeaderView(0);
        nome_cognome_TextView = header.findViewById(R.id.nome_cognome_nav_header);
        email_TextView = header.findViewById(R.id.email_nav_header);
        //setto le info del menu a tendina con i dati relativi al docente
        nome_cognome_TextView.setText(docente.getNome() + " " + docente.getCognome());
        email_TextView.setText(docente.getEmail());

        navigationView.setCheckedItem(R.id.nav_post_corso);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_post_corso:
                Log.e(TAG, "bundle: " + bundleCorso);
                PostDocenteFragment pf = new PostDocenteFragment();
                pf.setArguments(bundleCorso);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_corso_docente,pf).commit();
                break;
            case R.id.nav_visualizza_studenti_iscritti:
                Intent intent = new Intent(getApplicationContext(), ListaStudentiIscrittiActivity.class);
                intent.putExtra("id_corso",id_corso);
                startActivity(intent);
                break;
            case R.id.nav_visualizza_richieste_studente:
                Intent intent_richieste = new Intent(getApplicationContext(), ListaRichiesteStudentiActivity.class);
                intent_richieste.putExtra("codice_corso",codice_corso);
                startActivity(intent_richieste);
                break;
            case R.id.nav_logout:
                Logout();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        finish();
        startActivity(intent);
    }


}