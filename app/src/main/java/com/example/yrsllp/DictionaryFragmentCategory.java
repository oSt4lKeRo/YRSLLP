package com.example.yrsllp;

import static JavaClass.Category.getAllWord;
import static JavaClass.HttpHelper.getContent;
import static JavaClass.JsonHelper.readWordExampleResponse;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import JavaClass.Category;
import JavaClass.DictionarySharedViewModel;
import JavaClass.Word;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DictionaryFragmentCategory extends Fragment {

    private DictionarySharedViewModel sharedViewModel;
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> adapter;
    private NavController navController;
    private ArrayList<Word> words = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dictionary_fragment_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(DictionarySharedViewModel.class);
        RecyclerView recyclerView = view.findViewById(R.id.dictionary_category_list);

        sharedViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {

            words = getAllWord(categories);
            Log.i("DictionarySearchArray", words.toString());

            DictionaryCategoryAdapter.OnCategoryClickListener categoryClickListener = (category, position) -> {
                sharedViewModel.selectCategory(category);
                navController.navigate(R.id.action_dictionaryFragmentCategory_to_dictionaryFragmentWord);
            };

            DictionaryCategoryAdapter adapter = new DictionaryCategoryAdapter(requireContext(), categories, categoryClickListener);
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            recyclerView.setAdapter(adapter);
        });

        String[] searchWord = Word.getSpellingArray(words);
        Log.i("DictionarySearchArray", searchWord.toString());

        autoCompleteTextView = view.findViewById(R.id.dictionary_category_search);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, searchWord);
        autoCompleteTextView.setAdapter(adapter);

        autoCompleteTextView.setOnItemClickListener((parent, view1, position, id) -> {
            String selectedWordString = adapter.getItem(position);
            Word selectedWord = Word.findWordBySpelling(words, selectedWordString);
            if (selectedWord != null) {
                wordDetailsAndNavigate(selectedWord);
            }
        });

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString();
                str.toLowerCase();
                str.trim();
                fetchWordExample(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        navController = Navigation.findNavController(view);
    }

    private class FetchWordExampleTask extends AsyncTask<String, Void, String[]> {
        @Override
        protected String[] doInBackground(String... params) {
            String word = params[0];
            try {
                String str = getContent("http://51.250.97.205:1337/api/words?filters[spelling][$containsi]=" + word + "&pagination[pageSize]=10&pagination[page]=1");
                Log.i("DictionarySearch Response", str);
                words = readWordExampleResponse(str);
                Log.i("DictionarySearch Words", words.toString());
                return Word.getSpellingArray(words);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] finalWords) {
            if (finalWords != null) {
                Log.i("DictionarySearch UpdateAdapter", Arrays.toString(finalWords));
                adapter.clear();
                adapter.addAll(finalWords);
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void fetchWordExample(String word) {
        new FetchWordExampleTask().execute(word);
    }

    public void wordDetailsAndNavigate(Word word) {
        Bundle bundle = new Bundle();
        bundle.putString("word", word.getSpelling());
        bundle.putString("video_url", word.getVideoURL());
        navController.navigate(R.id.wordDetailFragment, bundle);
    }

    private String getContent(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

//    private ArrayList<Word> readWordExampleResponse(String response) throws JSONException {
//        ArrayList<Word> wordList = new ArrayList<>();
//        JSONObject jsonObject = new JSONObject(response);
//        JSONArray data = jsonObject.getJSONArray("data");
//
//        for (int i = 0; i < data.length(); i++) {
//            JSONObject wordObj = data.getJSONObject(i);
//            String spelling = wordObj.getString("spelling");
//            String videoURL = wordObj.getString("video_url");
//            wordList.add(new Word(spelling, videoURL));
//        }
//
//        return wordList;
//    }
}
