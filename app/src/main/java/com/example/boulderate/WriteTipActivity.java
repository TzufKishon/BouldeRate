package com.example.boulderate;

import static com.example.boulderate.User.getNameById;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WriteTipActivity extends AppCompatActivity {

    private TextInputEditText tipEditText;
    private Button submitTipButton;
    private ImageButton backBtn;
    private DatabaseReference tipsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_tip);

        tipEditText = findViewById(R.id.tipEditText);
        submitTipButton = findViewById(R.id.submitTipButton);
        backBtn = findViewById(R.id.backBtn);

        tipsRef = FirebaseDatabase.getInstance().getReference("tips");

        submitTipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tip = tipEditText.getText().toString().trim();
                if (!tip.isEmpty()) {
                    checkAndSubmitTip(tip);
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void checkAndSubmitTip(final String tip) {
        tipsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean tipExists = false;
                for (DataSnapshot tipSnapshot : dataSnapshot.getChildren()) {
                    String existingTip = tipSnapshot.child("tip").getValue(String.class);
                    if (existingTip != null && existingTip.equals(tip)) {
                        tipExists = true;
                        break;
                    }
                }
                if (!tipExists) {
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (currentUser != null) {
                        String userId = currentUser.getUid();
                        String userName = getNameById(userId);
                        submitTip(userId, userName, tip);
                    } else {
                        Toast.makeText(WriteTipActivity.this, "User not authenticated", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(WriteTipActivity.this, "Tip already exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle read error
            }
        });
    }

    private void submitTip(String userId, String userName, String tip) {
        tipsRef.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String lastTipKey = "";
                for (DataSnapshot tipSnapshot : dataSnapshot.getChildren()) {
                    lastTipKey = tipSnapshot.getKey();
                }

                long tipCount = 1;
                if (!lastTipKey.isEmpty()) {
                    tipCount = Long.parseLong(lastTipKey) + 1;
                }

                String tipId = String.valueOf(tipCount);

                DatabaseReference tipRef = tipsRef.child(tipId);
                tipRef.child("user/id").setValue(userId);
                tipRef.child("user/name").setValue(userName);
                tipRef.child("tip").setValue(tip)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(WriteTipActivity.this, "Tip submitted", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(WriteTipActivity.this, "Failed to submit tip", Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(WriteTipActivity.this, "Failed to submit tip", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
