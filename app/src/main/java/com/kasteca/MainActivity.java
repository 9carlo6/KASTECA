package com.kasteca;

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

public class MainActivity extends AppCompatActivity {

    private static String LAST_USER = "last_user";

    private TextView create_new_account_text;
    private EditText email_edit_text;
    private EditText password_edit_text;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        create_new_account_text = findViewById(R.id.New_Account_Text);
        prefs= getPreferences(MODE_PRIVATE);
        email_edit_text = findViewById(R.id.Email_Edit_Text);
        password_edit_text = findViewById(R.id.Password_Edit_Text);
        password_edit_text.setText("password");// lo teniamo per velocizzare, ma andra cancellato
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
        if(!mail.equalsIgnoreCase("")&& mail.contains("@")&& !pwd.equalsIgnoreCase("") && email_edit_text.getText()!=null && password_edit_text.getText() != null ) {
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
                                editor.commit();

                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                CollectionReference docenti = db.collection("Docenti");

                                Source source = Source.SERVER;

                                // controlla se l'utente che ha eseguito l'accesso è uno studente
                                docenti.get(source).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        FirebaseAuth mAuth1 = FirebaseAuth.getInstance();
                                        FirebaseUser currentUser1 = mAuth1.getCurrentUser();

                                        for (DocumentSnapshot document : task.getResult()) {
                                            // per ogni documento controllo presente nella collezione 'Docenti' controllo
                                            // se l'id dell'utente (appena loggato) è associato a un docente
                                            if (currentUser1.getUid().equalsIgnoreCase(document.getData().get("id").toString())) {
                                                //Intent intent = new Intent(getApplicationContext(), DocenteActiviy.class);
                                                //intent.putExtra("studente", true);
                                                //startActivity(intent);
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
                                            if (currentUser1.getUid().equalsIgnoreCase(document.getData().get("id").toString())) {
                                                //Intent intent = new Intent(getApplicationContext(), StudenteActiviy.class);
                                                //intent.putExtra("mayor", true);
                                                //startActivity(intent);
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

    // questa funzione serve per mostrare un dialog nel caso di LOGIN FALLITO
    public void showAlert(String s){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getResources().getString(R.string.Login_Failed));
        alertDialog.setMessage(s);
        alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
}
