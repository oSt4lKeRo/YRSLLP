package com.example.yrsllp;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import JavaClass.Word;

public class DictionarySearchAdapter extends ArrayAdapter<Word> implements Filterable {

    private List<Word> wordList;
    private List<Word> filteredWordList;

    public DictionarySearchAdapter(@NonNull Context context, @NonNull List<Word> objects) {
        super(context, android.R.layout.simple_dropdown_item_1line, objects);
        wordList = new ArrayList<>(objects);
        filteredWordList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return filteredWordList.size();
    }

    @Nullable
    @Override
    public Word getItem(int position) {
        return filteredWordList.get(position);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return wordFilter;
    }

    private Filter wordFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<Word> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(wordList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Word word : wordList) {
                    if (word.getSpelling().toLowerCase().contains(filterPattern)) {
                        suggestions.add(word);
                    }
                }
            }

            results.values = suggestions;
            results.count = suggestions.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredWordList.clear();
            if (results.values != null) {
                filteredWordList.addAll((List) results.values);
            }
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Word) resultValue).getSpelling();
        }
    };
}
