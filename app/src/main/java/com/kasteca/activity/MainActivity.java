package com.kasteca.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.kasteca.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static String LAST_USER = "last_user";

    private TextView create_new_account_text;
    private EditText email_edit_text;
    private EditText password_edit_text;
    private SharedPreferences prefs;

    private Bundle docente;
    private Bundle studente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        create_new_account_text = findViewById(R.id.New_Account_Text);
        prefs= getPreferences(MODE_PRIVATE);
        email_edit_text = findViewById(R.id.Email_Edit_Text);
        password_edit_text = findViewById(R.id.Password_Edit_Text);
        //password_edit_text.setText("password");// lo teniamo per velocizzare, ma andra cancellato.
        email_edit_text.setText(prefs.getString(LAST_USER,""));

        // testo che rimanda all'activity per la creazione di un nuovo account (RegistrationActivity)
        create_new_account_text.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }


    public void login(View v) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance(); // crea un istanza di FirebaseAuth (serve per l'autenticazione)
        mAuth.signOut(); // serve per fare il logout, nel caso in cui ci fosse un utente già loggato

        final String mail = email_edit_text.getText().toString();
        String pwd = password_edit_text.getText().toString();


        // se i campi non sono vuoti o invalidi allora procede con il login
        if(ControlloCampi(mail, pwd) && email_edit_text.getText()!=null && password_edit_text.getText() != null) {
            mAuth.signInWithEmailAndPassword(mail, pwd)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this,
                                        getResources().getString(R.string.Login_Successful), Toast.LENGTH_LONG).show();

                                // questo serve per conservare l'email dell'ultimo utente che fa l'accesso sul dispositivo
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString(LAST_USER, mail);
                                editor.apply();

                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                CollectionReference docenti = db.collection("Docenti");

                                Source source = Source.SERVER;

                                // controlla se l'utente che ha eseguito l'accesso è un docente
                                docenti.get(source).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        FirebaseAuth mAuth1 = FirebaseAuth.getInstance();
                                        FirebaseUser currentUser1 = mAuth1.getCurrentUser();

                                        for (DocumentSnapshot document : task.getResult()) {
                                            // per ogni documento controllo presente nella collezione 'Docenti' controllo
                                            // se l'id dell'utente (appena loggato) è associato a un docente
                                            if (currentUser1.getUid().equalsIgnoreCase(document.getId())) {
                                                Intent intent = new Intent(getApplicationContext(), LogDocenteActivity.class);

                                                //scarico i dati relativi al docente e li carico in un nuovo oggetto Docente
                                                //per passare un oggetto bisogna usare la classe Bundle
                                                docente = new Bundle();

                                                docente.putString("nome", document.getData().get("nome").toString());
                                                docente.putString("cognome", document.getData().get("cognome").toString());
                                                docente.putString("email", document.getData().get("email").toString());
                                                docente.putStringArrayList("lista_corsi", (ArrayList<String>) document.getData().get("lista_corsi"));

                                                intent.putExtras(docente);
                                                startActivity(intent);
                                            }
                                        }
                                    }
                                });

                                CollectionReference studenti = db.collection("Studenti");

                                // controlla se l'utente che ha eseguito l'accesso è uno studente
                                studenti.get(source).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        FirebaseAuth mAuth1 = FirebaseAuth.getInstance();
                                        FirebaseUser currentUser1 = mAuth1.getCurrentUser();

                                        for (DocumentSnapshot document : task.getResult()) {
                                            // per ogni documento controllo presente nella collezione 'Docenti' controllo
                                            // se l'id dell'utente (appena loggato) è associato a un docente
                                            if (currentUser1.getUid().equalsIgnoreCase(document.getId())) {
                                                Intent intent = new Intent(getApplicationContext(), LogStudenteActivity.class);

                                                //scarico i dati relativi al docente e li carico in un nuovo oggetto Docente
                                                //per passare un oggetto bisogna usare la classe Bundle
                                                studente = new Bundle();

                                                studente.putString("nome", document.getData().get("nome").toString());
                                                studente.putString("cognome", document.getData().get("cognome").toString());
                                                studente.putString("email", document.getData().get("email").toString());
                                                studente.putString("matricola", document.getData().get("matricola").toString());
                                                studente.putStringArrayList("lista_corsi", (ArrayList<String>) document.getData().get("lista_corsi"));

                                                intent.putExtras(studente);
                                                startActivity(intent);
                                            }
                                        }
                                    }
                                });

                            } else {
                                // se il LOGIN FALLISCE viene mostrato un dialog
                                showAlert(getResources().getString(R.string.Try_Again));
                            }
                        }
                    });
        }else{
            showAlert(getResources().getString(R.string.Insert_correct_data));
        }
    }


    public boolean ControlloCampi(String mail, String pwd){
        if(!mail.equalsIgnoreCase("")&& mail.contains("@")&& !pwd.equalsIgnoreCase("")){
            return true;
        }
        return false;
    }

    // questa funzione serve per mostrare un dialog nel caso di LOGIN FALLITO
    public void showAlert(String s){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getResources().getString(R.string.Login_Failed));
        alertDialog.setMessage(s);
        alertDialog.setNeutralButton(getResources().getString(R.string.Dialog_neutral_button_login_failed), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
}
