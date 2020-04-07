package com.kasteca.activity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kasteca.R;
import com.kasteca.object.Post;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class PostActivity extends AppCompatActivity {
    Post post;
    TextView testoView;
    TextView tagView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        if(getIntent().hasExtra("post")){
            post = getIntent().getParcelableExtra("post");
        }

        testoView = findViewById(R.id.testoPostTextView);
        tagView = findViewById(R.id.tagTextView);

        testoView.setText(post.getTesto());
        tagView.setText(post.getTag());

        if(post.getLink() == null){
            findViewById(R.id.getLinkButton).setVisibility(View.INVISIBLE);
        }

        if(post.getPdf() == null){
            findViewById(R.id.getPdfButton).setVisibility(View.INVISIBLE);
        }

    }

    public void downloadPdf(View v){
        Log.d(TAG, "Download del pdf");
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
        Log.d(TAG, "Visualizzazione commenti");
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
}
