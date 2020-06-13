package org.mobileProgramming.maintermproject.Fragment4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.mobileProgramming.maintermproject.R;

public class ReservationInfo extends AppCompatActivity {
    private TextView ReservationInfo_address;
    private TextView ReservationInfo_date;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_info);

        ReservationInfo_address = findViewById(R.id.ReservationInfo_address);
        ReservationInfo_date = findViewById(R.id.ReservationInfo_date);

       Intent intent = getIntent();
       String date = intent.getStringExtra("date");
       String address = intent.getStringExtra("address");

       ReservationInfo_address.setText(address);
       ReservationInfo_date.setText(date);

    }
}
