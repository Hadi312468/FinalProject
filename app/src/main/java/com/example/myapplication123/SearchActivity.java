package com.example.myapplication123;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class SearchActivity extends BaseActivity {

    private static final String PREFS_NAME = "NasaPrefs";
    private static final String PREF_LAST_DATE = "last_date";

    // Use DEMO_KEY for testing. Later replace with your own key from https://api.nasa.gov/
    private static final String API_KEY = "DEMO_KEY";

    private TextView txtSelectedDate, txtResultTitle, txtResultUrl;
    private ProgressBar progressBar;
    private Button btnPickDate, btnSearch, btnSaveFavourite;

    private String currentDate = "";
    private NasaImage currentImage;
    private NasaRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityTitle(getString(R.string.menu_search));
        setContentLayout(R.layout.activity_search);

        txtSelectedDate = findViewById(R.id.txtSelectedDate);
        txtResultTitle = findViewById(R.id.txtResultTitle);
        txtResultUrl = findViewById(R.id.txtResultUrl);
        progressBar = findViewById(R.id.progressBar);
        btnPickDate = findViewById(R.id.btnPickDate);
        btnSearch = findViewById(R.id.btnSearch);
        btnSaveFavourite = findViewById(R.id.btnSaveFavourite);

        repository = new NasaRepository(this);

        // Load last date from SharedPreferences, default to a safe date
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        currentDate = prefs.getString(PREF_LAST_DATE, "2019-07-20");
        txtSelectedDate.setText(getString(R.string.label_selected_date, currentDate));

        btnPickDate.setOnClickListener(v -> showDatePicker());
        btnSearch.setOnClickListener(v -> {
            if (currentDate.isEmpty()) {
                Toast.makeText(this, R.string.error_no_date, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,
                        getString(R.string.toast_searching, currentDate),
                        Toast.LENGTH_SHORT).show();
                new NasaTask().execute(currentDate);
            }
        });

        // Open HD image URL in browser when clicked
        txtResultUrl.setOnClickListener(v -> {
            if (currentImage != null && currentImage.getHdUrl() != null && !currentImage.getHdUrl().isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentImage.getHdUrl()));
                startActivity(intent);
            }
        });

        btnSaveFavourite.setOnClickListener(v -> {
            if (currentImage != null) {
                repository.insert(currentImage);
                Snackbar.make(v, R.string.snackbar_saved, Snackbar.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, R.string.error_nothing_to_save, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void showHelpDialog() {
        showSimpleHelp(getString(R.string.help_search));
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this,
                (DatePicker view, int y, int m, int d) -> {
                    String mm = (m + 1 < 10 ? "0" : "") + (m + 1);
                    String dd = (d < 10 ? "0" : "") + d;
                    currentDate = y + "-" + mm + "-" + dd;
                    txtSelectedDate.setText(
                            getString(R.string.label_selected_date, currentDate));

                    getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                            .edit()
                            .putString(PREF_LAST_DATE, currentDate)
                            .apply();
                }, year, month, day);

        dpd.show();
    }

    private class NasaTask extends AsyncTask<String, Void, NasaImage> {

        private String errorMessage = null;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected NasaImage doInBackground(String... strings) {
            String date = strings[0];
            String urlStr = "https://api.nasa.gov/planetary/apod?api_key="
                    + API_KEY + "&date=" + date;

            HttpURLConnection conn = null;
            try {
                URL url = new URL(urlStr);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                int code = conn.getResponseCode();
                InputStream is;

                // Use error stream if non-200
                if (code >= 200 && code < 300) {
                    is = conn.getInputStream();
                } else {
                    is = conn.getErrorStream();
                    errorMessage = "HTTP error " + code;
                }

                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();

                if (code < 200 || code >= 300) {
                    // Try to see if NASA sent a JSON error message
                    try {
                        JSONObject err = new JSONObject(sb.toString());
                        errorMessage = err.optString("msg", errorMessage);
                    } catch (Exception ignore) {
                        // ignore parsing error for error body
                    }
                    return null;
                }

                JSONObject obj = new JSONObject(sb.toString());

                // Some APOD entries are videos; we only handle images
                String mediaType = obj.optString("media_type", "image");
                if (!"image".equals(mediaType)) {
                    errorMessage = "This APOD is not an image (media_type=" + mediaType + ")";
                    return null;
                }

                String title = obj.optString("title");
                String imgUrl = obj.optString("url");
                String hdUrl = obj.optString("hdurl", imgUrl);

                return new NasaImage(date, title, imgUrl, hdUrl);

            } catch (Exception e) {
                e.printStackTrace();
                errorMessage = e.getMessage();
                return null;
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }

        @Override
        protected void onPostExecute(NasaImage nasaImage) {
            progressBar.setVisibility(ProgressBar.GONE);

            if (nasaImage != null) {
                currentImage = nasaImage;
                txtResultTitle.setText(nasaImage.getTitle());
                txtResultUrl.setText(nasaImage.getHdUrl());
            } else {
                // Show more useful message
                if (errorMessage == null || errorMessage.isEmpty()) {
                    Toast.makeText(SearchActivity.this,
                            R.string.error_loading, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SearchActivity.this,
                            "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                }
                txtResultTitle.setText("");
                txtResultUrl.setText("");
            }
        }
    }
}
