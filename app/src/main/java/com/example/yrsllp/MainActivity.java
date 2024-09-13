package com.example.yrsllp;

import static android.view.Gravity.CENTER;
import static JavaClass.HttpHelper.getContent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import JavaClass.DBHelper;
import JavaClass.HttpHelper;
import JavaClass.JsonHelper;
import JavaClass.ModuleToProfileSharedViewModel;
import JavaClass.StructureClass;

public class MainActivity extends NavigationActivity {
    public static final int[] progressArray = {0, 25, 50, 100};
    public static final int[] imageArray = {R.drawable.module1, R.drawable.module2, R.drawable.module3, R.drawable.module4};
    SharedPreferences sharedPreferences;
    ModuleToProfileSharedViewModel sharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_main, findViewById(R.id.content_frame));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        sharedViewModel = new ViewModelProvider(this).get(ModuleToProfileSharedViewModel.class);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        SQLiteDatabase db = initializeDatabase();
//        DBHelper.deleteDB(db);
        checkUserAuthentication(db);
        loadData(db);
    }

    private SQLiteDatabase initializeDatabase() {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        db.execSQL("PRAGMA foreign_keys=ON;");
        db.execSQL("CREATE TABLE IF NOT EXISTS modules (id INTEGER NOT NULL PRIMARY KEY, title TEXT, description TEXT, stepCount INTEGER, imagePath TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS lessons (id INTEGER NOT NULL, moduleNumber INTEGER NOT NULL, title TEXT, description TEXT, PRIMARY KEY (id, moduleNumber))");
        db.execSQL("CREATE TABLE IF NOT EXISTS steps (id INTEGER NOT NULL PRIMARY KEY, lectureNumber INTEGER NOT NULL, title TEXT, description TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS content (id INTEGER NOT NULL PRIMARY KEY, stepNumber INTEGER NOT NULL, type TEXT, content TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS user (id INTEGER NOT NULL PRIMARY KEY, name TEXT NOT NULL, password TEXT, userType TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS progress(userId INTEGER NOT NULL, stepID INTEGER NOT NULL, PRIMARY KEY (userId, stepID))");
        return db;
    }

    private void checkUserAuthentication(SQLiteDatabase db) {
        if (DBHelper.isDatabaseNameEmpty(db)) {
            navigateToActivity(AutActivity.class);
            return;
        }
        // Temporary condition to navigate to DictionaryActivity
//        if (true) {
//            navigateToActivity(DictionaryActivity.class);
//        }
    }

    private void loadData(SQLiteDatabase db) {
        if (HttpHelper.isNetworkAvailable(this)) {
            fetchModulesFromServer(db);
        } else {
            Toast.makeText(this, "Нет интернета", Toast.LENGTH_SHORT).show();
            Log.e("NoInternet", "Нет интернета");
            ArrayList<StructureClass.Module> modules = DBHelper.insertModule(db);
            createPage(modules);
        }
    }

    private void fetchModulesFromServer(SQLiteDatabase db) {
        new Thread(() -> {
            try {
                String response = getContent("http://51.250.97.205:1337/api/modules?publicationState=preview&populate=*");
                Log.i("GetModules", response);
                ArrayList<StructureClass.Module> modules = JsonHelper.readModule(response);

                if(sharedPreferences.getBoolean("save_module_info", false)) {
                    updateDatabaseWithModules(db, modules);
                }
                runOnUiThread(() -> createPage(modules));
            } catch (IOException | JSONException e) {
                Log.e("FetchModulesError", e.getLocalizedMessage());
                throw new RuntimeException(e);
            }
        }).start();
    }

    private void updateDatabaseWithModules(SQLiteDatabase db, ArrayList<StructureClass.Module> modules) {
        for (StructureClass.Module module : modules) {
            if ("RELEASE".equals(module.getAttributes().getStatus().getStatus()) && !DBHelper.checkModuleExistsById(db, module.getId())) {
                Log.i("moduleCheck", "Module " + module.getId() + " is valid");
                try {
                    DBHelper.saveModuleToDB(db, module);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Log.i("moduleCheck", "Module " + module.getId() + " is invalid or already exists");
            }
        }
    }

    private void createPage(ArrayList<StructureClass.Module> modules) {
        setInitialData(modules);
        sharedViewModel.setModules(modules);
        if(modules.size() != 0){
            setupModuleList(modules);
        } else {
            createEmptyPage();
        }
    }

    private void setupModuleList(ArrayList<StructureClass.Module> modules) {
        ModuleAdapter.OnModuleClickListener moduleClickListener = (module, position) -> {
            Intent intent = new Intent(MainActivity.this, LessonPage.class);
            intent.putExtra("moduleDescription", module.getAttributes().getDescription());
            intent.putExtra("moduleNumber", String.valueOf(module.getId()));
            startActivity(intent);
        };

        ModuleAdapter adapter = new ModuleAdapter(this, modules, moduleClickListener);
        RecyclerView moduleList = findViewById(R.id.moduleListView);
        moduleList.setLayoutManager(new LinearLayoutManager(this));
        moduleList.setAdapter(adapter);
    }

    private void createEmptyPage(){
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(CENTER);

        TextView textView = new TextView(this);
        textView.setText("Нет подключения к интернету и сохраненных модулей");
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

    private void setInitialData(ArrayList<StructureClass.Module> modules) {
        for (int i = 0; i < modules.size(); i++) {
            int number = i % progressArray.length;
            StructureClass.Module module = modules.get(i);
            module.getAttributes().setImageResource(imageArray[number]);
            module.getAttributes().setProgressStatus(progressArray[number]);
        }
    }

}
