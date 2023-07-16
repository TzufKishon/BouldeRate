package com.example.boulderate;

import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyRTFB {
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();

    public interface CB_User {
        void data(User user);
    }

    public static void saveNewUser(User user) {
        DatabaseReference ref = database.getReference("USERS");
        ref.child(user.getId()).setValue(user);
    }

    public static void getUserData(String id, CB_User cb_user) {
        DatabaseReference ref = database.getReference("USERS");
        ref.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                cb_user.data(user);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                cb_user.data(null);
            }
        });
    }

    public interface CB_UserRating {
        void userRatingExists(boolean exists);
    }
}
