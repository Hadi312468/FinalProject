package com.example.myapplication123;

public class NasaImage {
    private long id;
    private final String date;
    private final String title;
    private final String url;
    private final String hdUrl;

    public NasaImage(String date, String title, String url, String hdUrl) {
        this(-1, date, title, url, hdUrl);
    }

    public NasaImage(long id, String date, String title, String url, String hdUrl) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.url = url;
        this.hdUrl = hdUrl;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getDate() { return date; }
    public String getTitle() { return title; }
    public String getUrl() { return url; }
    public String getHdUrl() { return hdUrl; }

    @Override
    public String toString() {
        return date + " - " + title;
    }
}
