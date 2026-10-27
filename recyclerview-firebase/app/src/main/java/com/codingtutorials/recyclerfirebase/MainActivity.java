package com.codingtutorials.recyclerfirebase;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A live list of users from Firebase Realtime Database. Add a user (here or in the Firebase console)
 * and every phone running the app sees it appear instantly.
 */
public class MainActivity extends AppCompatActivity {
    private final DatabaseReference users = FirebaseDatabase.getInstance().getReference("users");
    private ValueEventListener listener;
    private MyAdapter adapter;
    TextView empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Screens.setUp(this, R.layout.activity_main);

        EditText name = findViewById(R.id.name);
        EditText email = findViewById(R.id.email);
        EditText age = findViewById(R.id.age);
        Button add = findViewById(R.id.btnAdd);
        empty = findViewById(R.id.textEmpty);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        adapter = new MyAdapter(user -> users.child(user.key).removeValue());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        add.setOnClickListener(v -> {
            String nameTXT = name.getText().toString().trim();
            if (nameTXT.isEmpty()) {
                name.setError(getString(R.string.need_name));
                return;
            }
            // push() creates a new unique key, so two users can share a name.
            users.push().setValue(new User(nameTXT, email.getText().toString().trim(), age.getText().toString().trim()));
            name.setText("");
            email.setText("");
            age.setText("");
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Listen only while the screen is visible, and stop in onStop to save battery and data.
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<User> list = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    User user = child.getValue(User.class);
                    if (user != null) {
                        user.key = child.getKey();
                        list.add(user);
                    }
                }
                adapter.submitList(list);
                empty.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, R.string.error_read, Toast.LENGTH_LONG).show();
            }
        };
        users.addValueEventListener(listener);
    }

    @Override
    protected void onStop() {
        users.removeEventListener(listener);
        super.onStop();
    }
}
