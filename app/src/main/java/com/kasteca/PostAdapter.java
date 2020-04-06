package com.kasteca;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kasteca.object.Post;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{
    private ArrayList<Post> posts;
    private OnPostListener mOnPostListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CardView cv;
        public ImageView icon_post;
        public TextView text_post;
        public TextView tag;
        public OnPostListener onPostListener;

        public ViewHolder(View v, OnPostListener onPostListener) {
            super(v);
            cv = v.findViewById(R.id.card_view_post);
            icon_post = v.findViewById(R.id.icon_post);
            text_post = v.findViewById(R.id.text_post);
            tag = v.findViewById(R.id.text_tag);
            this.onPostListener = onPostListener;

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onPostListener.onPostClick(getAdapterPosition());
        }
    }


    public PostAdapter(ArrayList<Post> post_dataset, OnPostListener onPostListener) {
        this.posts = post_dataset;
        this.mOnPostListener = onPostListener;
        Log.e(TAG, "la lista posts dentro l'adapter contiene: " + posts.toString());
    }

    // Crea nuove view (invocato dal layout manager)
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // crea una nuova view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.post_item, parent, false);
        PostAdapter.ViewHolder vh = new PostAdapter.ViewHolder(v, mOnPostListener);
        return vh;
    }

    // Sostituisce i contenuti della view (invocato dal layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.text_post.setText(posts.get(position).getTesto());
        holder.tag.setText(posts.get(position).getTag());

        if(posts.get(position).getPdf() != null){
            holder.icon_post.setImageResource(R.drawable.pdf_icon_foreground);
        }
        else if (posts.get(position).getLink() != null){
            holder.icon_post.setImageResource(R.drawable.link_icon_foreground);
        }
        else{
            holder.icon_post.setImageResource(R.drawable.message_icon_foreground);
        }
    }

    // Ritorna la dimensione del dataset (inocato dal layout manager)
    @Override
    public int getItemCount() {
        return posts.size();
    }

    public interface OnPostListener{
        void onPostClick(int position);
    }
}