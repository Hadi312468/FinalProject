package com.example.myapplication123;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private PeopleAdapter adapter;
    private final ArrayList<JSONObject> people = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);

        setContentView(R.layout.activity_main);
        setTitle("CST2335 Lab 7");

        listView = findViewById(R.id.people_list);
        adapter = new PeopleAdapter(this, people);
        listView.setAdapter(adapter);

        new FetchPeopleTask().execute();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            JSONObject person = people.get(position);
            Bundle b = toBundle(person);

            View detail = findViewById(R.id.detail_container);
            if (detail == null) {
                Intent i = new Intent(MainActivity.this, EmptyActivity.class);
                i.putExtras(b);
                startActivity(i);
            } else {
                DetailsFragment fragment = new DetailsFragment();
                fragment.setArguments(b);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.detail_container, fragment)
                        .commit();
            }
        });
    }

    private Bundle toBundle(JSONObject o) {
        Bundle b = new Bundle();
        b.putString("name", o.optString("name"));
        b.putString("height", o.optString("height"));
        b.putString("mass", o.optString("mass"));
        b.putString("hair_color", o.optString("hair_color"));
        b.putString("skin_color", o.optString("skin_color"));
        b.putString("eye_color", o.optString("eye_color"));
        b.putString("birth_year", o.optString("birth_year"));
        b.putString("gender", o.optString("gender"));
        return b;
    }

    @SuppressLint("StaticFieldLeak")
    private class FetchPeopleTask extends AsyncTask<Void, Void, List<JSONObject>> {
        @Override
        protected List<JSONObject> doInBackground(Void... voids) {
            ArrayList<JSONObject> result = new ArrayList<>();
            String next = "https://swapi.dev/api/people/?format=json";
            try {
                while (next != null && !isCancelled()) {
                    URL url = new URL(next);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(10000);
                    conn.setReadTimeout(10000);
                    try (InputStream is = conn.getInputStream();
                         BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) sb.append(line);

                        JSONObject root = new JSONObject(sb.toString());
                        JSONArray arr = root.getJSONArray("results");
                        for (int i = 0; i < arr.length(); i++) {
                            result.add(arr.getJSONObject(i));
                        }

                        next = root.isNull("next") ? null : root.optString("next", null);
                        if (next != null && !next.contains("format=json")) {
                            next = next + (next.contains("?") ? "&" : "?") + "format=json";
                        }
                    } finally {
                        conn.disconnect();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(List<JSONObject> data) {
            if (data != null && !data.isEmpty()) {
                people.clear();
                people.addAll(data);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(MainActivity.this, "No data received from SWAPI", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static class PeopleAdapter extends BaseAdapter {
        private final ArrayList<JSONObject> data;
        private final LayoutInflater inflater;

        PeopleAdapter(Context c, ArrayList<JSONObject> d) {
            data = d;
            inflater = LayoutInflater.from(c);
        }

        @Override public int getCount() { return data.size(); }
        @Override public Object getItem(int position) { return data.get(position); }
        @Override public long getItemId(int position) { return position; }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv;
            if (convertView == null) {
                tv = (TextView) inflater.inflate(R.layout.row_person, parent, false);
            } else {
                tv = (TextView) convertView;
            }
            tv.setText(data.get(position).optString("name", "Unknown"));
            return tv;
        }
    }
}
