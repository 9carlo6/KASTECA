package com.kasteca.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kasteca.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class NewPostActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private int SELECT_FILE =100;
    private Uri uriPdf;
    private String downloadUrlPdf;
    private String corso_id;

    private EditText testo_post_text;
    private EditText link_text;
    private TextView pdf_text;
    private Spinner tags_spinner;
    private ArrayList<String> tags;

    private String link;
    private String testo;
    private String tag;
    private Date data;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        if(getIntent().hasExtra("corso_id")) {
             corso_id = getIntent().getStringExtra("corso_id");
        }
        testo_post_text = findViewById(R.id.text_post_Edit_Text);
        link_text = findViewById(R.id.link_Edit_Text);
        pdf_text = findViewById(R.id.uri_pdf);
        pdf_text.setMovementMethod(new ScrollingMovementMethod());
        uriPdf = null;

        tags = new ArrayList<>();

        // Recupero della lista dei tag da Firebase
        db = FirebaseFirestore.getInstance();
        db.collection("Tag").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        tags.add(document.getId());
                        createSpinner();
                    }
                    Log.d(TAG, tags.toString());
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    // Metodo che viene chiamato quando si preme sul bottone per il caricamento di un file pdf
    public void selectPdf(View v){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(intent, SELECT_FILE);
    }

    public  void onActivityResult(int requestCode, int resultCode, Intent ritorno){
        super.onActivityResult(requestCode, resultCode, ritorno);

        if(resultCode == Activity.RESULT_OK){
            if(requestCode == SELECT_FILE){
                uriPdf = ritorno.getData();
                String uriString = uriPdf.toString();
                Log.e(TAG, "PDF URI: " + uriString);
                File file = new File(uriString);
                String displayName = null;

                if (uriString.startsWith("content://")) {
                    Cursor cursor = null;
                    try {
                        cursor = getContentResolver().query(uriPdf, null, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        }
                    } finally {
                        cursor.close();
                    }
                } else if (uriString.startsWith("file://")) {
                    displayName = file.getName();
                } else {
                    displayName = uriPdf.getPath();
                }
                pdf_text.setText(displayName);
            }
        }
    }

    // Metodo che viene chiamato quando si preme sul tasto dell'upload
    public void upload(View v){
        testo = testo_post_text.getText().toString();
        link = link_text.getText().toString();
        if(link.isEmpty()) link = null;
        data = new Date();

        if((tag == null) || (testo == null) || (testo.isEmpty())){
            showAlert(getResources().getString(R.string.missing_data));
        }
        else if(uriPdf != null){
            uploadPdfToStorage();
        }
        else{
            downloadUrlPdf = null;
            uploadPostToDatabase();
        }
    }

    // Metodo chiamato in caso di problemi durante l'upload
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

    // Metodo chiamato se c'è un pdf da caricare nello storage remoto
    public void uploadPdfToStorage(){
        FirebaseStorage myStorage = FirebaseStorage.getInstance();
        StorageReference rootStorageRef = myStorage.getReference();
        StorageReference documentRef = rootStorageRef.child("pdf");
        Log.e(TAG, "pdf uri in upload: " + uriPdf.toString());

        final StorageReference pdfRef = documentRef.child(pdf_text.getText().toString());
        final UploadTask uploadTask = pdfRef.putFile(uriPdf);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Nel caso di fallimento dell'upload
                showAlert(getResources().getString(R.string.upload_pdf_fallito));
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pdfRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        downloadUrlPdf = uri.toString();

                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.upload_pdf_successo), Toast.LENGTH_LONG).show();
                        // Solo se il caricamento del pdf va a buon fine viene aggiunto al Database remoto il post appena creato
                        uploadPostToDatabase();
                    }
                });
            }
        });
    }

    // Metodo chiamato per aggiungere il nuovo post al Database remoto
    public void uploadPostToDatabase(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference postsRef = db.collection("Post");

        Map<String, Object> newPost = new HashMap<>();

        newPost.put("corso", corso_id);
        newPost.put("data", new Timestamp(data));
        newPost.put("link", link);
        newPost.put("pdf", downloadUrlPdf);
        newPost.put("tag", tag);
        newPost.put("testo", testo);
        newPost.put("lista_commenti", new ArrayList<String>());

        postsRef.add(newPost)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        addPostToCorso(documentReference.getId());
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.upload_successo), Toast.LENGTH_LONG).show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 3500);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showAlert(getResources().getString(R.string.upload_post_error));
                    }
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        tag = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        tag = null;
    }

    public void createSpinner(){
        tags_spinner = findViewById(R.id.tags_spinner);

        // Creazione dell'adapter per lo spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tags);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Si attacca l'adapter allo spinner
        tags_spinner.setAdapter(dataAdapter);

        // Si attacca allo spinner il listener per l'item cliccato
        tags_spinner.setOnItemSelectedListener(this);
    }

    private void addPostToCorso(String postiId){
        db.collection("Corsi").document(corso_id).update("lista_post", FieldValue.arrayUnion(postiId));
    }
}
