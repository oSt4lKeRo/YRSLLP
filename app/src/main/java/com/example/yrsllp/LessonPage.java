package com.example.yrsllp;

import static android.view.Gravity.CENTER;
import static JavaClass.HttpHelper.getContent;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import JavaClass.DBHelper;
import JavaClass.HttpHelper;
import JavaClass.JsonHelper;
import JavaClass.ListObject;
import JavaClass.StructureClass;

public class LessonPage extends AppCompatActivity implements LessonFragment.OnLessonFragmentSendDataListener {

    ListView countriesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.step_fragment_test);

        Toolbar toolbar = findViewById(R.id.list_toolbar);
        toolbar.setTitle("YRSLLP");

        Bundle arguments = getIntent().getExtras();
        String moduleNumber = arguments.getString("moduleNumber");

        if (moduleNumber == null) {
            Log.i("moduleNumber", "ПЗдц");
        } else {
            Log.i("moduleNumber", moduleNumber);
        }

        if (HttpHelper.isNetworkAvailable(this)) {

            LessonFragment lessonListFragment = new LessonFragment();
            Bundle bundle = new Bundle();
            bundle.putString("moduleNumber", moduleNumber);
            lessonListFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.listFragment, lessonListFragment)
                    .commit();


        } else {
//            Toast.makeText(this, "Нет интернета", Toast.LENGTH_SHORT).show();
//
////            linearLayout.setBackground(R.color.backModulePage);
//
//            TextView textView = new TextView(this);
//            textView.setText("Курс проходит: Вильгельм");
//            textView.setTextSize(24);
//            linearLayout.addView(textView);
//
//            Button restartButton = new Button(this);
//            restartButton.setText("Попробовать еще раз");
//            restartButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    recreate();
//                }
//            });
//            linearLayout.addView(restartButton);
//
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
            ArrayList<StructureClass.Lesson> lessons = DBHelper.insertLessons(db, moduleNumber);
            if(lessons.size() != 0){
                LessonFragment lessonListFragment = new LessonFragment();
                Bundle bundle = new Bundle();
                bundle.putString("moduleNumber", moduleNumber);
                lessonListFragment.setArguments(bundle);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.listFragment, lessonListFragment)
                        .commit();
            } else {
                createEmptyPage();
            }


//            Log.i("LessonSize", String.valueOf(lessons.size()));
//
//            if (lessons.size() != 0) {
//                createLessonPage(lessons);
//            } else {
//                setContentView(linearLayout);
//            }
//            for (StructureClass.Lesson lesson : lessons) {
//                Button button = createLessonButton(lesson);
//                linearLayout.addView(button);
//            }

        }
//        setContentView(linearLayout);
    }


    private void createEmptyPage(){
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(CENTER);

        TextView textView = new TextView(this);
        textView.setText("Нет подключения к интернету и сохраненных лекций данного модуля");
        textView.setTextSize(24);
        textView.setGravity(CENTER);
        layout.addView(textView);

        layout.addView(createRestartButton());
        setContentView(layout);
    }

    private Button createRestartButton(){
        Button button = new Button(this);
        button.setText("Перезагрузить страницу");
        button.setOnClickListener(v -> {
            recreate();
        });
        return button;
    }


    @Override
    public void onSendData(ArrayList<StructureClass.Lesson> lessons, int position) {

        Intent intent = new Intent(LessonPage.this, StepPage.class);
        StructureClass.Lesson lesson = lessons.get(position);
        Log.i("LessonsNumber", Integer.toString(lesson.getId()));
        intent.putExtra("lessonsNumber", String.valueOf(lesson.getId()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);

    }
    
    public ArrayList<ListObject> generateListObject(ArrayList<StructureClass.Lesson> lessons){
        ArrayList<ListObject> listObjects = new ArrayList<>();
        for(StructureClass.Lesson lesson : lessons){
            ListObject listObject = new ListObject(lesson.getAttributes().getTitle());
            listObjects.add(listObject);
        }
        return listObjects;
    }

}