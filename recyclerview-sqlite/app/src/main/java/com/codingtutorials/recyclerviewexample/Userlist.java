package com.codingtutorials.recyclerviewexample;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/** Shows every saved user in a scrolling list of cards. */
public class Userlist extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView empty;
    DBHelper DB;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Screens.setUp(this, R.layout.activity_userlist);

        DB = new DBHelper(this);
        recyclerView = findViewById(R.id.recyclerview);
        empty = findViewById(R.id.textEmpty);

        List<User> users = DB.getdata();
        adapter = new MyAdapter(users, (user, position) -> {
            if (DB.deleteuserdata(user.name)) {
                adapter.removeAt(position);
                Toast.makeText(this, getString(R.string.deleted, user.name), Toast.LENGTH_SHORT).show();
                showEmptyIfNeeded();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        showEmptyIfNeeded();
    }

    private void showEmptyIfNeeded() {
        boolean none = adapter.getItemCount() == 0;
        empty.setVisibility(none ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(none ? View.GONE : View.VISIBLE);
    }
}
