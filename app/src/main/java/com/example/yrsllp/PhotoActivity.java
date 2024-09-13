package com.example.yrsllp;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

public class PhotoActivity extends NavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_photo, findViewById(R.id.content_frame));
    }

    @Override
    public void onBackPressed() {
        NavController navController = ((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment)).getNavController();
        if (navController.getCurrentDestination().getId() == R.id.photoHistoryFragment) {
            super.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }
}
