package Model;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DbHandler {

    private DatabaseReference mDatabase;

    // method to write object to real time database
    public void writeToFirebase(String tableName, String uID, Object obj) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(tableName).child(uID).setValue(obj);
    }

    // overload method to write nested child object to real time database
    public void writeToFirebase(String tableName, String uID, String tableName2, String uID2, Object obj) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(tableName).child(uID).child(tableName2).child(uID2).setValue(obj);
    }

    // overload method to write nested child object to real time database
    public void writeToFirebase(String tableName, String uID, String tableName2, String uID2, String tableName3, String uID3, Object obj) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(tableName).child(uID).child(tableName2).child(uID2).child(tableName3).child(uID3).setValue(obj);
    }

    //  method to read users from realtime database and store to arraylist
    public void readFromFirebase(String username) {

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference categoryRef = rootRef.child("User").child(username);

        categoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Global.lstStrings.clear();
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
                Global.lstStrings.clear();
                for (DataSnapshot d : snapshot.getChildren()) {
                    Global.lstStrings.add(d.getKey());
                    Global.lstCategory.add(d.getValue(Category.class));
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
                Global.lstStrings.clear();
                for (DataSnapshot d : snapshot.getChildren()) {
                    Global.lstStrings.add(d.getKey());
                    Global.lstItems.add(d.getValue(Item.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
