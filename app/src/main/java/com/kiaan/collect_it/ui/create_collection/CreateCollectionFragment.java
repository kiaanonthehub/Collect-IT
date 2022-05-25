package com.kiaan.collect_it.ui.create_collection;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.kiaan.collect_it.R;


import Model.CURRENT_USER;
import Model.Category;
import Model.dbHandler;

public class CreateCollectionFragment extends Fragment {

    // declare java variables
    private EditText etCatName, etCatDesc, etCatGoal;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        // instantiate view object
        View view = inflater.inflate(R.layout.fragment_create_collection, parent, false);

        // instantiate dbHandler object
        dbHandler db = new dbHandler();

        // initialise java components
        etCatName = (EditText) view.findViewById(R.id.editTextCollectionName);
        etCatDesc = (EditText) view.findViewById(R.id.editTextCollectionDescription);
        etCatGoal = (EditText) view.findViewById(R.id.editTextCollectionGoal);
        Button btnCreateCategory = (Button) view.findViewById(R.id.buttonCreateCollection);

        // set button onclick
        btnCreateCategory.setOnClickListener(view1 -> {


            // Declare variables
            String name, desc, goal;

            // initialise variables
            name = etCatName.getText().toString();
            desc = etCatDesc.getText().toString();
            goal = etCatGoal.getText().toString().trim();

            // instantiate category object
            Category c = new Category(name, desc, goal);

            // write category to real time database for current user
            db.writeToFirebase("User", CURRENT_USER.displayName, "Category", name, c);
            Log.d("onCreateView: display name", CURRENT_USER.displayName);
            Toast.makeText(getContext(), "Category : " + c.getName() + " has been saved.", Toast.LENGTH_SHORT).show();

            // refresh ui
            refreshUI();


        });


        // Defines the xml file for the fragment
        return view;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    public void refreshUI() {
        // clear the ui components
        etCatName.getText().clear();
        etCatDesc.getText().clear();
        etCatGoal.getText().clear();
    }
}
