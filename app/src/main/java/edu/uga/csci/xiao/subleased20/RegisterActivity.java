package edu.uga.csci.xiao.subleased20;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Register Activity Registers a new user into the authentication database so that they can have
 * access to rest of the app.
 */
public class RegisterActivity extends AppCompatActivity {
    DatabaseReference dbHelper;
    private static final String TAG = "RegisterActivity";
    private FirebaseAuth mAuth;

    //user parameters
    private String password;
    private String confirmation;
    private String email;
    private String phoneNum;
    private String id;

    //buttons and views
    Button register;
    TextView putPass;
    TextView confirmPass;
    TextView putEmail;
    TextView putPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //user authentication
        dbHelper = FirebaseDatabase.getInstance().getReference("users");
        mAuth = FirebaseAuth.getInstance();
        //connect views
        register =findViewById(R.id.registerNow);
        putPass = findViewById(R.id.passInput);
        confirmPass = findViewById(R.id.confirmPass);
        putEmail= findViewById(R.id.emailInput);
        putPhone= findViewById(R.id.phoneInput);
        //register button listener
        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                password = putPass.getText().toString();
                confirmation = confirmPass.getText().toString();
                email    = putEmail.getText().toString();
                phoneNum = putPhone.getText().toString();
                id = dbHelper.push().getKey();
                //makes sure important fields are not empty
                if(TextUtils.isEmpty(password)||TextUtils.isEmpty(email)||TextUtils.isEmpty(phoneNum)) {
                    Toast.makeText(getApplicationContext(), "Some Fields are Empty.", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (password.equals(confirmation)) {
                        //firebase authentication to create new user.
                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //checks for success
                                Log.d(TAG, "createUserWithEmail:onComplete" +task.isSuccessful());
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(getApplicationContext(), "Registered Successfully.", Toast.LENGTH_SHORT).show();
                                    User newUser = new User(email, phoneNum);
                                    dbHelper.child(id).setValue(newUser);
                                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Authentication Failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                    //makes sure password data matches.
                    else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Your passwords do not match, try again.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }


            }
        });

    }
}
