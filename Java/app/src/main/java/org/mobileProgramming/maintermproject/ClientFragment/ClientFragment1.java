package org.mobileProgramming.maintermproject.ClientFragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.mobileProgramming.maintermproject.ClientFragment1.ClientFragment1_FirstInput;
import org.mobileProgramming.maintermproject.ClientFragment1.ClientFragment1_SecondInput;
import org.mobileProgramming.maintermproject.R;


public class ClientFragment1 extends Fragment {
    Button ClientFragment1_userInfoBtn;
    Button ClientFragment1_userInfoChangeBtn;
    ImageButton ClientFragment1_card10;
    private TextView google_nickname;
    private DatabaseReference reference;
    private Bundle bundle;
    private String myAddress;
    private String displayname;
    private String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_client1, container, false);

        bundle = getArguments();
        if(bundle != null) {
            myAddress = bundle.getString("MyAddress");
            displayname = bundle.getString("displayname");
        }

        google_nickname = rootview.findViewById(R.id.google_nickname);
        google_nickname.setText(displayname+"님,");
        reference = FirebaseDatabase.getInstance().getReference().child("ClientBtnInfo");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { //반복문으로 데이터 list를 추출해냄
                    String num = snapshot.child("ClientBtnNum").getValue(String.class);
                    String uid = snapshot.child("ClientUid").getValue(String.class);
                    if (num.equals("1") && uid.equals(myUid)) {
                        ClientFragment1_userInfoBtn.setEnabled(false);
                        ClientFragment1_userInfoChangeBtn.setEnabled(true);
                        break;
                    } else {
                        continue;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        ClientFragment1_userInfoBtn = (Button) rootview.findViewById(R.id.ClientFragment1_userInfoBtn);
        ClientFragment1_userInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(getActivity(), ClientFragment1_FirstInput.class);
                intent.putExtra("myAddress", myAddress);
                startActivity(intent);
            }
        });

        ClientFragment1_userInfoChangeBtn = (Button) rootview.findViewById(R.id.ClientFragment1_userInfoChangeBtn);
        ClientFragment1_userInfoChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(getActivity(), ClientFragment1_SecondInput.class);
                startActivity(intent);
            }
        });
        ClientFragment1_card10 = (ImageButton) rootview.findViewById(R.id.ClientFragment1_card10);
        ClientFragment1_card10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://blog.naver.com/darktnt/221874430739"));
                startActivity(intent);
            }
        });
                return rootview;
    }

}
