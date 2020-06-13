package org.mobileProgramming.maintermproject.Fragment2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.applikeysolutions.cosmocalendar.settings.lists.DisabledDaysCriteria;
import com.applikeysolutions.cosmocalendar.settings.lists.DisabledDaysCriteriaType;
import com.applikeysolutions.cosmocalendar.view.CalendarView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.mobileProgramming.maintermproject.R;


import java.util.ArrayList;


public class ManagerIntroductionActivity extends AppCompatActivity  {
    private CalendarView introductionCalendarVview;
    private ImageView Manager_introduction_image;
    private TextView Manager_introduction_Title;
    private TextView Manager_introduction_Address;
    private TextView Manager_introduction_memo;
    private Button Manager_introduction_Btn;
    private Spinner Manager_introduction_spinner;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private DatabaseReference reference;
    private ArrayAdapter<String> spinnerAdapter;
    private ArrayList<String> spinnerList;
    private String selected;

    ///////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_introduction);
        Manager_introduction_image = findViewById(R.id.Manager_introduction_image);
        Manager_introduction_Title = findViewById(R.id.Manager_introduction_Title);
        Manager_introduction_Address = findViewById(R.id.Manager_introduction_Address);
        Manager_introduction_memo = findViewById(R.id.Manager_introduction_memo);
        Manager_introduction_Btn = findViewById(R.id.Manager_introduction_Btn);
        introductionCalendarVview = findViewById(R.id.introductionCalendarVview);
        introductionCalendarVview.setDisabledDaysCriteria(new DisabledDaysCriteria(1, 31, DisabledDaysCriteriaType.DAYS_OF_MONTH));
        introductionCalendarVview.setDisabledDayTextColor(getResources().getColor(R.color.colorHighlight));
        Intent intent = getIntent();

        final String SalesTitle = intent.getStringExtra("Salestitle");
        final String address = intent.getStringExtra("address");
        final String destinationUid = intent.getStringExtra("destinationUid");
        final String memo = intent.getStringExtra("memo");

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        StorageReference imageRef = storageReference.child("ClientPetImages").child(destinationUid + ".png");
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext())
                        .load(uri)
                        .into(Manager_introduction_image);
            }
        });

        Manager_introduction_Title.setText(SalesTitle);
        Manager_introduction_Address.setText(address);
        Manager_introduction_memo.setText(memo);
        final Intent waitIntent = new Intent(this, WaitActivity.class);
        waitIntent.putExtra("destinationUid", destinationUid);

        Manager_introduction_spinner = findViewById(R.id.Manager_introduction_spinner);
        spinnerList = new ArrayList<>();
        spinnerAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, spinnerList);
        Manager_introduction_spinner.setAdapter(spinnerAdapter);
        reference = FirebaseDatabase.getInstance().getReference().child("ManagerPossibleTime").child(destinationUid).child("time");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    spinnerList.add(snapshot.getValue().toString());
                }
                spinnerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Manager_introduction_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), spinnerList.get(position), Toast.LENGTH_SHORT).show();
                selected = spinnerList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Manager_introduction_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), selected, Toast.LENGTH_SHORT).show();
                ActivityOptions activityOptions = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    activityOptions = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.fromright, R.anim.toleft);
                    waitIntent.putExtra("ReservationTime", selected);
                    waitIntent.putExtra("Title", SalesTitle);
                    waitIntent.putExtra("address", address);
                    startActivity(waitIntent, activityOptions.toBundle());
                }
            }
        });


    }
}
