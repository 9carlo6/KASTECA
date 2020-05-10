package com.kasteca.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kasteca.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private Switch docente_studente_Switch;
    private EditText nome_edit_text;
    private EditText cognome_edit_text;
    private EditText email_edit_text;
    private EditText password_edit_text;
    private EditText matricola_edit_text;
    private TextView account_already_exists_text;

    private boolean isDocente;
    private boolean isCorrect;

    private String userID; // ci servirà per recuperare l'id dell'utente

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        docente_studente_Switch = findViewById(R.id.switchDocenteStudente);
        nome_edit_text = findViewById(R.id.editTextNome);
        cognome_edit_text = findViewById(R.id.editTextCognome);
        email_edit_text = findViewById(R.id.editTextEmail);
        password_edit_text = findViewById(R.id.editTextPassword);
        matricola_edit_text = findViewById(R.id.editTextMatricola);
        account_already_exists_text = findViewById(R.id.textViewAccountAlreadyExists);

        isDocente = true; // variabile booleana per verificare se la registrazione riguarda un docente o uno studente

        email_edit_text.setText(getResources().getString(R.string.dominio_unisannio)); // il campo email viene inizialmente settato con il dominio unisannio legato ai professori

        // testo che rimanda alla MainActivity
        account_already_exists_text.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        // le si preme sullo Switch uscirà il campo "matricola"
        docente_studente_Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    matricola_edit_text.setVisibility(View.VISIBLE);
                    isDocente = false;
                    email_edit_text.setText(getResources().getString(R.string.dominio_studenti_unisannio));
                }
                else{
                    matricola_edit_text.setVisibility(View.INVISIBLE);
                    isDocente = false;
                    email_edit_text.setText(getResources().getString(R.string.dominio_unisannio));
                }
            }
        });

    }

    public void CreateNewAccount(View v){
        isCorrect = false; // inizializzazione della variabile per verificare la correttezza dei campi inseriti

        FirebaseAuth mauth = FirebaseAuth.getInstance(); // crea un istanza di FirebaseAuth (serve per l'autenticazione)
        mauth.signOut(); // serve per fare il logout, nel caso in cui ci fosse un utente già loggato

        // in questa maniera vengono presi i valori dei campi inseriti
        final String nome = nome_edit_text.getText().toString();
        final String cognome = cognome_edit_text.getText().toString();
        final String mail = email_edit_text.getText().toString();
        final String password = password_edit_text.getText().toString();
        final String matricola = matricola_edit_text.getText().toString();

        // controllo inserimento campi registrazione docente
        if(!mail.equalsIgnoreCase("")
                && mail.contains("@unisannio.it")
                && !password.equalsIgnoreCase("")
                && !nome.equalsIgnoreCase("")
                && !cognome.equalsIgnoreCase("")
                && email_edit_text.getText()!=null
                && password_edit_text.getText() != null
                && nome_edit_text.getText() != null
                && cognome_edit_text.getText() != null){
            isCorrect = true;
        }

        // controllo inserimento campi registrazione studente
        if(!isDocente){
            if(!mail.equalsIgnoreCase("")
                    && mail.contains("@studenti.unisannio.it")
                    && !password.equalsIgnoreCase("")
                    && !nome.equalsIgnoreCase("")
                    && !cognome.equalsIgnoreCase("")
                    && !matricola.equalsIgnoreCase("")
                    && email_edit_text.getText()!=null
                    && password_edit_text.getText() != null
                    && nome_edit_text.getText() != null
                    && cognome_edit_text.getText() != null
                    && matricola_edit_text.getText() != null ){
                isCorrect = true;
            }
        }

        // se l'inserimento dei campi è corretto si può procedere con la registrazione
        // questo è il ramo della registrazione di un docente
        if(isCorrect && isDocente){
            mauth.createUserWithEmailAndPassword(mail, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful() && task.getResult() != null && task.getResult().getUser() != null) {
                                userID = task.getResult().getUser().getUid();

                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                CollectionReference docenti = db.collection("Docenti");

                                // in questa maniera creiamo un documento relativo al docente
                                Map<String, Object> obj = new HashMap<>();
                                obj.put("nome", nome);
                                obj.put("cognome", cognome);
                                obj.put("lista_corsi", null);
                                obj.put("email", mail);

                                docenti.document(userID).set(obj); // il nome del documente è lo stesso dell'id legato all'email

                                Toast.makeText(RegistrationActivity.this,
                                        getResources().getString(R.string.Registration_successful), Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);

                            } else {
                                Toast.makeText(RegistrationActivity.this,
                                        getResources().getString(R.string.Registration_Failed), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        }else if(isCorrect && !isDocente){
            mauth.createUserWithEmailAndPassword(mail, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful() && task.getResult() != null && task.getResult().getUser() != null) {
                                userID = task.getResult().getUser().getUid();

                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                CollectionReference studenti = db.collection("Studenti");

                                // in questa maniera creiamo un documento relativo al docente
                                Map<String, Object> obj = new HashMap<>();
                                obj.put("nome", nome);
                                obj.put("cognome", cognome);
                                obj.put("lista_corsi", new ArrayList<String>());
                                obj.put("email", mail);
                                obj.put("matricola", matricola);

                                studenti.document(userID).set(obj); // il nome del documente è lo stesso dell'id legato all'email

                                Toast.makeText(RegistrationActivity.this,
                                        getResources().getString(R.string.Registration_successful), Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);

                            } else {
                                Toast.makeText(RegistrationActivity.this,
                                        getResources().getString(R.string.Registration_Failed), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        }else{
            showAlert(getResources().getString(R.string.Not_Correct_Fields));
        }
    }

    public void showAlert(String s){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getResources().getString(R.string.Not_Correct_Fields));
        alertDialog.setMessage(s);
        alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

}
