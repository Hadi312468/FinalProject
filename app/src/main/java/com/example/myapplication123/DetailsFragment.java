package com.example.myapplication123;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class DetailsFragment extends Fragment {

    public DetailsFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_details, container, false);

        Bundle args = getArguments();
        if (args != null) {
            ((TextView) v.findViewById(R.id.tv_name)).setText(args.getString("name", ""));
            ((TextView) v.findViewById(R.id.tv_height)).setText(args.getString("height", ""));
            ((TextView) v.findViewById(R.id.tv_mass)).setText(args.getString("mass", ""));
        }
        return v;
    }
}
