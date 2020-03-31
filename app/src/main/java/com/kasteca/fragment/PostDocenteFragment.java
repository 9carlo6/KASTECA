package com.kasteca.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;
import com.kasteca.PostAdapter;
import com.kasteca.R;
import com.kasteca.activity.NewPostActivity;
import com.kasteca.activity.PostActivity;
import com.kasteca.object.Post;

import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class PostDocenteFragment extends Fragment implements PostAdapter.OnPostListener {
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private ArrayList<String> post_ids;
    private ArrayList<Post> posts;
    private String corso_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_docente_scrolling, container, false);
        corso_id = "";
        post_ids = new ArrayList<>();
        posts = new ArrayList<>();

        Source source = Source.SERVER;

        //Recupero dei post del corso
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRefCorso = db.collection("Corsi").document(corso_id);
        docRefCorso.get(source).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // Documento trovato
                    DocumentSnapshot document = task.getResult();
                    post_ids = (ArrayList<String>) document.getData().get("lista_post");
                } else {
                    showAlert(getResources().getString(R.string.get_posts_failed));
                }
            }
        });

        for(String id : post_ids){
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
                                (String)  document.get("testo"),
                                corso_id,
                                (Date) document.get("data"),
                                (String) document.get("link"),
                                Uri.parse((String) document.get("pdf")),
                                (ArrayList<String>) document.get("lista_commenti")
                        );

                        posts.add(post);
                    } else {
                        showAlert(getResources().getString(R.string.get_posts_failed));
                    }
                }
            });
        }

        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new PostAdapter(posts, this);
        recyclerView.setAdapter(adapter);

        fab = getView().findViewById(R.id.fab_add_post);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getBaseContext(), NewPostActivity.class);
                intent.putExtra("corso_id", corso_id);
                getActivity().startActivity(intent);
            }
        });

        return view;
    }

    // Metodo in caso di recupero fallito dei post di un corso
    public void showAlert(String s){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
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
        Intent intent = new Intent(getActivity().getBaseContext(), PostActivity.class);
        intent.putExtra("post", post);
        getActivity().startActivity(intent);
    }
}
