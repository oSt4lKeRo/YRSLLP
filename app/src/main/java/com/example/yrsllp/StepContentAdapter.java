package com.example.yrsllp;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import JavaClass.StepImageItem;
import JavaClass.StepTextItem;
import JavaClass.StepVideoItem;
import JavaClass.StructureClass;

public class StepContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_TEXT = 0;
    private static final int TYPE_IMAGE = 1;
    private static final int TYPE_VIDEO = 2;

    private final List<StructureClass.StepContent> contentList;
    private final Context context;

    public StepContentAdapter(Context context, List<StructureClass.StepContent> contentList) {
        this.contentList = contentList;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        StructureClass.StepContent content = contentList.get(position);
        if (content instanceof StepTextItem) {
            return TYPE_TEXT;
        } else if (content instanceof StepImageItem) {
            return TYPE_IMAGE;
        } else if (content instanceof StepVideoItem) {
            return TYPE_VIDEO;
        }
        return -1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == TYPE_IMAGE) {
            View view = inflater.inflate(R.layout.step_image, parent, false);
            return new ImageViewHolder(view);
        } else if (viewType == TYPE_VIDEO) {
            View view = inflater.inflate(R.layout.step_video, parent, false);
            return new VideoViewHolder(view);
        } else if (viewType == TYPE_TEXT) {
            Log.i("StepContentAdapter", "Creating TextViewHolder");
            View view = inflater.inflate(R.layout.step_text, parent, false);
            return new TextViewHolder(view);
        } else {
            throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        StructureClass.StepContent content = contentList.get(position);
        if (holder instanceof ImageViewHolder) {
            ((ImageViewHolder) holder).bind((StepImageItem) content);
        } else if (holder instanceof VideoViewHolder) {
            ((VideoViewHolder) holder).bind((StepVideoItem) content);
        } else if (holder instanceof TextViewHolder) {
            ((TextViewHolder) holder).bind((StepTextItem) content);
        } else {
            throw new IllegalArgumentException("Invalid view holder type");
        }
    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }

    static class TextViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        TextViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.stepContentText);
        }

        void bind(StepTextItem content) {
            textView.setText((String) content.getContent());
        }
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

        ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.stepContentImage);
        }

        void bind(StepImageItem content) {
            Glide.with(itemView.getContext())
                    .load((String) content.getContent())
                    .into(imageView);
        }
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        private final VideoView videoView;

        VideoViewHolder(View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.stepContentVideo);
        }

        void bind(StepVideoItem content) {
            Uri videoUri = Uri.parse((String) content.getContent());
            videoView.setVideoURI(videoUri);


            MediaController mediaController = new MediaController(itemView.getContext());
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);

            videoView.setOnPreparedListener(mediaPlayer -> {
                // Set MediaController to the correct position
                mediaController.setAnchorView(videoView);
                mediaController.setMediaPlayer(videoView);
                mediaController.setVisibility(View.VISIBLE);
            });


            videoView.requestFocus();
        }
    }
}
