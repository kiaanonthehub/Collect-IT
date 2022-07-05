package com.kiaan.collect_it.ui.create_item;

import static Model.CURRENT_USER.permissions;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.textfield.TextInputLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kiaan.collect_it.R;
import com.kiaan.collect_it.ui.NavigationActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import Model.CURRENT_USER;
import Model.Item;
import Model.LoadingDialog;
import Model.dbHandler;


public class CreateItemFragment extends Fragment {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    private static final Object REQUEST_CAMERA = 100;
    public static Uri imageLocalUri = Uri.EMPTY;
    private final ActivityResultLauncher<String[]> activityResultLauncher;
//    LoadingDialog loadingDialog = new LoadingDialog(getActivity());
    // declare java components
    EditText mDisplayDate;
    DatePickerDialog.OnDateSetListener mDateSetListener;
    Spinner spinner;
    TextInputLayout inputLayoutName;
    // declare variables
    EditText etItmName, etItmDesc;
    Button btnCreateItm;
    String name, desc, aquiDate, cat;
    ImageView imageview_button;
    FirebaseStorage storage;
    private final ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    //here we will handle the result of our intent
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        //image picked
                        //get uri of image
                        Intent data = result.getData();
                        Uri imageUri = data.getData();

                        imageview_button.setImageURI(imageUri);
                        uploadImage(imageUri);

                        Intent intent = new Intent(getActivity(), NavigationActivity.class);
                        startActivity(intent);

                    } else {
                        //cancelled
                        Toast.makeText(getContext(), "Cancelled...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );
    // instantiate dbHandler object
    dbHandler db = new dbHandler();

    public CreateItemFragment() {
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
            Log.e("activityResultLauncher", "" + result.toString());
            Boolean areAllGranted = true;
            for (Boolean b : result.values()) {
                areAllGranted = areAllGranted && b;
            }

        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_create_item, parent, false);


        //permissions
        String[] appPerms;
        appPerms = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

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


            // initialise variables and get users input
            name = etItmName.getText().toString();
            desc = etItmDesc.getText().toString();

            // get the selected value from the spinner
            cat = spinner.getSelectedItem().toString();

            if (cat.isEmpty()) {
                Toast.makeText(getContext(), "Please select a category", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                if (imageLocalUri.equals(Uri.EMPTY)) {
                    Toast.makeText(getContext(), "Please upload an image", Toast.LENGTH_SHORT).show();
                } else {
                    // instantiate Item object
                    Item i = new Item(name, desc, cat, aquiDate, imageLocalUri.toString());

                    // write object to firebase
                    // users
                    db.writeToFirebase("User", CURRENT_USER.displayName, "Category", i.getCategory(), "Item", i.getName(), i);

                    // collections
                    db.writeToFirebase("Collections", CURRENT_USER.displayName, i.getName(), i);
                    Toast.makeText(getContext(), "Item successfully added to collection", Toast.LENGTH_SHORT).show();

                    // refresh the ui
                    refreshUI();

                    // reset fields
                }
            } catch (NullPointerException e) {
                Toast.makeText(CreateItemFragment.super.getContext(), "Please select an image", Toast.LENGTH_SHORT).show();
                throw e;
            }
            initialiseFields();

        });

        imageview_button.setOnClickListener(view13 -> {
            this.activityResultLauncher.launch(appPerms);
            chooseImage();

        });

        return view;
    }

    // function to let's the user to choose image from camera or gallery
    public void chooseImage() {

        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit" }; // create a menuOption Array

        // create a dialog for showing the optionsMenu

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        // set the items in builder

        builder.setItems(optionsMenu, (dialogInterface, i) -> {

            if (optionsMenu[i].equals("Take Photo")) {

                // Open the camera and get the photo

                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);
            }
            else if (optionsMenu[i].equals("Choose from Gallery")) {

                // choose from  external storage

                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);

            }
            else if (optionsMenu[i].equals("Exit")) {
                dialogInterface.dismiss();
            }

        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            imageview_button.setImageBitmap(captureImage);

            uploadImage(captureImage);

        }
        //handle dialog, display and upload image
        if (resultCode != Activity.RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == Activity.RESULT_OK && data != null) {
                        Bitmap captureImage = (Bitmap) data.getExtras().get("data");
                        imageview_button.setImageBitmap(captureImage);
                        Toast.makeText(CreateItemFragment.super.getContext(), "Image uploading, please wait", Toast.LENGTH_SHORT).show();
                        uploadImage(captureImage);
                    }
                    break;
                case 1:
                    if (resultCode == Activity.RESULT_OK && data != null) {
                        Uri captureImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (captureImage != null) {
                            Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(captureImage, filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                imageview_button.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                Toast.makeText(CreateItemFragment.super.getContext(), "Image uploading, please wait", Toast.LENGTH_SHORT).show();
                                uploadImage(captureImage);
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
    }

    public void uploadImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference reference = storage.getReference().child("image/" + UUID.randomUUID().toString());

        UploadTask uploadTask = reference.putBytes(data);


        uploadTask.addOnFailureListener(exception -> {

            // Handle unsuccessful uploads
            Toast.makeText(CreateItemFragment.super.getContext(), "Image loaded failed", Toast.LENGTH_SHORT).show();
        }).addOnSuccessListener(taskSnapshot -> {

//            loadingDialog.startLoadingDialog();

//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    loadingDialog.dismissDialog();
//                }
//            }, 5000);

            reference.getDownloadUrl().addOnSuccessListener(uri -> imageLocalUri = uri);

            Toast.makeText(CreateItemFragment.super.getContext(), "Image loaded successfully", Toast.LENGTH_SHORT).show();
        });
    }

    private void uploadImage(Uri imageUri) {
        if (imageUri != null) {
            StorageReference reference = storage.getReference().child("image/" + UUID.randomUUID().toString());
            reference.putFile(imageUri).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    reference.getDownloadUrl().addOnSuccessListener(uri -> imageLocalUri = uri);

                    Toast.makeText(CreateItemFragment.super.getContext(), "Image loaded successfully", Toast.LENGTH_SHORT).show();

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
        imageview_button.setImageResource(R.drawable.camera_icon);
        mDisplayDate.setText("Date of Acquisition");
    }

    private boolean validateItemName() {

        String input = inputLayoutName.getEditText().getText().toString().trim();

        if (input.isEmpty()) {
            inputLayoutName.setError("Item name is required*");
            return false;
        } else {
            inputLayoutName.setError(null);
            return true;
        }
    }

    private void openGallery() {

        Intent takePictureIntent = new Intent(Intent.ACTION_PICK);

        takePictureIntent.setType("image/*");
        galleryActivityResultLauncher.launch(takePictureIntent);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 100);
    }


    private void askPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
        } else {
            Log.e("DB", "PERMISSION GRANTED");
        }
    }

    private void requestPermissions(FragmentActivity activity, String[] strings, int requestIdMultiplePermissions) {
    }

    private void initialiseFields() {
        imageLocalUri = Uri.parse("");
        name = "";
        desc = "";
        aquiDate = "";
        cat = "";
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

