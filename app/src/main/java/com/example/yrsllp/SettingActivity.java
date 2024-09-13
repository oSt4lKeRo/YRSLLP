package com.example.yrsllp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class SettingActivity extends NavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_setting, findViewById(R.id.content_frame));
        SettingFragment settingFragment = new SettingFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_setting_container, settingFragment)
                .commit();
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.fragment_setting_container, new SettingFragment());
//        transaction.commit();
    }
}