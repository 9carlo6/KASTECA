package com.kasteca.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;
import com.kasteca.PostAdapter;
import com.kasteca.R;
import com.kasteca.object.Post;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class PostDocenteActivity extends AppCompatActivity implements PostAdapter.OnPostListener{

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private ArrayList<String> post_ids;
    private String corso_id;
    private ArrayList<Post> posts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_docente_scrolling);

        corso_id = getIntent().getStringExtra("corso_id");
        post_ids = new ArrayList<>();
        posts = new ArrayList<>();

        final Source source = Source.SERVER;

        //Recupero dei post del corso
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRefCorso = db.collection("Corsi").document(corso_id);
        docRefCorso.get(source).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // Documento trovato
                    DocumentSnapshot document = task.getResult();
                    post_ids = (ArrayList<String>) document.getData().get("lista_post");
                    for(String id : post_ids) {
                        DocumentReference docRefPost = db.collection("Post").document(id);
                        docRefPost.get(source).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    // Documento trovato
                                    DocumentSnapshot document = task.getResult();
                                    Post post = new Post(
                                            document.getId(),
                                            (String) document.get("tag"),
                                            (String) document.get("testo"),
                                            corso_id,
                                            ((Timestamp) document.get("data")).toDate(),
                                            (String) document.get("link"),
                                            Uri.parse((String) document.get("pdf")),
                                            (ArrayList<String>) document.get("lista_commenti")
                                    );

                                    posts.add(post);
                                    setRecyclerView();

                                } else {
                                    showAlert(getResources().getString(R.string.get_posts_failed));
                                }
                            }
                        });
                    }

                } else {
                    showAlert(getResources().getString(R.string.get_posts_failed));
                }
            }
        });


        fab = findViewById(R.id.fab_add_post);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewPostActivity.class);
                intent.putExtra("corso_id", corso_id);
                startActivity(intent);
            }
        });
    }

    // Metodo in caso di recupero fallito dei post di un corso
    public void showAlert(String s){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getResources().getString(R.string.get_failed));
        alertDialog.setMessage(s);
        alertDialog.setNeutralButton(getResources().getString(R.string.Dialog_neutral_button_login_failed), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onPostClick(int position) {
        Post post = posts.get(position);
        Intent intent = new Intent(this, PostActivity.class);
        intent.putExtra("post", post);
        startActivity(intent);
    }

    public void setRecyclerView(){
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new PostAdapter(posts, this);
        recyclerView.setAdapter(adapter);
    }
}
