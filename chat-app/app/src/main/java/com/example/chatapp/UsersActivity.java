package com.example.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/** Everyone else who has an account. Tap a person to chat with them. */
public class UsersActivity extends AppCompatActivity {
    private final DatabaseReference users = FirebaseDatabase.getInstance().getReference("users");
    private ValueEventListener listener;
    private UserAdapter adapter;
    private View empty;
    private String me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        me = FirebaseAuth.getInstance().getUid();
        if (me == null) {
            openLogin();
            return;
        }
        Screens.setUp(this, R.layout.activity_users);
        empty = findViewById(R.id.textEmpty);

        adapter = new UserAdapter(user -> startActivity(ChatActivity.intent(this, user)));
        RecyclerView list = findViewById(R.id.recyclerUsers);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            openLogin();
        });
    }

    /** Listen while the screen is visible. Firebase calls onDataChange now, and again whenever anyone joins. */
    @Override
    protected void onStart() {
        super.onStart();
        if (me == null) return;
        listener = users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<User> people = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    User user = child.getValue(User.class);
                    if (user == null || me.equals(child.getKey())) continue;
                    user.uid = child.getKey();
                    people.add(user);
                }
                adapter.submitList(people);
                empty.setVisibility(people.isEmpty() ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UsersActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (listener != null) users.removeEventListener(listener);
    }

    private void openLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
