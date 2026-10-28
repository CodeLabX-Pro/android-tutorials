package com.example.chatapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

/** One row per person: a round initial, the name and the email. */
public class UserAdapter extends ListAdapter<User, UserAdapter.Holder> {
    public interface OnUserClick {
        void onClick(User user);
    }

    private final OnUserClick onClick;

    public UserAdapter(OnUserClick onClick) {
        super(DIFF);
        this.onClick = onClick;
    }

    private static final DiffUtil.ItemCallback<User> DIFF = new DiffUtil.ItemCallback<User>() {
        @Override
        public boolean areItemsTheSame(@NonNull User a, @NonNull User b) {
            return Objects.equals(a.uid, b.uid);
        }

        @Override
        public boolean areContentsTheSame(@NonNull User a, @NonNull User b) {
            return Objects.equals(a.name, b.name) && Objects.equals(a.email, b.email);
        }
    };

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        User user = getItem(position);
        holder.avatar.setText(user.initial());
        holder.name.setText(user.name);
        holder.email.setText(user.email);
        holder.itemView.setOnClickListener(v -> onClick.onClick(user));
    }

    static class Holder extends RecyclerView.ViewHolder {
        final TextView avatar, name, email;

        Holder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.textAvatar);
            name = itemView.findViewById(R.id.textName);
            email = itemView.findViewById(R.id.textEmail);
        }
    }
}
