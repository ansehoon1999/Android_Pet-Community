package org.mobileProgramming.maintermproject.Fragment2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.mobileProgramming.maintermproject.Fragment4.MessageActivity;
import org.mobileProgramming.maintermproject.R;
import org.mobileProgramming.maintermproject.model.ReservationModel;

public class WaitActivity extends AppCompatActivity {
    private TextView wait_Salestitle;
    private TextView wait_Address;
    private TextView wait_ReservationTime;
    private Button wait_okay;
    private String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);

        wait_Salestitle = findViewById(R.id.wait_Salestitle);
        wait_Address = findViewById(R.id.wait_address);
        wait_ReservationTime = findViewById(R.id.wait_ReservationTime);
        wait_okay = findViewById(R.id.waitOkay);
        Intent intent = getIntent();

        final String Salestitle = intent.getStringExtra("Title");
        final String address = intent.getStringExtra("address");
        final String ReservationTime = intent.getStringExtra("ReservationTime");
        final String destinationUid = intent.getStringExtra("destinationUid");

        wait_Salestitle.setText(Salestitle);
        wait_Address.setText(address);
        wait_ReservationTime.setText(ReservationTime);

        final Intent messageIntent = new Intent(this, MessageActivity.class);
        messageIntent.putExtra("destinationUid", destinationUid);
        messageIntent.putExtra("Title", Salestitle);
        messageIntent.putExtra("ReservationTime", destinationUid);
        messageIntent.putExtra("address", address);
        wait_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            ReservationModel reservationModel = new ReservationModel();
            reservationModel.myUiddestinationUid = myUid + destinationUid;
            reservationModel.destinationUidmyUid = destinationUid + myUid;
            reservationModel.Salestitle = Salestitle;
            reservationModel.address = address;
            reservationModel.date = ReservationTime;
            reservationModel.client = myUid;
            reservationModel.manager = destinationUid;

            FirebaseDatabase.getInstance().getReference().child("ReservationInfo")
                 .child(myUid+destinationUid).setValue(reservationModel);

                ActivityOptions activityOptions = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    activityOptions = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.fromright, R.anim.toleft);

                    startActivity(messageIntent, activityOptions.toBundle());
                }
            }
        });
    }
}
