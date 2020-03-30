package com.kasteca.activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kasteca.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class NewPostActivity extends AppCompatActivity {

    private EditText testo_post_text;
    private EditText link_text;
    private TextView pdf_text;
    private Spinner tags_spinner;
    private ArrayList<String> tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        testo_post_text = findViewById(R.id.text_post__Edit_Text);
        link_text = findViewById(R.id.link_Edit_Text);
        pdf_text = findViewById(R.id.uri_pdf);
        tags_spinner = findViewById(R.id.tags_spinner);

        tags = new ArrayList<>();

        // Recupero della lista dei tag da Firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Tag").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        tags.add(document.getId());
                    }
                    Log.d(TAG, tags.toString());
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        // Creazione dell'adapter per lo spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tags);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Si attacca l'adapter allo spinner
        tags_spinner.setAdapter(dataAdapter);
    }

    public void uploadPdf(View v){

    }

    public void uploadPost(View v){
        String testo = testo_post_text.getText().toString();
        String link = link_text.getText().toString();
        String pdf = pdf_text.getText().toString();
        String tag = tags_spinner.getSelectedItem().toString();

        if((tag == null) || (testo == null) || (testo.isEmpty())){
            showAlert(getResources().getString(R.string.missing_data));
        }
        else{

        }
    }

    public void showAlert(String s){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getResources().getString(R.string.upload_failed));
        alertDialog.setMessage(s);
        alertDialog.setNeutralButton(getResources().getString(R.string.Dialog_neutral_button_login_failed), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

}
