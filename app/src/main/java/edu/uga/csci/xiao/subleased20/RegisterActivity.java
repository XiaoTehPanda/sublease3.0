package edu.uga.csci.xiao.subleased20;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    DatabaseReference dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        dbHelper = FirebaseDatabase.getInstance().getReference("users");
        Button  register = (Button)  findViewById(R.id.registerNow);
        final TextView putUser = (TextView)findViewById(R.id.nameInput);
        final TextView putPass = (TextView)findViewById(R.id.passInput);
        final TextView confirmPass = (TextView)findViewById(R.id.confirmPass);
        final TextView putEmail= (TextView)findViewById(R.id.emailInput);
        final TextView putPhone= (TextView)findViewById(R.id.phoneInput);

        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                String username = putUser.getText().toString();
                String password = putPass.getText().toString();
                String confirmation = confirmPass.getText().toString();
                String email    = putEmail.getText().toString();
                String phoneNum = putPhone.getText().toString();
                String id = dbHelper.push().getKey();
                if(password.equals(confirmation))
                {
                    User newUser = new User(id, username,password, email, phoneNum);
                    dbHelper.child(id).setValue(newUser);
                }
                else
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "Your passwords do not match, try again.", Toast.LENGTH_SHORT);
                    toast.show();
                }



            }
        });

    }
}
