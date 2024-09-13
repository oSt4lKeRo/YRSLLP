package com.example.yrsllp;

import static JavaClass.HttpHelper.getContent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import JavaClass.Category;
import JavaClass.DBHelper;
import JavaClass.DictionarySharedViewModel;
import JavaClass.HttpHelper;
import JavaClass.JsonHelper;
import JavaClass.StructureClass;

public class DictionaryActivity extends NavigationActivity {

    private static final String TAG = "DictionaryActivity";
    private static final String CATEGORY_URL = "http://51.250.97.205:1337/api/categories?publicationState=preview&sort[0]=order";
    private DictionarySharedViewModel sharedViewModel;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_dictionary_word, findViewById(R.id.content_frame));

        sharedViewModel = new ViewModelProvider(this).get(DictionarySharedViewModel.class);
        executorService = Executors.newSingleThreadExecutor();

        if (HttpHelper.isNetworkAvailable(this)) {
            fetchCategories();
        } else {
            showToast("No network connection available.");
        }
    }

    private void fetchCategories() {
        executorService.execute(() -> {
            try {
                String response = getContent(CATEGORY_URL);
                Log.i(TAG, response);

                ArrayList<Category> categories = JsonHelper.readCategories(response);
                runOnUiThread(() -> sharedViewModel.setCategories(categories));

            } catch (IOException | JSONException e) {
                Log.e(TAG, "Error fetching categories", e);
                runOnUiThread(() -> showToast("Error fetching categories: " + e.getLocalizedMessage()));
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        NavController navController = navHostFragment.getNavController();

        if (!navController.navigateUp()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
