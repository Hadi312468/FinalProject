package com.example.myapplication123;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText input;
    private Switch switchUrgent;
    private Button btnAdd;
    private ListView listView;

    private TodoDbHelper dbHelper;
    private SQLiteDatabase db;
    private final ArrayList<Todo> items = new ArrayList<>();
    private TodoAdapter adapter;

    private static final String TAG = "LAB5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input = findViewById(R.id.input);
        switchUrgent = findViewById(R.id.switchUrgent);
        btnAdd = findViewById(R.id.btnAdd);
        listView = findViewById(R.id.todoList);

        dbHelper = new TodoDbHelper(this);
        db = dbHelper.getWritableDatabase();

        loadTodosFromDb();
        adapter = new TodoAdapter(this, items);
        listView.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> {
            String text = input.getText().toString().trim();
            if (text.isEmpty()) return;

            boolean urgent = switchUrgent.isChecked();

            ContentValues cv = new ContentValues();
            cv.put(TodoDbHelper.COL_TEXT, text);
            cv.put(TodoDbHelper.COL_URGENT, urgent ? 1 : 0);
            long newId = db.insert(TodoDbHelper.TABLE_TODO, null, cv);

            items.add(new Todo(newId, text, urgent));
            adapter.notifyDataSetChanged();

            input.setText("");
            switchUrgent.setChecked(false);
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Todo t = items.get(position);
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.delete_title))
                    .setMessage(getString(R.string.delete_msg))
                    .setPositiveButton(getString(R.string.yes), (d, w) -> deleteTodo(position, t))
                    .setNegativeButton(getString(R.string.no), null)
                    .show();
        });

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            deleteTodo(position, items.get(position));
            return true;
        });
    }

    private void deleteTodo(int position, Todo t) {
        db.delete(TodoDbHelper.TABLE_TODO,
                TodoDbHelper.COL_ID + "=?",
                new String[]{String.valueOf(t.id)});
        items.remove(position);
        adapter.notifyDataSetChanged();
    }

    private void loadTodosFromDb() {
        Cursor c = db.query(TodoDbHelper.TABLE_TODO,
                null, null, null, null, null, TodoDbHelper.COL_ID + " ASC");
        printCursor(c, db);
        items.clear();
        while (c.moveToNext()) {
            long id = c.getLong(c.getColumnIndexOrThrow(TodoDbHelper.COL_ID));
            String text = c.getString(c.getColumnIndexOrThrow(TodoDbHelper.COL_TEXT));
            boolean urgent = c.getInt(c.getColumnIndexOrThrow(TodoDbHelper.COL_URGENT)) == 1;
            items.add(new Todo(id, text, urgent));
        }
        c.close();
    }

    private void printCursor(Cursor c, SQLiteDatabase db) {
        Log.i(TAG, "DB version: " + db.getVersion());
        Log.i(TAG, "Column count: " + c.getColumnCount());
        String[] cols = c.getColumnNames();
        StringBuilder sb = new StringBuilder("Columns: ");
        for (int i = 0; i < cols.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(cols[i]);
        }
        Log.i(TAG, sb.toString());
        Log.i(TAG, "Row count: " + c.getCount());

        int row = 0;
        if (c.moveToFirst()) {
            do {
                StringBuilder rowSb = new StringBuilder("Row " + row + ": ");
                for (String name : cols) {
                    int idx = c.getColumnIndexOrThrow(name);
                    rowSb.append(name).append("=")
                            .append(c.getString(idx)).append("  ");
                }
                Log.i(TAG, rowSb.toString());
                row++;
            } while (c.moveToNext());
            c.moveToFirst();
        }
    }
}
