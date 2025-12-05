package com.example.myapplication123;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DetailFragment extends Fragment {

    private long imageId = -1;
    private String url;
    private String hdUrl;
    private NasaRepository repository;

    public static DetailFragment newInstance(long id, String date, String title,
                                             String url, String hdUrl) {
        DetailFragment f = new DetailFragment();
        Bundle b = new Bundle();
        b.putLong("id", id);
        b.putString("date", date);
        b.putString("title", title);
        b.putString("url", url);
        b.putString("hdurl", hdUrl);
        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail, container, false);

        TextView tDate = v.findViewById(R.id.txtDetailDate);
        TextView tTitle = v.findViewById(R.id.txtDetailTitle);
        TextView tUrl = v.findViewById(R.id.txtDetailUrl);
        Button btnOpen = v.findViewById(R.id.btnOpenInBrowser);
        Button btnRemove = v.findViewById(R.id.btnRemoveFavourite);

        repository = new NasaRepository(requireContext());

        Bundle args = getArguments();
        if (args != null) {
            imageId = args.getLong("id", -1);
            String date = args.getString("date");
            String title = args.getString("title");
            url = args.getString("url");
            hdUrl = args.getString("hdurl");

            tDate.setText(date);
            tTitle.setText(title);
            tUrl.setText(hdUrl != null && !hdUrl.isEmpty() ? hdUrl : url);
        }

        btnOpen.setOnClickListener(view -> {
            String target = (hdUrl != null && !hdUrl.isEmpty()) ? hdUrl : url;
            if (target != null && !target.isEmpty()) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(target)));
            }
        });

        btnRemove.setOnClickListener(view -> {
            if (imageId > 0) {
                repository.delete(imageId);
                Toast.makeText(requireContext(),
                        R.string.toast_removed, Toast.LENGTH_SHORT).show();
                requireActivity().finish();
            } else {
                Toast.makeText(requireContext(),
                        R.string.error_nothing_to_save, Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }
}
