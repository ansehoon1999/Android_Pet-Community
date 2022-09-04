package org.mobileProgramming.maintermproject.ManagerFragment1;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.OrientationHelper;

import android.annotation.SuppressLint;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.applikeysolutions.cosmocalendar.utils.SelectionType;
import com.applikeysolutions.cosmocalendar.view.CalendarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.mobileProgramming.maintermproject.R;
import org.mobileProgramming.maintermproject.model.ManagerPossibleTime;

import java.text.SimpleDateFormat;
import java.util.List;

public class ReservationActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{
    private CalendarView calendarView;
    private ManagerPossibleTime managerPossibleTime;

    private Button ManagerFragment1_ReservationOkayBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        initViews();
        managerPossibleTime = new ManagerPossibleTime();
        ManagerFragment1_ReservationOkayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushReservationInfo();
            }
        });

    }

    private void pushReservationInfo() {
        String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        List<java.util.Calendar> days = calendarView.getSelectedDates();

        String result="";
        for( int i=0; i<days.size(); i++)
        {
            java.util.Calendar calendar = days.get(i);

            @SuppressLint("WrongConstant")
            final int day = calendar.get(Calendar.DAY_OF_MONTH);
            @SuppressLint("WrongConstant") final int month = calendar.get(Calendar.MONTH);
            @SuppressLint("WrongConstant") final int year = calendar.get(Calendar.YEAR);
            String week = new SimpleDateFormat("EE").format(calendar.getTime());
            String day_full = year + "년 "+ (month+1)  + "월 " + day + "일 " + week + "요일";

           managerPossibleTime.time.put(myUid+day_full, day_full);
            result += (day_full + "\n");
        }
        FirebaseDatabase.getInstance().getReference().child("ManagerPossibleTime").child(myUid).setValue(managerPossibleTime);
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        if(result.equals("")) {
            Toast.makeText(this, "try again please", Toast.LENGTH_SHORT).show();
        } else {

            finish();
        }
    }

    private void initViews() {
        calendarView = (CalendarView) findViewById(R.id.calendar_view);
        ManagerFragment1_ReservationOkayBtn = (Button) findViewById(R.id.ManagerFragment1_ReservationOkayBtn);
        calendarView.setCalendarOrientation(OrientationHelper.HORIZONTAL);
        ((RadioGroup) findViewById(R.id.rg_selection_type)).setOnCheckedChangeListener(this);

    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        clearSelectionsMenuClick();
        switch (checkedId) {

            case R.id.rb_single:
                calendarView.setSelectionType(SelectionType.SINGLE);
                break;

            case R.id.rb_multiple:
                calendarView.setSelectionType(SelectionType.MULTIPLE);
                break;

            case R.id.rb_range:
                calendarView.setSelectionType(SelectionType.RANGE);
                break;



        }

    }
    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.clear_selections:
                clearSelectionsMenuClick();
                return true;

            case R.id.show_selections:
                List<Calendar> days = calendarView.getSelectedDates();

                String result="";
                for( int i=0; i<days.size(); i++)
                {
                    Calendar calendar = days.get(i);

                    Toast.makeText(this, days.get(i).toString(), Toast.LENGTH_SHORT).show();
                    final int day = calendar.get(Calendar.DAY_OF_MONTH);
                    final int month = calendar.get(Calendar.MONTH);
                    final int year = calendar.get(Calendar.YEAR);
                    String week = new SimpleDateFormat("EE").format(calendar.getTime());
                    String day_full = year + "년 "+ (month+1)  + "월 " + day + "일 " + week + "요일";
                    result += (day_full + "\n");
                }
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

*/


    private void clearSelectionsMenuClick() {
        calendarView.clearSelections();

    }



}
