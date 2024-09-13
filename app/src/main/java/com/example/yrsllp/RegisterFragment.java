package com.example.yrsllp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

public class RegisterFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.reg_fragment, container, false);

        view.findViewById(R.id.switchToAuth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AutActivity) getActivity()).switchToAuthFragment();
            }
        });

        return view;
    }
}