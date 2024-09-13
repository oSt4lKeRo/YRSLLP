package com.example.yrsllp;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;
import android.widget.Toast;

import JavaClass.DBHelper;

public class SettingFragment extends PreferenceFragmentCompat {

    private DBHelper dbHelper;
    private SQLiteDatabase db;



    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        dbHelper = new DBHelper(getContext());
        db = dbHelper.getWritableDatabase();

        // Preference for clearing database
        Preference clearModuleDatabasePreference = findPreference("clear_module_info");
        if (clearModuleDatabasePreference != null) {
            clearModuleDatabasePreference.setOnPreferenceClickListener(preference -> {
                DBHelper.clearModuleInfo(db);
                Toast.makeText(getContext(), "Информация о модулях очищена", Toast.LENGTH_SHORT).show();
                return true;
            });
        }

        Preference clearPhotoDatabasePreference = findPreference("clear_photo");
        if(clearPhotoDatabasePreference != null){
            clearPhotoDatabasePreference.setOnPreferenceClickListener(preference -> {

                return true;
            });
        }

        // Preference for changing font size
//        Preference fontSizePreference = findPreference("font_size");
//        if (fontSizePreference != null) {
//            fontSizePreference.setOnPreferenceChangeListener((preference, newValue) -> {
//                // Handle font size change
//                // You may need to refresh the UI or notify relevant parts of your app
//                Toast.makeText(getContext(), "Font size changed to: " + newValue, Toast.LENGTH_SHORT).show();
//                return true;
//            });
//        }


    }
}
