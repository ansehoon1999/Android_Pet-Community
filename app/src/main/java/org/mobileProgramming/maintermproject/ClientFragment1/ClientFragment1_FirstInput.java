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

public class ClientFragment1_FirstInput extends AppCompatActivity {
    private static final int PICK_FROM_ALBUM = 10;
    private EditText userName;
    private EditText address;
    private EditText age;
    private EditText job;
    private Button next;
    private ImageView profile;
    private Uri imageUri;
    private String myAddress;
    private String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_fragment1__first_input);

        Intent intent = getIntent();
        myAddress = intent.getStringExtra("myAddress");

        profile = findViewById(R.id.FirstInput_Profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });

        userName = findViewById(R.id.FirstInput_UserName);
        address = findViewById(R.id.FirstInput_Address);
        address.setText(myAddress);
        age = findViewById(R.id.FirstInput_Age);
        job = findViewById(R.id.FirstInput_Job);

        next = findViewById(R.id.FirstInput_NextBtn);



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageUri == null || userName.equals("") || address.equals("") || age.equals("") || job.equals("") ) {
                    Toast.makeText(getApplicationContext(), "사진 및 기입 정보를 입력해주세요!", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseStorage.getInstance().getReference().child("ClientImages").child(myUid + ".png")
                            .putFile(imageUri);


                    User user = new User();
                        user.clientType = "clientInfo";
                        user.type = "client";
                        user.userName = userName.getText().toString();
                        user.address = address.getText().toString();
                        user.age = age.getText().toString();
                        user.job = job.getText().toString();
                        user.uid = myUid;
                        user.permit = "false";

                    FirebaseDatabase.getInstance().getReference().child("clientInfo")
                            .child(myUid).setValue(user);
                    finish();
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        if (requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK) {
            profile.setImageURI(data.getData()); //가운데 뷰를 바꿈
            imageUri = data.getData(); //이미지 경로 원본
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
