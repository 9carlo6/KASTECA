package com.kasteca;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.w3c.dom.Text;

public class LogDocenteActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

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
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //recuper il docente autenticato dall'intent inviato dalla MainActivity e creo una nuova istanza docente
        bundleDocente = getIntent().getExtras();
        docente = new Docente();
        docente.setNome(bundleDocente.getString("nome"));
        docente.setCognome(bundleDocente.getString("cognome"));
        docente.setEmail(bundleDocente.getString("email"));
        docente.setLista_corsi(bundleDocente.getStringArrayList("lista_corsi"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.log_docente, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        nome_cognome_docente_TextView = findViewById(R.id.nome_cognome_docente_nav_header);
        email_docente_TextView = findViewById(R.id.email_docente_nav_header);

        //setto le info del menu a tendina con i dati relativi al docente
        nome_cognome_docente_TextView.setText(docente.getNome() + " " + docente.getCognome());
        email_docente_TextView.setText(docente.getEmail());

        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
