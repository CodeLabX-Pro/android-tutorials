package com.codingtutorials.imagesqlite;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/** Shows saved images in a grid. BitmapFactory turns the stored bytes back into a picture. */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.Holder> {
    public interface OnDelete {
        void onDelete(SavedImage image);
    }

    private final List<SavedImage> images = new ArrayList<>();
    private final OnDelete onDelete;

    public ImageAdapter(OnDelete onDelete) {
        this.onDelete = onDelete;
    }

    public void setImages(List<SavedImage> newImages) {
        int old = images.size();
        images.clear();
        notifyItemRangeRemoved(0, old);
        images.addAll(newImages);
        notifyItemRangeInserted(0, images.size());
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        SavedImage image = images.get(position);
        holder.photo.setImageBitmap(BitmapFactory.decodeByteArray(image.bytes, 0, image.bytes.length));
        holder.name.setText(image.name);
        holder.delete.setOnClickListener(v -> onDelete.onDelete(image));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        final ImageView photo;
        final TextView name;
        final ImageButton delete;

        Holder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.photo);
            name = itemView.findViewById(R.id.name);
            delete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
