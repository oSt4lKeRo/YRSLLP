package com.example.yrsllp;

import static JavaClass.HttpHelper.getContent;
import static JavaClass.JsonHelper.readWordExampleResponse;

import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import JavaClass.Category;
import JavaClass.DictionarySharedViewModel;
import JavaClass.Word;

public class DictionarySearchFragment extends Fragment {

    private static final String ARG_WORD = "word";
    private static final String ARG_VIDEO_URL = "video_url";

    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> adapter;

    private String word;
    private String videoUrl;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            word = getArguments().getString(ARG_WORD);
            videoUrl = getArguments().getString(ARG_VIDEO_URL);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dictionary_fragment_search, container, false);

        TextView wordTextView = view.findViewById(R.id.dictionary_search_text);
        VideoView videoView = view.findViewById(R.id.dictionary_search_video);

        wordTextView.setText(word);
        Uri videoUri = Uri.parse(videoUrl);
        videoView.setVideoURI(videoUri);
        videoView.requestFocus();
        videoView.start();

        return view;
    }
}
