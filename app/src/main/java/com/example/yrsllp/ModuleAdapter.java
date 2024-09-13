package com.example.yrsllp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import JavaClass.StructureClass;

public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ViewHolder>{


    interface OnModuleClickListener{
        void onMoudleClick(StructureClass.Module module, int position);
    }

    private final OnModuleClickListener onClickListener;
    private final LayoutInflater inflater;
    private final List<StructureClass.Module> modules;

    ModuleAdapter(Context context, List<StructureClass.Module> modules, OnModuleClickListener onClickListener) {
        this.modules = modules;
        this.inflater = LayoutInflater.from(context);
        this.onClickListener = onClickListener;
    }
    @Override
    public ModuleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.aut_page, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ModuleAdapter.ViewHolder holder, int position) {
        position = holder.getAdapterPosition();
        StructureClass.Module module = modules.get(position);
        holder.moduleImage.setImageResource(module.getAttributes().getImageResource());
        holder.moduleName.setText(module.getAttributes().getTitle());
        holder.moduleProgress.setProgress(module.getAttributes().getProgressStatus());

        int finalPosition = position;
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                onClickListener.onMoudleClick(module, finalPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modules.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView moduleImage;
        final TextView moduleName;
        final ProgressBar moduleProgress;
        ViewHolder(View view){
            super(view);
            moduleImage = view.findViewById(R.id.module_image);
            moduleName = view.findViewById(R.id.module_title);
            moduleProgress = view.findViewById(R.id.module_progress);
        }
    }
}