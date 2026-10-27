package com.codingtutorials.recyclerviewexample;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/** The form: add a user, delete one by name, or open the list. */
public class MainActivity extends AppCompatActivity {
    EditText name, email, age;
    Button insert, delete, view;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Screens.setUp(this, R.layout.activity_main);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        age = findViewById(R.id.age);
        insert = findViewById(R.id.btnInsert);
        delete = findViewById(R.id.btnDelete);
        view = findViewById(R.id.btnView);
        DB = new DBHelper(this);

        view.setOnClickListener(v -> startActivity(new Intent(this, Userlist.class)));

        insert.setOnClickListener(v -> {
            String nameTXT = name.getText().toString().trim();
            if (nameTXT.isEmpty()) {
                Toast.makeText(this, R.string.need_name, Toast.LENGTH_SHORT).show();
                return;
            }
            boolean added = DB.insertuserdata(nameTXT, email.getText().toString().trim(), age.getText().toString().trim());
            Toast.makeText(this, added ? R.string.inserted : R.string.not_inserted, Toast.LENGTH_SHORT).show();
            if (added) {
                name.setText("");
                email.setText("");
                age.setText("");
            }
        });

        delete.setOnClickListener(v -> {
            String nameTXT = name.getText().toString().trim();
            boolean removed = DB.deleteuserdata(nameTXT);
            Toast.makeText(this, removed ? getString(R.string.deleted, nameTXT) : getString(R.string.not_deleted), Toast.LENGTH_SHORT).show();
        });
    }
}
