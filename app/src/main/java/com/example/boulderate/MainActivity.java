package com.example.boulderate;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView main_LST_gyms;
    private AppCompatImageView main_IMG_background;

    private Button tipsBtn;
    private ArrayList<Gym> gyms;
    private Adapter_Gym adapter_gym;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main_LST_gyms = findViewById(R.id.main_LST_gyms);
        main_IMG_background = findViewById(R.id.main_IMG_background);
        tipsBtn = findViewById(R.id.tipsBtn);
        Imager.me().imageByDrawableCrop(main_IMG_background, R.drawable.background);
        initList();
        gyms = DataManager.mockGyms();
        updateList(gyms);
        //write();
        setupTipsButton();
        readGymsFromDatabase();
        initList();

    }

    private void setupTipsButton() {
        tipsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TipsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateGymRatings(Gym[] updatedGyms) {
        for (Gym updatedGym : updatedGyms) {
            for (int i = 0; i < adapter_gym.getItemCount(); i++) {
                Gym gym = adapter_gym.getItem(i);
                if (gym.getTitle().equals(updatedGym.getTitle())) {
                    gym.setRating(updatedGym.getRating());
                    RecyclerView.ViewHolder viewHolder = main_LST_gyms.findViewHolderForAdapterPosition(i);
                    if (viewHolder != null) {
                        TextView ratingLabel = viewHolder.itemView.findViewById(R.id.gym_LBL_rating);
                        if (ratingLabel != null) {
                            ratingLabel.setText(String.valueOf(updatedGym.getRating()));
                        }
                        RatingBar ratingBar = viewHolder.itemView.findViewById(R.id.gym_RTG_rating);
                        if (ratingBar != null) {
                            ratingBar.setRating((float) updatedGym.getRating());
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        readGymsFromDatabase();
        initList();
    }

    private void readGymsFromDatabase() {
        DatabaseReference gymsRef = FirebaseDatabase.getInstance().getReference().child("GYMS");
        gymsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Gym> gymList = new ArrayList<>();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String gymTitle = childSnapshot.child("title").getValue(String.class);
                    String gymImage = childSnapshot.child("image").getValue(String.class);
                    double gymRating = childSnapshot.child("rating").getValue(Double.class);
                    Gym gym = new Gym(gymTitle, gymImage, gymRating);
                    gymList.add(gym);
                }
                updateGymRatings(gymList.toArray(new Gym[0]));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void write() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference gymsRef = database.getReference("GYMS");

        for (Gym gym : gyms) {
            gymsRef.child(gym.getTitle()).setValue(gym)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("pttt", "Gym added successfully: " + gym.getTitle());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("pttt", "Failed to add gym: " + gym.getTitle() + ", " + e.getMessage());
                        }
                    });
        }
    }

    private void updateList(ArrayList<Gym> gyms) {
        adapter_gym.updateList(gyms);
    }

    private void initList() {
        adapter_gym = new Adapter_Gym(gyms);
        adapter_gym.setOnGymClickListener(new Adapter_Gym.OnGymClickListener() {
            @Override
            public void onClick(View view, Gym gym, int position) {
                Toast.makeText(MainActivity.this, gym.getTitle() + " clicked", Toast.LENGTH_SHORT).show();
                String gymName = gym.getTitle();
                Intent intent = new Intent(view.getContext(), Rating.class);
                intent.putExtra("gymName", gymName);
                ratingLauncher.launch(intent);
            }
        });
        main_LST_gyms.setLayoutManager(new GridLayoutManager(this, 2));
        main_LST_gyms.setHasFixedSize(true);
        main_LST_gyms.setAdapter(adapter_gym);
    }

    private final ActivityResultLauncher<Intent> ratingLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                }
            }
    );

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}