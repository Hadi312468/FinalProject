package com.example.myapplication123;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class DadJokeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildContent(R.layout.content_dadjoke);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(R.string.app_name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_choice1) {
            Toast.makeText(this, "You clicked on item 1", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_choice2) {
            Toast.makeText(this, "You clicked on item 2", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_overflow_text) {
            Toast.makeText(this, "Help clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
