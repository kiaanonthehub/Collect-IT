package Model;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class dbHandler {

    private DatabaseReference mDatabase;

    // method to write object to real time database
    public void writeToFirebase(String tableName, String userID, Object obj) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(tableName).child(userID).setValue(obj);
    }

    // overload method to write nested child object to real time database
    public void writeToFirebase(String tableName, String userID, String category, String categoryName, Object obj) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(tableName).child(userID).child(category).child(categoryName).setValue(obj);
    }

    // overload method to write nested child object to real time database
    public void writeToFirebase(String tableName, String userName, String category, String categoryName, String item, String itemName, Object obj) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(tableName).child(userName).child(category).child(categoryName).child(item).child(itemName).setValue(obj);
    }

    //  method to read users from realtime database and store to arraylist
    public void readFromFirebase(String username) {

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference categoryRef = rootRef.child("User").child(username);

        categoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot d : snapshot.getChildren()) {
                    Global.lstStrings.add(d.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    // overload method to read category from realtime database and store to arraylist
    public void readFromFirebase(String username, String category) {

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference categoryRef = rootRef.child("User").child(username).child(category);

        categoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot d : snapshot.getChildren()) {
                    //Global.lstViewCategory.add(d.getKey().toString());
                    //Global.lstCategory.add(d.getValue(Category.class));
                    CURRENT_USER.lstItemCat.add(d.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    // overload method to read items from realtime database and store to arraylist
    public void readFromFirebase(String username, String category,String categoryName) {

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference categoryRef = rootRef.child("User").child(username).child(category).child(categoryName).child("Item");

        categoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot d : snapshot.getChildren()) {
                    Global.lstViewCategory.add(d.getKey());
                    Global.lstItems.add(d.getValue(Item.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
