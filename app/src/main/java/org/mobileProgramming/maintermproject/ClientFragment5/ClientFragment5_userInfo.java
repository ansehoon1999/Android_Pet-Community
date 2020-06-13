package org.mobileProgramming.maintermproject.ClientFragment5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.mobileProgramming.maintermproject.R;

public class ClientFragment5_userInfo extends AppCompatActivity {
    private FirebaseAuth auth;
    private TextView userInfo_userName;
    private TextView userInfo_address;
    private TextView userInfo_job;
    private TextView userInfo_age;
    private ImageView ClientFragment1_userInfoProfile;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_fragment5_user_info);
        final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userInfo_userName = findViewById(R.id.ClientFragment5_userInfo_userName);
        userInfo_address = findViewById(R.id.ClientFragment5_userInfo_address);
        userInfo_job = findViewById(R.id.ClientFragment5_userInfo_job);
        userInfo_age = findViewById(R.id.ClientFragment5_userInfo_age);
        ClientFragment1_userInfoProfile = findViewById(R.id.ClientFragment1_userInfoProfile);


        Intent intent = getIntent();
        final String userName =  intent.getStringExtra("userName");
        final String address =  intent.getStringExtra("address");
        final String age =  intent.getStringExtra("age");
        final String job =  intent.getStringExtra("job");

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        StorageReference imageRef = storageReference.child("ClientImages").child(myUid+".png");
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext())
                        .load(uri)
                        .apply(new RequestOptions().circleCrop())
                        .into(ClientFragment1_userInfoProfile);
            }
        });
        userInfo_userName.setText(userName);
        userInfo_address.setText(address);
        userInfo_age.setText(age);
        userInfo_job.setText(job);

    }
}
