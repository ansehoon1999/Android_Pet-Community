package org.mobileProgramming.maintermproject.ManagerFragment1;

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

public class ManagerFragment1_SecondInput extends AppCompatActivity {
    private static final int PICK_FROM_ALBUM = 10;
    private EditText SalesTitle;
    private EditText hashtag1;
    private EditText hashtag2;
    private EditText hashtag3;
    private Button okay;
    private ImageView Petprofile;
    private Uri PetUri;
    private EditText memo;
    private EditText address;
    private String myAddress;
    private String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_fragment1__second_input);

        Intent intent = getIntent();
        myAddress = intent.getStringExtra("myAddress");

        Petprofile = findViewById(R.id.ManagerFragment1_Petprofile);
        Petprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });

        SalesTitle = findViewById(R.id.ManagerFragment1_SalesTitle);
        address = findViewById(R.id.ManagerFragment1_address2);
        address.setText(myAddress);

        hashtag1 = findViewById(R.id.ManagerFragment1_hashtag1);
        hashtag2 = findViewById(R.id.ManagerFragment1_hashtag2);
        hashtag3 = findViewById(R.id.ManagerFragment1_hashtag3);
        memo = findViewById(R.id.ManagerFragment1_memo);

        okay = findViewById(R.id.ManagerFragment1_okay);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PetUri == null || address.equals("") || SalesTitle.equals("") || hashtag1.equals("")
                        || hashtag2.equals("") || hashtag3.equals("") || memo.equals("")) {
                    Toast.makeText(getApplicationContext(), "사진 및 기입 정보를 입력해주세요!", Toast.LENGTH_SHORT).show();
                } else {

                    FirebaseStorage.getInstance().getReference().child("ManagerSalesImages")
                            .child(myUid + ".png").putFile(PetUri);

                    User user = new User();
                    user.uid = myUid;
                    user.type = "manager";
                    user.clientType = "SalesInfo";
                    user.address = address.getText().toString();
                    user.Salestitle = SalesTitle.getText().toString();
                    user.hashtag1 = hashtag1.getText().toString();
                    user.hashtag2 = hashtag2.getText().toString();
                    user.hashtag3 = hashtag3.getText().toString();
                    user.memo = memo.getText().toString();

                    FirebaseDatabase.getInstance().getReference().child("SalesInfo")
                            .child(myUid).setValue(user);
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
