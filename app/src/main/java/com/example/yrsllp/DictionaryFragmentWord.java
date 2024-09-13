package com.example.yrsllp;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.ArrayList;

import JavaClass.DictionarySharedViewModel;
import JavaClass.Word;

public class DictionaryFragmentWord extends Fragment {
    private DictionarySharedViewModel sharedViewModel;
    private PlayerView playerView;
    private ExoPlayer player;
    private int currentWordIndex = 0;

    private TextView categoryTextView;
    private TextView backButton;
    private TextView nextButton;
    private RecyclerView recyclerView;
    private VideoView videoView;
    private ProgressBar progressBar;
    private ArrayList<Word> words;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dictionary_fragment_word, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(DictionarySharedViewModel.class);
        categoryTextView = view.findViewById(R.id.dictionary_word_text);
        backButton = view.findViewById(R.id.dictionary_word_back_button);
        nextButton = view.findViewById(R.id.dictionary_word_next_button);
        recyclerView = view.findViewById(R.id.dictionary_word_list);
        videoView = view.findViewById(R.id.dictionary_word_video);
        progressBar = view.findViewById(R.id.dictionary_word_video_progress_bar);

        setupMediaController();
        setupObservers();
        setupButtons();
    }

    private void setupMediaController() {
        MediaController mediaController = new MediaController(getContext());
        mediaController.setAnchorView(videoView);
        mediaController.setVisibility(View.GONE);
        videoView.setMediaController(mediaController);
    }

    private void setupObservers() {
        sharedViewModel.getSelectedCategory().observe(getViewLifecycleOwner(), category -> {
            if (category != null) {
                categoryTextView.setText(category.getTitle());
                words = (ArrayList<Word>) category.getWords();

                DictionaryWordAdapter.OnWordClickListener onWordClickListener = (word, position) -> {
                    if(position != currentWordIndex) {
                        currentWordIndex = position;
                        updatePage();
                    }
                };

                DictionaryWordAdapter adapter = new DictionaryWordAdapter(requireContext(), words, onWordClickListener);
                recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                recyclerView.setAdapter(adapter);
                updatePage();
            }
        });
    }

    private void setupButtons() {
        backButton.setOnClickListener(v -> previousWord());
        nextButton.setOnClickListener(v -> nextWord());
    }

    private void updatePage() {
        if (currentWordIndex > 0) {
            backButton.setVisibility(View.VISIBLE);
            backButton.setText(words.get(currentWordIndex - 1).getSpelling());
        } else {
            backButton.setVisibility(View.INVISIBLE);
        }

        if (currentWordIndex < words.size() - 1) {
            nextButton.setVisibility(View.VISIBLE);
            nextButton.setText(words.get(currentWordIndex + 1).getSpelling());
        } else {
            nextButton.setVisibility(View.INVISIBLE);
        }

        Uri videoUri = Uri.parse(words.get(currentWordIndex).getVideoURL());
        videoView.setVideoURI(videoUri);
        videoView.requestFocus();
        videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // Hide the progress bar when the video is ready to play
                progressBar.setVisibility(View.GONE);
                videoView.start();
            }
        });

        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                    // Show the progress bar when buffering starts
                    progressBar.setVisibility(View.VISIBLE);
                } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                    // Hide the progress bar when buffering ends
                    progressBar.setVisibility(View.GONE);
                }
                return false;
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // Hide the progress bar when the video is completed
                progressBar.setVisibility(View.GONE);
            }
        });

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                // Hide the progress bar in case of an error
                progressBar.setVisibility(View.GONE);
                // Handle the error
                return true;
            }
        });

        // Show the progress bar while the video is loading
        progressBar.setVisibility(View.VISIBLE);

    }

    private void previousWord() {
        if (currentWordIndex > 0) {
            currentWordIndex--;
            updatePage();
        }
    }

    private void nextWord() {
        if (currentWordIndex < words.size() - 1) {
            currentWordIndex++;
            updatePage();
        }
    }
}
