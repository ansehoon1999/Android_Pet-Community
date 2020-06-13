package org.mobileProgramming.maintermproject.ManagerFragment;

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


public class ManagerFragment5 extends Fragment {
    private TextView ManagerFragment5_GoogleEmail; //닉네임 text
    private ImageView ManagerFragment5_GoogleProfile; //이미지 뷰
    private Button ManagerFragment5_userInfo;
    private Button ManagerFragment5_myInfo;
    private DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_manager5, container, false);
        final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ManagerFragment5_GoogleProfile = rootview.findViewById(R.id.ManagerFragment5_GoogleProfile);
        ManagerFragment5_GoogleEmail = rootview.findViewById(R.id.ManagerFragment5_GoogleEmail);

        if(getArguments() != null) {
            String nickname = getArguments().getString("nickname");
            String photoUrl = getArguments().getString("photoUrl");
            Glide.with(this).load(photoUrl).into(ManagerFragment5_GoogleProfile); //profile url를 이미지 뷰에 세팅
            ManagerFragment5_GoogleEmail.setText(nickname);

        } else {
            Toast.makeText(getActivity(), "not", Toast.LENGTH_SHORT).show();
        }


        ManagerFragment5_myInfo = rootview.findViewById(R.id.ManagerFragment5_myInfo);
        ManagerFragment5_userInfo = rootview.findViewById(R.id.ManagerFragment5_userInfo);
        ManagerFragment5_userInfo.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(getActivity(), org.mobileProgramming.maintermproject.ManagerFragment5.ManagerFragment5_userInfo.class);
                final Bundle userInfoBundle = new Bundle();
                reference = FirebaseDatabase.getInstance().getReference();
                reference.child("managerInfo").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) { //반복문으로 데이터 list를 추출해냄
                            String uid = snapshot.child("uid").getValue(String.class); //uid
                            String type = snapshot.child("type").getValue(String.class);
                            String clientType = snapshot.child("clientType").getValue(String.class);
                            if (uid.equals(myUid) && type.equals("manager") && clientType.equals("managerInfo")) {
                                String userName = snapshot.child("userName").getValue(String.class); // username
                                String address = snapshot.child("address").getValue(String.class); // address
                                String job = snapshot.child("job").getValue(String.class); // job
                                String age = snapshot.child("age").getValue(String.class); // age
                                userInfoBundle.putString("userName", userName);
                                userInfoBundle.putString("address", address);
                                userInfoBundle.putString("job", job);
                                userInfoBundle.putString("age", age);
                                intent.putExtras(userInfoBundle);
                                startActivity(intent);
                            } else {
                                continue;
                            }


                        }}

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        ManagerFragment5_myInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), org.mobileProgramming.maintermproject.ClientFragment5.ClientFragment5_myInfo.class);
                startActivity(intent);
            }
        });
        return rootview;
    }
}
