package com.kasteca.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.kasteca.R;
import com.kasteca.activity.NewPostActivity;
import com.kasteca.adapter.PostAdapterFirestore;
import com.kasteca.object.Post;

import java.util.ArrayList;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class PostDocenteFragment extends Fragment{

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private String corso_id;
    private ArrayList<Post> posts;
    FirestoreRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle= getArguments();
        Log.e(TAG, "Bunndle nel fragment: " + bundle);
        corso_id = bundle.getString("codice_corso");
        bundle.getString("nome_docente");
        bundle.getString("cognome_docente");
        posts = new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_post_docente, container, false);

        //Recupero dei post del corso
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference postReference = db.collection("Post");
        Query query = postReference.whereEqualTo("corso", corso_id).orderBy("data", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post.class).build();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
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


        fab = getActivity().findViewById(R.id.fab_add_post);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NewPostActivity.class);
                intent.putExtra("corso_id", corso_id);
                startActivity(intent);
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