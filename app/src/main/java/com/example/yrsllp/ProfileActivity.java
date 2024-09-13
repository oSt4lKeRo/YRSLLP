package com.example.yrsllp;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import JavaClass.CircularProgressBar;
import JavaClass.DBHelper;
import JavaClass.ModuleToProfileSharedViewModel;
import JavaClass.StructureClass;

public class ProfileActivity extends NavigationActivity {
    private TextView profileNameView;
    private CircularProgressBar circularProgressBar;
    private TextView moduleName;
    private ModuleToProfileSharedViewModel sharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_profil, findViewById(R.id.content_frame));

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);

        profileNameView = findViewById(R.id.profile_user_name);
        profileNameView.setText(DBHelper.getNameFromDB(db));

        sharedViewModel = new ViewModelProvider(this).get(ModuleToProfileSharedViewModel.class);
        sharedViewModel.getModules().observe(this, modules -> {
            Log.i("ModuleToProfileShared", "Observer triggered");
            if (modules != null) {
                Log.i("ModuleToProfileShared", modules.toString());
                ProfileProgressAdapter profileProgressAdapter = new ProfileProgressAdapter(this, modules);
                RecyclerView recyclerView = findViewById(R.id.profile_progress_list);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(profileProgressAdapter);
            }
        });


    }

    public void logout(View view){
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        DBHelper.clearUserTable(db);
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(intent);
    }
}