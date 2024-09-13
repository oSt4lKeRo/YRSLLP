package com.example.yrsllp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import JavaClass.ListObject;

public class ObjectListAdapter extends RecyclerView.Adapter<ObjectListAdapter.ViewHolder>{
    interface OnObjectClickListener {
        void onObjectClick(int position);
    }

    private final OnObjectClickListener onClickListener;
    private final LayoutInflater inflater;
    private final List<ListObject> listObjects;

    ObjectListAdapter(Context context, List<ListObject> listObjects, OnObjectClickListener onClickListener) {
        this.inflater = LayoutInflater.from(context);
        this.onClickListener = onClickListener;
        this.listObjects = listObjects != null ? listObjects : Collections.emptyList();
    }

    @NonNull
    @Override
    public ObjectListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.object_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ObjectListAdapter.ViewHolder holder, int position) {
        ListObject listObject = listObjects.get(position);
        holder.objectName.setText(listObject.getName());

        holder.itemView.setOnClickListener(v -> onClickListener.onObjectClick(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return listObjects.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView objectName;

        ViewHolder(View view) {
            super(view);
            objectName = view.findViewById(R.id.objectTitle);
        }
    }
}
