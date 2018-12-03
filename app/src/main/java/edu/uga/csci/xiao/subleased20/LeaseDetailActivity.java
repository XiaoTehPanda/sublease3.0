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

/**
 * This activity displays a listview item's details for each of the query entries the user selects.
 * Information changes depending on the listview item the user selects.
 */
public class LeaseDetailActivity extends AppCompatActivity {
    private Intent intent;
    private String uid;
    private String currentUid;
    private String leaseID;
    private String address;
    private String information;
    private String semester;
    private String duration;
    private String contactInfo;
    private String token;
    private int price;

    private TextView addressView;
    private TextView priceView;
    private TextView yearView;
    private TextView extraInfoView;
    private Button   deletebutton;
    private Button   editButton;

    //Variables define firebase imports
    private DatabaseReference dbRef;
    private DatabaseReference childDB;
    private FirebaseDatabase dbHelper;
    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lease_detail);

        //intents from clicking on a list item
        intent = getIntent();

        //listitem will correspond to a sublease object, with appropriate fields and data.
        leaseID = intent.getStringExtra("leaseID");
        uid = intent.getStringExtra("uid");
        token = intent.getStringExtra("intent");
        address = intent.getStringExtra("address");
        information = intent.getStringExtra("information");
        semester = intent.getStringExtra("semester");
        contactInfo = intent.getStringExtra("contact");
        duration = Integer.toString(intent.getIntExtra("duration", 2018));
        price = intent.getIntExtra("price", 400);


        //database ref to firebase, as well as a firebase auth to make sure a current user is valid
        dbRef = FirebaseDatabase.getInstance().getReference("sublease");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser !=null){
            currentUid = firebaseUser.getUid();
        }
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

        //text stylizing for textviews
        information = "Extra Information:\n"+ information + "\n\nFor more Details, contact: " + contactInfo ;
        semester = "For Lease During " + semester + " semester of " + duration;

        //link views/buttons to their respective variables.
        addressView = findViewById(R.id.addressDetail);
        extraInfoView = findViewById(R.id.extraextra);
        yearView = findViewById(R.id.durationDetail);
        priceView = findViewById(R.id.priceDetail);
        deletebutton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editLease);

        //sets current views to match information on the listview item.
        addressView.setText(address);
        extraInfoView.setText(information);
        yearView.setText(semester);
        priceView.setText("$" + price + " per Month");

        //if the post is indeed the user's own post, allow them to edit or delete post
        if(token.equals("mypost")){
            deletebutton.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.VISIBLE);
        }
        else{}

        //delete button will make a call to the firebase database, and remove that call location's values.
        deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference newRef = FirebaseDatabase.getInstance().getReference("sublease").child(leaseID);
                newRef.removeValue();

            }
        });

        //intents to post lease activity, and user can change information there
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), postLeaseActivity.class);
                intent.putExtra("token", "edit");
                intent.putExtra("leaseID", leaseID);
                startActivity(intent);
            }
        });

    }

    //onStart/onStop to check for current user authentication.
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
