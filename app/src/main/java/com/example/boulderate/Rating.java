package com.example.boulderate;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Map;

public class Rating extends AppCompatActivity {
    private String gymName;
    private DatabaseReference gymRef;
    private DatabaseReference averageRatingRef;
    private RatingBar ratingBar;
    private Button sendButton;
    private TextView averageRatingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating);
        gymName = getIntent().getStringExtra("gymName");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        gymRef = database.getReference("GYMS").child(gymName);
        averageRatingRef = gymRef.child("rating");
        TextView titleText = findViewById(R.id.title_text);
        titleText.setText(gymName);
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ratingBar = findViewById(R.id.rating_bar);
        sendButton = findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentUserId = FirebaseAuth.getInstance().getUid();
                checkUserRating(currentUserId, new MyRTFB.CB_UserRating() {
                    @Override
                    public void userRatingExists(boolean exists) {
                        if (exists) {
                            updateUserRating(currentUserId, ratingBar.getRating());
                        } else {
                            MyRTFB.getUserData(currentUserId, new MyRTFB.CB_User() {
                                @Override
                                public void data(User user) {
                                    if (user != null) {
                                        writeUserRating(user, ratingBar.getRating());
                                        calculateAverageRating();
                                        Toast.makeText(Rating.this, "Rating sent", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(Rating.this, "User data not found", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    private void updateUserRating(String userId, float rating) {
        gymRef.child("usersRatings").orderByChild("user/id").equalTo(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean userRatingExists = false;
                        for (DataSnapshot ratingSnapshot : dataSnapshot.getChildren()) {
                            ratingSnapshot.getRef().child("rating").setValue(rating)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(Rating.this, "Rating updated", Toast.LENGTH_SHORT).show();
                                            calculateAverageRating();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Rating.this, "Failed to update rating", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            userRatingExists = true;
                        }
                        if (!userRatingExists) {
                            MyRTFB.getUserData(userId, new MyRTFB.CB_User() {
                                @Override
                                public void data(User user) {
                                    if (user != null) {
                                        writeUserRating(user, rating);
                                        calculateAverageRating();
                                        Toast.makeText(Rating.this, "Rating sent", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(Rating.this, "User data not found", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    private void checkUserRating(String userId, final MyRTFB.CB_UserRating callback) {
        gymRef.child("usersRatings").orderByChild("user/id").equalTo(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean exists = dataSnapshot.exists();
                        callback.userRatingExists(exists);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callback.userRatingExists(false);
                    }
                });
    }


    private void writeUserRating(User user, float rating) {
        Map<String, Object> userRatingMap = new HashMap<>();
        userRatingMap.put("user", user);
        userRatingMap.put("rating", rating);
        String currentUserId = FirebaseAuth.getInstance().getUid();
        gymRef.child("usersRatings").child(currentUserId).setValue(userRatingMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Rating.this, "Rating saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Rating.this, "Failed to save rating", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void calculateAverageRating() {
        gymRef.child("usersRatings").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int numRatings = 0;
                float totalRating = 0;
                for (DataSnapshot ratingSnapshot : dataSnapshot.getChildren()) {
                    Map<String, Object> ratingMap = (Map<String, Object>) ratingSnapshot.getValue();
                    if (ratingMap != null) {
                        float rating = Float.parseFloat(ratingMap.get("rating").toString());
                        totalRating += rating;
                        numRatings++;
                    }
                }
                if (numRatings > 0) {
                    float averageRating = totalRating / numRatings;
                    updateAverageRating(averageRating);
                } else {
                    updateAverageRating(0);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void updateAverageRating(float averageRating) {
        averageRatingRef.setValue(averageRating)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        averageRatingText.setText(String.valueOf(averageRating));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Rating.this, "Failed to update rating", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
