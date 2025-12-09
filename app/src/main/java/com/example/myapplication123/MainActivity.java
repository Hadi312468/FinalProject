package com.example.myapplication123;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityTitle(getString(R.string.app_name));
        setContentLayout(R.layout.activity_main);

        Button btnSearch = findViewById(R.id.btnGoSearch);
        Button btnFav = findViewById(R.id.btnGoFavourites);

        btnSearch.setOnClickListener(v ->
                startActivity(new Intent(this, SearchActivity.class)));

        btnFav.setOnClickListener(v ->
                startActivity(new Intent(this, FavouritesActivity.class)));
    }

    @Override
    protected void showHelpDialog() {
        showSimpleHelp(getString(R.string.help_main));
    }
}