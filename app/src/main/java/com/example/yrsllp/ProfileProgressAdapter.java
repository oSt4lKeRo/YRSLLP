package com.example.yrsllp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import JavaClass.CircularProgressBar;
import JavaClass.StructureClass;

public class ProfileProgressAdapter extends RecyclerView.Adapter<ProfileProgressAdapter.ViewHolder>{
    private final LayoutInflater inflater;
    private final List<StructureClass.Module> modules;

    ProfileProgressAdapter(Context context, List<StructureClass.Module> modules) {
        this.modules = modules;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ProfileProgressAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.profile_progress_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProfileProgressAdapter.ViewHolder holder, int position) {
        position = holder.getAdapterPosition();
        StructureClass.Module module = modules.get(position);
        //Заполнение внутренностей
        holder.progressModuleName.setText(module.getAttributes().getTitle());
        holder.progressBar.setProgress(module.getAttributes().getProgressStatus());

        int finalPosition = position;

    }

    @Override
    public int getItemCount() {
        return modules.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final CircularProgressBar progressBar;
        final TextView progressModuleName;
        ViewHolder(View view){
            super(view);
            progressBar = view.findViewById(R.id.profile_progress_circular);
            progressModuleName = view.findViewById(R.id.profile_progress_module_name);
        }
    }
}