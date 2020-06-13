package org.mobileProgramming.maintermproject.ManagerFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.mobileProgramming.maintermproject.CustomerAdapter2;
import org.mobileProgramming.maintermproject.ManagerFragment1.ReservationActivity;
import org.mobileProgramming.maintermproject.R;
import org.mobileProgramming.maintermproject.model.User;

import java.util.ArrayList;

public class ManagerFragment2 extends Fragment {
    final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private FloatingActionButton actionButton;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManger;
    private ArrayList<User> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_manager2, container, false);

        context = rootview.getContext();
        recyclerView = rootview.findViewById(R.id.manager_recyclerView); //아디 연결
        recyclerView.setHasFixedSize(true); //리사이클러뷰 기존성능 강화
        layoutManger = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManger);
        arrayList = new ArrayList<>(); //User 객체를 담을 어레이 리스트 (어댑터 쪽으로)

        actionButton = rootview.findViewById(R.id.fab);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Reserveintent = new Intent(getActivity(), ReservationActivity.class);
                startActivity(Reserveintent);
            }
        });
        database = FirebaseDatabase.getInstance(); //파이어베이스 데이터베이스 연동
        databaseReference = database.getReference("SalesInfo"); //파이어 베이스에서 user
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear(); //기존 배열리스트가 존재하지 않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { //반복문으로 데이터 list를 추출해냄
                    User user = snapshot.getValue(User.class); //만들어뒀던 User 객체에 데이터를 담는다
                    if(myUid.equals(user.uid)) {
                        continue;
                    }
                    arrayList.add(user); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                }
                adapter.notifyDataSetChanged(); //리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //디비를 가져오던 중 에러 발생 시
                Log.e("MainActivity", String.valueOf(databaseError.toException()));//에러문 출력
            }
        });

        adapter = new CustomerAdapter2(arrayList, context);
        recyclerView.setAdapter(adapter); //리사이클러뷰에 어댑터 연결
        return rootview;
    }

}
