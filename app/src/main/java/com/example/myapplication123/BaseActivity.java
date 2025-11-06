package com.example.myapplication123;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public abstract class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout drawerLayout;
    protected MaterialToolbar toolbar;
    protected NavigationView navView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navView = findViewById(R.id.nav_view);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_24);
        toolbar.setNavigationOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(navView)) {
                drawerLayout.closeDrawer(navView);
                Toast.makeText(this, "Menu closed", Toast.LENGTH_SHORT).show();
            } else {
                drawerLayout.openDrawer(navView);
                Toast.makeText(this, "Menu opened", Toast.LENGTH_SHORT).show();
            }
        });

        navView.setNavigationItemSelectedListener(this);
        navView.setCheckedItem(R.id.nav_home);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }

    protected void setChildContent(@LayoutRes int layoutResId) {
        getLayoutInflater().inflate(layoutResId, findViewById(R.id.content_frame), true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        navView.setCheckedItem(id);

        if (id == R.id.nav_home) {
            Toast.makeText(this, "Home selected", Toast.LENGTH_SHORT).show();
            if (!(this instanceof MainActivity)) {
                startActivity(new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        } else if (id == R.id.nav_dadjoke) {
            Toast.makeText(this, "Dad Joke selected", Toast.LENGTH_SHORT).show();
            if (!(this instanceof DadJokeActivity)) {
                startActivity(new Intent(this, DadJokeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        } else if (id == R.id.nav_exit) {
            Toast.makeText(this, "Exiting…", Toast.LENGTH_SHORT).show();
            finishAffinity();
        }

        drawerLayout.closeDrawer(navView);
        return true;
    }
}
