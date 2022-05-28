package com.kiaan.collect_it.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kiaan.collect_it.R;

import java.util.ArrayList;

import Model.CURRENT_USER;
import Model.ItemAdapter;
import Model.ItemViewModel;

public class FilterItemsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    // get database reference
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference categoryRef = rootRef.child("User").child(CURRENT_USER.displayName).child("Category").child(CURRENT_USER.filterCategory).child("Item");

    private ItemAdapter adapter;

    private ArrayList<ItemViewModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_items);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new ItemAdapter(this, list);

        recyclerView.setAdapter(adapter);

        categoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Iterable<DataSnapshot> children = snapshot.getChildren();

                // retrieve data from real time database
                for (DataSnapshot child : children) {
                    ItemViewModel model = child.getValue(ItemViewModel.class);
                    list.add(model);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

/*
Code Attribution
Author : CodingSTUFF
Subject : Retrieve Data From Firebase Realtime Database in RecyclerView (Android Studio 2020) #androidstudio
Code available : https://www.youtube.com/redirect?event=video_description&redir_token=QUFFLUhqbEU5NWJKNUJUMm9RbV9xSU53VzhWN3k3SGdvQXxBQ3Jtc0tualpVZlpORzhSdlVCRVB1TXJvSDFzajhnUDR6bnhka0pocU9qdGNWMDVWOFBtLTcxTkNWRUdaQ3JQU0ZQd2dlakFITkxITlV2SlV2QkpoNFVMMHFJdDVzWThKd1hySVJWblJIeTUzMjI3akZHRXpIUQ&q=https%3A%2F%2Fsanathgosavi.blogspot.com%2F2020%2F11%2Fupload-data-in-realtime-database-and.html&v=V4E5ROnbrGk
Repo available : https://sanathgosavi.blogspot.com/2020/11/upload-data-in-realtime-database-and.html
Date accessed : [28/05/2022]

Author : Brandan Jones
Subject : Retreiving Java object data from Firebase in Android Studio
Code available : https://www.youtube.com/watch?v=aPLh31MWewc
Date accessed : [28/05/2022]
 */