package com.example.myapplication123;

public class Todo {
    public long id;
    public String text;
    public boolean urgent;

    public Todo(long id, String text, boolean urgent) {
        this.id = id;
        this.text = text;
        this.urgent = urgent;
    }

    @Override public String toString() { return text; }
}
