package com.kasteca.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kasteca.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PostDocenteFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post_docente_content, container, false);
    }
}
