package com.kasteca;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class LogDocenteActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private Bundle bundleDocente;
    private Docente docente;

    private TextView nome_cognome_docente_TextView;
    private TextView email_docente_TextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_docente);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new CorsiFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_corsi);
        }

        //recuper il docente autenticato dall'intent inviato dalla MainActivity e creo una nuova istanza docente
        bundleDocente = getIntent().getExtras();
        docente = new Docente();
        docente.setNome(bundleDocente.getString("nome"));
        docente.setCognome(bundleDocente.getString("cognome"));
        docente.setEmail(bundleDocente.getString("email"));
        docente.setLista_corsi(bundleDocente.getStringArrayList("lista_corsi"));

        View header=navigationView.getHeaderView(0);
        nome_cognome_docente_TextView = (TextView)header.findViewById(R.id.nome_cognome_docente_nav_header);
        email_docente_TextView = (TextView)header.findViewById(R.id.email_docente_nav_header);
        //setto le info del menu a tendina con i dati relativi al docente
        nome_cognome_docente_TextView.setText(docente.getNome() + " " + docente.getCognome());
        email_docente_TextView.setText(docente.getEmail());


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_corsi:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CorsiFragment()).commit();
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


}
