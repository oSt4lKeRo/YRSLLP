package com.example.yrsllp;

import static android.content.Context.MODE_PRIVATE;
import static JavaClass.HttpHelper.getContent;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import JavaClass.DBHelper;
import JavaClass.HttpHelper;
import JavaClass.JsonHelper;
import JavaClass.ListObject;
import JavaClass.StepSharedViewModel;
import JavaClass.StructureClass;

public class StepListFragment extends Fragment {

    private ArrayList<StructureClass.Step> stepList;
    private RecyclerView stepListView;
    private StepSharedViewModel sharedViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            String lessonNumber = getArguments().getString("lessonsNumber");

            if (HttpHelper.isNetworkAvailable(getContext())) {
                new Thread(() -> {
                    try {
                        String str = getContent("http://51.250.97.205:1337/api/lectures/" + lessonNumber + "?populate=*");
                        Log.i("GetStepFromLecture " + lessonNumber, str);

                        try {
                            stepList = JsonHelper.readStepFromLectures(str);
                            Log.i("Steps", stepList.toString());
                        } catch (JSONException e) {
                            Log.e("JSONTroubleStep", e.getLocalizedMessage());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    new Handler(Looper.getMainLooper()).post(() -> {
                        createPage();
                    });

                }).start();
            } else {

                SQLiteDatabase db = getContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
                stepList = DBHelper.insertSteps(db, lessonNumber);

                createPage();
            }
        } else {
            Log.e("StepListFragment", "No arguments found");
        }
    }


    public void createPage(){
        ArrayList<ListObject> listObjects = generateListObject(stepList);

        ObjectListAdapter.OnObjectClickListener onObjectClickListener = position -> {
            sharedViewModel.setSteps(stepList);
            sharedViewModel.setActualPage(position);
            NavHostFragment.findNavController(StepListFragment.this)
                    .navigate(R.id.action_stepListFragment_to_stepContentFragment);
        };
        ObjectListAdapter adapter = new ObjectListAdapter(getContext(), listObjects, onObjectClickListener);
        stepListView.setAdapter(adapter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.object_list, container, false);

        stepListView = view.findViewById(R.id.object_list_rec);
        stepListView.setLayoutManager(new LinearLayoutManager(getContext()));

        sharedViewModel = new ViewModelProvider(requireActivity()).get(StepSharedViewModel.class);

        return view;
    }

    private ArrayList<ListObject> generateListObject(ArrayList<StructureClass.Step> steps) {
        ArrayList<ListObject> listObjects = new ArrayList<>();
        for (StructureClass.Step step : steps) {
            ListObject listObject = new ListObject(step.getAttributes().getTitle());
            listObjects.add(listObject);
        }
        return listObjects;
    }
}
