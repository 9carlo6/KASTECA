package com.kasteca.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.kasteca.fragment.CorsiDocenteFragment;
import com.kasteca.fragment.CreazioneCorsoFragment;
import com.kasteca.object.Docente;
import com.kasteca.R;
import com.kasteca.util.EspressoIdlingResource;

public class LogDocenteActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String LOG="LogDocenteActivity";

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private Bundle bundleDocente;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Docente docente;

    private TextView nome_cognome_TextView;
    private TextView email_TextView;

    private CorsiDocenteFragment cf=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_docente);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view_docente);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        //recupero il docente autenticato dall'intent inviato dalla MainActivity e creo una nuova istanza docente
        bundleDocente = getIntent().getExtras();
        docente = new Docente();
        docente.setId(bundleDocente.getString("id"));
        docente.setNome(bundleDocente.getString("nome"));
        docente.setCognome(bundleDocente.getString("cognome"));
        docente.setEmail(bundleDocente.getString("email"));

        View header=navigationView.getHeaderView(0);
        nome_cognome_TextView = header.findViewById(R.id.nome_cognome_nav_header);
        email_TextView = header.findViewById(R.id.email_nav_header);
        //setto le info del menu a tendina con i dati relativi al docente
        nome_cognome_TextView.setText(docente.getNome() + " " + docente.getCognome());
        email_TextView.setText(docente.getEmail());

        navigationView.setCheckedItem(R.id.nav_corsi_docente);

        //refreshing della lista dei corsi.
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout_lista_corsi_docente);
        //swipeRefreshLayout.setColorSchemeResources(R.color.refresh, R.color.refresh1, R.color.refresh2);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        caricamentoFragmentCorsi();
                    }
                },1000);
            }
        });

        //Contatore idling resource per test con espresso
        //EspressoIdlingResource.decrement();

        //Avvio del fragment dei corsi del docente.
       caricamentoFragmentCorsi();
    }

    //metodo per il caricamento del fragment contenente i corsi.
    private void caricamentoFragmentCorsi(){
        if(bundleDocente != null){
            cf= new CorsiDocenteFragment();
            cf.setArguments(bundleDocente);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_docente, cf).commit();
        }else{
            Log.e(LOG,"Errore nel caricamento del fragment: bundleDocente null.");
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_corsi_docente:
                caricamentoFragmentCorsi();
                break;
            case R.id.nav_aggiungi_corso:
                CreazioneCorsoFragment ccf= new CreazioneCorsoFragment();
                ccf.setArguments(bundleDocente);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_docente, ccf).commit();
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
