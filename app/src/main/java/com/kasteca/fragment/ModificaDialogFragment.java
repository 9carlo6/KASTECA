package com.kasteca.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.kasteca.R;

public class ModificaDialogFragment extends AppCompatDialogFragment {

    private final String TAG= "MODIFICA_DIALOG";

    private EditDialogListener listener;
    private EditText testo;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.modifica_commento_risposta_dialog, null);
        builder.setView(view)
                .setTitle("Modifica")
                .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG,"Scelto di annullare le modifiche");
                    }
                })
        .setPositiveButton("Modifica", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.applyText(testo.getText().toString());
            }
        });

        testo= view.findViewById(R.id.modifica_edittext);

        return builder.create();
    }

    public void setListener(EditDialogListener listener){
        this.listener=listener;
    }

    public interface EditDialogListener{
        void applyText(String newText);
    }


}
