package org.mobileProgramming.maintermproject.ClientFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.mobileProgramming.maintermproject.R;

public class ClientFragment5 extends Fragment {
    private TextView ClientFragment5_GoogleEmail; //닉네임 text
    private ImageView ClientFragment5_GoogleProfile; //이미지 뷰
    private Button  ClientFragment5_userInfo;
    private Button  ClientFragment5_petInfo;
    private Button  ClientFragment5_myInfo;
    private Button  ClientFragment5_personal;
    private DatabaseReference reference;
    private String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       ViewGroup rootview =  (ViewGroup) inflater.inflate(R.layout.fragment_client5, container, false);

        ClientFragment5_GoogleEmail = rootview.findViewById(R.id.ClientFragment5_GoogleEmail);
        ClientFragment5_GoogleProfile = rootview.findViewById(R.id.ClientFragment5_GoogleProfile);
        if(getArguments() != null) {
            String nickname = getArguments().getString("nickname");
            String photoUrl = getArguments().getString("photoUrl");
            Glide.with(this).load(photoUrl).into(ClientFragment5_GoogleProfile); //profile url를 이미지 뷰에 세팅
            ClientFragment5_GoogleEmail.setText(nickname);
        } else {
            Toast.makeText(getActivity(), "not", Toast.LENGTH_SHORT).show();
        }

        reference = FirebaseDatabase.getInstance().getReference();

        ClientFragment5_userInfo = rootview.findViewById(R.id.ClientFragment5_userInfo);
        ClientFragment5_petInfo = rootview.findViewById(R.id.ClientFragment5_petInfo );
        ClientFragment5_myInfo  = rootview.findViewById(R.id.ClientFragment5_myInfo);
        ClientFragment5_personal = rootview.findViewById(R.id.ClientFragment5_personal);

        ClientFragment5_userInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(getActivity(), org.mobileProgramming.maintermproject.ClientFragment5.ClientFragment5_userInfo.class);
                final Bundle userInfoBundle = new Bundle();

                reference.child("clientInfo").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) { //반복문으로 데이터 list를 추출해냄
                            String uid = snapshot.child("uid").getValue(String.class); //uid
                            String type = snapshot.child("type").getValue(String.class);
                            String clientType = snapshot.child("clientType").getValue(String.class);
                            if (uid.equals(myUid) && type.equals("client") && clientType.equals("clientInfo")) {
                                String userName = snapshot.child("userName").getValue(String.class); // username
                                String address = snapshot.child("address").getValue(String.class); // address
                                String job = snapshot.child("job").getValue(String.class); // job
                                String age = snapshot.child("age").getValue(String.class); // age
                                userInfoBundle.putString("userName",userName);
                                userInfoBundle.putString("address",address);
                                userInfoBundle.putString("job",job);
                                userInfoBundle.putString("age",age);
                                intent.putExtras(userInfoBundle);


                                startActivity(intent);
                            } else {
                                continue;
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });
        ClientFragment5_petInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(getActivity(), org.mobileProgramming.maintermproject.ClientFragment5.ClientFragment5_petInfo.class);
                final Bundle userInfoBundle = new Bundle();

                reference.child("petInfo").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) { //반복문으로 데이터 list를 추출해냄
                            String uid = snapshot.child("uid").getValue(String.class); //uid
                            String type = snapshot.child("type").getValue(String.class);
                            String clientType = snapshot.child("clientType").getValue(String.class);
                            if (uid.equals(myUid) && type.equals("client") && clientType.equals("petInfo")) {
                                String userName = snapshot.child("petName").getValue(String.class); // username
                                String address = snapshot.child("petAge").getValue(String.class); // address
                                String job = snapshot.child("petType").getValue(String.class); // job
                                String allergy = snapshot.child("petAllgery").getValue(String.class); // age
                                userInfoBundle.putString("petName",userName);
                                userInfoBundle.putString("petAge",address);
                                userInfoBundle.putString("petType",job);
                                userInfoBundle.putString("petAllergy",allergy);
                                intent.putExtras(userInfoBundle);
                                startActivity(intent);
                            } else {
                                continue;
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
        ClientFragment5_myInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), org.mobileProgramming.maintermproject.ClientFragment5.ClientFragment5_myInfo.class);
                startActivity(intent);
            }
        });

        ClientFragment5_personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
       return rootview;
    }

}
