package com.example.myapplication123;

import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;

public class DetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityTitle(getString(R.string.title_detail));
        setContentLayout(R.layout.activity_detail);

        long id = getIntent().getLongExtra("id", -1);
        String date = getIntent().getStringExtra("date");
        String title = getIntent().getStringExtra("title");
        String url = getIntent().getStringExtra("url");
        String hdurl = getIntent().getStringExtra("hdurl");

        DetailFragment fragment = DetailFragment.newInstance(id, date, title, url, hdurl);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.detail_container, fragment);
        ft.commit();
    }

    @Override
    protected void showHelpDialog() {
        showSimpleHelp(getString(R.string.help_detail));
    }
}
