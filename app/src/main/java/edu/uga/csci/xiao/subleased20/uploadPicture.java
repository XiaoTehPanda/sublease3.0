package edu.uga.csci.xiao.subleased20;

import android.content.ContentResolver;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class uploadPicture extends AppCompatActivity {

    private FirebaseUser mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser currentFirebaseUser;
    private DatabaseReference dbHelper;
    private StorageReference storeHelper;
    private StorageReference fbStorage;

    private TextView imageNameView;
    private Button submitImage;
    private Button uploadImage;
    private ImageView imageView;
    private Intent intent;

    private String leaseid;
    private String imageName;
    private String image;
    private Uri imageUri;
    private UploadTask uploadTask;

    private Sublease sublease;

    private static final int IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_picture);
        storeHelper = FirebaseStorage.getInstance().getReference("sublease");
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        dbHelper = FirebaseDatabase.getInstance().getReference("sublease/" + currentFirebaseUser.getUid() + "/");

        imageNameView = findViewById(R.id.imageName);
        imageView = findViewById(R.id.imageUpload);
        submitImage = findViewById(R.id.submitPic);
        uploadImage = findViewById(R.id.uploadPic);

        image =imageNameView.getText().toString();
        intent = getIntent();
        leaseid = intent.getStringExtra("leaseID");

        dbHelper.equalTo(leaseid).limitToFirst(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dss:dataSnapshot.getChildren())
                {
                    sublease = dss.getValue(Sublease.class);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(uploadPicture.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, IMAGE_REQUEST);
            }
        });

        submitImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(image)) {
                    sublease.setName(image);
                }
                storeHelper.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return storeHelper.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            String stringUri;
                            stringUri = downloadUri.toString();
                            sublease.setImageURL(stringUri);
                            dbHelper.child(leaseid).setValue(sublease);
                        }
                    }
                });
            }
        });
    }

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
}
