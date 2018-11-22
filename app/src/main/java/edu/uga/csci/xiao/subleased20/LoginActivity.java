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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    DatabaseReference dbHelper, ref2;
    String loggedIn = "false";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = FirebaseDatabase.getInstance().getReference();
        ref2 = dbHelper.child("users");
        final TextView usernameText = (TextView) findViewById(R.id.userInput);
        final TextView passwordText = (TextView) findViewById(R.id.passInput);
        Button login = (Button) findViewById(R.id.loginButton);
        Button register = (Button) findViewById(R.id.registerButton);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = usernameText.getText().toString();
                final String password = passwordText.getText().toString();

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
            }
        });

        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
