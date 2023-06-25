package com.example.simplesearch;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText nameEditText, idEditText, searchEditText;
    private Button addButton, searchButton;
    private TextView resultTextView;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameEditText = findViewById(R.id.nameEditText);
        idEditText = findViewById(R.id.idEditText);
        addButton = findViewById(R.id.addButton);
        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        resultTextView = findViewById(R.id.resultTextView);

        // Create or open the database
        database = openOrCreateDatabase("my_database.db", MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS students (name VARCHAR, id VARCHAR);");

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String id = idEditText.getText().toString();
                insertData(name, id);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchId = searchEditText.getText().toString();
                searchStudent(searchId);
            }
        });
    }

    private void insertData(String name, String id) {
        String insertQuery = "INSERT INTO students (name, id) VALUES ('" + name + "', '" + id + "');";
        database.execSQL(insertQuery);
        nameEditText.setText("");
        idEditText.setText("");
    }

    private void searchStudent(String id) {
        String searchQuery = "SELECT * FROM students WHERE id = '" + id + "';";
        Cursor cursor = database.rawQuery(searchQuery, null);
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            resultTextView.setText("Name: " + name);
        } else {
            resultTextView.setText("No student found with ID: " + id);
        }
        cursor.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (database != null) {
            database.close();
        }
    }
}

