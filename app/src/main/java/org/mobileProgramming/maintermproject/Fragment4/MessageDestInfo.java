package org.mobileProgramming.maintermproject.Fragment4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.mobileProgramming.maintermproject.R;

public class MessageDestInfo extends AppCompatActivity {

    private TextView userInfo_userName;
    private TextView userInfo_address;
    private TextView userInfo_job;
    private TextView userInfo_age;
    private ImageView userInfo_image;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_fragment5_user_info);

        userInfo_image = findViewById(R.id.ClientFragment1_userInfoProfile);
        userInfo_userName = findViewById(R.id.ClientFragment5_userInfo_userName);
        userInfo_address = findViewById(R.id.ClientFragment5_userInfo_address);
        userInfo_job = findViewById(R.id.ClientFragment5_userInfo_job);
        userInfo_age = findViewById(R.id.ClientFragment5_userInfo_age);

        Intent intent = getIntent();
        final String userName =  intent.getStringExtra("userName");
        final String address =  intent.getStringExtra("address");
        final String age =  intent.getStringExtra("age");
        final String job =  intent.getStringExtra("job");
        final String destinationUid = intent.getStringExtra("destinationUid");

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        StorageReference imageRef = storageReference.child("ClientImages").child(destinationUid+".png");
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext())
                        .load(uri)
                        .apply(new RequestOptions().circleCrop())
                        .into(userInfo_image);
            }
        });


        userInfo_userName.setText(userName);
        userInfo_address.setText(address);
        userInfo_age.setText(age);
        userInfo_job.setText(job);



    }
}
