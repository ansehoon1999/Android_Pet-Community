package org.mobileProgramming.maintermproject.ClientFragment1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import org.mobileProgramming.maintermproject.R;
import org.mobileProgramming.maintermproject.model.User;

public class ClientFragment1_SecondInput extends AppCompatActivity {
    private static final int PICK_FROM_ALBUM = 10;
    private EditText PetName;
    private EditText PetAge;
    private EditText PetType;
    private EditText PetAllergy;
    private Button okay;
    private ImageView Petprofile;
    private Uri PetUri;
    private String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_fragment1__second_input);
        Petprofile = findViewById(R.id.SecondInput_Petprofile);
        Petprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });
        PetName = findViewById(R.id.SecondInput_PetName);
        PetAge = findViewById(R.id.SecondInput_PetAge);
        PetType = findViewById(R.id.SecondInput_PetType);
        PetAllergy = findViewById(R.id.SecondInput_Allergy);

        okay = findViewById(R.id.SecondInput_OkayBtn);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PetUri == null || PetName.equals("") || PetAge.equals("") || PetType.equals("")) {
                    Toast.makeText(getApplicationContext(), "사진 및 기입 정보를 입력해주세요!", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseStorage.getInstance().getReference().child("ClientPetImages").child(myUid + ".png")
                            .putFile(PetUri);

                    User user = new User();
                    user.type = "client";
                    user.clientType = "petInfo";
                    user.petName = PetName.getText().toString();
                    user.petAge = PetAge.getText().toString();
                    user.petType = PetType.getText().toString();
                    if (PetAllergy.equals("")) {
                        user.petAllgery = "";
                    } else {
                        user.petAllgery = PetAllergy.getText().toString();
                    }
                    user.uid = myUid;
                    FirebaseDatabase.getInstance().getReference().child("petInfo").child(myUid)
                            .setValue(user);
                    finish();
                }
                                }
                            });


                    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        if (requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK) {
            Petprofile.setImageURI(data.getData()); //가운데 뷰를 바꿈
            PetUri = data.getData(); //이미지 경로 원본
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
