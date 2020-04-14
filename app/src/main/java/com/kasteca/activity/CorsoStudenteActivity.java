package com.kasteca.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.kasteca.R;
import com.kasteca.fragment.PostDocenteFragment;
import com.kasteca.object.Studente;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class CorsoStudenteActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Studente studente;
    private String codice_corso;
    private Bundle bundleStudente;


    private TextView nome_cognome_TextView;
    private TextView email_TextView;
    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corso_studente);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bundleStudente= getIntent().getExtras();

        //Recupero dati dal bundle
        studente = new Studente();
        studente.setId(bundleStudente.getString("id"));
        studente.setNome( bundleStudente.getString("nome"));
        studente.setCognome(bundleStudente.getString("cognome"));
        studente.setEmail(bundleStudente.getString("email"));
        studente.setMatricola(bundleStudente.getString("matricola"));
        codice_corso= bundleStudente.getString("codice_corso");

        drawer = findViewById(R.id.corso_studente_drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view_corso_studente);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        /*Completare transizione fragment
          Modificare il nome e il tipo dell'oggetto del fragment dei post dello studente
        if(savedInstanceState == null){
            PostStudenteFragment pf = new PostStudenteFragment();
            pf.setArguments(bundleStudente);
            getSupportFragmentManager().beginTransaction().replace(R.id.  , pf).commit();
            navigationView.setCheckedItem(R.id.nav_post_corso);
        }
        */


        View header=navigationView.getHeaderView(0);
        nome_cognome_TextView = header.findViewById(R.id.nome_cognome_nav_header);
        email_TextView = header.findViewById(R.id.email_nav_header);
        //setto le info del menu a tendina con i dati relativi al docente
        nome_cognome_TextView.setText(studente.getNome() + " " + studente.getCognome());
        email_TextView.setText(studente.getEmail());

        navigationView.setCheckedItem(R.id.nav_post_corso);


    }


    //Da completare con il collegamento alle activity/fragment di Katia
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_post_corso:
                /*Aggiunstare
                PostStudenteFragment pf = new PostStudenteFragment();
                pf.setArguments(bundleStudente);
                getSupportFragmentManager().beginTransaction().replace(R.id.  , pf).commit();
                 navigationView.setCheckedItem(R.id.nav_post_corso);
                 */
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
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        finish();
        startActivity(intent);
    }

}
