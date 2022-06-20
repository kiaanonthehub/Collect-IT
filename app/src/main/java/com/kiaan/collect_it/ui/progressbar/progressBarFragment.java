package com.kiaan.collect_it.ui.progressbar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.kiaan.collect_it.R;
import com.kiaan.collect_it.databinding.FragmentHomeBinding;

public class progressBarFragment extends Fragment {
    private FragmentHomeBinding binding;

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_progressbar, parent, false);

        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
    }
}
