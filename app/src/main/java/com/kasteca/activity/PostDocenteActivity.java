package com.kasteca.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.kasteca.PostAdapter;
import com.kasteca.R;
import com.kasteca.object.Post;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

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

        //Recupero dei post del corso
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference postReference = db.collection("Post");
        Source source = Source.SERVER;

        postReference.whereEqualTo("corso", corso_id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot documento_post : task.getResult()){
                        Map<String, Object> p = documento_post.getData();

                        String link;
                        if(p.get("link") == null) link = null;
                        else link = p.get("link").toString();

                        String pdf;
                        if(p.get("pdf") == null) pdf = null;
                        else pdf = p.get("pdf").toString();

                        Post post = new Post(
                                documento_post.getId(),
                                p.get("tag").toString(),
                                p.get("testo").toString(),
                                corso_id,
                                ((Timestamp) p.get("data")).toDate(),
                                link,
                                pdf
                        );
                        posts.add(post);
                    }
                    setRecyclerView();
                }
                else {
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
