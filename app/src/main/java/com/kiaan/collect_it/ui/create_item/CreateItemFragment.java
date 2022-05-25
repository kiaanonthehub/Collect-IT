package com.kiaan.collect_it.ui.create_item;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kiaan.collect_it.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Model.CURRENT_USER;
import Model.Item;
import Model.dbHandler;


public class CreateItemFragment extends Fragment {

    // declare java components
    TextView mDisplayDate;
    DatePickerDialog.OnDateSetListener mDateSetListener;
    // declare variables
    String name, desc, aquiDate, cat, uri;
    // instantiate dbHandler object
    dbHandler db = new dbHandler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_item, parent, false);

        // initialise java components
        // declare java components
        EditText etItmName = (EditText) view.findViewById(R.id.editTextItemName);
        EditText etItmDesc = (EditText) view.findViewById(R.id.editTextItemDescription);
        Button btnCreateItm = (Button) view.findViewById(R.id.buttonCreateItem);

        // get database reference
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference categoryRef = rootRef.child("User").child(CURRENT_USER.displayName);

        // set listener to retrieve data from realtime database
        categoryRef.child("Category").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

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

                // declare and initialise java component
                Spinner spinner = (Spinner) view.findViewById(R.id.spinner);

                // declare and initialise Array Adapter to bind values to the spinner in real time
                ArrayAdapter<String> addressAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, list);
                addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(addressAdapter);

                // get the selected value from the spinner
                cat = spinner.getSelectedItem().toString();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // initialise java components
        mDisplayDate = view.findViewById(R.id.textViewDateAquisition);

        // set onclick listener
        mDisplayDate.setOnClickListener(view1 -> {

            // get calendar instance
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            // instantiate DatePickerDialog object
            DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });

        mDateSetListener = (datePicker, y, month, d) -> {

            month = month + 1;
            String date = d + "/" + month + "/" + y;
            aquiDate = date;
            // set view
            mDisplayDate.setText(date);

        };

        // set button onclick
        btnCreateItm.setOnClickListener(view12 -> {

            // initialise variables and get users input
            name = etItmName.getText().toString();
            desc = etItmDesc.getText().toString();
            uri = "test";

            // instantiate Item object
            Item i = new Item(name, desc, cat, aquiDate, uri);

            // write object to firebase
            db.writeToFirebase("User", CURRENT_USER.displayName, "Category", i.getCategory(), "Item", i.getName(), i);
            Toast.makeText(getContext(), "Item successfully added to collection", Toast.LENGTH_SHORT).show();
        });


        return view;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
    }


}

/*
Code Attribution
Author : Levi Moreira
Subject : How can I populate a Spinner with Firebase data?
Code available : https://stackoverflow.com/questions/49053155/how-can-i-populate-a-spinner-with-firebase-data [answered Mar 1, 2018 at 16:42]
Date accessed : [25/05/2022]

Author : CodingWithMitch
Subject : Android Beginner Tutorial #25 - DatePicker Dialog [Choosing a Date from a Dialog Pop-Up]
Code available : https://www.youtube.com/watch?v=hwe1abDO2Ag [Apr 6, 2017]
Date accessed : [25/05/2022]
 */

