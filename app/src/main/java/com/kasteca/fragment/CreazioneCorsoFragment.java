package com.kasteca.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kasteca.R;
import com.kasteca.object.Docente;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreazioneCorsoFragment extends Fragment {

    private String LOG="DEBUG_CCF";

    private Docente docente;
    private EditText nome;
    private EditText descrizione;
    private EditText codice;
    private Button conferma;

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle= getArguments();
        //Recupero dati dal bundle
        docente = new Docente();
        docente.setNome(bundle.getString("nome"));
        docente.setCognome(bundle.getString("cognome"));
        docente.setEmail(bundle.getString("email"));
        docente.setId(bundle.getString("id"));

        view = inflater.inflate(R.layout.fragment_creazione_corso, container, false);
        //Carichiamo editText.
        this.nome= (EditText) view.findViewById(R.id.nome);
        this.descrizione= (EditText) view.findViewById(R.id.descrizione);
        this.codice= (EditText) view.findViewById(R.id.codice);
        this.conferma= (Button) view.findViewById(R.id.conferma_button);

        //ClickListener del Button.
        this.conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confermaCreazione();
            }
        });


        return view;
    }

    private void confermaCreazione(){

        if( (nome.getText().length()!=0) && (codice.getText().length()!=0) && (descrizione.getText().length()!=0)){

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference corsiReference = db.collection("Corsi");

            //Se i dati possono essere inviati
            Map<String,Object> documentSend= new HashMap<>();
            documentSend.put("nome_corso",nome.getText().toString());
            documentSend.put("codice",codice.getText().toString());
            documentSend.put("descrizione",descrizione.getText().toString());
            documentSend.put("docente",docente.getId());
            documentSend.put("lista_post", Arrays.asList());
            documentSend.put("lista_studenti",Arrays.asList());

            Date date= new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            documentSend.put("anno_accademico",calendar.get(Calendar.YEAR)+"/"+(Integer.parseInt(calendar.get(Calendar.YEAR)))+1);

            Log.e(LOG,"anno_accademico: "+calendar.get(Calendar.YEAR)+"/"+calendar.get(Calendar.YEAR)+1);

            //Invio il documento
            //Utilizzo add in questo modo si autogenererà l'id del documento
            corsiReference.add(documentSend).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.d(LOG, "DocumentSnapshot successfully written!");
                    //Chiamiamo il metodo per aggiungere lid del corso nel documento del docente
                    addIdinDocente(documentReference.getId());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(LOG, "Error writing document", e);
                }
            });

        }else{
            //eccezione da definire
            Log.e(LOG,"I dati da inviare non vanno bene");
            Toast.makeText(getActivity().getApplicationContext(),"Errore, dati mancanti.",Toast.LENGTH_LONG);

        }
    }

    private void addIdinDocente(String idCorso){
        Log.d(LOG, "Aggiungo del corso nel docente");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference docentiReference = db.collection("Docenti");
        docentiReference.document(docente.getId()).update("lista_corsi", FieldValue.arrayUnion(idCorso));
    }

}
