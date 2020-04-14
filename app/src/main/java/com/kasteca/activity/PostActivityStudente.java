package com.kasteca.activity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.kasteca.R;
import com.kasteca.adapter.CommentiAdapterFirestore;
import com.kasteca.object.Commento;
import com.kasteca.object.Post;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class PostActivityStudente extends AppCompatActivity {
    private Post post;
    private String nomeCognome;
    private TextView testoView;
    private TextView tagView;
    private TextView nomeCognomeView;
    private TextView dataView;
    private TextView numeroCommentiView;
    private TextView linkView;
    private PopupWindow popWindow;
    private CommentiAdapterFirestore adapter = null;
    private RecyclerView recyclerView;
    private ImageButton inviaCommento;
    private EditText scriviCommento;

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

        nomeCognomeView = findViewById(R.id.nome_cognome_textView);
        dataView = findViewById(R.id.data_textView);
        tagView = findViewById(R.id.tagTextView);
        testoView = findViewById(R.id.testoPostTextView);
        numeroCommentiView = findViewById(R.id.commenti_numero_textView);

        nomeCognomeView.setText(nomeCognome);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        if(post.getData() != null)  dataView.setText(sdf.format(post.getData()));
        testoView.setText(post.getTesto());
        tagView.setText(post.getTag());
        linkView = findViewById(R.id.link_textView);

        if(post.getLink() == null){
            linkView.setVisibility(View.INVISIBLE);
        }
        else{
            linkView.setText(post.getLink());
        }

        if(post.getPdf() == null){
            findViewById(R.id.getPdfButton).setVisibility(View.INVISIBLE);
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference postReference = db.collection("Commenti");
        Query query = postReference.whereEqualTo("post", post.getId()).orderBy("data", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Commento> options = new FirestoreRecyclerOptions.Builder<Commento>().setQuery(query, Commento.class).build();
        adapter = new CommentiAdapterFirestore(options);
    }

    public void downloadPdf(View v){
        /*FirebaseStorage myStorage = FirebaseStorage.getInstance();
        StorageReference rootStorageRef = myStorage.getReference();
        StorageReference documentRef = rootStorageRef.child("documents/score_c.pdf");

        File externalDir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File f = new File(externalDir.toString()+"/score_c.pdf");

        if (f.exists())
            f.delete();

        documentRef.getFile(f).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(),"File has been created", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Toast.makeText(getApplicationContext(),"Error downloading the file", Toast.LENGTH_LONG).show();
            }
        });*/

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

        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // si visualizza il layout del popup
        final View inflatedView = layoutInflater.inflate(R.layout.popup_comments_layout, null,false);
        // si cerca la recycle view nel popup layout
        recyclerView = (RecyclerView) inflatedView.findViewById(R.id.recycler_view_commenti);
        // si cerca l'Edit Text nel popup layout
        scriviCommento = inflatedView.findViewById(R.id.writeComment);
        // si cerca il bottone per l'invio di un commento nel popup layout
        inviaCommento = inflatedView.findViewById(R.id.send_commento_button);
        // si rende il bottone disabilitato fino a che non viene scritta almeno una lettera nell'Edit Text
        // inviaCommento.setEnabled(false);

        inviaCommento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference commentiRef = db.collection("Commenti");

                Map<String, Object> newCommento = new HashMap<>();
                newCommento.put("testo", scriviCommento.getText().toString());
                newCommento.put("data", new Date());
                newCommento.put("lista_risposte", new ArrayList<String>());
                newCommento.put("post", post.getId());
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                newCommento.put("proprietario_commento", currentUser.getUid());

                commentiRef.add(newCommento)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                FirebaseFirestore.getInstance().collection("Post").document(post.getId()).update("lista_commenti", FieldValue.arrayUnion(documentReference.getId()));
                                scriviCommento.getText().clear();
                            }
                        });
            }
        });


        // si prendono le dimensioni del dispositivo
        Display display = getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);


        // si inizializza la recyclerView
        setRecyclerView();


        // si setta la grandezza della pop window a seconda delle dimensioni del dispositivo
        popWindow = new PopupWindow(inflatedView, size.x - 50,size.y - 400, true );
        // si setta come background una forma rettangolare con gli angoli arrotondati
        popWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popup_shape));
        // si rende "focusable" per mostrare la testiera e scrivere nell'EditText
        // lo si fa solo se il booleano passato al metodo come argomento è true
        if(isAddCommentClicked) popWindow.setFocusable(true);
        // si fa in modo che si possa toccare lo schermo al di fuori della finestra,
        // cosa che porta alla chiusura della finestra stessa
        popWindow.setOutsideTouchable(true);

        popWindow.setAnimationStyle(R.style.PopupAnimation);

        // si mostra la finestra dal basso dello schermo
        popWindow.showAtLocation(v, Gravity.BOTTOM, 0,100);
    }


    private void setRecyclerView(){

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                recyclerView.scrollToPosition(0);
            }
        });
        recyclerView.setAdapter(adapter);

        adapter.setOnRispondiClickListener(new CommentiAdapterFirestore.OnRispondiClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Log.e(TAG, "Rispondo al commento " + adapter.getSnapshots().getSnapshot(position).getId());
            }
        });

        adapter.setOnVisualizzaRisposteClickListener(new CommentiAdapterFirestore.OnVisualizzaRisposteClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Log.e(TAG, "Visualizzo le risposte al commento " + adapter.getSnapshots().getSnapshot(position).getId());
            }
        });

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
}
