package com.example.yrsllp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

public class AuthFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.aut_fragment, container, false);

        view.findViewById(R.id.buttonSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToDB(v);
            }
        });

        view.findViewById(R.id.switchToRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AutActivity) getActivity()).switchToRegisterFragment();
            }
        });

        return view;
    }

    public void saveToDB(View view){
        EditText editText = getView().findViewById(R.id.editTextText);
        String enterText = String.valueOf(editText.getText());
        enterText = enterText.replaceAll(" ", "");
        if (!enterText.equals("Name") && !enterText.equals("")) {
            Log.i("UserName", enterText);
            SQLiteDatabase db = getActivity().openOrCreateDatabase("app.db", getActivity().MODE_PRIVATE, null);
            db.execSQL("INSERT OR REPLACE INTO user VALUES (?)", new String[]{enterText});
            db.close();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(getActivity(), "Введены некорректные данные", Toast.LENGTH_SHORT).show();
        }
    }
}