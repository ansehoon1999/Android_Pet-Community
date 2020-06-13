package org.mobileProgramming.maintermproject.ClientFragment;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.mobileProgramming.maintermproject.CustomAdapter;
import org.mobileProgramming.maintermproject.Fragment2.ManagerIntroductionActivity;
import org.mobileProgramming.maintermproject.R;
import org.mobileProgramming.maintermproject.model.User;

import java.util.ArrayList;


public class ClientFragment2 extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManger;
    private ArrayList<User> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private Context context;
    private String DestinationUid;
   //=================================gps
    private String myAddress;
    private Bundle bundle;
    private ActivityOptions activityOptions = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       final ViewGroup rootview = (ViewGroup)  inflater.inflate(R.layout.fragment_client2, container, false);

        context = rootview.getContext();
        recyclerView = rootview.findViewById(R.id.recyclerView); //아디 연결
        recyclerView.setHasFixedSize(true); //리사이클러뷰 기존성능 강화
        layoutManger = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManger);
        arrayList = new ArrayList<>(); //User 객체를 담을 어레이 리스트 (어댑터 쪽으로)
        final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance(); //파이어베이스 데이터베이스 연동

        bundle = getArguments();
        if(bundle != null) {
            myAddress = bundle.getString("MyAddress").substring(17,20);
        }

        databaseReference = database.getReference("SalesInfo"); //파이어 베이스에서 user
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear(); //기존 배열리스트가 존재하지 않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { //반복문으로 데이터 list를 추출해냄
                    User user = snapshot.getValue(User.class); //만들어뒀던 User 객체에 데이터를 담는다

                    if(myUid.equals(user.uid) || !user.address.contains(myAddress)) {
                        continue;
                    }
                    arrayList.add(user); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                }
                adapter.notifyDataSetChanged(); //리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        adapter = new CustomAdapter(arrayList, context, new CustomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final Intent intent = new Intent(view.getContext(), ManagerIntroductionActivity.class);
                final Bundle salesInfoBundle = new Bundle();
                final String destinationUid = arrayList.get(position).uid;
               DestinationUid = destinationUid;
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) { //반복문으로 데이터 list를 추출해냄
                            String uid = snapshot.child("uid").getValue(String.class); //uid
                            if (uid.equals(destinationUid)) {
                                String SalesTitle = snapshot.child("Salestitle").getValue(String.class); // username
                                String address = snapshot.child("address").getValue(String.class); // address
                                salesInfoBundle.putString("destinationUid", destinationUid);
                                salesInfoBundle.putString("Salestitle", SalesTitle);
                                salesInfoBundle.putString("address", address);
                                intent.putExtras(salesInfoBundle);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    activityOptions = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.fromright, R.anim.toleft);
                                    startActivity(intent, activityOptions.toBundle());
                                }

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
            }
        });
        recyclerView.setAdapter(adapter); //리사이클러뷰에 어댑터 연결
       return rootview;
    }
}
