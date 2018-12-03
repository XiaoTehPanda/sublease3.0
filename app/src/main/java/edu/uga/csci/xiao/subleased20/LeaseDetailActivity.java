package edu.uga.csci.xiao.subleased20;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class LeaseDetailActivity extends AppCompatActivity {
    private Intent intent;
    private String uid;
    private String currentUid;
    private String leaseID;
    private String address;
    private String information;
    private String semester;
    private String duration;
    private String token;
    private int price;

    private TextView addressView;
    private TextView priceView;
    private TextView yearView;
    private TextView extraInfoView;
    private Button   deletebutton;
    private ImageView imageView;

    private DatabaseReference dbRef;
    private DatabaseReference childDB;
    private FirebaseDatabase dbHelper;
    private StorageReference storeRef;
    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lease_detail);

        intent = getIntent();
        leaseID = intent.getStringExtra("leaseID");
        uid = intent.getStringExtra("uid");
        token = intent.getStringExtra("intent");
        address = intent.getStringExtra("address");
        information = intent.getStringExtra("information");
        semester = intent.getStringExtra("semester");
        duration = Integer.toString(intent.getIntExtra("duration", 2018));
        price = intent.getIntExtra("price", 400);



        dbRef = FirebaseDatabase.getInstance().getReference("sublease");
        storeRef = FirebaseStorage.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUid = firebaseUser.getUid();
        mAuth = FirebaseAuth.getInstance();

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

        information = "Extra Information:\n"+ information;
        semester = "For Lease During " + semester + " semester of " + duration;

        addressView = findViewById(R.id.addressDetail);
        extraInfoView = findViewById(R.id.extraextra);
        yearView = findViewById(R.id.durationDetail);
        priceView = findViewById(R.id.priceDetail);
        imageView = findViewById(R.id.leaseImage);
        deletebutton = findViewById(R.id.deleteButton);

        addressView.setText(address);
        extraInfoView.setText(information);
        yearView.setText(semester);
        priceView.setText("$" + price + " per Month");


        if(token.equals("mypost")){
            deletebutton.setVisibility(View.VISIBLE);
        }
        else{}

        deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference newRef = FirebaseDatabase.getInstance().getReference("sublease").child(leaseID);
                newRef.removeValue();

            }
        });
/*
        dbRef.orderByChild(uid).equalTo(address).limitToFirst(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dss:dataSnapshot.getChildren())
                {
                    Sublease sublease = dss.getValue(Sublease.class);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LeaseDetailActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
*/

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
