package com.kasteca.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.ui.AppBarConfiguration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.kasteca.fragment.CorsiDocenteFragment;
import com.kasteca.object.Corso;
import com.kasteca.object.Docente;
import com.kasteca.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LogDocenteActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String LOG = "DEBUG CORSI";

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private Bundle bundleDocente;
    private static Docente docente;


    private TextView nome_cognome_TextView;
    private TextView email_TextView;

    private static FragmentManager fm=null;



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
        /*
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_docente,
                    new CorsiDocenteFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_corsi_docente);
        }
        */

        //recuper il docente autenticato dall'intent inviato dalla MainActivity e creo una nuova istanza docente
        bundleDocente = getIntent().getExtras();
        docente = new Docente();
        docente.setNome(bundleDocente.getString("nome"));
        docente.setCognome(bundleDocente.getString("cognome"));
        docente.setEmail(bundleDocente.getString("email"));
        ArrayList<String> corsi=bundleDocente.getStringArrayList("lista_corsi");
        //docente.setLista_corsi(bundleDocente.getStringArrayList("lista_corsi"));
        docente.setId(bundleDocente.getString("id"));

        //metodo per l'aggiunta dei corsi nella classe docente
        if(recuperoCorsi(corsi))
            Log.d(LOG,"Aggiunta corsi è terminata con successo.");
        Log.d(LOG,docente.toString());

        View header=navigationView.getHeaderView(0);
        nome_cognome_TextView = header.findViewById(R.id.nome_cognome_nav_header);
        email_TextView = header.findViewById(R.id.email_nav_header);
        //setto le info del menu a tendina con i dati relativi al docente
        nome_cognome_TextView.setText(docente.getNome() + " " + docente.getCognome());
        email_TextView.setText(docente.getEmail());

        /*
        //L'operazione di invio del bundle al fragment dovrà avvenire solo dopo che sarà terminata
        //l'operazione di download dei contenuti
        //Quindi sarà richiamata dal listener che di occupa di fare ciò.
        //Avvio del fragment, invio del Bundle contente il docente.
        Bundle data= new Bundle();
        data.putSerializable("docente",docente);

        //Crazione Fragment
        CorsiDocenteFragment fragmentCorsiDocente= new CorsiDocenteFragment();
        //Passo al fragment il bundle contenente il docente.
        fragmentCorsiDocente.setArguments(data);
        //Avvio del fragment
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_docente,
                   fragmentCorsiDocente).commit();
            navigationView.setCheckedItem(R.id.nav_corsi_docente);
        }
        */

    }


    boolean recuperoCorsi(ArrayList<String> corsi) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference corsiReference = db.collection("Corsi");
        //QuerySnapshot q =db.collection("Corsi").whereEqualTo(com.google.firebase.firestore.FieldPath.documentId(),  idCorso);
        //where(firebase.firestore.FieldPath.documentId(), '==', "scindv");
        Source source = Source.SERVER;

        Log.e(LOG, "Lista codici corsi: ");

        if (!corsi.isEmpty()) {
            //Per ogni id corso che abbiamo, facciamo una query, lo cerchiamo e lo aggiungiamo alla classe studente.
            for (String idCorso : corsi) {
                Log.e(LOG, "Codice corso: " + idCorso);
                corsiReference.whereEqualTo(com.google.firebase.firestore.FieldPath.documentId(), idCorso).get(source).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            //FirebaseAuth mAuth1 = FirebaseAuth.getInstance();
                            //FirebaseUser user = mAuth1.getCurrentUser();
                            if (task.getResult() == null) {
                                Log.e(LOG, "risultato della query null");
                            } else {
                                List<DocumentSnapshot> testDocuments = task.getResult().getDocuments();
                                for (DocumentSnapshot d : testDocuments) {
                                    Log.e(LOG, d.getData().toString());
                                    Log.e(LOG, d.getData().get("codice").toString());
                                    Log.e(LOG, d.getData().get("descrizione").toString());
                                    Log.e(LOG, d.getData().get("anno_accademico").toString());
                                    Log.e(LOG, d.getData().get("nome_corso").toString());
                                    Log.e(LOG, d.getId());
                                }

                                //Per ogni corso controllo se è del docente
                                for (DocumentSnapshot document : task.getResult().getDocuments()) {

                                    //if (user.getUid().equalsIgnoreCase(document.get("id").toString())) {
                                    //Controllo del risultato della query:
                                    if (document == null) {
                                        Log.e(LOG, "risultato della query null: document == null");
                                    } else {
                                        List<DocumentSnapshot> corsi = task.getResult().getDocuments();
                                        for (DocumentSnapshot d : corsi) {
                                            Log.e(LOG, d.getData().toString());
                                            Map<String, Object> c = d.getData();
                                            Corso corso = new Corso(
                                                    c.get("nome_corso").toString(),
                                                    c.get("anno_accademico").toString(),
                                                    c.get("descrizione").toString(),
                                                    LogDocenteActivity.docente,
                                                    c.get("codice").toString(),
                                                    d.getId());
                                            Log.e(LOG, "Corso: " + corso.toString());
                                            if (docente.getLista_corsi() == null)
                                                docente.setLista_corsi(new ArrayList<Corso>());
                                            docente.addCorso(corso);

                                        }
                                    }

                                }

                                //Dopo la creazione dei corsi dobbiamo richiamare il metodo di questa
                                // activity per la creazione del fragment dei corsi del docente
                                creazioneFragment(docente);
                            }

                        }
                    }
                });


                //Controllo se la lista dei corsi del docente è stata creata senza problemi
                if (docente.getLista_corsi() != null)
                    return true;
                else
                    return false;
            }

            return true;
        } else {
            Log.e(LOG, "Nessun corso presente per questo docente.");
            return false;
        }

    }




    //Metodo per la creazione del fragment dei corsi del docente
    private void creazioneFragment(Docente docente){
        //Crazione Fragment
        CorsiDocenteFragment fragmentCorsiDocente= new CorsiDocenteFragment(docente,this);
        //Avvio del fragment
        if(fm != null){
            fm.beginTransaction().replace(R.id.fragment_container_docente,
                    fragmentCorsiDocente).commit();
        }

    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_corsi_docente:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_docente,
                        new CorsiDocenteFragment()).commit();
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
