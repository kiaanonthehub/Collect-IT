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