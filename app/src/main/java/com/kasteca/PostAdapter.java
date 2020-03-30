package com.kasteca;

import android.content.Context;
import android.content.Intent;
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

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{
    private ArrayList<Post> posts = null;
    private Context context;

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


    public PostAdapter(ArrayList<Post> post_dataset, Context context) {
        posts = post_dataset;
        this.context = context;
    }

    // Crea nuove view (invocato dal layout manager)
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // crea una nuova view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.post_item, parent, false);
        PostAdapter.ViewHolder vh = new PostAdapter.ViewHolder(v);
        return vh;
    }

    // Sostituisce i contenuti della view (invocato dal layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.text_post.setText(posts.get(position).getTesto());
        holder.tag.setText(posts.get(position).getTag());

        if(posts.get(position).getPdf() != null){
            //holder.icon_post.setImageResource(R.drawable.pdf_icon);
        }
        else if (posts.get(position).getLink() != null){
            //holder.icon_post.setImageResource(R.drawable.link_icon);
        }
        else{
            //holder.icon_post.setImageResource(R.drawable.other_icon);
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
   /* @Override
    public void onBindViewHolder(ArtistAdapter.ViewHolder holder, final int position) {

        holder.description.setText(artists.get(position).getName());

        holder.icon.setImageResource(R.drawable.ic_account_box_white_24dp);

        holder.description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent artistIntent = new Intent(context, ArtistActivity.class);
                artistIntent.putExtra("id", artists.get(position).getId());
                artistIntent.putExtra("name", artists.get(position).getName());
                artistIntent.putExtra("year_started", artists.get(position).getYear_started());
                artistIntent.putExtra("year_quit", artists.get(position).getYear_quit());
                context.startActivity(artistIntent);
            }
        });
    } */

    // Ritorna la dimensione del dataset (inocato dal layout manager)
    @Override
    public int getItemCount() {
        return posts.size();
    }
}
