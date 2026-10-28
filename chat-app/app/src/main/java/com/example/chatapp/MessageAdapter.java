package com.example.chatapp;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Shows messages as bubbles: yours on the right, theirs on the left.
 * Two view types let RecyclerView use a different layout for each.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.Holder> {
    private static final int SENT = 1;
    private static final int RECEIVED = 2;

    private final List<Message> messages = new ArrayList<>();
    private final String me;

    public MessageAdapter(String me) {
        this.me = me;
    }

    void add(Message message) {
        messages.add(message);
        notifyItemInserted(messages.size() - 1);
    }

    @Override
    public int getItemViewType(int position) {
        return me.equals(messages.get(position).sender) ? SENT : RECEIVED;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = viewType == SENT ? R.layout.item_message_sent : R.layout.item_message_received;
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Message message = messages.get(position);
        holder.text.setText(message.text);
        // Shows the time the way the phone is set up, such as 14:05 or 2:05 PM.
        holder.time.setText(DateFormat.getTimeFormat(holder.itemView.getContext()).format(new Date(message.time)));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        final TextView text, time;

        Holder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.textMessage);
            time = itemView.findViewById(R.id.textTime);
        }
    }
}
