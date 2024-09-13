package com.example.yrsllp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import JavaClass.StepSharedViewModel;
import JavaClass.StructureClass;

public class StepContentFragment extends Fragment {

    private RecyclerView stepListView;
    private ArrayList<StructureClass.Step> steps;
    private int actualPage;
    private StepSharedViewModel sharedViewModel;
    private VideoView videoView;
    private TextView pageCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.step_content_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        stepListView = view.findViewById(R.id.step_recyclerView);
        stepListView.setLayoutManager(new LinearLayoutManager(getContext()));

        sharedViewModel = new ViewModelProvider(requireActivity()).get(StepSharedViewModel.class);

        sharedViewModel.getSteps().observe(getViewLifecycleOwner(), steps -> {
            this.steps = steps;
            sharedViewModel.getActualPage().observe(getViewLifecycleOwner(), actualPage -> {
                this.actualPage = actualPage;
                setSelectedItem(steps.get(actualPage).getAttributes().getContent());
            });
        });

        Button nextButton = view.findViewById(R.id.step_nextButton);
        Button backButton = view.findViewById(R.id.step_backButton);
        pageCount = view.findViewById(R.id.step_page_count);


        nextButton.setOnClickListener(v -> {
            if (actualPage < steps.size() - 1) {
                actualPage++;
                sharedViewModel.setActualPage(actualPage); // Обновляем actualPage в SharedViewModel
                setSelectedItem(steps.get(actualPage).getAttributes().getContent());
            }
        });

        backButton.setOnClickListener(v -> {
            if (actualPage > 0) {
                actualPage--;
                sharedViewModel.setActualPage(actualPage); // Обновляем actualPage в SharedViewModel
                setSelectedItem(steps.get(actualPage).getAttributes().getContent());
            }
        });
    }

    public void setSelectedItem(List<StructureClass.StepContent> selectedItem) {
        StepContentAdapter adapter = new StepContentAdapter(getContext(), selectedItem);
        stepListView.setAdapter(adapter);
        pageCount.setText((actualPage + 1) + " / " + steps.size());
    }
}
