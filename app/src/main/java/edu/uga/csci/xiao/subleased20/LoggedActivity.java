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

/**
 * The loggedActivity displays a homescreen is presents more options to a registered user.
 */
public class LoggedActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged);
        //preparation for current user authetication.
        mAuth = FirebaseAuth.getInstance();

        //connects buttons
        Button logout = (Button)findViewById(R.id.loggingOut);
        Button postLease = (Button)findViewById(R.id.loggedPosting);
        Button searchLease = (Button)findViewById(R.id.loggedSearching);
        Button account =       (Button)findViewById(R.id.loggedAccount);

        //user authentication
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

        //button to change to a user account's activities
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UserActivities.class));
            }
        });

        //button to logout
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), IntialPage.class));
            }
        });

        //button to post a lease
        postLease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), postLeaseActivity.class);
                intent.putExtra("token", "log");
                startActivity(intent);
            }
        });

        //button to search a lease.
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
