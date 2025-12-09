package com.example.myapplication123;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.List;

public class FavouritesActivity extends BaseActivity {

    private ListView listView;
    private NasaRepository repository;
    private List<NasaImage> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityTitle(getString(R.string.menu_favourites));
        setContentLayout(R.layout.activity_favourites);

        listView = findViewById(R.id.listFavourites);
        repository = new NasaRepository(this);

        loadData();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            NasaImage img = data.get(position);
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("id", img.getId());
            intent.putExtra("date", img.getDate());
            intent.putExtra("title", img.getTitle());
            intent.putExtra("url", img.getUrl());
            intent.putExtra("hdurl", img.getHdUrl());
            startActivity(intent);
        });

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            NasaImage img = data.get(position);
            new AlertDialog.Builder(this)
                    .setTitle(R.string.dialog_delete_title)
                    .setMessage(R.string.dialog_delete_message)
                    .setPositiveButton(android.R.string.yes, (d, w) -> {
                        repository.delete(img.getId());
                        Toast.makeText(this, R.string.toast_deleted, Toast.LENGTH_SHORT).show();
                        loadData();
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();
            return true;
        });
    }

    private void loadData() {
        data = repository.getAll();
        listView.setAdapter(new NasaImageAdapter(this, data));
    }

    @Override
    protected void showHelpDialog() {
        showSimpleHelp(getString(R.string.help_favourites));
    }
}