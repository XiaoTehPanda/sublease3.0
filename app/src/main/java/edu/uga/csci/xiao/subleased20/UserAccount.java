package edu.uga.csci.xiao.subleased20;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class UserAccount extends AppCompatActivity {
    private static final String TAG = "User Account";
    TextView emailText;
    TextView phoneText;
    TextView newPassText;
    TextView confirmPassText;
    TextView newPass;
    TextView confirmNew;

    Button passChange ;
    Button submitChange;

    FirebaseDatabase dbHelper;
    DatabaseReference dbRef;
    User dbUser;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        mAuth = FirebaseAuth.getInstance();

        user = FirebaseAuth.getInstance().getCurrentUser();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {
                    Log.d("LoginActivity", "onAuthStateChanged:signed_in:" + user.getUid());
                    Toast.makeText(UserAccount.this, "Successfully Signed In," + user.getEmail(), Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.d("LoginActivity", "onAuthStateChanged:signed_out");
                    Toast.makeText(UserAccount.this, "Successfully Signed Out,", Toast.LENGTH_SHORT).show();
                }
            }
        };

        emailText = findViewById(R.id.emailText);
        phoneText = findViewById(R.id.phoneText);

        newPass = findViewById(R.id.newPass);
        confirmNew = findViewById(R.id.confirmNewPass);
        newPassText = findViewById(R.id.newPassText);
        confirmPassText= findViewById(R.id.confirmNewText);

        passChange = findViewById(R.id.changePassButton);
        submitChange = findViewById(R.id.submitChange);

        emailText.setText(user.getEmail());
        passChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPass.setVisibility(View.VISIBLE);
                confirmNew.setVisibility(View.VISIBLE);
                submitChange.setVisibility(View.VISIBLE);
                newPassText.setVisibility(View.VISIBLE);
                confirmPassText.setVisibility(View.VISIBLE);
            }
        });

        submitChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user!=null){
                    if(newPass.getText().toString().equals(confirmNew.getText().toString())) {
                        user.updatePassword(newPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(),"Password Changed Successfully.", Toast.LENGTH_SHORT).show();
                                    newPass.setVisibility(View.INVISIBLE);
                                    confirmNew.setVisibility(View.INVISIBLE);
                                    submitChange.setVisibility(View.INVISIBLE);
                                    newPassText.setVisibility(View.INVISIBLE);
                                    confirmPassText.setVisibility(View.INVISIBLE);
                                }
                                else {
                                Toast.makeText(getApplicationContext(),"Password Changed Failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Passwords Do Not Match", Toast.LENGTH_SHORT).show();
                    }
                }
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

/*
mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {
                    Log.d("LoginActivity", "onAuthStateChanged:signed_in:" + user.getUid());
                    Toast.makeText(UserAccount.this, "Successfully Signed In," + user.getEmail(), Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.d("LoginActivity", "onAuthStateChanged:signed_out");
                    Toast.makeText(UserAccount.this, "Successfully Signed Out,", Toast.LENGTH_SHORT).show();
                }
            }
        };
 */