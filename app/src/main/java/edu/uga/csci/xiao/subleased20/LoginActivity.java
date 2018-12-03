package edu.uga.csci.xiao.subleased20;

import android.content.Context;
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
import com.google.firebase.database.DatabaseReference;


/**
 * Login Activity does what it does, logs a user in, provided they are valid. Also allows for a user
 * to register
 */
public class LoginActivity extends AppCompatActivity {

    //need to have an firebase authenticator
    private String id;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String username;
    private String password;

    private TextView usernameText;
    private TextView passwordText;
    private Button login;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        usernameText = findViewById(R.id.userInput);
        passwordText = findViewById(R.id.passInput);
        login = findViewById(R.id.loginButton);
        register = findViewById(R.id.registerButton);

        //firebase auth

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {
                    Log.d("LoginActivity", "onAuthStateChanged:signed_in:" + user.getUid());
                    Toast.makeText(LoginActivity.this, "Successfully Signed In," + user.getEmail(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), LoggedActivity.class);
                    startActivity(intent);

                }
                else {
                    Log.d("LoginActivity", "onAuthStateChanged:signed_out");
                    Toast.makeText(LoginActivity.this, "Successfully Signed Out,", Toast.LENGTH_SHORT).show();
                }
            }
        };

        //on click listener when user signs in
        login.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                username = usernameText.getText().toString();
                password = passwordText.getText().toString();

                //checks to make sure fields are not empty
                if(username.equals("") || password.equals("")) {
                    Toast.makeText(LoginActivity.this, "Email/Password Field(s) are empty.", Toast.LENGTH_SHORT).show();
                }
                else {

                    mAuth.signInWithEmailAndPassword(username,password);


                }

                //commented code block that was deprecated
            }
        });

        //register on click listener
        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
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


//IGNORE
  /*
                ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<String> users = new ArrayList<>();
                        for(DataSnapshot dss: dataSnapshot.getChildren())
                        {
                            users.add(String.valueOf(dss.child("username").getValue()));
                            users.add(String.valueOf(dss.child("password").getValue()));
                        }
                        //Toast.makeText(LoginActivity.this, Arrays.toString(users.toArray()),Toast.LENGTH_SHORT).show();
                        for(int i = 0; i < users.size(); i++)
                        {
                            if(i%2 == 0)
                            {
                                String confirmUser = users.get(i);
                                String confirmPass = users.get(i+1);
                                if(confirmUser.equalsIgnoreCase(username)&&confirmPass.equals(password))
                                {
                                    loggedIn = "true";
                                    Toast.makeText(LoginActivity.this, "Accepted, Logging you in...", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }
                        }
                        if(loggedIn.equals("true"))
                        {
                            Intent intent = new Intent(getApplicationContext(), LoggedActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "Incorrect Username/Password", Toast.LENGTH_SHORT).show();
                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                */
