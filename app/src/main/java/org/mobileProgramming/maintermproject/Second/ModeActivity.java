package org.mobileProgramming.maintermproject.Second;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.mobileProgramming.maintermproject.R;
import org.mobileProgramming.maintermproject.Third.Client;
import org.mobileProgramming.maintermproject.Third.Manager;

public class ModeActivity extends AppCompatActivity {
    Button ClientBtn;
    Button ManagerBtn;
    private String nickName;
    private String photoUrl;
    private String displayname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);

        Intent intent = getIntent();
        nickName = intent.getStringExtra("nickname");
        photoUrl = intent.getStringExtra("photoUrl");
        displayname = intent.getStringExtra("displayname");

        ClientBtn =  findViewById(R.id.Clientbtn);
        ClientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Client.class);
                Bundle salesBundle = new Bundle();
                salesBundle.putString("nickname", nickName);
                salesBundle.putString("photoUrl", photoUrl);
                salesBundle.putString("displayname", displayname);
                intent.putExtras(salesBundle);
                startActivity(intent);
            }
        });
        ManagerBtn = findViewById(R.id.Managerbtn);
        ManagerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Manager.class);
                Bundle salesBundle = new Bundle();
                salesBundle.putString("nickname", nickName);
                salesBundle.putString("photoUrl", photoUrl);
                salesBundle.putString("displayname", displayname);
                intent.putExtras(salesBundle);
                startActivity(intent);
            }
        });
    }
}
