package com.kasteca.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.kasteca.R;
import com.kasteca.activity.NewPostActivity;
import com.kasteca.activity.PostActivity;
import com.kasteca.adapter.PostAdapterFirestore;
import com.kasteca.object.Post;

public class PostDocenteFragment extends Fragment{

    private RecyclerView recyclerView;
    private String corso_id;
    private String nome_cognome;
    private String id_docente;
    private PostAdapterFirestore adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle= getArguments();
        corso_id = bundle.getString("id_corso");
        nome_cognome = bundle.getString("nome_docente") + " " + bundle.getString("cognome_docente");
        id_docente = bundle.getString("id_docente");
        View view = inflater.inflate(R.layout.fragment_post_docente, container, false);

        //Recupero dei post del corso
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference postReference = db.collection("Post");
        Query query = postReference.whereEqualTo("corso", corso_id).orderBy("data", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post.class).build();

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_post_docente);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PostAdapterFirestore(options);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                recyclerView.scrollToPosition(0);
            }
        });
        recyclerView.setAdapter(adapter);
        adapter.setOnClickListener(new PostAdapterFirestore.OnClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Post post = documentSnapshot.toObject(Post.class);
                post.setId(adapter.getSnapshots().getSnapshot(position).getId());
                Intent intent = new Intent(getContext(), PostActivity.class);
                intent.putExtra("docente", nome_cognome);
                intent.putExtra("id_docente", id_docente);
                intent.putExtra("post", post);
                getActivity().startActivity(intent);
            }
        });


        return view;
    }



   @Override
    public void onStart(){
       super.onStart();
       adapter.startListening();
   }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }


}
