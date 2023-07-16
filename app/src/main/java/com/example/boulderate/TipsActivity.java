package com.example.boulderate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TipsActivity extends AppCompatActivity {

    private LinearLayout tipsLinearLayout;
    private ValueEventListener tipsValueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);

        tipsLinearLayout = findViewById(R.id.tipsLinearLayout);

        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.addBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TipsActivity.this, WriteTipActivity.class);
                startActivity(intent);
            }
        });

        FirebaseDatabase.getInstance().getReference("tips")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        updateTips(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

        tipsValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                updateTips(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        FirebaseDatabase.getInstance().getReference("tips")
                .addValueEventListener(tipsValueEventListener);
    }

    private void updateTips(DataSnapshot dataSnapshot) {
        List<DataSnapshot> tipSnapshots = new ArrayList<>();
        for (DataSnapshot tipSnapshot : dataSnapshot.getChildren()) {
            tipSnapshots.add(tipSnapshot);
        }

        Collections.sort(tipSnapshots, new Comparator<DataSnapshot>() {
            @Override
            public int compare(DataSnapshot snapshot1, DataSnapshot snapshot2) {
                String key1 = snapshot1.getKey();
                String key2 = snapshot2.getKey();
                return key2.compareTo(key1);
            }
        });

        tipsLinearLayout.removeAllViews();

        for (DataSnapshot tipSnapshot : tipSnapshots) {
            String tip = tipSnapshot.child("tip").getValue(String.class);
            String userId = tipSnapshot.child("user/id").getValue(String.class);
            String userName = tipSnapshot.child("user/name").getValue(String.class);
            if (tip != null) {
                String formattedTip = userName + ": " + tip;
                addTipToLayout(formattedTip);
            }
        }
    }

    private void addTipToLayout(String tip) {
        TextView tipTextView = new TextView(this);
        tipTextView.setText(tip);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        int margin = 16; // Hardcoded margin value
        layoutParams.setMargins(margin, margin, margin, margin);
        tipTextView.setLayoutParams(layoutParams);

        tipsLinearLayout.addView(tipTextView);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(TipsActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tipsValueEventListener != null) {
            FirebaseDatabase.getInstance().getReference("tips")
                    .removeEventListener(tipsValueEventListener);
        }
    }
}
