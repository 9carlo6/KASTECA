package com.kasteca.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.kasteca.R;
import com.kasteca.adapter.CommentiAdapterFirestoreDocente;
import com.kasteca.adapter.CommentiAdapterFirestoreStudente;
import com.kasteca.adapter.RisposteAdapterFirestoreDocente;

import com.kasteca.adapter.RisposteAdapterFirestoreStudente;
import com.kasteca.fragment.ModificaDialogFragment;
import com.kasteca.object.Commento;
import com.kasteca.object.Post;
import com.kasteca.object.Risposta;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class PostActivity extends AppCompatActivity {
    private final String LOG="POST_ACTIVITY";

    private Commento commento;
    private Post post;
    private String nomeCognome;
    private String idDocente;
    private TextView testoView;
    private TextView visuale;
    private PopupWindow popWindow;
    private CommentiAdapterFirestoreDocente adapter = null;
    private RecyclerView recyclerView;
    private EditText scriviCommento;
    private EditText scriviRisposta;
    private View viewPop;


    private String nomeCognomeStudente= null;
    private String idStudente= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);


        if(getIntent().hasExtra("post")){
            post = getIntent().getParcelableExtra("post");
        }

        if(getIntent().hasExtra("docente")){
            nomeCognome = getIntent().getStringExtra("docente");
        }

        if(getIntent().hasExtra("id_docente")){
            idDocente = getIntent().getStringExtra("id_docente");
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference postReference = db.collection("Commenti");
        Query query = postReference.whereEqualTo("post", post.getId()).orderBy("data", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Commento> options = new FirestoreRecyclerOptions.Builder<Commento>().setQuery(query, Commento.class).build();

        if(getIntent().hasExtra("nome") && getIntent().hasExtra("cognome") && getIntent().hasExtra("id")){
            nomeCognomeStudente = getIntent().getStringExtra("nome")+" "+getIntent().getStringExtra("cognome");
            idStudente= getIntent().getStringExtra("id");
            adapter = new CommentiAdapterFirestoreStudente(options, idDocente, nomeCognome, nomeCognomeStudente, idStudente);
        }else
            adapter = new CommentiAdapterFirestoreDocente(options, idDocente, nomeCognome);

        //Verifichiamo il current user per vedere se èp uno studente
        //Nel caso è uno studente salviamo il suo nome ed il suo cognome

        TextView nomeCognomeView = findViewById(R.id.nome_cognome_textView);
        TextView dataView = findViewById(R.id.data_textView);
        TextView tagView = findViewById(R.id.tagTextView);
        testoView = findViewById(R.id.testoPostTextView);

        nomeCognomeView.setText(nomeCognome);
        SimpleDateFormat sdf = new SimpleDateFormat(getResources().getString(R.string.formato_data), Locale.ITALY);
        if(post.getData() != null)  dataView.setText(sdf.format(post.getData()));
        testoView.setText(post.getTesto());
        tagView.setText(post.getTag());
        TextView linkView = findViewById(R.id.link_textView);

        if(post.getLink() == null){
            linkView.setVisibility(View.INVISIBLE);
        }
        else{
            linkView.setText(post.getLink());
        }

        if(post.getPdf() == null){
            findViewById(R.id.getPdfButton).setVisibility(View.INVISIBLE);
        }

    }



    public void downloadPdf(View v){
        if( haveStoragePermission()){
            String nomeFile = post.getPdf().substring(post.getPdf().indexOf("/pdf"));
            nomeFile = nomeFile.substring(7, nomeFile.indexOf("?"));
            if(!nomeFile.contains(".pdf")) nomeFile = nomeFile + ".pdf";
            File file = new File(String.valueOf(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)));

            DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(post.getPdf()))
                    .setTitle(getResources().getString(R.string.titolo))
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationUri(Uri.fromFile(file));

            if (downloadManager != null) {
                downloadManager.enqueue(request);
            }
        }
    }

    public  boolean haveStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else {
            // se dispositivo ha un API level minore di 23, non c'è bisogno di chiedere dinamicamente il permesso
            return true;
        }
    }

    public void openLink(View v){
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(post.getLink()));
            startActivity(browserIntent);
        }
        catch (ActivityNotFoundException e) {
            showAlert(getResources().getString(R.string.download_webBrowser));
        }
    }

    public void seeComments(View v){
        onShowPopup(v, false);
    }

    public void addComment(View v){
        onShowPopup(v, true);
    }


    public void onShowPopup(View v, boolean isAddCommentClicked){

        final LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewPop=v;
        // si visualizza il layout del popup
        final View inflatedView = layoutInflater.inflate(R.layout.popup_comments_layout, (ViewGroup) null,false);
        // si cerca la recycle view nel popup layout
        recyclerView = inflatedView.findViewById(R.id.recycler_view_commenti);
        // si cerca l'Edit Text nel popup layout
        scriviCommento = inflatedView.findViewById(R.id.writeComment);
        // si cerca il bottone per l'invio di un commento nel popup layout
        ImageButton inviaCommento = inflatedView.findViewById(R.id.send_commento_button);
        // si rende il bottone disabilitato fino a che non viene scritta almeno una lettera nell'Edit Text
        // inviaCommento.setEnabled(false);
        visuale = inflatedView.findViewById(R.id.visuale);

        inviaCommento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!scriviCommento.getText().toString().trim().isEmpty()) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference commentiRef = db.collection("Commenti");

                    Map<String, Object> newCommento = new HashMap<>();
                    newCommento.put("testo", scriviCommento.getText().toString());
                    newCommento.put("data", new Date());
                    newCommento.put("lista_risposte", new ArrayList<String>());
                    newCommento.put("post", post.getId());
                    FirebaseAuth mauth = FirebaseAuth.getInstance();
                    FirebaseUser currentUser = mauth.getCurrentUser();
                    newCommento.put("proprietarioCommento", currentUser.getUid());

                    commentiRef.add(newCommento)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    FirebaseFirestore.getInstance().collection("Post").document(post.getId()).update("lista_commenti", FieldValue.arrayUnion(documentReference.getId()));
                                    scriviCommento.getText().clear();
                                }
                            });
                }
                else{
                    scriviCommento.getText().clear();
                }
            }
        });

        // si prendono le dimensioni del dispositivo
        Display display = getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);

        // si inizializza la recyclerView
        setRecyclerView();

        // si setta la grandezza della pop window a seconda delle dimensioni del dispositivo  size.y - 400
        popWindow = new PopupWindow(inflatedView, size.x - 50,WindowManager.LayoutParams.WRAP_CONTENT, true );
        // si setta come background una forma rettangolare con gli angoli arrotondati
        popWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popup_shape,null));
        // si vuole vedere la testiera e scrivere nell'EditText
        // lo si fa solo se il booleano passato al metodo come argomento è true
        if(isAddCommentClicked){
            scriviCommento.requestFocus();
            scriviCommento.postDelayed(new Runnable() {
                @Override
                public void run() {

                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(scriviCommento, InputMethodManager.SHOW_IMPLICIT);
                }
            }, 200);
        }

        // si fa in modo che si possa toccare lo schermo al di fuori della finestra,
        // cosa che porta alla chiusura della finestra stessa
        popWindow.setOutsideTouchable(true);

        popWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        popWindow.setAnimationStyle(R.style.PopupAnimation);

        // si mostra la finestra dal basso dello schermo
        popWindow.showAtLocation(this.testoView, Gravity.BOTTOM, 0,100);

    }




    private void setRecyclerView(){

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Controllo l'adapter

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                recyclerView.scrollToPosition(0);
            }
        });
        recyclerView.setAdapter(adapter);

        adapter.setOnRispondiClickListener(new CommentiAdapterFirestoreDocente.OnRispondiClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                setRecyclerViewRisposte(adapter.getSnapshots().getSnapshot(position),true);
                Log.e(TAG, "Rispondo al commento " + adapter.getSnapshots().getSnapshot(position).getId());
            }
        });

        adapter.setOnVisualizzaRisposteClickListener(new CommentiAdapterFirestoreDocente.OnVisualizzaRisposteClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                setRecyclerViewRisposte(adapter.getSnapshots().getSnapshot(position),false);
                Log.e(TAG, "Visualizzo le risposte al commento " + adapter.getSnapshots().getSnapshot(position).getId());
            }
        });

        adapter.setDelete(new RisposteAdapterFirestoreDocente.Delete() {
            @Override
            public void deleteOnClick(DocumentSnapshot documentSnapshot) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference commentiReference = db.collection("Commenti");
                commentiReference.document(documentSnapshot.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {
                        Log.d(LOG,"Commento eliminato");
                    }
                });

                CollectionReference risposteReference = db.collection("Risposte_Commenti");
                risposteReference.whereEqualTo("commento",documentSnapshot.getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            CollectionReference risposteR = db.collection("Risposte_Commenti");
                            for(DocumentSnapshot ds: task.getResult()){
                                risposteR.document(ds.getId()).delete();
                            }
                        }
                    }
                });

                //Elimino il commento dall'array Commenti del Post
                CollectionReference postCollection= db.collection("Post");
                postCollection.document(post.getId()).update(
                        "lista_commenti",
                            FieldValue.
                                arrayRemove(documentSnapshot.getId())
                        );

            }
        });

        adapter.setModificaClickListener(new CommentiAdapterFirestoreDocente.OnModificaClickListener() {
            @Override
            public void onItemClick(final DocumentSnapshot documentSnapshot, int position) {
                ModificaDialogFragment md= new ModificaDialogFragment();
                md.setListener(new ModificaDialogFragment.EditDialogListener() {
                    @Override
                    public void applyText(String newText) {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        CollectionReference commentiReference = db.collection("Commenti");
                        commentiReference.document(documentSnapshot.getId()).update("testo",newText)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void avoid) {
                                        Log.d(LOG,"Commento modificato con successo.");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e(LOG,"Modifica commento non riuscita.");
                                    }
                                });
                    }
                });

                md.show(getSupportFragmentManager(),"Modifica Commento");

            }
        });

    }

    private void setRecyclerViewRisposte(DocumentSnapshot commentoSnapshot ,Boolean isAddingRespond){
        //Richiesta di FireBase per le risposte
        popWindow.dismiss();
        Log.e(TAG,"Cerchiamo le risposte del commento: "+commentoSnapshot.getId());
        commento= new Commento(
                commentoSnapshot.getId(),
                commentoSnapshot.get("testo").toString(),
                commentoSnapshot.getDate("data"),
                commentoSnapshot.get("post").toString(),
                commentoSnapshot.get("proprietarioCommento").toString()
        );

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference risposteReference = db.collection("Risposte_Commenti");
        //Query query = risposteReference.whereEqualTo("commento", commento.getId()).orderBy("data", Query.Direction.ASCENDING);
        Query query = risposteReference.whereEqualTo("commento", commento.getId()).orderBy("data", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Risposta> options = new FirestoreRecyclerOptions.Builder<Risposta>().setQuery(query, Risposta.class).build();

        //Controlliamo se è un docente o uno studente e scegliamo l'adapter
        RisposteAdapterFirestoreDocente risposteAdapterFirestore = null;
        if(nomeCognomeStudente != null && idStudente!= null)
            risposteAdapterFirestore = new RisposteAdapterFirestoreStudente(options, idDocente, nomeCognome, nomeCognomeStudente, idStudente);
        else
            risposteAdapterFirestore = new RisposteAdapterFirestoreDocente(options, idDocente, nomeCognome);

        risposteAdapterFirestore.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                recyclerView.scrollToPosition(0);
            }
        });

        //Aggiungo interfaccia per l'eliminazione del commento
        risposteAdapterFirestore.setDelete(new RisposteAdapterFirestoreDocente.Delete() {
            @Override
            public void deleteOnClick(DocumentSnapshot documentSnapshot) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference risposteReference = db.collection("Risposte_Commenti");
                risposteReference.document(documentSnapshot.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {
                        Log.d(LOG,"Risposta correttamente eliminata.");
                    }
                });

                //Rimuovo l'id della risposta dall'array risposte del commento
                CollectionReference postCollection= db.collection("Commenti");
                postCollection.document(commento.getId()).update(
                        "lista_risposte",
                        FieldValue.
                                arrayRemove(documentSnapshot.getId())
                );
            }
        });

        risposteAdapterFirestore.setModificaClickListener(new CommentiAdapterFirestoreDocente.OnModificaClickListener() {
            @Override
            public void onItemClick(final DocumentSnapshot documentSnapshot, int position) {
                ModificaDialogFragment md= new ModificaDialogFragment();
                md.setListener(new ModificaDialogFragment.EditDialogListener() {
                    @Override
                    public void applyText(String newText) {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        CollectionReference commentiReference = db.collection("Risposte_Commenti");
                        commentiReference.document(documentSnapshot.getId()).update("testo",newText)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void avoid) {
                                        Log.d(LOG,"Risposta modificato con successo.");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e(LOG,"Modifica Risposta non riuscita.");
                                    }
                                });
                    }
                });

                md.show(getSupportFragmentManager(),"Modifica Risposta");

            }
        });

        risposteAdapterFirestore.startListening();

        final LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // si visualizza il layout del popup
        final View inflatedView = layoutInflater.inflate(R.layout.popup_risposte_layout, (ViewGroup) null,false);
        //settiamo il bottone per tornare ai commenti
        ImageView backButton= inflatedView.findViewById(R.id.back_to_comment);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow.dismiss();
                seeComments(v);
            }
        });

        //settiamo il layout del commento di riferimento
        //Settiamo il nome del proprietario del commento nel modo giusto
        TextView nomeComm= inflatedView.findViewById(R.id.nome_cognome_comm_view);
        if(!commento.getProprietarioCommento().equals(idDocente)) {
            //Controlliamo se è loggato lo studente
            if(nomeCognomeStudente!=null && idStudente!= null && commento.getProprietarioCommento().equals(idStudente)){
                nomeComm.setText(nomeCognomeStudente);
            }else{
                nomeComm.setText(commento.getProprietarioCommento().substring(0, 6));

            }
            nomeComm.setText(commento.getProprietarioCommento().substring(0, 6));
        }
        else{
            nomeComm.setText(nomeCognome);
        }

        TextView testoComm= inflatedView.findViewById(R.id.testo_comm_view);
        testoComm.setText(commento.getTesto());
        // si cerca la recycle view nel popup layout
        recyclerView = inflatedView.findViewById(R.id.recycler_view_risposte);
        // si cerca l'Edit Text nel popup layout
        scriviRisposta = inflatedView.findViewById(R.id.writeRisposta);
        // si cerca il bottone per l'invio di un commento nel popup layout
        ImageButton inviaRisposta = inflatedView.findViewById(R.id.send_risposta_button);
        visuale = inflatedView.findViewById(R.id.visuale);

        inviaRisposta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!scriviRisposta.getText().toString().trim().isEmpty()) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference risposteReference = db.collection("Risposte_Commenti");

                    Map<String, Object> newRisposta = new HashMap<>();
                    newRisposta.put("testo", scriviRisposta.getText().toString());
                    newRisposta.put("data", new Date());
                    newRisposta.put("commento", commento.getId());
                    FirebaseAuth mauth = FirebaseAuth.getInstance();
                    FirebaseUser currentUser = mauth.getCurrentUser();
                    newRisposta.put("proprietario", currentUser.getUid());

                    risposteReference.add(newRisposta)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    FirebaseFirestore.getInstance().collection("Commenti").document(commento.getId()).update("lista_risposte", FieldValue.arrayUnion(documentReference.getId()));
                                    scriviRisposta.getText().clear();
                                }
                            });
                }
                else{
                    scriviRisposta.getText().clear();
                }
            }
        });

        // si prendono le dimensioni del dispositivo
        Display display = getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);


        // si setta la grandezza della pop window a seconda delle dimensioni del dispositivo
        popWindow = new PopupWindow(inflatedView, size.x - 50,WindowManager.LayoutParams.WRAP_CONTENT, true );
        //popWindow.setContentView(inflatedView);

        // si setta come background una forma rettangolare con gli angoli arrotondati
        popWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popup_shape,null));

        //Per fare in modo che la popup si ricostruisca nel modo corretto
        popWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // si vuole vedere la testiera e scrivere nell'EditText
        // lo si fa solo se il booleano passato al metodo come argomento è true
        if(isAddingRespond){
            scriviRisposta.requestFocus();
            scriviRisposta.postDelayed(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(scriviRisposta, InputMethodManager.SHOW_IMPLICIT);
                }
            }, 200);
        }
        // si fa in modo che si possa toccare lo schermo al di fuori della finestra,
        // cosa che porta alla chiusura della finestra stessa
        popWindow.setOutsideTouchable(true);

        popWindow.setAnimationStyle(R.style.PopupAnimation);

        // si mostra la finestra dal basso dello schermo
        popWindow.showAtLocation(this.testoView, Gravity.BOTTOM, 0,100);



        //cambiamo l'adapter della recycleView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(risposteAdapterFirestore);

    }

    public void showAlert(String s){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getResources().getString(R.string.no_activity));
        alertDialog.setMessage(s);
        alertDialog.setNeutralButton(getResources().getString(R.string.Dialog_neutral_button_login_failed), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    @Override
    protected void onStart(){
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop(){
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        String nomeFile = post.getPdf().substring(post.getPdf().indexOf("/pdf"));
        nomeFile = nomeFile.substring(7, nomeFile.indexOf("?"));
        if(!nomeFile.contains(".pdf")) nomeFile = nomeFile + ".pdf";
        File file = new File(String.valueOf(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)));

        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(post.getPdf()))
                .setTitle(nomeFile)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationUri(Uri.fromFile(file));

        if(downloadManager != null) downloadManager.enqueue(request);
    }

}
