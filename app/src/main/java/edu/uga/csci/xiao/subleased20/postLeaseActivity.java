package edu.uga.csci.xiao.subleased20;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;

/**
 * postLeaseActivity allows a user to post a new sublease entry, which will be store in a firebase
 * database.
 */
public class postLeaseActivity extends AppCompatActivity {
    //sublease fields and parameters
    private String semesterChoice;
    private String subleaseName;
    private String subleasePrice;
    private String subleaseLength;
    private String addInfo;
    private String token;
    private String lease;
    //firebase features
    private FirebaseUser mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser currentFirebaseUser;
    private DatabaseReference dbHelper;
    //views
    private TextView leaseName;
    private TextView leasePrice;
    private TextView leaseLength;
    private TextView additionalInfo;
    private Button submitButton;
    //others
    private Intent intent;
    private Sublease sublease;
    private List<Sublease> subleaseList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //intent to make sure a user is editing an old sublease entry or creating a new one.
        intent = getIntent();
        lease = intent.getStringExtra("leaseID");
        token = intent.getStringExtra("token");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_lease);

        //gets dbRef and firebase user's current uid, in case a person is editing their entry.
        dbHelper = FirebaseDatabase.getInstance().getReference("sublease");
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

        //connect views
        leaseName  = findViewById(R.id.postLeaseName);
        leasePrice = findViewById(R.id.postRentPrice);
        leaseLength = findViewById(R.id.postLeaseLength);
        additionalInfo = findViewById(R.id.addInfo);

        submitButton = findViewById(R.id.submitLease);

        //radio group to defer to different semesters in a school year
        RadioGroup semester =(RadioGroup)findViewById(R.id.myRadioGroup);
        RadioButton fall = (RadioButton)findViewById(R.id.fall);
        RadioButton spring = (RadioButton)findViewById(R.id.spring);
        RadioButton summer = (RadioButton)findViewById(R.id.summer);
        semester.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.fall)
                {
                    semesterChoice = "Fall";
                }
                else if(checkedId == R.id.spring)
                {
                    semesterChoice = "Spring";
                }
                else if(checkedId == R.id.summer)
                {
                    semesterChoice = "Summer";
                }
            }

        });

        //submit listener when a user posts their sublease
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if(token.equals("edit")){
                        editPost();
                    }
                    else{
                        newPost();
                    }

                }

        });

    }

    //used in case a user wished to edit their post instead of creating a new one
    private void editPost() {
        subleaseName = leaseName.getText().toString();
        subleasePrice= leasePrice.getText().toString();
        subleaseLength=leaseLength.getText().toString();
        addInfo= additionalInfo.getText().toString();
        if(!(TextUtils.isEmpty(subleaseName) || TextUtils.isEmpty(subleasePrice) || TextUtils.isEmpty(subleaseLength))){
            Sublease sublease = new Sublease(lease, currentFirebaseUser.getUid(), subleaseName, Integer.parseInt(subleasePrice), Integer.parseInt(subleaseLength),
                    addInfo, semesterChoice, currentFirebaseUser.getEmail());
            dbHelper.child(lease).setValue(sublease);
            Intent intent = new Intent(getApplicationContext(), LoggedActivity.class);
            startActivity(intent);
            //after finishing inserting into the database, goes back to the home screen.
        }

        else {
            Toast.makeText(getApplicationContext(), "Empty Field(s), Please Fill Them in", Toast.LENGTH_SHORT).show();
        }

    }

    //user in case a user wished to create a new post instead of editing
    private void newPost(){
        subleaseName = leaseName.getText().toString();
        subleasePrice= leasePrice.getText().toString();
        subleaseLength=leaseLength.getText().toString();
        addInfo= additionalInfo.getText().toString();
        if(!(TextUtils.isEmpty(subleaseName) || TextUtils.isEmpty(subleasePrice) || TextUtils.isEmpty(subleaseLength))){
            //since the post will be new, generate a new key to identify the new sublease.
            String leaseID = dbHelper.push().getKey();
            Sublease sublease = new Sublease(leaseID, currentFirebaseUser.getUid(), subleaseName, Integer.parseInt(subleasePrice), Integer.parseInt(subleaseLength),
                    addInfo, semesterChoice, currentFirebaseUser.getEmail());
            dbHelper.child(leaseID).setValue(sublease);
            Intent intent = new Intent(getApplicationContext(), LoggedActivity.class);
            startActivity(intent);
            //after finishing inserting into the database, goes back to the home screen.
        }

        else {
            Toast.makeText(getApplicationContext(), "Empty Field(s), Please Fill Them in", Toast.LENGTH_SHORT).show();
        }
    }

    //DEPRECATED

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data!= null && data.getData() != null){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    private String getImageExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mrMime = MimeTypeMap.getSingleton();
        return mrMime.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }
    */

         /*
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, IMAGE_REQUEST);


            }
        });
        */
}

