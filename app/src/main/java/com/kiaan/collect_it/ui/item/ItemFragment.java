package com.kiaan.collect_it.ui.item;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kiaan.collect_it.R;
import com.kiaan.collect_it.ui.FilterItemsActivity;
import com.kiaan.collect_it.ui.UpdateCollectionActivity;

import java.util.ArrayList;
import java.util.List;

import Model.CURRENT_USER;
import Model.Item;

public class ItemFragment extends Fragment {

    // declare java variables
    Spinner spnFilterItems;
    Button btnViewFilterItems;
    Button btnViewCat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_item, parent, false);

        // initialise java components
        spnFilterItems = view.findViewById(R.id.spinnerFilterItems);
        btnViewFilterItems = view.findViewById(R.id.buttonViewItems);
        btnViewCat = view.findViewById(R.id.buttonViewCategory);

        //  disable the button
        btnViewFilterItems.setEnabled(false);

        // initialise static value
        CURRENT_USER.filterCategory = null;

        // populate the spinner box
        // get database reference
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference categoryRef = rootRef.child("User").child(CURRENT_USER.displayName);

        // set listener to retrieve data from realtime database
        categoryRef.child("Category").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // declare temp arraylist
                final List<String> list = new ArrayList<>();

                // retrieve data from real time database
                for (DataSnapshot addressSnapshot : dataSnapshot.getChildren()) {
                    String value = addressSnapshot.getKey();
                    if (value != null) {
                        // add value to the list
                        list.add(value);
                    }

                }
                // initialise java component
                spnFilterItems = (Spinner) view.findViewById(R.id.spinnerFilterItems);

                // declare and initialise Array Adapter to bind values to the spinner in real time
                ArrayAdapter<String> addressAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, list);
                addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnFilterItems.setAdapter(addressAdapter);

                // check if the list is empty
                if (!list.isEmpty()) {

                    // enable button
                    btnViewFilterItems.setEnabled(true);

                    // button onclick method
                    btnViewFilterItems.setOnClickListener(view1 -> {

                        // get the selected value from the spinner
                        CURRENT_USER.filterCategory = spnFilterItems.getSelectedItem().toString();

                        // navigate to the new activity
                        Intent intent = new Intent(getActivity(), FilterItemsActivity.class);
                        startActivity(intent);

                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnViewCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get the selected value from the spinner
                CURRENT_USER.filterCategory = spnFilterItems.getSelectedItem().toString();

                Intent intent = new Intent(getActivity(), UpdateCollectionActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
    }
}
