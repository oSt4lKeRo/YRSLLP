package com.example.yrsllp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.yrsllp.R;

import java.util.ArrayList;
import java.util.List;

import JavaClass.PhotoHistoryItem;

public class PhotoHistoryAdapter extends RecyclerView.Adapter<PhotoHistoryAdapter.ViewHolder> {
    private Context context;
    private List<PhotoHistoryItem> photoHistoryList;
    private List<PhotoHistoryItem> selectedItems = new ArrayList<>();

    public PhotoHistoryAdapter(Context context, List<PhotoHistoryItem> photoHistoryList) {
        this.context = context;
        this.photoHistoryList = photoHistoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.photo_history_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PhotoHistoryItem item = photoHistoryList.get(position);
        Glide.with(context).load(item.getPhotoPath()).into(holder.photoImageView);
        holder.photoTextView.setText(item.getResponse());
        holder.photoCheckbox.setChecked(selectedItems.contains(item));

        holder.photoCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedItems.add(item);
            } else {
                selectedItems.remove(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return photoHistoryList.size();
    }

    public List<PhotoHistoryItem> getSelectedItems() {
        return selectedItems;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView photoImageView;
        TextView photoTextView;
        CheckBox photoCheckbox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            photoImageView = itemView.findViewById(R.id.photoImageView);
            photoTextView = itemView.findViewById(R.id.photoTextView);
            photoCheckbox = itemView.findViewById(R.id.photoCheckbox);
        }
    }
}
