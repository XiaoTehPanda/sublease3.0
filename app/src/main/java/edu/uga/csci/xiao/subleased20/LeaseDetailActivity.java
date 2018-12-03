package edu.uga.csci.xiao.subleased20;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private String address;
    private String information;
    private String semester;
    private String duration;
    private int price;

    private TextView addressView;
    private TextView priceView;
    private TextView yearView;
    private TextView extraInfoView;
    private Button   saveButton;
    private ImageView imageView;

    private DatabaseReference dbRef;
    private DatabaseReference childDB;
    private FirebaseDatabase dbHelper;
    private StorageReference storeRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lease_detail);

        intent = getIntent();
        address = intent.getStringExtra("address");
        information = intent.getStringExtra("information");
        semester = intent.getStringExtra("semester");
        duration = Integer.toString(intent.getIntExtra("duration", 2018));
        price = intent.getIntExtra("price", 400);
        uid = intent.getStringExtra("uid");

        dbRef = FirebaseDatabase.getInstance().getReference("sublease");
        storeRef = FirebaseStorage.getInstance().getReference();

        information = "Extra Information:\n"+ information;
        semester = "For Lease During " + semester + " semester of " + duration;

        addressView = findViewById(R.id.addressDetail);
        extraInfoView = findViewById(R.id.extraextra);
        yearView = findViewById(R.id.durationDetail);
        priceView = findViewById(R.id.priceDetail);
        saveButton = findViewById(R.id.saveButton);
        imageView = findViewById(R.id.leaseImage);

        addressView.setText(address);
        extraInfoView.setText(information);
        yearView.setText(semester);
        priceView.setText("$" + price + " per Month");
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
}
