package com.kiaan.collect_it.ui.logout;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.kiaan.collect_it.R;
import com.kiaan.collect_it.ui.LoginActivity;
import com.kiaan.collect_it.ui.NavigationActivity;

public class LogoutFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        // instantiate alert dialog object
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // set properties
        builder.setTitle("Logout");
        builder.setMessage("Are you sure?");

        // if user selects yes
        builder.setPositiveButton("YES", (dialog, which) -> {

            // instantiate intent object to navigate to the sign in screen
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);

            // close dialog
            dialog.dismiss();
        });

        // if user selects no
        builder.setNegativeButton("NO", (dialog, which) -> {

            // instantiate intent object to navigate to the navigation home screen
            Intent intent = new Intent(getActivity(), NavigationActivity.class);
            startActivity(intent);

            // close dialog
            dialog.dismiss();
        });

        // create and display the dialog
        AlertDialog alert = builder.create();
        alert.show();

        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_home, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }
}
/*
Code Attribution
Author : nikki
Subject : How to display a Yes/No dialog box on Android?
Code available : https://stackoverflow.com/questions/2478517/how-to-display-a-yes-no-dialog-box-on-android [answered Aug 31, 2012 at 10:29]
Date accessed : [23/05/2022]

Author : Michael Celey
Subject : Start an activity from a fragment
Code available : https://stackoverflow.com/questions/15478105/start-an-activity-from-a-fragment [answered Mar 18, 2013 at 13:41]
Date accessed : [23/05/2022]
 */