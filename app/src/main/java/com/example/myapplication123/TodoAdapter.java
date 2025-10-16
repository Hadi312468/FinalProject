package com.example.myapplication123;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class TodoAdapter extends ArrayAdapter<Todo> {
    public TodoAdapter(Context ctx, List<Todo> items) { super(ctx, 0, items); }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            row = LayoutInflater.from(getContext()).inflate(
                    R.layout.row_todo, parent, false);
        }
        TextView tv = row.findViewById(R.id.rowText);
        Todo item = getItem(position);
        tv.setText(item.text);

        if (item.urgent) {
            row.setBackgroundColor(0xFFFF0000); // red
            tv.setTextColor(0xFFFFFFFF);        // white text
        } else {
            row.setBackgroundColor(0xFFFFFFFF); // white
            tv.setTextColor(0xFF000000);        // black
        }
        return row;
    }
}
