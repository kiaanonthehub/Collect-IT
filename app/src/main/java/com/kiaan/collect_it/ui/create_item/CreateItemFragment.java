package com.kiaan.collect_it.ui.create_item;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kiaan.collect_it.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import Model.CURRENT_USER;
import Model.Item;
import Model.dbHandler;


public class CreateItemFragment extends Fragment {

    // declare java components
    EditText mDisplayDate;
    DatePickerDialog.OnDateSetListener mDateSetListener;
    Spinner spinner;
    TextInputLayout inputLayoutName;

    // declare variables
    EditText etItmName, etItmDesc;
    Button btnCreateItm;
    String name, desc, aquiDate, cat, uri;
    ImageView imageview_button;
    Uri imageUri;
    FirebaseStorage storage;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> mGetContent;
    // instantiate dbHandler object
    dbHandler db = new dbHandler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_create_item, parent, false);

        // initialise java components
        // declare java components
        etItmName = (EditText) view.findViewById(R.id.editTextItemName);
        etItmDesc = (EditText) view.findViewById(R.id.editTextItemDescription);
        btnCreateItm = (Button) view.findViewById(R.id.buttonCreateItem);
        imageview_button = (ImageView) view.findViewById(R.id.imageview_button);
        inputLayoutName = view.findViewById(R.id.textInputLayout7);
        storage = FirebaseStorage.getInstance();

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
                spinner = (Spinner) view.findViewById(R.id.spinner);

                // declare and initialise Array Adapter to bind values to the spinner in real time
                ArrayAdapter<String> addressAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, list);
                addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(addressAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // initialise java components
        // mDisplayDate = view.findViewById(R.id.textViewDateAquisition);
        mDisplayDate = view.findViewById(R.id.editTextDateAquisition);

        // set onclick listener
        mDisplayDate.setOnClickListener(view1 ->
        {

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

            // validation
            if (!validateItemName()) {
                return;
            }

            //upload button on click
            uploadImage();

            // initialise variables and get users input
            name = etItmName.getText().toString();
            desc = etItmDesc.getText().toString();

            // get the selected value from the spinner
            cat = spinner.getSelectedItem().toString();

            if(cat.isEmpty())
            {
                Toast.makeText(getContext(), "Please select a category", Toast.LENGTH_SHORT).show();
                return;
            }
            
           // uri = imageUri.toString();

            // instantiate Item object
            Item i = new Item(name, desc, cat, aquiDate, imageUri.toString());

            // write object to firebase
            // users
            db.writeToFirebase("User", CURRENT_USER.displayName, "Category", i.getCategory(), "Item", i.getName(), i);

            // collections
            db.writeToFirebase("Collections", CURRENT_USER.displayName, i.getName(), i);
            Toast.makeText(getContext(), "Item successfully added to collection", Toast.LENGTH_SHORT).show();

            // refresh the ui
            refreshUI();
        });
       // imageview_button.setOnClickListener(
         //       view13 -> mGetContent.launch("image/*"));




        activityResultLauncher =  registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

            Bundle bundle = result.getData().getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");
            imageview_button.setImageBitmap(bitmap);

           String path =  MediaStore.Images.Media.insertImage(getContext().getContentResolver(),bitmap,"val",null);
            imageUri = Uri.parse(path);




            //  imageUri = result.getData().getData();
        });

        imageview_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //startActivityForResult(cInt,1);
                activityResultLauncher.launch(cInt);
                imageUri =  cInt.getData();
                cInt.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            }
        });

        return view;
    }




/*    //Start of a new activity for a result image upload
    ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {


                    if (result != null) {
                        imageview_button.setImageURI(result);
                        imageUri = result;
                    }
                }
            });*/

    private void uploadImage() {
        if (imageUri != null) {
            StorageReference reference = storage.getReference().child("image/" + UUID.randomUUID().toString());

            reference.putFile(imageUri).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    Toast.makeText(CreateItemFragment.super.getContext(), "Image loaded successful", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(CreateItemFragment.super.getContext(), "Image loaded failed", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
    }

    public void refreshUI() {

        etItmName.getText().clear();
        etItmDesc.getText().clear();
        imageview_button.setImageDrawable(null);
        mDisplayDate.setText("Date of Acquisition");
    }

    private boolean validateItemName() {

        String input = inputLayoutName.getEditText().getText().toString().trim();

        if (input.isEmpty()) {
            inputLayoutName.setError("Email Address is  required*");
            return false;
        } else {
            inputLayoutName.setError(null);
            return true;
        }
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

Author : Kon
Subject : How to clear an ImageView in Android?
Code available : https://stackoverflow.com/questions/2859212/how-to-clear-an-imageview-in-android [answered Apr 9, 2011 at 16:36]
Date accessed : [27/05/2022]
 */

