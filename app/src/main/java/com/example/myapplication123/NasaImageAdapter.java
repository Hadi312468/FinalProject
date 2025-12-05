package com.example.myapplication123;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class NasaImageAdapter extends ArrayAdapter<NasaImage> {

    public NasaImageAdapter(@NonNull Context context, @NonNull List<NasaImage> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView,
                        @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(android.R.layout.simple_list_item_2, parent, false);
        }
        NasaImage img = getItem(position);
        TextView t1 = convertView.findViewById(android.R.id.text1);
        TextView t2 = convertView.findViewById(android.R.id.text2);
        if (img != null) {
            t1.setText(img.getTitle());
            t2.setText(img.getDate());
        }
        return convertView;
    }
}
