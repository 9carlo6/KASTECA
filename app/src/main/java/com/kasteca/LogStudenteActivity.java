package com.kasteca;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.ActionBarDrawerToggle;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.appcompat.widget.Toolbar;
        import androidx.core.view.GravityCompat;
        import androidx.drawerlayout.widget.DrawerLayout;
        import androidx.navigation.ui.AppBarConfiguration;

        import android.os.Bundle;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.TextView;

        import com.google.android.material.navigation.NavigationView;

public class LogStudenteActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private Bundle bundleStudente;
    private Studente studente;

    private TextView nome_cognome_TextView;
    private TextView email_TextView;
    private TextView matricola_TextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_studente);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout_studente);
        NavigationView navigationView = findViewById(R.id.nav_view_studente);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_studente,
                    new CorsiDocenteFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_corsi_studente);
        }

        //recuper il docente autenticato dall'intent inviato dalla MainActivity e creo una nuova istanza docente
        bundleStudente = getIntent().getExtras();
        studente = new Studente();
        studente.setNome(bundleStudente.getString("nome"));
        studente.setCognome(bundleStudente.getString("cognome"));
        studente.setEmail(bundleStudente.getString("email"));
        studente.setMatricola(bundleStudente.getString("matricola"));
        studente.setLista_corsi(bundleStudente.getStringArrayList("lista_corsi"));

        View header=navigationView.getHeaderView(0);
        nome_cognome_TextView = header.findViewById(R.id.nome_cognome_nav_header);
        email_TextView = header.findViewById(R.id.email_nav_header);
        matricola_TextView = header.findViewById(R.id.matricola_nav_header);
        //setto le info del menu a tendina con i dati relativi al docente
        nome_cognome_TextView.setText(studente.getNome() + " " + studente.getCognome());
        email_TextView.setText(studente.getEmail());
        matricola_TextView.setText(studente.getMatricola());


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_corsi_studente:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_studente,
                        new CorsiDocenteFragment()).commit();
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
