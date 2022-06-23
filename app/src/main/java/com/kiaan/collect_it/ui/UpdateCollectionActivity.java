package com.kiaan.collect_it.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kiaan.collect_it.R;

import java.util.ArrayList;
import java.util.HashMap;

import Model.CURRENT_USER;
import Model.CollectionViewModel;

public class UpdateCollectionActivity extends AppCompatActivity {

    // declare java components
    EditText etUpdateName, etUpdateDesc, etUpdateGoal;
    Button updateCollection;

    // get database reference
    private final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private final DatabaseReference categoryRef = rootRef.child("User").child(CURRENT_USER.displayName).child("Category").child(CURRENT_USER.filterCategory);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_collection);

        // initialise java components
        etUpdateName = findViewById(R.id.editTextUpdateCollectionName);
        etUpdateDesc = findViewById(R.id.editTextUpdateCollectionDesc);
        etUpdateGoal = findViewById(R.id.editTextUpdateCollectionGoal);
        updateCollection = findViewById(R.id.buttonUpdateCollection);

        // populate the edittext box
        populateData();

        // onButtonClick

        updateCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name, desc, goal;

                name = etUpdateName.getText().toString();
                desc = etUpdateDesc.getText().toString();
                goal = etUpdateGoal.getText().toString();

                HashMap<String, Object> hash = new HashMap<>();
                hash.put("name", name);
                hash.put("description", desc);
                hash.put("goal", goal);


                categoryRef.updateChildren(hash).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(UpdateCollectionActivity.this, "Data successfully updated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UpdateCollectionActivity.this, NavigationActivity.class);
                        startActivity(intent);

                    }
                });
            }
        });

    }

    private void populateData() {

        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ArrayList<String> lstColl = new ArrayList<>();

                for (DataSnapshot i : snapshot.getChildren()) {
                    lstColl.add(i.getValue().toString());
                }

                if (!(lstColl.isEmpty())) {
                    etUpdateDesc.setText(lstColl.get(1));
                    etUpdateGoal.setText(lstColl.get(2));
                    etUpdateName.setText(lstColl.get(3));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}



