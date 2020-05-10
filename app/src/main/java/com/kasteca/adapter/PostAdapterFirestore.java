package com.kasteca.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.kasteca.R;
import com.kasteca.object.Post;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostAdapterFirestore extends FirestoreRecyclerAdapter<Post, PostAdapterFirestore.ViewHolder> {

    private OnClickListener mClickListener;

    public PostAdapterFirestore(@NonNull FirestoreRecyclerOptions<Post> options){
        super(options);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.post_item, parent, false);
        return new PostAdapterFirestore.ViewHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull final Post model) {
        if(model.getTesto().length() > 20){
            holder.text_post.setText(MessageFormat.format("{0}...", model.getTesto().trim().substring(0, 21)));
        }
        else{
            holder.text_post.setText(model.getTesto().trim());
        }
        holder.tag.setText(model.getTag());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ITALY);
        holder.data.setText(sdf.format(model.getData()));

        if(model.getPdf() != null){
            holder.icon_post.setImageResource(R.drawable.pdf_icon_foreground);
        }
        else if (model.getLink() != null){
            holder.icon_post.setImageResource(R.drawable.link_icon_foreground);
        }
        else{
            holder.icon_post.setImageResource(R.drawable.message_icon_foreground);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView icon_post;
        private TextView text_post;
        public TextView tag;
        public TextView data;

        ViewHolder(View v) {
            super(v);
            icon_post = v.findViewById(R.id.icon_post);
            text_post = v.findViewById(R.id.text_post);
            tag = v.findViewById(R.id.text_tag);
            data = v.findViewById(R.id.data_item_textView);

            v.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && mClickListener != null){
                        mClickListener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface OnClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnClickListener(OnClickListener clickListener){
        mClickListener = clickListener;
    }

}
