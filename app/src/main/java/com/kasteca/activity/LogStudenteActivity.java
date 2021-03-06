package com.kasteca.activity;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.ActionBarDrawerToggle;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.appcompat.widget.Toolbar;
        import androidx.core.view.GravityCompat;
        import androidx.drawerlayout.widget.DrawerLayout;
        import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

        import android.content.Intent;
        import android.os.Bundle;
        import android.os.Handler;
        import android.util.Log;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.TextView;

        import com.google.android.material.navigation.NavigationView;
        import com.google.firebase.auth.FirebaseAuth;
        import com.kasteca.R;
        import com.kasteca.fragment.CorsiStudenteFragment;
        import com.kasteca.object.Studente;

        import java.text.MessageFormat;

public class LogStudenteActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Bundle bundleStudente;
    private Studente studente;

    int LAUNCH_RICHIESTA_ISCRIZIONE_ACTIVITY = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String log = "LogStudente activity";
        Log.e(log,"inizializzazione activity");

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


        Log.e(log," Recupero bundle");

        //recuper lo studente autenticato dall'intent inviato dalla MainActivity e creo una nuova istanza docente
        bundleStudente = getIntent().getExtras();
        if(bundleStudente != null) {
            studente = new Studente();
            studente.setId(bundleStudente.getString("id"));
            studente.setNome(bundleStudente.getString("nome"));
            studente.setCognome(bundleStudente.getString("cognome"));
            studente.setEmail(bundleStudente.getString("email"));
            studente.setMatricola(bundleStudente.getString("matricola"));
        }

        View header=navigationView.getHeaderView(0);
        TextView nomeCognomeTextView = header.findViewById(R.id.nome_cognome_nav_header);
        TextView emailTextView = header.findViewById(R.id.email_nav_header);
        TextView matricolaTextView = header.findViewById(R.id.matricola_nav_header);
        //setto le info del menu a tendina con i dati relativi al docente
        nomeCognomeTextView.setText(MessageFormat.format("{0} {1}", studente.getNome(), studente.getCognome()));
        emailTextView.setText(studente.getEmail());
        matricolaTextView.setText(studente.getMatricola());

        //da eliminare
        Log.e(log,"Cambiamento Fragment");
        //Avvio il fragment con i corsi dello studente.

        //refreshing della lista dei corsi.
        swipeRefreshLayout = findViewById(R.id.swipeLayout_lista_corsi_studente);
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

        caricamentoFragmentCorsi();

    }

    private void caricamentoFragmentCorsi(){
        CorsiStudenteFragment csf = new CorsiStudenteFragment();
        csf.setArguments(bundleStudente);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_studente, csf).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_corsi_studente:
                caricamentoFragmentCorsi();
                break;
            case R.id.nav_iscrizione_corso:
                Intent intent = new Intent(getApplicationContext(), RichiestaIscrizioneActivity.class);
                intent.putExtra("id_studente", studente.getId());
                startActivityForResult(intent, LAUNCH_RICHIESTA_ISCRIZIONE_ACTIVITY);
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

    @Override
    protected void onResume() {
        super.onResume();
        caricamentoFragmentCorsi();
    }

    public void Logout(){
        FirebaseAuth mauth = FirebaseAuth.getInstance();
        mauth.signOut();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        finish();
        startActivity(intent);
    }
}
