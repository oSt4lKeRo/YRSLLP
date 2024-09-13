package com.example.yrsllp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import JavaClass.Word;

public class DictionaryWordAdapter extends RecyclerView.Adapter<DictionaryWordAdapter.WordViewHolder> {

    public interface OnWordClickListener {
        void onWordClick(Word word, int position);
    }

    private final OnWordClickListener onClickListener;
    private final LayoutInflater inflater;
    private final List<Word> words;

    public DictionaryWordAdapter(Context context, List<Word> words, OnWordClickListener onClickListener) {
        this.inflater = LayoutInflater.from(context);
        this.words = words;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.dictionary_word_list_view, parent, false);
        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        Word word = words.get(holder.getAdapterPosition());
        holder.wordName.setText(word.getSpelling());

        holder.itemView.setOnClickListener(v -> onClickListener.onWordClick(word, holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    public static class WordViewHolder extends RecyclerView.ViewHolder {
        final TextView wordName;

        public WordViewHolder(View view) {
            super(view);
            wordName = view.findViewById(R.id.dictionary_word_list_text);
        }
    }
}
