package com.example.yrsllp;

import static android.view.Gravity.CENTER;
import static JavaClass.HttpHelper.getContent;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import JavaClass.DBHelper;
import JavaClass.HttpHelper;
import JavaClass.JsonHelper;
import JavaClass.ListObject;
import JavaClass.StructureClass;

public class LessonFragment extends Fragment {

    interface OnLessonFragmentSendDataListener {
        void onSendData(ArrayList<StructureClass.Lesson> lessons, int position);
    }

    private OnLessonFragmentSendDataListener fragmentSendDataListener;
    private ArrayList<StructureClass.Lesson> lessons;
    private RecyclerView lessonsView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            fragmentSendDataListener = (OnLessonFragmentSendDataListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " должен реализовывать интерфейс OnStepFragmentInteractionListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.object_list, container, false);
        lessonsView = view.findViewById(R.id.object_list_rec);
        lessonsView.setLayoutManager(new LinearLayoutManager(getContext())); // Устанавливаем LayoutManager

        Bundle arguments = getArguments();
        if (arguments != null) {
            String moduleNumber = arguments.getString("moduleNumber");
            if (HttpHelper.isNetworkAvailable(getContext())) {

                new Thread(() -> {
                    try {
                        String str = getContent("http://51.250.97.205:1337/api/modules/" + moduleNumber + "?populate=*");
                        Log.i("GetLectureFromModule " + moduleNumber, str);

                        try {
                            lessons = JsonHelper.readLectures(str);
                            Log.i("Lecture", lessons.toString());
                        } catch (JSONException e) {
                            Log.e("JSONTroubleStep", e.getLocalizedMessage());
                        }

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    new Handler(Looper.getMainLooper()).post(() -> {
                        createPage();
                    });

                }).start();
            } else {

                Toast.makeText(getContext(), "Нет интернета", Toast.LENGTH_SHORT).show();
                Log.e("NoInternet", "Нет интернета");

                DBHelper dbHelper = new DBHelper(getContext());
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                lessons = DBHelper.insertLessons(db, moduleNumber);
                createPage();
            }
        }

        return view;
    }


    private void createPage(){
        ArrayList<ListObject> listObjects = generateListObject(lessons);
        ObjectListAdapter.OnObjectClickListener onObjectClickListener = position -> fragmentSendDataListener.onSendData(lessons, position);
        ObjectListAdapter adapter = new ObjectListAdapter(getContext(), listObjects, onObjectClickListener);
        lessonsView.setAdapter(adapter);
    }

    private ArrayList<ListObject> generateListObject(ArrayList<StructureClass.Lesson> lessons) {
        ArrayList<ListObject> listObjects = new ArrayList<>();
        for (StructureClass.Lesson lesson : lessons) {
            ListObject listObject = new ListObject(lesson.getAttributes().getTitle());
            listObjects.add(listObject);
        }
        return listObjects;
    }
}
