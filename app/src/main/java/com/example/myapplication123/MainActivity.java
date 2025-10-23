package com.example.myapplication123;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private ProgressBar progressBar;
    private CatImages task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.catImage);
        progressBar = findViewById(R.id.progress);

        task = new CatImages();
        task.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (task != null) task.cancel(true);
    }

    class CatImages extends AsyncTask<String, Integer, String> {

        private Bitmap currentBitmap = null;
        private boolean newImageReady = false;

        @Override
        protected void onPreExecute() {
            progressBar.setMax(100);
            progressBar.setProgress(0);
        }

        @Override
        protected String doInBackground(String... params) {
            while (!isCancelled()) {
                try {
                    JSONObject json = new JSONObject(httpGet("https://cataas.com/cat?json=true"));
                    String id = json.optString("id");

                    String fullImgUrl = "https://cataas.com/cat/" + id + "?width=1920&height=1080&fit=cover";

                    File outFile = new File(getFilesDir(), id + ".jpg");
                    if (outFile.exists()) {
                        currentBitmap = BitmapFactory.decodeFile(outFile.getAbsolutePath());
                    } else {
                        byte[] data = httpGetBytes(fullImgUrl);
                        try (FileOutputStream fos = new FileOutputStream(outFile)) {
                            fos.write(data);
                        }
                        currentBitmap = BitmapFactory.decodeFile(outFile.getAbsolutePath());
                    }

                    newImageReady = true;
                    for (int i = 0; i <= 100 && !isCancelled(); i++) {
                        publishProgress(i);
                        Thread.sleep(30);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int p = values[0];
            progressBar.setProgress(p);

            if (newImageReady && currentBitmap != null) {
                imageView.setImageBitmap(currentBitmap);
                newImageReady = false;
            }
        }
    }


    private String httpGet(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(15000);
        try (InputStream is = new BufferedInputStream(conn.getInputStream());
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            return sb.toString();
        } finally {
            conn.disconnect();
        }
    }

    private byte[] httpGetBytes(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(20000);
        try (InputStream is = new BufferedInputStream(conn.getInputStream())) {
            byte[] buf = new byte[8192];
            int read;
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            while ((read = is.read(buf)) != -1) {
                baos.write(buf, 0, read);
            }
            return baos.toByteArray();
        } finally {
            conn.disconnect();
        }
    }
}
