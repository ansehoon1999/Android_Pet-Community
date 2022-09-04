package org.mobileProgramming.maintermproject.ClientFragment5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.mobileProgramming.maintermproject.R;

public class ClientFragment5_petInfo extends AppCompatActivity {
    private TextView petInfo_name;
    private TextView petInfo_age;
    private TextView petInfo_type;
    private TextView petInfo_allergy;
    private ImageView ClientFragment1_petInfoProfile;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    private String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_fragment5_pet_info);
        petInfo_name = findViewById(R.id.ClientFragment5_petInfo_name);
        petInfo_age = findViewById(R.id.ClientFragment5_petInfo_age);
        petInfo_type = findViewById(R.id.ClientFragment5_petInfo_type);
        petInfo_allergy = findViewById(R.id.ClientFragment5_petInfo_allergy);
        ClientFragment1_petInfoProfile = findViewById(R.id.ClientFragment1_petInfoProfile);

        Intent intent = getIntent();
        final String Name =  intent.getStringExtra("petName");
        final String age =  intent.getStringExtra("petAge");
        final String type =  intent.getStringExtra("petType");
        final String allergy =  intent.getStringExtra("petAllergy");

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        StorageReference imageRef = storageReference.child("ClientPetImages").child(myUid+".png");
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext())
                        .load(uri)
                        .apply(new RequestOptions().circleCrop())
                        .into(ClientFragment1_petInfoProfile);
            }
        });

        petInfo_name.setText(Name);
        petInfo_age.setText(age);
        petInfo_type.setText(type);
        petInfo_allergy.setText(allergy);

    }
}
