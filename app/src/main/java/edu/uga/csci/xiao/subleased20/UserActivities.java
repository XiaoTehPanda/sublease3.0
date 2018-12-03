package edu.uga.csci.xiao.subleased20;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * UserActivtites allows a user to choose to view their own sublease posts, or view their account
 * information
 */
public class UserActivities extends AppCompatActivity {
    //buttons
    private Button myPosts;
    private Button accountInfo;
    //firebase features.
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_activities);

        //begins user authentication.
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {
                    Log.d("LoginActivity", "onAuthStateChanged:signed_in:" + user.getUid());

                }
                else {
                    Log.d("LoginActivity", "onAuthStateChanged:signed_out");

                }
            }
        };

        //connects views
        myPosts = findViewById(R.id.myposts);
        accountInfo = findViewById(R.id.accountinfo);

        //click listener intents to search lease and makes sure queries are limited to the current user
        myPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LeaseListView.class);
                intent.putExtra("intent", "myPost");
                startActivity(intent);
            }
        });
        //click listener intents to user' account information activity.
        accountInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UserAccount.class));
            }
        });
    }

    //onstart/onstop for authentication
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
