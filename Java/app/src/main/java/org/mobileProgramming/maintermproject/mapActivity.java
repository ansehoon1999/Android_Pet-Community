package org.mobileProgramming.maintermproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;

public class mapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Geocoder geocoder;
    private GoogleMap mMap;
    private GpsTracker gpsTracker;
    private DatabaseReference reference;
    private String destinationUid;
    private String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private String resultaddress;
    private List<Address> addressList = null;
    private String str;
    String []splitStr;
    String address; // 주소
    String latitude; // 위도
    String longitude; // 경도
    LatLng point;
    MarkerOptions mOptions2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intent = getIntent();
        destinationUid = intent.getStringExtra("destinationUid");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {


        geocoder = new Geocoder(this);
        mMap = googleMap;
        gpsTracker = new GpsTracker(mapActivity.this);

        // 여기 manager의 주소를 디비에서 가져오는거로 바꿔야함함
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("ReservationInfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { //반복문으로 데이터 list를 추출해냄
                    String myUiddestinationUid = snapshot.child("myUiddestinationUid").getValue(String.class);

                    if (myUiddestinationUid.equals(myUid+destinationUid) || myUiddestinationUid.equals(destinationUid+myUid)) {

                        address = snapshot.child("address").getValue(String.class); // username
                        str = address.substring(5);

                        try {
                            // editText에 입력한 텍스트(주소, 지역, 장소 등)을 지오 코딩을 이용해 변환
                            addressList = geocoder.getFromLocationName(
                                    str, // 주소
                                    10); // 최대 검색 결과 개수
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }


                        splitStr = addressList.get(0).toString().split(",");
                        address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1,splitStr[0].length() - 2); // 주소
                        latitude = splitStr[10].substring(splitStr[10].indexOf("=") + 1); // 위도
                        longitude = splitStr[12].substring(splitStr[12].indexOf("=") + 1); // 경도


                        // 좌표(위도, 경도) 생성
                        point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                        // 마커 생성
                        mOptions2 = new MarkerOptions();
                        mOptions2.title("search result");
                        mOptions2.snippet(address);
                        mOptions2.position(point);
                        // 마커 추가
                        mMap.addMarker(mOptions2);
                        // 해당 좌표로 화면 줌
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point,15));


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

}
