package com.example.yrsllp;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yrsllp.PhotoHistoryAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import JavaClass.PhotoHistoryItem;

public class PhotoHistoryFragment extends Fragment {
    private PhotoHistoryAdapter adapter;
    private List<PhotoHistoryItem> photoHistoryList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.photo_history_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.photo_history_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        photoHistoryList = loadPhotoHistory();
        adapter = new PhotoHistoryAdapter(getContext(), photoHistoryList);
        recyclerView.setAdapter(adapter);

        Button deleteAllButton = view.findViewById(R.id.photo_history_deleteAllButton);
        deleteAllButton.setOnClickListener(v -> deleteAllPhotos());

        Button deleteSelectedButton = view.findViewById(R.id.photo_history_deleteSelectedButton);
        deleteSelectedButton.setOnClickListener(v -> deleteSelectedPhotos());
    }

    private List<PhotoHistoryItem> loadPhotoHistory() {
        List<PhotoHistoryItem> list = new ArrayList<>();
        File picturesDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (picturesDir != null && picturesDir.exists()) {
            for (File file : picturesDir.listFiles()) {
                if (file.getName().endsWith(".jpg")) {
                    File responseFile = new File(file.getParent(), file.getName().replace(".jpg", ".txt"));
                    String response = null;
                    if (responseFile.exists()) {
                        response = readFile(responseFile);
                    }
                    list.add(new PhotoHistoryItem(file.getAbsolutePath(), response));
                }
            }
        }
        return list;
    }

    private void deleteAllPhotos() {
        File picturesDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (picturesDir != null && picturesDir.exists()) {
            for (File file : picturesDir.listFiles()) {
                file.delete();
            }
        }
        photoHistoryList.clear();
        adapter.notifyDataSetChanged();
        Toast.makeText(getActivity(), "All photos deleted", Toast.LENGTH_SHORT).show();
    }

    private void deleteSelectedPhotos() {
        List<PhotoHistoryItem> selectedItems = adapter.getSelectedItems();
        for (PhotoHistoryItem item : selectedItems) {
            File file = new File(item.getPhotoPath());
            if (file.exists()) {
                file.delete();
            }
            File responseFile = new File(file.getParent(), file.getName().replace(".jpg", ".txt"));
            if (responseFile.exists()) {
                responseFile.delete();
            }
            photoHistoryList.remove(item);
        }
        adapter.notifyDataSetChanged();
        Toast.makeText(getActivity(), "Selected photos deleted", Toast.LENGTH_SHORT).show();
    }

    public static String readFile(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            return new String(data);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
