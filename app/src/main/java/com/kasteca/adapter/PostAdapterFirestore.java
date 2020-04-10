package com.kasteca.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.kasteca.R;
import com.kasteca.activity.PostActivity;
import com.kasteca.object.Post;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class PostAdapterFirestore extends FirestoreRecyclerAdapter<Post, PostAdapterFirestore.ViewHolder> {

    public PostAdapterFirestore(@NonNull FirestoreRecyclerOptions<Post> options){
        super(options);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.post_item, parent, false);
        PostAdapterFirestore.ViewHolder vh = new PostAdapterFirestore.ViewHolder(v);
        return vh;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull final Post model) {
        Log.e(TAG, model.toString());
        final Post mmodel = model;
        holder.text_post.setText(model.getTesto());
        holder.tag.setText(model.getTag());

        if(model.getPdf() != null){
            holder.icon_post.setImageResource(R.drawable.pdf_icon_foreground);
        }
        else if (model.getLink() != null){
            holder.icon_post.setImageResource(R.drawable.link_icon_foreground);
        }
        else{
            holder.icon_post.setImageResource(R.drawable.message_icon_foreground);
        }
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PostActivity.class);
                intent.putExtra("post", mmodel);
                view.getContext().startActivity(intent);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cv;
        public ImageView icon_post;
        public TextView text_post;
        public TextView tag;

        public ViewHolder(View v) {
            super(v);
            cv = v.findViewById(R.id.card_view_post);
            icon_post = v.findViewById(R.id.icon_post);
            text_post = v.findViewById(R.id.text_post);
            tag = v.findViewById(R.id.text_tag);
        }
    }

}
