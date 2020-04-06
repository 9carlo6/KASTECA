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

        import com.google.android.material.navigation.NavigationView;
        import com.google.firebase.auth.FirebaseAuth;
        import com.kasteca.fragment.CorsiDocenteFragment;
        import com.kasteca.R;
        import com.kasteca.fragment.CorsiStudenteFragment;
        import com.kasteca.object.Studente;

        import java.util.ArrayList;

public class LogStudenteActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    private String LOG ="LogStudente activity";

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Bundle bundleStudente;
    private Studente studente;

    private TextView nome_cognome_TextView;
    private TextView email_TextView;
    private TextView matricola_TextView;

    private CorsiStudenteFragment csf=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(LOG,"inizializzazione activity");

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


        Log.e(LOG," Recupero bundle");

        //recuper lo studente autenticato dall'intent inviato dalla MainActivity e creo una nuova istanza docente
        bundleStudente = getIntent().getExtras();
        studente = new Studente();
        studente.setId(bundleStudente.getString("id"));
        studente.setNome(bundleStudente.getString("nome"));
        studente.setCognome(bundleStudente.getString("cognome"));
        studente.setEmail(bundleStudente.getString("email"));
        studente.setMatricola(bundleStudente.getString("matricola"));

        Log.e(LOG,"Dati studente: "+studente.toString());

        View header=navigationView.getHeaderView(0);
        nome_cognome_TextView = header.findViewById(R.id.nome_cognome_nav_header);
        email_TextView = header.findViewById(R.id.email_nav_header);
        matricola_TextView = header.findViewById(R.id.matricola_nav_header);
        //setto le info del menu a tendina con i dati relativi al docente
        nome_cognome_TextView.setText(studente.getNome() + " " + studente.getCognome());
        email_TextView.setText(studente.getEmail());
        matricola_TextView.setText(studente.getMatricola());

        //da eliminare
        Log.e(LOG,"Cambiamento Fragment");
        //Avvio il fragment con i corsi dello studente.
        Bundle bundle= new Bundle();
        bundle.putString("id", studente.getId());
        bundle.putString("nome", studente.getNome());
        bundle.putString("cognome", studente.getCognome());
        bundle.putString("email", studente.getEmail());
        bundle.putString("matricola", studente.getMatricola());
        bundle.putStringArrayList("id_corsi",bundleStudente.getStringArrayList("id_corsi"));
        /////////////////////////////////////////////////////////////////
        Log.e(LOG,"Bundle: "+bundleStudente.toString());

        //refreshing della lista dei corsi.
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout_lista_corsi_studente);
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
        csf= new CorsiStudenteFragment();
        csf.setArguments(bundleStudente);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_studente, csf).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_corsi_studente:
                caricamentoFragmentCorsi();
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
        inflater.inflate(R.menu.settings_menu_studente, menu);
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
