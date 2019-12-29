package com.example.swapswop.Fragments;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.swapswop.R;
import com.example.swapswop.SigninActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * This class is the backend of the settings fragment.
 * @author Osama Tanveer
 * @version 1.2, 13 May 2019
 */
public class SettingsFragment extends Fragment {

    Button logoutButton;
    FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        logoutButton = view.findViewById(R.id.logoutButton);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth = auth.getInstance();
                auth.signOut();
                Intent intent = new Intent(getActivity(), SigninActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
