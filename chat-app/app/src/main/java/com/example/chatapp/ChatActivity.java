package com.example.chatapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/** A conversation between you and one other person. New messages appear instantly on both phones. */
public class ChatActivity extends AppCompatActivity {
    private DatabaseReference messages;
    private ChildEventListener listener;

    static Intent intent(Context context, User user) {
        return new Intent(context, ChatActivity.class)
                .putExtra("uid", user.uid)
                .putExtra("name", user.name);
    }

    /** Both people must open the same chat, so the id is the two uids in alphabetical order. */
    static String chatId(String a, String b) {
        return a.compareTo(b) < 0 ? a + "_" + b : b + "_" + a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Screens.setUp(this, R.layout.activity_chat);
        String me = Objects.requireNonNull(FirebaseAuth.getInstance().getUid());
        String other = Objects.requireNonNull(getIntent().getStringExtra("uid"));
        User person = new User(getIntent().getStringExtra("name"), null);

        ((TextView) findViewById(R.id.textName)).setText(person.name);
        ((TextView) findViewById(R.id.textAvatar)).setText(person.initial());
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        MessageAdapter adapter = new MessageAdapter(me);
        RecyclerView list = findViewById(R.id.recyclerMessages);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true); // Start at the newest message, like every chat app.
        list.setLayoutManager(layoutManager);
        list.setAdapter(adapter);

        messages = FirebaseDatabase.getInstance().getReference("chats").child(chatId(me, other)).child("messages");

        EditText input = findViewById(R.id.inputMessage);
        findViewById(R.id.btnSend).setOnClickListener(v -> {
            String text = input.getText().toString().trim();
            if (text.isEmpty()) return;
            Map<String, Object> message = new HashMap<>();
            message.put("sender", me);
            message.put("text", text);
            message.put("time", ServerValue.TIMESTAMP);
            // push() makes a new key that sorts by time, so messages stay in order.
            messages.push().setValue(message);
            input.setText("");
        });

        // onChildAdded runs once for every existing message, then again for each new one.
        listener = messages.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message message = snapshot.getValue(Message.class);
                if (message == null) return;
                adapter.add(message);
                list.scrollToPosition(adapter.getItemCount() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        messages.removeEventListener(listener);
    }
}
