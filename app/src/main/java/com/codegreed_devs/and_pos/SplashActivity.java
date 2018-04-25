package com.codegreed_devs.and_pos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    static boolean calledAlready = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!calledAlready) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            calledAlready = true;
        }
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        for (int i = 0; i < 3000; i++) {

        }
    }
    private void updateUI(FirebaseUser currentUser) {
        if (currentUser!=null){
            Intent intent= new Intent(SplashActivity.this, Security_Pin.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent= new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    }

