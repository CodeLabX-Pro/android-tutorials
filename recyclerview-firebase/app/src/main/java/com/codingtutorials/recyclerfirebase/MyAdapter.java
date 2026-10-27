package com.codingtutorials.recyclerfirebase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Shows users as cards. ListAdapter compares the old and new lists (DiffUtil), so when Firebase
 * sends fresh data only the changed cards animate, instead of the whole list redrawing.
 */
public class MyAdapter extends ListAdapter<User, MyAdapter.MyViewHolder> {

    public interface OnDeleteClick {
        void onDelete(User user);
    }

    private final OnDeleteClick onDelete;

    public MyAdapter(OnDeleteClick onDelete) {
        super(DIFF);
        this.onDelete = onDelete;
    }

    private static final DiffUtil.ItemCallback<User> DIFF = new DiffUtil.ItemCallback<User>() {
        @Override
        public boolean areItemsTheSame(@NonNull User a, @NonNull User b) {
            return a.key != null && a.key.equals(b.key);
        }

        @Override
        public boolean areContentsTheSame(@NonNull User a, @NonNull User b) {
            return String.valueOf(a.name).equals(b.name) && String.valueOf(a.email).equals(b.email) && String.valueOf(a.age).equals(b.age);
        }
    };

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.userentry, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = getItem(position);
        holder.name.setText(user.name);
        holder.email.setText(user.email);
        holder.age.setText(holder.itemView.getContext().getString(R.string.age_value, user.age));
        holder.delete.setOnClickListener(v -> onDelete.onDelete(user));
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, email, age;
        ImageButton delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textname);
            email = itemView.findViewById(R.id.textemail);
            age = itemView.findViewById(R.id.textage);
            delete = itemView.findViewById(R.id.btnDeleteRow);
        }
    }
}
