package com.example.boulderate;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Arrays;

public class Activity_Login extends AppCompatActivity {
    private Button open;
    private EditText editTextText;
    private boolean userRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        open = findViewById(R.id.open);
        editTextText = findViewById(R.id.editTextText);

        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openApp();
            }
        });
        open.setEnabled(false);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            login();
        } else {
            checkIfUserInMyServer();
            if (userRegistered == false)
                login();
        }
    }

    private void registerUser(String name) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            User user = new User()
                    .setId(firebaseUser.getUid())
                    //.setPhone(firebaseUser.getPhoneNumber())
                    .setName(name);

            MyRTFB.saveNewUser(user);
            Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show();
            userRegistered = true;
            proceedToMain();
        } else {
        }
    }

    private ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            (result) -> {
                IdpResponse response = result.getIdpResponse();

                if (result.getResultCode() == RESULT_OK) {
                    checkIfUserInMyServer();
                } else {
                    if (response == null) {
                        showSnackbar("Sign in cancelled");
                        return;
                    }
                    if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                        showSnackbar("No internet connection");
                        return;
                    }
                    showSnackbar("Unknown error");
                    Log.e("pttt", "Sign-in error: ", response.getError());
                }
            });

    private void login() {
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(Arrays.asList(
                        new AuthUI.IdpConfig.PhoneBuilder().build()
                ))
                .build();

        signInLauncher.launch(signInIntent);
    }

    private void openApp() {
        String name = editTextText.getText().toString().trim();
        if (userRegistered) { // Check if the user has already registered
            proceedToMain();
        } else {
            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
            } else {
                registerUser(name);
            }
        }
    }

    private void checkIfUserInMyServer() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            MyRTFB.getUserData(firebaseUser.getUid(), new MyRTFB.CB_User() {
                @Override
                public void data(User user) {
                    if (user == null) {
                        userRegistered = false;
                        open.setEnabled(true);
                    } else {
                        Toast.makeText(Activity_Login.this, "Welcome back " + user.getName(), Toast.LENGTH_LONG).show();
                        userRegistered = true;
                        openApp();
                    }
                }
            });
        } else {
            userRegistered = false;
            open.setEnabled(true);
        }
    }

    private void proceedToMain() {
        startActivity(new Intent(Activity_Login.this, MainActivity.class));
        finish();
    }

    private void showSnackbar(String message) {
        Snackbar
                .make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
                .setAction("OK", null)
                .show();
    }
}