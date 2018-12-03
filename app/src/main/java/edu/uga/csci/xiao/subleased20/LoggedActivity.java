package edu.uga.csci.xiao.subleased20;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoggedActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged);
        mAuth = FirebaseAuth.getInstance();

        Button logout = (Button)findViewById(R.id.loggingOut);
        Button postLease = (Button)findViewById(R.id.loggedPosting);
        Button searchLease = (Button)findViewById(R.id.loggedSearching);
        Button account =       (Button)findViewById(R.id.loggedAccount);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {
                    Log.d("LoginActivity", "onAuthStateChanged:signed_in:" + user.getUid());
                    Toast.makeText(LoggedActivity.this, "Successfully Signed In," + user.getEmail(), Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.d("LoginActivity", "onAuthStateChanged:signed_out");
                    Toast.makeText(LoggedActivity.this, "Successfully Signed Out,", Toast.LENGTH_SHORT).show();
                }
            }
        };

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UserActivities.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), IntialPage.class));
            }
        });

        postLease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), postLeaseActivity.class));
            }
        });

        searchLease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SearchLease.class));
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth.addAuthStateListener(mAuthListener);
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}