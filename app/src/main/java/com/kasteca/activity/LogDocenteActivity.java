package com.kasteca.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.ui.AppBarConfiguration;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.kasteca.fragment.CorsiDocenteFragment;
import com.kasteca.object.Docente;
import com.kasteca.R;

import java.util.ArrayList;

public class LogDocenteActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String LOG = "DEBUG CORSI";

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private Bundle bundleDocente;
    private static Docente docente;


    private TextView nome_cognome_TextView;
    private TextView email_TextView;

    private CorsiDocenteFragment fragmentCorsiDocente=null;

    private FragmentManager fm=null;



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

        //inizializzo il fragmentManager che mi servirà per le operazioni sui fragment.
        fm= getSupportFragmentManager();
        navigationView.setCheckedItem(R.id.nav_corsi_docente);

        //recuper il docente autenticato dall'intent inviato dalla MainActivity e creo una nuova istanza docente
        bundleDocente = getIntent().getExtras();
        docente = new Docente();
        docente.setNome(bundleDocente.getString("nome"));
        docente.setCognome(bundleDocente.getString("cognome"));
        docente.setEmail(bundleDocente.getString("email"));
        ArrayList<String> corsi=bundleDocente.getStringArrayList("lista_corsi");
        //docente.setLista_corsi(bundleDocente.getStringArrayList("lista_corsi"));
        docente.setId(bundleDocente.getString("id"));


        View header=navigationView.getHeaderView(0);
        nome_cognome_TextView = header.findViewById(R.id.nome_cognome_nav_header);
        email_TextView = header.findViewById(R.id.email_nav_header);
        //setto le info del menu a tendina con i dati relativi al docente
        nome_cognome_TextView.setText(docente.getNome() + " " + docente.getCognome());
        email_TextView.setText(docente.getEmail());

        creazioneFragment(bundleDocente);

    }





    //Metodo per la creazione del fragment dei corsi del docente
    private void creazioneFragment(Bundle bundle){
        //Crazione Fragment
        //Ho aggiunto il fragment manager nel costruttore per vedere se si poteva aggiungere nell'adapter della recycle view.
        this.fragmentCorsiDocente= new CorsiDocenteFragment();

        //Creazione del Bundle contenente informazioni per il fragment.
        //Sarà lo stesso contenente le informazioni contenute durante il login.
        fragmentCorsiDocente.setArguments(bundle);

        //Avvio del fragment
        if(fm != null){
            fm.beginTransaction().replace(R.id.fragment_container_docente,
                    fragmentCorsiDocente).commit();
        }else
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_docente,
                    fragmentCorsiDocente).commit();

    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){

            case R.id.nav_corsi_docente:
                //Controlliamo prima se non è stato già creato il frame
                //Potremmo sempre ricaricarlo evitando di fare questo controllo
                // -da chiedere-
                if(this.fragmentCorsiDocente==null)
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_docente,
                        new CorsiDocenteFragment()).commit();
                else
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_docente,
                            this.fragmentCorsiDocente).commit();
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

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu_docente, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

/*    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_logout:
                Logout();
                break;
        }
        return true;
    }*/

    public void Logout(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        finish();
        startActivity(intent);
    }


}
