package com.dera.memoapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.dera.memoapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashScreenActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    private void updateUI(FirebaseUser user){
        if(user == null){
            Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
            startActivity(intent);

            finish();
        } else{
            mDatabase.getReference("aiivondb/employees/" + user.getUid() +"/username")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            mUsername  = dataSnapshot.getValue(String.class);
                            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                            intent.putExtra("UserName", mUsername);
                            Log.d("Splash: ", mUsername);
                            startActivity(intent);

                            finish();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


        }
    }
}
