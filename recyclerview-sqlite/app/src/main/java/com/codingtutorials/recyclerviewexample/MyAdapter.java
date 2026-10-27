package com.codingtutorials.recyclerviewexample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/** Turns each User into a card in the list. */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    /** Called when the delete icon on a card is tapped. */
    public interface OnDeleteClick {
        void onDelete(User user, int position);
    }

    private final List<User> users;
    private final OnDeleteClick onDelete;

    public MyAdapter(List<User> users, OnDeleteClick onDelete) {
        this.users = users;
        this.onDelete = onDelete;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.userentry, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = users.get(position);
        holder.name_id.setText(user.name);
        holder.email_id.setText(user.email);
        holder.age_id.setText(holder.itemView.getContext().getString(R.string.age_value, user.age));
        holder.delete.setOnClickListener(v -> {
            int current = holder.getBindingAdapterPosition();
            if (current != RecyclerView.NO_POSITION) onDelete.onDelete(users.get(current), current);
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void removeAt(int position) {
        users.remove(position);
        notifyItemRemoved(position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name_id, email_id, age_id;
        ImageButton delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name_id = itemView.findViewById(R.id.textname);
            email_id = itemView.findViewById(R.id.textemail);
            age_id = itemView.findViewById(R.id.textage);
            delete = itemView.findViewById(R.id.btnDeleteRow);
        }
    }
}
