package com.example.myapplication123;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public abstract class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected Toolbar toolbar;
    protected ActionBarDrawerToggle drawerToggle;
    private TextView txtVersion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setContentLayout(@LayoutRes int layoutResId) {
        setContentView(R.layout.activity_base);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtVersion = findViewById(R.id.txtVersion);
        if (txtVersion != null) txtVersion.setText(getString(R.string.version_label));

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();
        drawerToggle.getDrawerArrowDrawable().setColor(
                ContextCompat.getColor(this, android.R.color.white));

        FrameLayout contentFrame = findViewById(R.id.content_frame);
        View child = LayoutInflater.from(this).inflate(layoutResId, contentFrame, false);
        contentFrame.addView(child);
    }

    protected void setActivityTitle(String title) {
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(title);
    }

    protected abstract void showHelpDialog();

    protected void showSimpleHelp(String msg) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.menu_help))
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle != null && drawerToggle.onOptionsItemSelected(item)) return true;
        if (item.getItemId() == R.id.action_help) {
            showHelpDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) startActivity(new Intent(this, MainActivity.class));
        else if (id == R.id.nav_search) startActivity(new Intent(this, SearchActivity.class));
        else if (id == R.id.nav_favourites) startActivity(new Intent(this, FavouritesActivity.class));
        else if (id == R.id.nav_settings) startActivity(new Intent(this, SettingsActivity.class));

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}